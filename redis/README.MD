# Redis 

# Redis TTL 이슈
레디스의 학습 테스트를 진행 하던 중 TTL 설정 시 발생하는 이슈를 확인 할 수 있었다.

다음과 같은 도메인이 있다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "fruits", timeToLive = 1)
public class Fruits {

    @Id
    private String id;

    private String name;

    private Integer stock;

    @Indexed
    private LocalDateTime createdAt;

    @Builder
    private Fruits(String name, Integer stock, LocalDateTime createTime) {
        this.name = name;
        this.stock = stock;
        this.createdAt = createTime;
    }

    public static Fruits createFruit(String name, Integer stock, LocalDateTime createTime) {
        return Fruits.builder()
                .name(name)
                .stock(stock)
                .createTime(createTime)
                .build();
    }
}
```

TTL이 1초이기때문에 1초 뒤에는 Redis에 저장됐던 데이터가 삭제되어있음을 예상하고 다음과 같이 테스트 코드를 작성했다.

```java
    @DisplayName("TTL을 지정했을 때 이후에 탐색이 되면 안된다.")
    @Test
    void findExceedTTLFruits() throws InterruptedException {
        // given
        LocalDateTime saveTime = LocalDateTime.now();
        Fruits apple = Fruits.createFruit("사과", 10, saveTime);
        Fruits banana = Fruits.createFruit("바나나", 15, saveTime);

        fruitsRepository.save(apple);
        fruitsRepository.save(banana);
        
        // when
        Thread.sleep(1000);
        Optional<Fruits> findBanana = fruitsRepository.findById(banana.getId());
        long count = fruitsRepository.count();

        // then
        assertThat(findBanana).isEmpty();
        assertThat(count).isEqualTo(0);
    }
```

그리고 예상과 달리 결과는 다음과 같이 나오게 되었다.

![이미지1](image.png)

저장했던 객체 자체를 참조하고자 할 경우에는 데이터가 없는 것을 볼 수 있지만, 현재 Set에 저장된 데이터는 삭제가 되어있지 않아 데이터 정합성에 문제가 생긴 것이였다.

이를 해결하기 위해서는 TTL이 만료가 되었을 경우 만료가 되었는지 파악하기 위한 이벤트를 받을 필요가 있었다.
그리고 스프링 부트는 이벤트를 받을 수 있게 해주는 애노테이션을 제공해주고 있었다.

```java
@SpringBootTest
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
```

위와 같이 애노테이션을 추가해주고 다시 테스트를 진행해봤다. 하지만 이렇게 추가를 해도 위 테스트는 동일하게 실패를 하게 되었다. 이벤트를 처리하는데 걸리는 시간이 필요해서 그런건가 생각해서 1000ms 이후 테스트가 실행되던 부분을 2000ms로 수정을 하였고, 예상대로 테스트가 통과하는 것을 볼 수 있었다.

[참고]
https://hyperconnect.github.io/2022/12/12/fix-increasing-memory-usage.html

## Redis LocalDateTime Serialize Issue
Spring Data Redis는 Key-Value 구조의 자료구조만 사용하기 때문에 다른 자료구조도 테스트 해보기 위하여 RedisTemplate를 직접 만들어서 학습 테스트를 진행해봤습니다.

```java
redisTemplate.opsForValue();
```

를 사용하여 key-value 자료구조에 대해서 Primitive Type, Reference Type 두 경우에 대해서 테스트를 진행해보고자 하였습니다.

```java
    @DisplayName("redisTemplate key-value 형식으로 기본형 타입 저장")
    @Test
    void saveValueOpsPrimitiveType() {
        // given
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();

        String key = "KEY";
        stringObjectValueOperations.set(key, 1);
        stringObjectValueOperations.set(key, 2);
        stringObjectValueOperations.set(key, 3);

        // when
        Integer value = (Integer) stringObjectValueOperations.get(key);
        Set<String> keys = redisTemplate.keys(key);

        // then
        assertThat(keys.size()).isEqualTo(1);
        assertThat(value).isEqualTo(3);
    }
```

먼저, key-value로 기본형 타입을 저장하는 테스트를 진행해봤습니다. 예상대로 마지막 key에 대한 값이 덮어져서 가져와지는 것을 확인 할 수 있었습니다.

다음으로 객체 타입을 저장하려고 시도하였습니다.
먼저, RedisTemplate는 다음과 같이 생성을 했었습니다.

```java
    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
```

이어서, 기본 값 타입 테스트와 비슷하게 다음과 같이 테스트를 작성하였습니다.
```java
    @DisplayName("redisTemplate key-value 형식으로 객체 타입 저장")
    @Test
    void saveValueOpsReferenceType() {
        // given
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        String key = "ReferenceKey";

        LocalDateTime serverTime = LocalDateTime.now();

        Fruits apple = Fruits.createFruit("사과", 10, serverTime);
        Fruits banana = Fruits.createFruit("바나나", 15, serverTime);

        stringObjectValueOperations.set(key, apple);
        stringObjectValueOperations.set(key, banana);
        Set<String> keys = redisTemplate.keys(key);

        // when
        Fruits value = (Fruits) stringObjectValueOperations.get(key);

        // then
        assertThat(keys.size()).isEqualTo(1);
        assertThat(value).usingRecursiveComparison().isEqualTo(banana);
    }
```

위와 큰 차이가 없기 때문에 가뿐하게 통과 할 줄 알았으나, 테스트는 실패를 했습니다.
에러를 살펴보니 다음과 같은 메시지가 있었습니다.

```bash
Could not write JSON: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.example.redis.domain.redis.Fruits["createdAt"])
```
LocalDateTime을 Serialize 할 수 없으니 jsr310 Module을 추가해달라는 것이었습니다.
다음과 같이 라이브러리 의존성을 추가해주었습니다.

```gradle
implementation group: 'com.fasterxml.jackson.datatype', name: 'jackson-datatype-jsr310', version: '2.16.0'
```
그러나, 똑같은 에러가 나타나게 되었습니다.

해당 에러를 해결하기 위해 구글링을 한 결과 한 포스팅에서 해답을 찾을 수 있었습니다.

https://velog.io/@bagt/Redis-%EC%97%AD%EC%A7%81%EB%A0%AC%ED%99%94-%EC%82%BD%EC%A7%88%EA%B8%B0-feat.-RedisSerializer

해당 포스팅을 참고하여 Redis를 위한 ObjectMapper를 커스터마이징 해주고 RedisTemplate에 다음과 같이 주입해주었습니다.

```java
    @Bean(name = "redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                      @Qualifier("redisObjectMapper") ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
```
그리고 결과는 아래와 같이 성공하는 것을 볼 수 있었습니다.

![result](image-1.png)

[참고]
https://velog.io/@bagt/Redis-%EC%97%AD%EC%A7%81%EB%A0%AC%ED%99%94-%EC%82%BD%EC%A7%88%EA%B8%B0-feat.-RedisSerializer