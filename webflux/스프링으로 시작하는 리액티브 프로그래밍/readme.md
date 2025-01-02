# 스프링으로 시작하는 리액티브 프로그래밍

## 리액티브 프로그래밍 마인드맵
![image](https://github.com/user-attachments/assets/5f274236-2e56-471b-9a55-7f3d12750f0e)


## 마인드맵 내용 정리 -> to gpt

### Publisher & Subscriber
Publisher: 데이터를 발행(emit)하는 주체.
Subscriber: 데이터를 구독하며, onNext / onComplete / onError와 같은 Signal을 통해 발행되는 데이터를 전달받고, 필요시 구독을 해지(dispose)합니다.

Flux(여러 개의 데이터를 발행할 수 있는 시퀀스)와 Mono(오직 한 개의 데이터만 발행할 수 있는 시퀀스)로 크게 구분됩니다.
또한 시퀀스가 데이터를 “한 번만 소비할 수 있는가?” 혹은 “나중에 구독해도 이전 데이터를 받을 수 있는가?”에 따라 Cold Sequence와 Hot Sequence로 나누어집니다.

### Sequence
Cold Sequence: 구독할 때마다 새롭게 데이터를 방출(예: API 요청 시 매번 새로운 결과)
Hot Sequence: 이미 발행 중인 데이터를 공유(share)해서, 나중에 구독해도 중간부터 데이터를 수신

### Signal
Reactor에서 전달되는 이벤트(예: onNext, onComplete, onError)를 통칭합니다.
Mono나 Flux 내부/외부에서 해당 시그널을 통해 “데이터가 왔다(onNext)”, “에러가 발생했다(onError)”, “모든 데이터가 완료되었다(onComplete)” 같은 상태를 알립니다.

### BackPressure
구독자(Subscriber) 측에서 데이터 소비 속도를 제어하기 위해 필요한 개념.
생산자(Publisher)가 너무 빠르게 데이터를 발행할 때, Subscriber가 감당할 수 있는 만큼만 처리하도록 Reactor가 다양한 전략을 제공합니다.

- ERROR: 버퍼가 가득 차면 예외 발생(에러 처리)
- DROP: 버퍼 초과 시, 새로 들어오는 데이터 혹은 오래된 데이터를 버립니다
- BUFFER: 데이터가 쌓이도록 버퍼를 무제한(또는 일정량)으로 쌓아두는 전략
- LATEST: 최신 데이터만 유지하고, 오래된 데이터를 버리는 전략
- BUFFER DROP-OLDEST / BUFFER DROP-LATEST: 버퍼가 가득 찼을 때, 가장 오래된(or 최신) 데이터를 버리는 등 다양한 변형 전략

### Sinks
Reactor 3.x 이상에서 제공하는 Sinks API는 직접 데이터를 push할 수 있는 도구입니다.
예: Sinks.many().unicast(), multicast(), multicastReplay() 등
- unicast: 구독자가 한 명만 존재하는 경우 사용
- multicast: 여러 구독자에게 동시에 데이터 방출
- multicast replay: 나중에 구독자가 생기더라도, 이미 발행된 데이터를 다시 재생(Replay)하여 전달

### Mono와 Flux
Mono: 단 하나의 데이터(OnNext) 혹은 완료/에러(OnComplete/OnError)만을 처리하는 시퀀스
Flux: 여러 개의 데이터를 연속적으로 방출할 수 있는 시퀀스
내부적으로 onNext, onComplete, onError 등의 시그널을 통해 데이터를 전달
