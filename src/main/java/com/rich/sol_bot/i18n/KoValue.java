package com.rich.sol_bot.i18n;

public class KoValue {
    // deal
    public static final String dealTokenToChoose = "⚙️ 거래할 계약 주소를 입력하세요.";
    public static final String dealPinRefreshText = "새로고침";
    public static final String dealFastModeText = "빠른 모드";
    public static final String dealProtectModeText = "클램핑 방지 모드";
    public final static String dealToShowPLText = "수익 그래프 생성";
    public final static String dealLimitText = "예약 주문";
    public final static String dealToIcebergText = "빙산 전략 (강력한 클램핑 방지, 대규모 주문 분할)";
    public final static String dealToIcebergBuyText = "빙산 전략 구매";
    public final static String dealToIcebergSellText = "빙산 전략 판매";
    public final static String dealToIcebergReturnText= "돌아가기";
    public static final String dealTokenPagePreText = "이전 페이지";
    public static final String dealTokenPageNexText = "다음 페이지";
    public final static String dealConfirmSuccessText = "주문 확인";
    public final static String dealConfirmCancelText = "취소";
    public static final String dealLimitFastModeText = "빠른 모드";
    public static final String dealLimitProtectModeText = "클램핑 방지 모드";
    public final static String dealLimitPxBuyText = "지정가 매수";
    public final static String dealLimitPxSellText = "지정가 매도";
    public final static String dealLimitRateBuyText = "변동폭 매수";
    public final static String dealLimitRateSellText = "변동폭 매도";
    public final static String dealLimitListOrdersText = "이 토큰의 예약 주문 보기";
    public final static String dealLimitReturnText = "돌아가기";
    public final static String dealLimitPxBuyContent = """
    지정 가격과 매수 금액을 쉼표로 구분하여 입력하세요.
    예: 0.001,0.1을 입력하면 0.001 USDT 가격에 도달 시 0.1 SOL을 자동으로 매수합니다.

    현재 가격: `%s` (클릭하여 복사)""";
    public final static String dealLimitPxSellContent = """
    지정 가격과 매도 비율을 쉼표로 구분하여 입력하세요.
    예: 0.001,50을 입력하면 0.001 USDT 가격에 도달 시 50%%를 자동으로 매도합니다.

    현재 가격: `%s` (클릭하여 복사)""";
    public final static String dealLimitRateBuyContent = """
    가격 변동폭과 매수 금액을 쉼표로 구분하여 입력하세요. 양수는 상승(익절), 음수는 하락(손절)을 의미합니다.
    예:
    50,0.01을 입력하면 50%% 상승 시 0.01 SOL 매수
    -50,0.01을 입력하면 50%% 하락 시 0.01 SOL 매수를 의미합니다.

    현재 가격: `%s` (클릭하여 복사)""";
    public final static String dealLimitRateSellContent = """
    가격 변동폭과 매도 비율을 쉼표로 구분하여 입력하세요. 양수는 상승(익절), 음수는 하락(손절)을 의미합니다.
    예:
    50,50을 입력하면 50%% 상승 시 50%% 매도
    -50,50을 입력하면 50%% 하락 시 50%% 매도를 의미합니다.

    현재 가격: `%s` (클릭하여 복사)""";
    public final static String dealLimitCreateSuccess = """
    ✅ %s-예약 주문 설정 성공

    주문 방향: %s
    현재 가격: %s  USDT
    주문 가격: %s USDT
    주문 금액/수량: %s %s

    ⚠️ 예약 주문은 현재 가격이 주문 가격에 도달해야 실행됩니다. 가격 변동이나 토큰 수량 변화로 인해 실제 매수 가격에 오차가 발생할 수 있습니다.
    """;
    public final static String dealLimitOrderKeyBoardFoot = """
    예약 주문：
    방향: %s
    토큰 가격: %s USDT
    금액/수량: %s %s
    우선 순위 수수료: %s SOL
    남은 시간: %s""";

    public final static String dealOwnedLimitOrderContent = " ⚔️ 설정된 예약 주문 목록";
    public final static String dealOwnedLimitOrderNoContent = " 설정된 예약 주문 없음";
    public final static String dealOwnedCloseAllOrderText = "모든 예약 주문 취소";
    public final static String dealLimitOrderRefreshText = "새로고침";
    public final static String dealLimitOrderDeleteText = "예약 주문 삭제";
    public final static String dealLimitOrderReturnAllListText = "돌아가기";
    public final static String dealLimitOrderCancelAllByTokenText = "해당 토큰의 모든 예약 주문 취소";
    public final static String dealPumpLaunchFastText = "한 번에 100% 판매 (빠른 모드)";
    public final static String dealPumpLaunchProtectText = "한 번에 100% 판매 (클램핑 방지 모드)";
    public final static String dealBuyTextPrefix = "구매 ";
    public final static String dealSellTextPrefix = "판매 ";

    public static final String dealImportAmountBuy = "구매 금액을 입력하세요. 0.1을 입력하면 0.1 SOL을 매수합니다. 입력 후 즉시 거래가 제출됩니다.\n";
    public static final String dealImportAmountSell = "판매 비율을 입력하세요. 0.5를 입력하면 50%를 매도합니다. 입력 후 즉시 거래가 제출됩니다.\n";

    public static final String dealKeyboardHead = """
    \uD83D\uDD04 *매수/매도 모드* - %s
    계약 주소: `%s`
    """;

    public static final String dealLimitKeyboardHead = """
    \uD83D\uDD04 *예약 주문 모드* - %s
    계약 주소: `%s`
    """;

    public static final String dealSubmitSuccess = "거래가 제출되었습니다. 성공을 기다려 주세요!\n";

    public static final String dealSuccessContent = """
    ✅ 거래 성공! %s
    온체인 거래 정보 [여기에서 확인](%s)
    %s %s -> %s %s
    체결 평균 가격: %s

    지갑 잔액:
    %s %s
    %s %s
    """;

    public static final String dealSuccessNormalContent = """
    ✅ 거래 성공!
    온체인 거래 정보 [여기에서 확인](%s)
    %s %s -> %s %s
    체결 평균 가격: %s

    지갑 잔액:
    %s %s
    %s %s
    """;

    public static final String dealSuccessLimitContent = """
    예약 주문 - %s
    ✅ 거래 성공!
    온체인 거래 정보 [여기에서 확인](%s)
    %s %s -> %s %s
    체결 평균 가격: %s

    지갑 잔액:
    %s %s
    %s %s
    """;

    public static final String dealSuccessIcePart = """
    ✅ 거래 성공! %s
    온체인 거래 정보 [여기에서 확인](%s)
    %s %s -> %s %s
    체결 평균 가격: %s
    """;

    public static final String iceDealSuccessContentLast = """
    ✅ 빙산 전략 실행 성공
    지갑 잔액:
    %s %s
    %s %s
    """;

    public static final String dealFailLimitPrefix = "예약 주문 - %s \n";
    public static final String dealFailContentPrefix = "❌ 거래 실패! %s \n";
    public static final String dealFailContent = """
    가스비, 슬리피지 등의 비용을 지불할 충분한 잔액을 확보하세요!""";

    public final static String dealToIcebergBuyLongText = """
    단일 구매 금액과 거래 횟수를 쉼표로 구분하여 입력하세요. 예: 0.1,5를 입력하면 총 0.5 SOL을 5회에 걸쳐 구매하며, 각 거래 당 0.1 SOL이 구매됩니다. 가스비는 5회 지불됩니다. (최대 10회 거래 가능)""";

    public final static String dealToIcebergSellLongText = """
    단일 판매 비율과 거래 횟수를 쉼표로 구분하여 입력하세요. 예: 0.2,5를 입력하면 전체 잔액의 100%를 5회에 걸쳐 판매하며, 각 거래 당 20%를 판매합니다. 가스비는 5회 지불됩니다. (최대 10회 거래 가능)""";

    public final static String dealToConfirmTwice = """
    대규모 주문 - 두 번째 확인

    거래 금액: %s SOL
    거래 모드: %s

    ⚠️ 대규모 주문은 클램핑 방지 모드나 빙산 전략으로 주문을 분할하는 것을 권장합니다.""";

    public final static String dealTokenInfoTimeAfter = "카운트다운: %s일 %s시간 %s분 후 거래 시작";
    public final static String dealTokenInfoTimeBefore = "생성 시간: %s일 %s시간 %s분 전 거래 시작";

    // Invite
    public static final String inviteKeyBoardContent = """
    \uD83D\uDD17 초대 링크: %s\s
    \uD83D\uDCB5 출금 가능: %s SOL（처리 중: %s SOL）
    \uD83D\uDCB0 누적 출금: %s SOL \
    \uD83D\uDC65 누적 초대: %s 명\s
    \uD83D\uDCD6 규칙:
    1. 다른 사용자를 초대하면 그들의 거래 수수료의 %s을 영구적으로 얻을 수 있습니다.
    2. 최소 0.01 SOL 이상일 경우 출금 가능합니다.
    """;

    public static final String inviteRefreshText = "새로 고침";
    public static final String inviteWithdrawText = "지갑으로 출금";
    public static final String inviteChooseWalletHead = "⚙️ 출금을 위한 지갑 선택";
    public static final String inviteChooseWalletOtherText = "다른 지갑";
    public static final String inviteChooseWalletReturnText = "돌아가기";
    public static final String inviteWaitWalletInput = """
    ⚙️ 이번 출금을 받을 SOL 주소를 입력하세요 (SOL 네트워크만 지원)
    ⚠️ 주소를 확인하십시오. 송금 후에는 취소할 수 없으며, 주소 입력 오류로 인한 자금 손실은 책임지지 않습니다!
    """;

    public static final String inviteTransferSuccessText = """
    ✅ SOL 송금 성공!
    온체인 거래 정보 [여기에서 확인](%s)
    송금된 SOL: %s SOL
    """;

    // Position
    public static final String positionWalletChoose = "\uD83D\uDCC8 보유 자산 조회 - 지갑을 선택하세요\n\n";
    public static final String positionWalletChooseText = "지갑 ";
    public static final String positionReturnToWalletListText = "돌아가기";
    public static final String positionSwitchWalletText = "지갑 전환";
    public static final String positionRefreshText = "새로 고침";
    public static final String positionPrePageText = "이전 페이지";
    public static final String positionNextPageText = "다음 페이지";
    public static final String positionMapIsShowing = "수익 그래프 생성 중...";

    // Scraper
    public static final String scraperNewTaskText = "\uD83D\uDD2A 새로운 작업 추가";
    public static final String scraperStartImportNameMsg = """
    ⚙️ 스크래핑할 트위터 이름을 입력하세요 (아이디 @ 뒤의 부분만 입력)
    """;

    public static final String scraperStartTwitterErrMsg = """
    오류: 해당 트위터 이름을 찾을 수 없습니다. 다시 입력하세요.
    ⚙️ 스크래핑할 트위터 이름을 입력하세요 (아이디 @ 뒤의 부분만 입력)
    """;

    public static final String scraperStartInputAmountMsg = "금액을 입력하세요. 0.1을 입력하면 0.1 SOL 매수 작업이 생성됩니다.";
    public static final String scraperSendSuccess = """
    ✅ 스크래핑 작업 %s - 거래가 시작되었습니다.
    \uD83C\uDD94 트위터 ID: %s
    토큰 이름: %s
    계약 주소: %s
    현재 작업 트리거 횟수: %s
    """;

    public static final String scraperRefreshText = "새로 고침";
    public static final String scraperExtraGasText = "추가 가스비";
    public static final String scraperCountText = "실행 횟수";
    public static final String scraperCountImportMsg = "스크래핑 작업 실행 횟수를 입력하세요. 5를 입력하면 5번 성공적으로 실행 후 작업이 자동 삭제됩니다. 0을 입력하면 횟수 제한이 없습니다. 기본값은 1회입니다.";
    public static final String scraperFastModeText = "고속 모드";
    public static final String scraperProtectModeText = "클램핑 방지 모드";
    public static final String scraperAmountContent = "- - 단일 매수 금액 - -";
    public static String generateDealBuyTextPrefix = "구매 ";
    public static final String scraperRmTaskText = "❌ 작업 삭제";
    public static final String scraperNoTask = """
    🔪 현재 스크래핑 작업이 없습니다.
    """;

    public static final String scraperTaskInfoTemplate = """
    \uD83D\uDD2A 스크래핑 작업 %s
    
    \uD83C\uDD94 트위터 ID: %s
    ✅ 성공 횟수: %s
    
    \uD83D\uDC5B 지갑 잔액: %s SOL
    \uD83D\uDD22 실행 횟수: %s
    \uD83D\uDCB0 단일 매수 금액: %s SOL
    """;
    // setting
    public static final String settingStart = """
    초고속 모드 슬리피지: %s
    방어 모드 슬리피지: %s
    매수 우선 수수료: %s SOL
    매도 우선 수수료: %s SOL
    스나이퍼 우선 수수료: %s SOL
    기본 지갑: %s""";

    public static final String settingFastSlippageText = "슬리피지 설정 - 초고속 모드";
    public static final String settingProtectSlippageText = "슬리피지 설정 - 방어 모드";
    public static final String settingBuyGasText = "매수 가스 우선 수수료";
    public static final String settingSellGasText = "매도 가스 우선 수수료";
    public static final String settingSniperGasText = "스나이퍼 가스 우선 수수료";
    public static final String settingJitoGasText = "방어 모드 우선 수수료";
    public static final String settingPreferWalletText = "기본 지갑 설정";
    public static final String settingAutoSellText = "자동 매도";

    public static final String settingAutoSellContent = """
    자동 매도를 활성화하면 시스템이 설정한 매개변수에 따라 매수한 모든 토큰에 대해 자동으로 지정가 매도 주문을 추가합니다.
    가격 상승/하락률과 매도 비율을 자유롭게 설정할 수 있습니다. 동일한 토큰을 여러 번 매수하면 시스템이 여러 개의 설정된 주문을 생성하며, 메인 메뉴의 '설정된 주문'에서 수동으로 취소할 수 있습니다.
    """;

    public static final String settingAutoSellSwitchText = "활성화됨";
    public static final String settingAutoSellConfigText = "주문 설정 추가";
    public static final String settingAutoSellConfig = """
    가격 상승/하락률과 자동 매도 비율을 쉼표로 구분하여 입력하세요.
    예시:
    50,100을 입력하면 가격이 50% 상승 시 100% 매도
    -50,100을 입력하면 가격이 50% 하락 시 100% 매도를 의미합니다.
    """;

    public static final String settingForFast = """
    슬리피지를 입력하세요:
    초고속 모드: 권장 10-20, 최소 5 이상.
    방어 모드: 권장 20-50, 성공률 보장.
    """;

    public static final String settingForProtect = """
    슬리피지를 입력하세요:
    초고속 모드: 권장 10-20, 최소 5 이상.
    방어 모드: 권장 20-50, 성공률 보장.
    """;

    public static final String settingForBuyGas = """
    매수 우선 수수료를 입력하세요 (거래 속도를 결정함), 범위 0-1 SOL.
    현재 우선 수수료: %s SOL.
    일반 거래는 0.003 SOL, 빠른 거래는 0.005 SOL, 초고속은 0.01 SOL 이상을 권장합니다.
    """;

    public static final String settingForSellGas = """
    매도 우선 수수료를 입력하세요 (거래 속도를 결정함), 범위 0-1 SOL.
    현재 우선 수수료: %s SOL.
    일반 거래는 0.003 SOL, 빠른 거래는 0.005 SOL, 초고속은 0.01 SOL 이상을 권장합니다.
    """;

    public static final String settingForSniper = """
    스나이퍼 우선 수수료를 입력하세요 (거래 속도를 결정함), 범위 0-1 SOL.
    현재 우선 수수료: %s SOL.
    """;

    public static final String settingForJito = """
    방어 모드 우선 수수료를 입력하세요 (거래 속도를 결정함), 범위 0-1 SOL.
    현재 우선 수수료: %s SOL.
    """;

    public static final String settingPreferWalletHead = "⚙️ 기본 지갑 설정\n";
    public static final String isModeOnText = "✅ 자동 매도 활성화됨";
    public static final String isModeOffText = "❌ 자동 매도 비활성화됨";

    public static String isModeOn(boolean isOn) {
        return isOn ? isModeOnText : isModeOffText;
    }

    public static final String settingPreferWalletReturnText = "돌아가기";

    // sniper
    public static final String sniperInputTokenContent = "⚙️ 스나이핑하려는 계약 주소를 입력하세요\n";
    public static final String sniperInputAmountContent = """
    ⚙️ 각 지갑에서 허용할 최대 지출 금액을 입력하세요.
    SOL 형식으로 0.00을 입력할 수 있으며, 소수점 이하 두 자리까지 허용됩니다.
    """;
    public static final String sniperInputExtraGasContent = """
    ⚙️ 자동 스나이핑에 사용할 추가 가스비를 입력하세요.
    의미: 거래를 더 빨리 완료하기 위해 노드에 뇌물로 추가로 지불할 SOL 금액입니다. 자동 스나이핑에 사용되며, 최소 입력 값은 0.01 SOL입니다.
    SOL 형식으로 0.00을 입력할 수 있으며, 소수점 이하 두 자리까지 허용됩니다.
    """;

    public static final String sniperCreateSuccessKeyBoardHead = """
    \uD83D\uDD2B *자동 스나이핑*-%s
    계약 주소: `%s`
    """;

    public static final String sniperKeyboardRefreshText = "새로 고침";
    public static final String sniperSlippageFastModeText = "슬리피지 - 초고속 모드";
    public static final String sniperSlippageProtectModeText = "슬리피지 - 방어 모드";
    public static final String sniperExtraGasText = "추가 가스비";
    public static final String sniperDeleteText = "이 스나이핑 삭제";
    public static final String sniperListNameTextList = "자동 스나이핑 목록\n\n";
    public static final String sniperListNameText = "자동 스나이핑";

    public static String sniperBindButton(Boolean bind) {
        return bind ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ";
    }

    public static String generateDealBuyText = "구매 ";
    public static final String setFastModeSuccess = "✅ 초고속 모드 설정 성공";
    public static final String setProtectModeSuccess = "✅ 방어 모드 설정 성공";
    public static final String deleteSnapPlanSuccess = "✅ 스나이핑 설정 삭제 성공";
    public static final String chooseAtLeastWallet = "❌ 최소한 하나 이상의 지갑을 선택하세요.";
    public static final String sniperCreatedSuccess = "✅ 자동 스나이핑 설정 성공";
    public static final String sniperPumpLaunchContent = """
    \uD83D\uDE80 PUMP %s - %s
    계약 주소: `%s`
    
    \uD83D\uDCDD 풀 소유: RAY\s
    \uD83D\uDCB8 토큰 가격: %s USDT
    \uD83D\uDC8E 보유 가치: %s SOL
    """;
    // start
    public static final String initStart =
            """
            Duck Sniper Solana Bot에 오신 것을 환영합니다!
    
            \uD83D\uDCAC 사용 안내\s
            1. 지갑 개인 키를 안전하게 보관하세요. Bot은 귀하의 개인 키 정보를 저장하지 않으며, 저장할 수도 없습니다.
            2. 투자 활동에 앞서 반드시 프로젝트 정보를 철저히 이해하세요. ❗주의: 투자에는 자산 상실의 위험이 있습니다.\s
            3. 초대 링크를 통해 친구를 Duck Sniper에 초대하면 영구적으로 거래 수수료의 40%를 리워드로 받을 수 있습니다. \uD83C\uDF7B
            4. 사용 중 문제가 발생하면 Rush Duck 커뮤니티에 가입하여 관리자에게 문의하세요: https://t.me/rushduckarmy
    
            이용해 주셔서 감사합니다!
            """;

    public static final String initStartNoWallet = """
    처음 사용하시거나 모든 지갑이 해제된 것으로 감지되었습니다. '지갑' 섹션에서 새 지갑을 생성하거나 기존 지갑을 가져오세요.
    사용 중 문제가 발생하면 Rush Duck 커뮤니티에 가입하여 관리자에게 문의하세요: https://t.me/rushduckarmy""";

    public static final String initStartDefaultWallet = """
    지갑 주소: `%s`\s
    잔액: %s SOL ($%s)
    
    현재 잔액을 업데이트하려면 '새로 고침' 버튼을 클릭하세요. 사용 중 문제가 발생하면 Rush Duck 커뮤니티에 가입하여 관리자에게 문의하세요: https://t.me/rushduckarmy""";

    public static final String initStartRefreshText = "새로 고침";
    public static final String initStartAutoSniperText = "\uD83D\uDD2B 자동 스나이핑";
    public static final String initStartDealText = "\uD83E\uDD1D 구매/판매";
    public static final String initStartOwnedSniperText = "⚔️ 설정된 스나이핑";
    public static final String initStartOwnedLimitOrderText = "\uD83D\uDDD2 설정된 지정가 주문";
    public static final String initStartScraperText = "\uD83D\uDD2A 스크래퍼";
    public static final String initStartPositionText = "\uD83D\uDCC8 포지션 보기";
    public static final String initStartWalletText = "\uD83D\uDCB0 지갑";
    public static final String initStartSettingText = "⚙️ 설정";
    public static final String initStartInviteText = "\uD83D\uDC65 초대 리워드";
    public static final String initStartHelpText = "\uD83D\uDCD6 도움말";
    public static final String returnToInitStartText = "돌아가기";
    public static final String returnToInitStartText2 = "\uD83C\uDFE0 메인 메뉴";
    public static final String initStartHelpContent = """
    📖 지갑
    두 가지 방법으로 지갑을 연결하여 거래를 시작할 수 있습니다.
    1. 개인 키를 가져와 기존 지갑을 연결
    2. Duck Sniper Bot으로 새 지갑 생성
    주의사항:
    ⛔️ 주요 지갑 또는 대규모 자산이 포함된 지갑은 가져오지 마세요.
    ⛔️ 개인 키를 외부에 공개하지 마세요.

    📖 지갑 관리
    Duck Sniper Bot은 최대 10개의 지갑을 지원합니다.
    기본 지갑 변경, 지갑 이름 설정 등이 가능합니다.

    📖 자동 스나이핑
    Duck Sniper Bot은 Raydium 풀의 시작 스나이핑을 지원하며, 계약 주소를 보내기만 하면 금액과 방어 모드를 설정할 수 있습니다. 프로젝트가 시작되면 Bot이 즉시 구매를 실행합니다. 설정에서 스나이핑 우선 가스비를 조정하여 속도를 높일 수 있습니다. 0.01 SOL 이상으로 설정하는 것을 권장합니다.

    📖 Pump 토큰 거래
    pump.fun은 Solana의 빠른 토큰 발행 플랫폼입니다. 새 프로젝트는 Bonding Curve 완료 전까지 Raydium 유동성을 추가하지 않습니다.
    Pump 프로젝트를 거래하려면 계약 주소나 pump 링크를 Duck Sniper Bot에 보내 구매/판매를 선택하세요.

    📖 구매/판매
    특정 프로젝트를 구매/판매하려면 계약 주소를 입력하세요. 시스템은 자동으로 프로젝트 정보를 인식합니다. 프로젝트 데이터, 커뮤니티 정보, 안전 정보 등을 제공하여 빠르게 거래 결정을 내릴 수 있습니다.

    📖 빠른 구매/판매
    금액을 클릭하면 시스템이 자동으로 구매/판매를 완료합니다. 기본 금액이 요구 사항과 다를 경우, 마지막 옵션을 클릭하여 사용자 지정 금액을 입력하세요.

    📖 지정가 주문
    지정가 주문을 설정하여 시장 가격 도달 시 자동으로 거래를 실행합니다. 예를 들어, 0.1,1을 입력하면 $0.1에 도달했을 때 1 SOL을 자동 구매합니다.

    📖 포지션 보기
    현재 포지션의 토큰 이름, 포지션 가치, 상승/하락, 보유 기간을 확인할 수 있습니다.

    📖 초대 리워드
    초대 링크를 통해 새 사용자를 초대하면 거래 수수료의 40%를 영구적으로 리워드로 받을 수 있습니다. (최소 0.01 SOL부터 인출 가능)

    📖 설정
    기본 설정은 초보자 거래 요구를 충족하며, 조정이 필요하지 않습니다. 변동성이 큰 토큰을 거래하거나 속도를 높이고 싶을 경우, 슬리피지 및 가스 설정을 조정하세요.

    🏘️ [공식 커뮤니티](https://t.me/rushduckarmy)
    🐦 [트위터 링크](https://x.com/DuckSniper_Bot)\
    """;
    // wallet
    public static final String walletCreateText = "지갑 생성";
    public static final String walletImportText = "지갑 가져오기";
    public static final String walletRemoveText = "지갑 해제";
    public static final String walletSetNameText = "지갑 이름 설정";
    public static final String walletExportPriText = "지갑 개인 키 내보내기";
    public static final String walletTransferSOLText = "SOL 전송";
    public static final String walletListTitle = "내 지갑 목록:\n\n";
    public static final String walletShowContent = """
    내 지갑: %s\s
    지갑 주소: `%s`\s
    SOL 잔액: %s SOL

    """;
    public static final String createWalletStart = """
    Duck Sniper SOL 봇에 오신 것을 환영합니다.
    현재 시스템에서 지갑 정보를 감지하지 못했습니다. 이는 처음 사용하거나 모든 지갑을 삭제했음을 의미합니다.
    계속 사용하려면 지갑을 생성하거나 가져오세요.
    """;
    public static final String createWalletName = "✍️ 지갑 이름을 설정하세요. 10자 이내로, 한글, 알파벳, 숫자를 조합할 수 있습니다.";
    public static final String createWalletSuccess = """
    ✅ 지갑이 성공적으로 생성되었습니다.
    ⚠️ 개인 키를 반드시 안전하게 보관하세요. 개인 키는 다시 표시되지 않습니다.
    \uD83D\uDCA1 개인 키: [%s]
    생성된 SOL 지갑 주소: `%s`
    """;

    public static final String importWalletKey = "⚙️지갑 개인 키를 입력하세요\n\n";
    public static final String importWalletSuccess = """
    ✅ 지갑 가져오기 성공
    추가된 지갑: %s\s
    잔액: %s SOL""";
    public static final String walletToChooseContent = "⚙️지갑을 선택하세요";
    public static final String walletToSetNameContent = "⚙️지갑 이름을 설정하세요. 10자 이내, 한글, 알파벳, 숫자 조합 가능\n";
    public static final String walletToTransferSolContent = "⚙️전송할 SOL 수량을 입력하세요\n\n입력 형식: 0.0000, 최대 4자리 소수까지 허용";
    public static final String walletTransferInputAddress = """
    ⚙️전송할 SOL의 수신 주소를 입력하세요 (SOL 네트워크만 지원)

    ⚠️ 주소를 반드시 확인하세요. 전송 후 취소할 수 없으며, 주소 오류로 인한 자산 손실은 사용자 책임입니다.
    """;
    public static final String walletTransferSubmitSuccess = "거래가 제출되었습니다. 거래 성공을 기다려주세요!\n\n";
    public static final String walletTransferFailText = "❌ SOL 잔액 부족으로 SOL 전송 실패";
    public static final String walletTransferSuccessText = """
    ✅ SOL 전송 - 거래 성공!
    블록체인 거래 정보 [여기를 클릭](%s)
    전송된 SOL: %s SOL
    지갑 잔액: %s SOL""";

    public static final String tokenStaticContentPart1Sniper = """
    *토큰*:
    \uD83D\uDCB0 현재 시가총액: %s USDT\s
    \uD83D\uDCB8 토큰 가격: %s USDT\s
    \uD83D\uDD1D 상위 10% 보유율: %s\s

    *풀*:
    \uD83C\uDFE6 풀 금액: %s SOL\s
    📝 풀 소유자: %s %s\s
    ⏳ %s\s
    발행 없음 (%s)  권한 포기 (%s) 풀 소각 (%s)\s

    """;

    public static final String tokenStaticContentPart2 = """
    *커뮤니티*:
    \uD83D\uDC65 참여 인원: %s\s
    ⚖️ 손익 비율: %s : %s\s

    """;

    public static final String tokenStaticContentPart3 = """
    *링크*:
    [X](%s) | [Telegram](%s)
    [DexScreener](%s) | [Birdeye](%s) | [Dextools](%s) | [Pump](%s) | [GMGN](%s) \s

    """;

    public static final String tokenWalletContentPart4Sniper = """
    *스나이핑*:
    \uD83D\uDC5B 지갑 잔액: %s SOL
    \uD83D\uDCB0 스나이핑 금액: %s SOL
    \uD83D\uDCA8 스나이핑 우선 가스비: %s SOL
    """;

    public static final String tokenWalletContentPart4Deal = """
    *지갑*:
    \uD83D\uDCB5 현재 잔액: %s SOL \s
    \uD83D\uDCB1 평균 매수가: %s USDT\s
    \uD83D\uDC8E 보유 자산 가치: %s SOL\s
    \uD83D\uDCB9 포지션 손익: %s\s
    """;

    public static final String dealAutoSellNoConfig = "현재 설정된 내용이 없습니다.";
    public static final String dealAutoSellConfigTitle = "설정된 내용:\n";
    public static final String dealAutoSellConfigContent = "설정 %s: 상승 %s 판매 %s\n";
    public static final String dealAutoSellConfigCancel = "설정 취소";
    public static final String initStartLanguageText = "\uD83C\uDF0D 언어 변경\n";




}
