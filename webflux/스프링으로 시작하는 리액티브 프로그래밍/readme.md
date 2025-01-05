# 스프링으로 시작하는 리액티브 프로그래밍

## 리액티브 프로그래밍 마인드맵
![image](https://github.com/user-attachments/assets/502ba1ad-80df-4274-9d54-ecff565fc252)


## 마인드맵 내용 정리

## Publisher & Subscriber
### Publisher
데이터를 발행(emit)하는 주체입니다. Mono나 Flux 등이 대표적인 Publisher의 구현체로, onNext, onComplete, onError와 같은 시그널을 통해 데이터를 외부로 전달합니다.

### Subscriber
Publisher로부터 데이터를 구독(Subscribing)하여 전달받는 주체입니다. onNext, onComplete, onError 시그널을 처리하며, 필요 시 구독을 해지(dispose)할 수도 있습니다.

## Flux & Mono

- Flux: 여러 개의 데이터를 순차적으로 발행할 수 있는 시퀀스
- Mono: 단 하나의 데이터만 발행 가능한 시퀀스

## Sequence (Cold / Hot)
### Cold Sequence
구독할 때마다 새롭게 데이터를 생성하거나 API 요청을 수행하여, 구독자마다 같은 로직을 다시 실행합니다. 예: HTTP 요청 시 매번 새로운 응답을 받는 경우.

### Hot Sequence
이미 발행 중인 데이터를 여러 구독자가 공유(share)하여, 나중에 구독한 Subscriber도 “중간부터” 데이터를 받을 수 있습니다.

## Signal
Reactor에서 전달되는 이벤트를 통칭합니다. 예:

- onNext: 새로운 데이터가 도착
- onComplete: 모든 데이터 발행이 완료
- onError: 에러 발생
이러한 시그널을 통해 Mono 또는 Flux가 내부/외부에서 상태를 알립니다.

## BackPressure
Subscriber가 처리할 수 있는 양보다 더 빠르게 Publisher가 데이터를 밀어낼 경우, 과부하(버퍼 초과, 메모리 부족 등)가 발생할 수 있습니다. 이를 방지하기 위한 개념이 BackPressure이며, Reactor에서는 여러 가지 전략을 제공합니다.

- ERROR: 버퍼가 가득 찼을 때 예외를 발생시킵니다.
- DROP: 버퍼가 넘쳤을 경우 새로 들어오는 데이터(또는 가장 오래된 데이터를) 버립니다.
- BUFFER: 메모리가 허용하는 한 데이터를 무제한(또는 일정 크기)으로 버퍼링합니다.
- LATEST: 최신 데이터만 유지하고, 이전 데이터를 버리는 전략입니다.
- BUFFER DROP-OLDEST / BUFFER DROP-LATEST: 버퍼가 가득 찼을 때 가장 오래된 데이터를 버리거나, 가장 최신 데이터를 버리는 등 세밀한 전략입니다.

## Sinks
Reactor 3.x 이상에서 제공되는 Sinks API는 직접 데이터를 “push(emit)” 할 수 있는 도구입니다.

### Sinks.many().unicast()
구독자가 한 명뿐인 상황에서, 한 줄로 데이터를 밀어넣어 발행할 수 있습니다.
### Sinks.many().multicast()
여러 구독자에게 동시에 데이터를 방출합니다. 다중 Subscriber에 대한 Broadcast 개념입니다. 단, HotSequence로 동작합니다.
### Sinks.many().multicastReplay()
새로운 구독자가 나중에 추가되어도, 이미 발행된 데이터를 다시 재생(Replay)해서 전달할 수 있습니다. 즉, ColdSequence로 동작합니다.

Sinks는 “Thread-safe”하게 동작하여, 여러 Producer(데이터 밀어넣는 주체)로부터의 이벤트를 안전하게 처리할 수 있도록 지원합니다.

## Scheduler
Scheduler는 Reactor에서 스레드를 제어하고 관리하기 위한 추상화 레이어입니다.

### Schedulers.immediate()
현재 스레드에서 그대로 실행합니다. 별도의 스레드 전환이 없으므로, 즉시 실행하고 단순한 로직에서 활용할 수 있습니다.

### Schedulers.single()
단일(싱글)스레드를 이용하는 Scheduler입니다. 스레드 전환이 필요하지만, 오직 하나의 스레드를 사용하므로 순서 보장이 필요하거나 리소스가 적은 환경 및 일회성 실행에 경우 사용할 수 있습니다.

### Schedulers.parallel()
CPU 연산 집약적인 작업 시에 적합한 Scheduler입니다. 일반적으로 CPU 코어(논리)개수만큼 풀을 만들어서 동시에 작업을 수행합니다.

### Schedulers.boundedElastic()
네트워크/파일 I/O 등 블로킹(Blocking) 작업에 적합한 Scheduler입니다. 필요한 만큼(단, 내부적으로는 제한적 CPU 코어 * 10) 스레드를 확장하여 작업을 처리합니다.
ExecuterService에서 생성하는 스레드 풀을 사용한다.

### publishOn() / subscribeOn()

- subscribeOn(): 구독(Subscription)이 시작될 때의 스레드를 지정합니다.
- publishOn(): 데이터 발행(onNext)이 진행될 스레드를 지정합니다.
연산자 체인에서 적절히 배치함으로써, 특정 구간을 다른 스레드에서 처리하도록 제어할 수 있습니다.
