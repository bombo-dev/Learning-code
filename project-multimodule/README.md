# 최종 프로젝트 멀티 모듈 회고

최종 프로젝트를 진행하면서 멀티 모듈 설계에서 겪었던 이슈들을 몇 가지 공유하고자 한다.

먼저, 초기에 계획 된 우리 팀의 멀티모듈 구조는 다음과 같이 설계되었다.

![Image1](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_1.png)

위 사진처럼 멀티모듈 구조를 레이어별로 가져가기로 했다.

레이어별로 구조를 가져가기 이전에 먼저 모듈을 어떻게 가져가는 것이 좋을지에 대해 팀원들과 얘기를 나누었다.

하지만, 프로젝트 기간이 짧은 만큼 도메인이 많이 발생 할 것으로 예상되지 않고 멀티 모듈을 구성했을 때 모듈 별로 부트 서버를 따로 띄울 일이 프로젝트 구조 상 발생하지 않을 것 같다는 전제에서 시작했다.

고민을 레이어별로 모듈을 나누는 것과 도메인 별로 모듈을 나누는 것 두 가지를 고려했기 때문에 자연스럽게 레이어 별로 모듈을 가져가기로 결정하였다.

참고로, 멀티모듈을 어떻게 가져가는 것이 좋을까에 대해서는 다음과 포스팅 들을 참조했다.

https://techblog.woowahan.com/2637/  
https://www.youtube.com/watch?v=uvG-amw2u2s  
https://www.youtube.com/watch?v=ipDzLJK-7Kc  
https://jojoldu.tistory.com/123  

## 첫 번째 이슈

우리는 프로젝트를 진행하면서 Domain 영역에서는 Spring Data Jpa를 의존한다.
이상적으로는 Domain에는 그 어떤 것도 의존하지 않고 (JPA 포함) 도메인에 대한 비즈니스 로직만이 있는 것이 좋을 것 같다고 생각했지만 개발 비용이 전체적으로 올라갈 수 있겠다고 판단하여 도메인 모듈은 JPA를 의존하도록 하였다.

그리고 이후에 동적 쿼리를 적용하기 위해 QueryDSL을 위한 설정을 미리 해두기로 결정하였다.

우리 팀은 QueryDSL 또한 Infra 영역에 속한다고 판단하였다.
QueryDSL 자체를 외부에서 사용하고 변경 가능성이 있는 모듈이라고 생각했기 때문이다.

따라서, Domain 모듈이 QueryDSL이 있는 Infra 모듈을 다음과 같이 의존하도록 설계하였다.

![Image2](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_2.png)

여기서, 이슈가 발생을 했다. QueryDSL에 있는 Infra 영역에서 Domain에 대한 QClass를 생성 할 방법을 찾지 못했다.

분명히, 방법이 있을 것이라고 생각했지만 Gradle에 대한 이해도가 아직 낮아서인지 Domain에 있는 JPA Class를 읽어서 Infra 영역에 생기게 끔 할 방법을 찾을 수 없었다.

QueryDSL을 어떻게 할 것 인지. 외부의 기술로 볼 것인지에 대한 선택이 필요했다.

팀원들과의 의논 끝에 QueryDSL도 결국 JPA에 의존을 하게 된다. "이미 도메인에서 JPA를 의존하고 있으니 QueryDSL도 Domain에서 의존하게 하여 간편하게 가져가자." 라고 결론이 났다. **(마치 common에 몰아넣기 같은 문제가 벌써 발생하고 있다.)**

이렇게 Domain 영역에 QueryDSL의 의존성이 추가되게 되었다. 
하지만, QueryDSL이 담겨있는 코드는 QueryDSL이 있는 Infra 영역에서 구현하게 끔 하는 것이 어떤가 하는 의견이 나왔고, 이렇게 함으로써 QueryDSL을 작성하기로 한 Module이 Domain 모듈을 의존하고 있기 때문에 DIP를 활용하여 코드 구조를 깔끔하게 가져 갈 수 있을 것이라고 생각했다.

여기서, 두 번째 이슈가 발생한다.

## 두 번째 이슈

QueryDSL을 이용하여 구현한 class를 해당 클래스의 의존관계에서는 위와 같이 참조가 끊겼기에 classPath에서 찾지 못하였고 Spring Data Jpa가 해당 메서드명에 맞는 쿼리를 자동 생성하고자 시도하고 명명규칙이 맞지 않아 생성 할 수 없는 문제가 발생하게 되었다.

해당 문제를 해결하기 위해서는 해당 classPath를 알도록 어떠한 중간 매개체가 필요했다.
이러한 문제를 해결하기 위해 인프라 구조를 다음과 같이 가져가게 되었다.

![Image3](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_3.png)

Mapper 모듈에서 querydsl의 모듈을 컴파일단에서 의존하고 application에서 Mapper 모듈을 의존하도록 하였다.

이러한 구조를 가지게 되어서 이전처럼 Spring Data Jpa가 해당 클래스를 알 수 있게 되었다. 위와 같이 멀티 모듈을 구성을 했을 때 우리 팀은 "와, 진짜 깔끔하게 의존성을 분리했다!" 라는 생각을 가지고 있었다.

그리고 위와 같은 이슈를 멘토님에게 피드백을 요청드렸다. 위와 같은 구조를 가져가면서 이슈를 설명드렸다. 그리고 위 처럼 모듈을 분리한 것에 대해서 어떻게 생각하는지 의견을 여쭤봤다.

모듈을 잘 분리한 것 같다는 대답을 예상했던 것과 달리 너무나도 복잡하게 분리한 것 같다는 답변을 듣게 되었다. 결국 개발은 혼자 하는게 아니라 다른 사람이 해당 프로젝트에 참여했을 때 프로젝트의 구조를 빠르게 파악 할 수 있어야 하는데, 멘토님과 다른 팀의 서브멘토님의 말씀으로는

"제가 이 프로젝트에 참여하게 된다면 의존성 분리를 이해하기가 너무 어려울 것 같다는 생각이 가장 먼저 들었다. 레이어 별로 모듈을 분리를 한 것은 좋지만 좀 더 간단하게 분리를 하는 것이 좋지 않을까"

하는 답변을 듣게 되었다.

해당 피드백을 듣고 난 이후 queryDSL에 대한 모듈에 대한 의존성을 다음과 같이 분리를 하게 되었다.

![Image4](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_4.png)

queryDSL을 외부 의존성으로 보지말고 JPA 사용한다면 queryDSL을 거의 확정적으로 사용하게 될 것이라고 판단했기 때문이다.

## 세 번째 이슈

QueryDSL은 다음과 같은 이유로 모듈을 분리하지 않게 되었다. 하지만 export 모듈로 사용 할 Redis와 MongoDB는 외부의 모듈로 분리해야한다고 생각했다.
이를 다음과 같이 모듈 분리를 시도하였다.

![Image5](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_5.png)

하지만, 이렇게 모듈을 분리를 하고 난 이후 들게 된 생각은 인프라 모듈 자체에 대한 의존성이 너무 높아진 부분이다.

의존성을 역전하기 위해서 아래 사진처럼 Application 영역에 인터페이스를 두고 infra 모듈이 애플리케이션 영역을 의존하여 확장성을 높이고자 하였다.

![Image6](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_6.png)

하지만 두 번째 이슈와 비슷한 문제가 발생을 했다. 
infra 모듈에 대한 참조가 끊겨서 구성 클래스를 찾을 수 없는 문제가 생긴 것이다. 이러한 문제를 해결하기 위해서는 실행 모듈이 있는 api가 참조 가능한 어떠한 모듈에서 infra 모듈을 결국 참조해야만 했다. 
이를 해결하기 위한 방법을 고민을 해봤으나, 가장 간단한 방법은 api 모듈이 infra 모듈을 다음과 같이 참조하도록 하면 문제가 해결이 되긴 했다.

![Image7](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_7.png)

하지만, 이 방법이 올바르다는 생각이 들지 않았다.

애시당초 이렇게 할 때쯤 레이어 별로 멀티모듈을 나누었는데 "지금 우리가 하고 있는 이 방식이 레이어 별로 멀티 모듈을 잘 분리하고 있는 것이 맞는가? 이렇게 하는게 맞는가?"에 대한 고민을 하게 되었다.

## 결론

최종적으로 위와 같은 구조로 가지고 갔지만 다시 한 번 멀티 모듈을 설계한다면 다음과 같이 설계 할 것 같다.

![Image8](https://github.com/bombo-dev/Learning-code/blob/main/project-multimodule/image/image_8.png)

너무나도 많은 추상화는 복잡도를 높이고, 오히려 코드의 가독성을 떨구기도 하며 빈 주입, 테스트 등 여러가지 문제점이 너무나도 많이 발생하게 되었다.

우아한 멀티모듈에서도 위와 비슷한 여러가지 이슈들을 겪게 되었다고 하였는데, 비슷한 이슈들이 많이 발생을 했던 것으로 보인다.

모듈에 대한 의존성 참조를 최소화 하기 위하여 되도록 implementation을 사용하려고 하였지만, 왜 배민에서도 참조되는 모듈에 대해서는 api(컴파일 의존성)을 적용하여 의존성을 강하게 두었는지에 대해서도 새삼 느낄 수 있는 부분이었다.
