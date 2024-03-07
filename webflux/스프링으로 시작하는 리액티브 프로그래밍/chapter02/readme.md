# 리액티브 스트림즈(Reactive Streams)
리액티브 라이브러리를 **어떻게 구현할지 정의해 놓은 별도의 표준 인터페이스**를 리액티브 스트림즈라고 한다. 즉, **데이터 스트림을 Non-Blocking이면서 비동기적인 방식으로 처리하기 위한 리액티브 라이브러리의 표준 사양**이다.

## 리액티브 스트림즈 구성요소
### Publisher
- 데이터를 생성하고 통지하는 역할
```java
public interface Publisher<T> {
	void subscribe(Subscriber<? super T> s);
}
```
코드를 보면 의아한 부분이 있다. Publisher는 데이터를 방출해내는 역할을 하는데 Publisher가 구독을 하는 행위를 가지고 있다.  

Kafka에서는 동일한 메시지의 브로커에 Topic을 바라보면서 Publisher는 Topic에 메시지를 전송하고 Consumer가 Topic의 메시지를 구독해서 가져오는 형태를 취하지만 Publisher는 중간 매개체 없이 강결합되어있기때문에 **Publisher가 파라미터로 전달받는 Subscriber를 구독하는 형태**를 취한다.

아래는 실제로 Publisher를 구현한 코드이다.
```java
@Override
public void subscribe(Flow.Subscriber<? super List<ByteBuffer>> subscriber) {
    synchronized (reading) {
        if (subscription == null) {
            subscription = new HttpWriteSubscription();
        }
        this.subscriber = subscriber;
    }
    subscriber.onSubscribe(subscription);
    signal();
}
```

### Subscriber
- 구독한 Publisher로부터 통지된 데이터를 전달받아서 처리하는 역할
```java
public interface Subscriber<T> {
	void onSubscribe(Subscription s);
	void onNext(T t);
	void onError(Throwable t);
	void onComplete();
}
```
1. onSubscribe
   - **구독 시작 시점에 어떤 처리를 하는 역할**을 수행. 주로, 요청할 데이터 개수 지정 및 구독 해제 요청
   - 파라미터로 전달받는 Subscription을 통해 해당 과정을 수행
2. onNext
   - Publisher가 통지한 **데이터를 처리**
3. onError
   - Publisher가 통지한 **데이터에서 에러가 발생했을 때 처리**
4. onComplete
   - Publisher가 **데이터 통지를 완료했음**을 알릴 때 사용
### Subscription
- Publisher에 **요청할 데이터의 개수를 지정하고, 데이터의 구독을 취소**하는 역할
```java
public interface Subscription {
  void request(long n);
  void cancel();
}
```
- Publisher와 Subscriber는 서로 **각각의 스레드에서 비동기적으로 통신을 하는데, 이때 데이터의 개수를 지정해주지 않으면 시스템 부하**로 이루어질 수 있습니다.
- 네트워크에서의 흐름제어와 유사하다.
### Processor
- Publisher와 Subscriber의 기능을 모두 가지고 있음.

## 리액티브 스트림즈 관련 용어
### Signal
- **Publisher와 Subscriber 간에 주고받는 모든 상호작용을 의미**한다.
- 리액티브 스트림즈의 표준에서 퍼블릭 인터페이스는 모두 signal 이다.
### Demand
- Publisher가 아직 **Subscriber 에게 전달하지 않은 요청 데이터**를 의미한다.
### Emit
- onNext Signal을 의미한다. 즉, **데이터를 전송하는 것**을 의미한다.
### Upstream/DownStream
- operation 단계에서 리액티브 라이브러리는 선언형 프로그래밍(메서드 체이닝)으로 이루어지는데, 이때 이때 **상대적으로 상위에 있는 스트림을 UpSteram 하위에 있는 스트림을 DownStream**이라고 한다.
```kotlin
Flux
	.just(1, 2, 3, 4, 5, 6)
	.filter { it % 2 == 0 }	
	.map { it * 2 }
 	.subscribe(println(it))
```
위의 예시에서 just의 입장에서 map을 사용해 반환된 Flux는 DownStream이고 map의 입장에서는 just는 UpStream이다.
### Sequence
- Publisher가 **Emit하는 데이터의 연속적인 흐름**을 의미한다.
### Operator
- **중간 연산자들을 의미**한다. 위의 예시에서 just, filter, map을 의미한다.
### Source
- **최초에 가장 먼저 생성된 데이터**를 의미한다.
