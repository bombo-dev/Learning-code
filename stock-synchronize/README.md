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
   - 3402ms 소요.
   - 문제점 : WAS 서버가 증설된 경우 동시성 이슈가 다시 발생 할 수 있음.
   
   2. 

2. Aspect Test
  - 동시성 테스트의 전체 소요 시간을 확인하기 위해 AOP를 도입.
  - Service 코드에 AOP를 둘 경우 매 호출마다 걸리는 시간이 작성되므로 의도와는 다름.
  - 시도
    - 테스트 메서드 상단에 애노테이션을 추가 (실패)
    - 임의로 테스트 메서드 내부에 시간 확인 로직을 추가.
    - 출력 속도를 고려하여, log로 출력.
