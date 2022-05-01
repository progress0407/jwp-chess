# Level 2 체스

## 1단계 요구사항

- [x] 페어와 함께 Spark Web App 을 Spring 으로 옮긴다
- [x] 페어와 함께 옮긴 Spring Web App 을 다시 본인 것으로 옮긴다
  - (왜냐하면 레벨 1 때 리펙터링 등의 요구사항을 충분히 만족하지 못했기 때문...)
- [x] 본인의 것을 Spring으로 전환한다
- [x] 리펙터링을 한다
  - [x] 체스의 움직임을 button 클릭이 아닌 마우스 커서 클릭으로 변경한다 
  - [x] 선택된 기물은 색이 칠해진다
- [x] 여러 브라우저를 켜서 진행하여도 체스 미션이 진행되게끔한다
- [x] 프로덕션과 테스트 환경을 별도로 준비한다
 
# Level 1 체스

# 기능 요구 사항
## 1단계 요구사항
- 체스 게임을 할 수 있는 체스판을 초기화한다.
- 체스판에서 말의 위치 값은 가로 위치는 왼쪽부터 a ~ h이고, 세로는 아래부터 위로 1 ~ 8로 구현한다.
  - [x] 체스판의 행은 아래부터 위로 8개의 `Rank`를 가진다.
  - [x] 체스판의 열은 왼쪽부터 오른쪽으로 8개의 `File`을 가진다.
- [x] 체스판에서 각 진영은 검은색(대문자)과 흰색(소문자) 편으로 구분한다.
  - 검은색 진영은 위쪽, 흰색 진영은 아래쪽에 위치한다.
### 입출력
- [x] 게임 시작, 종료 사용자 입력을 받는다.
- [x] 게임 시작을 입력 받았을 경우 체크보드와 기물을 초기화하여 보여준다.
- [x] 종료를 입력하였을 경우 게임을 종료한다
### 출력 예시
```html
체스 게임을 시작합니다.
게임 시작은 start, 종료는 end 명령을 입력하세요.
start
RNBQKBNR
PPPPPPPP
........
........
........
........
pppppppp
rnbqkbnr

end
```

## 2단계 요구사항
- 체스 말의 이동 규칙을 찾아보고 체스 말이 이동할 수 있도록 구현한다.
- move source위치 target위치을 실행해 이동한다.

## 3단계 요구사항
- 체스 게임은 상대편 King이 잡히는 경우 게임에서 진다. King이 잡혔을 때 게임을 종료해야 한다.
- 체스 게임은 현재 남아 있는 말에 대한 점수를 구할 수 있어야 한다.
- "status" 명령을 입력하면 각 진영의 점수를 출력하고 어느 진영이 이겼는지 결과를 볼 수 있어야 한다.

### 점수 계산 규칙
- 체스 프로그램에서 현재까지 남아 있는 말에 따라 점수를 계산할 수 있어야 한다.
- 각 말의 점수는 queen은 9점, rook은 5점, bishop은 3점, knight는 2.5점이다.
- pawn의 기본 점수는 1점이다. 하지만 같은 세로줄에 같은 색의 폰이 있는 경우 1점이 아닌 0.5점을 준다.
- king은 잡히는 경우 경기가 끝나기 때문에 점수가 없다.
- 한 번에 한 쪽의 점수만을 계산해야 한다.

## 4 ~ 5 단계 요구사항

- Spark 기반의 웹 MVC 프레임워크를 이용해서 체스 Web App 을 만든다 !