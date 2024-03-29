# 동시성 이슈

1. 동일한 메모리에 같은 순간에 접근하여 데이터를 조작하는 race condition 은 심심치 않게 발견 됨.

    1. **synchronized로 동시성 문제 해결하기**
        - 동일하게 동시성 문제가 발생하는 것을 볼 수 있었음.
        - `@Transactional` 을 사용하면 Proxy 객체를 호출하게 되는데 이 과정에서 발생한 문제였음.
        
   ```java
    class StockServiceProxy {
    
    private final StockService stockService;
    
    StockServiceProxy(StockService stockService) {
        this.stockService = stockService;
    }
    
    public void decrease(Long id, Long quantity) {
        startTransaction();
        stockService.decrease(id, quantity);
        // transaction 종료되서 갱신되기 전에 동일한 객체에 접근하여 발생한 문제.
        endTransaction();
        }
    }
    ```
   - `@Transactional` 을 제거해서 동시성 이슈를 해결.
   - 1596ms 소요.
   - 문제점 : WAS 서버가 증설된 경우 동시성 이슈가 다시 발생 할 수 있음.

   2. **비관적 락(Exclusive Lock) 으로 동시성 문제 해결하기**
        - 비관적 락은 데이터베이스의 테이블 혹은 레코드에 직접적으로 락을 건다.
        - 비관적 락은 동시성 이슈가 빈번하게 발생 할 경우, 낙관적 락에 비해서 성능상의 이점을 가져 갈 수 있다.
        - 하지만, 레코드 자체에 락을 걸기 때문에 데이터 읽기가 빈번하게 발생한다면 성능이 저하 될 수 있다.
        - 또 다른 문제는 데드락이 발생 할 수 있다.
        - MySQL에서 Shared Lock 은 여러 트랜잭션에 대해 여러 번 Lock을 걸 수 있으나, Execlusive Lock은 하나의 트랜잭션만 걸 수 있다.
        - 또한, Shared Lock과 Execlusive Lock은 공존 할 수 없는데 서로 다른 트랜잭션에 대해 Shared Lock이 걸려있는 상태에서 Execlusive Lock을 걸려고 할 때 데드락이 발생한다.
        - 걸린 시간 : 615ms

   3. **낙관적 락(Optimistic Lock) 으로 동시성 문제 해결하기**
       - 낙관적 락은 데이터베이스 자체에 잠금을 거는 것이 아니라, 애플리케이션 코드를 이용하여 동시성 문제를 해결한다.
       - 동시성이 발생했는지에 대한 여부는 테이블의 version 필드를 이용하여 동시성을 확인 할 수 있다.
       - 비관적 락에 비해서, 실패할 경우 재시도하는 로직 (busy call) 이 발생하여 성능 상 떨어 질 수 있지만, 동시성 문제가 잦지 않으면 성능 개선을 볼 수 있다.
       - 따라서, 동시성 문제가 자주 발생한다면 비관적 락을, 드물게 발생한다면 낙관적 락을 사용하자.
       - MySQL의 Repeatable Read 로 인하여, Service 코드에서 낙관적 락이 정상적으로 반영이 되지 않을 수 있다는 포스팅을 확인해봤다.
       - 테스트 결과 : MySQL의 Repeatable Read 문제가 아닌 H2 데이터베이스의 문제로 보인다.
       - 걸린 시간 : 570ms
      
   4. **네임드 락(Named Lock) 으로 동시성 문제 해결하기**
       - 네임드 락은 비관적 락처럼 테이블이나 레코드 자체에 락을 거는 것이 아니라 Lock 전용 테이블에 락을 건다.
       - 네임드 락은 주로 분산락을 목적으로 사용이 된다.
       - 하지만 락을 획득하는 과정으로 인해 로직을 수행하기 위한 로직이 락을 해제하기를 기다려야 할 수 있다.
       - 따라서, 네임드 락을 사용하는 로직이 있다면 데이터 소스를 분리하는 것이 좋다.
       - `분리를 시도해봤으나, 문제가 발생하여 질문을 올린 상태`
       - 걸린 시간 : 946ms
      
   5. **분산 락(Distribution Lock) 레디스로 동시성 해결하기**
       - **Lettuce** : SpinLock 방식의 분산 락 방식
         - SpinLock 방식이므로 락 획득에 부하를 줄 수 있음. 따라서, 적절한 부하 관리가 필요.
         - 재시도가 필요 없는 경우 사용
         - 걸린 시간 : 1603ms
       - **Redisson** : pub-sub(Channel) 방식의 분산 락 방식
         - 콜백 방식으로 이루어져 있어, spinlock에 비해 부하가 덜 함.
         - 재시도가 필요한 경우 사용
         - 걸린 시간 : 1227ms

## 2. Aspect Test
  - 동시성 테스트의 전체 소요 시간을 확인하기 위해 AOP를 도입.
  - Service 코드에 AOP를 둘 경우 매 호출마다 걸리는 시간이 작성되므로 의도와는 다름.
  - 시도
    - 테스트 메서드 상단에 애노테이션을 추가 (실패)
    - 임의로 테스트 메서드 내부에 시간 확인 로직을 추가.
    - 출력 속도를 고려하여, log로 출력.

## 3. ExecutorService
  - 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해서 제공되는 JAVA 라이브러리.
  - 없었다면, 매 작업마다 Thread를 만들어서 처리하고, 제거해주는 작업을 손수 해줬어야 함.
  - Task만 지정해주면 ThreadPool을 이용해서 Task를 수행

### 수행 방식
- ThreadPool에 있는 Thread 수보다 Task가 많으면 미 실행된 Task는 Queue에 저장되고 실행을 마친 Thread가 순차적으로 수행

### 쓰레드풀 생성 방식
1. CachedThreadPool
  - `Executors.newCachedToreadPool();`
  - 쓰레드를 캐싱하는 쓰레드 풀 (일정시간동안 쓰레드를 검사하여 60초동안 작업이 없으면 Pool에서 제거)
  - Thread를 제한 없이 무한정 생성하기 때문에, 쓰레드 수가 폭발적으로 증가할 수도 있음.
2. FixedThreadPool
  - `Executors.newFixedThreadPool(int nThreads)`
  - 고정된 개수를 가진 쓰레드 풀
  - 머신의 CPU 코어 수를 기준으로 생성하면 더 좋은 퍼포먼스를 얻을 수 있다.
3. SingleThreadExecutor
  - 한 개의 쓰레드로 작업을 처리하는 쓰레드풀
  - 싱글 쓰레드 고려사항을 알아서 처리해준다.

### 작업 할당 메서드
1. execute()
  - return 타입이 void로 Task의 실행 결과나 Task의 상태를 알 수 없다.
2. submit()
  - Task를 할당하고 Future 타입의 결과값을 받는다.
  - 결과가 리턴이 되어야하기 때문에 Callable을 구현한 Task를 인자로 준다.
3. invokeAny()
  - Task를 Collection에 넣어서 인자를 넘겨준다.
  - 실행에 성공한 Task 중 하나의 리턴값만 만환한다.
4. invokeAll()
  - Task를 Collection에 넣어서 인자로 넘겨줄 수 있다.
  - Task의 리턴값을 `List<Future<>>`를 반환한다.

### 작업 종료 메서드
1. shutdown()
  - 실행 중인 모든 Task가 수행되면 종료
2. shutdownNow()
  - 모든 Thread가 동시에 종료되는 것을 보장하지는 않고 실행되지 않은 Task를 반환
3. awaitTermination()
  - 일정 시간동안 실행중인 Task가 완료되기를 기다림.

## CountDownLatch
- 어떤 쓰레드가 다른 쓰레드에서 작업이 완료될 때 까지 기다릴 수 있도록 해주는 클래스
- `CountDownLatch countDownLatch = new CountDownLatch(n)`

1. countDown()
  - Latch의 숫자가 1개씩 감소
2. await()
  - Latch의 숫자가 0이 될 때까지 기다리는 코드
  - 인자로 Timeout을 전달 가능, 정해진 시간만 기다리도록 가능