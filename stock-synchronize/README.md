# 동시성 이슈

1. 동일한 메모리에 같은 순간에 접근하여 데이터를 조작하는 race condition 은 심심치 않게 발견 됨.

    1. 이를 해결하기 위해 synchronized 도입
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
  - 
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