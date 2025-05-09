package com.rich.sol_bot.i18n;

public class ViValue {
    // deal
    public static final String dealTokenToChoose = "⚙️ Nhập địa chỉ hợp đồng bạn muốn mua/bán";
    public static final String dealPinRefreshText = "Làm mới";
    public static final String dealFastModeText = "Chế độ nhanh";
    public static final String dealProtectModeText = "Chế độ bảo vệ";
    public final static String dealToShowPLText = "Tạo biểu đồ lợi nhuận";
    public final static String dealLimitText = "Đặt lệnh";
    public final static String dealToIcebergText = "Chiến lược Iceberg (Chống kẹp mạnh, chia nhỏ đơn hàng lớn)";
    public final static String dealToIcebergBuyText = "Mua theo chiến lược Iceberg";
    public final static String dealToIcebergSellText = "Bán theo chiến lược Iceberg";
    public final static String dealToIcebergReturnText = "Quay lại";
    public static final String dealTokenPagePreText = "Trang trước";
    public static final String dealTokenPageNexText = "Trang sau";
    public final static String dealConfirmSuccessText = "Xác nhận đặt lệnh";
    public final static String dealConfirmCancelText = "Hủy bỏ";
    public static final String dealLimitFastModeText = "Chế độ nhanh";
    public static final String dealLimitProtectModeText = "Chế độ bảo vệ";
    public final static String dealLimitPxBuyText = "Mua theo lệnh giới hạn giá";
    public final static String dealLimitPxSellText = "Bán theo lệnh giới hạn giá";
    public final static String dealLimitRateBuyText = "Mua theo tỷ lệ thay đổi giá";
    public final static String dealLimitRateSellText = "Bán theo tỷ lệ thay đổi giá";
    public final static String dealLimitListOrdersText = "Xem các lệnh đặt của đồng này";
    public final static String dealLimitReturnText = "Quay lại";
    public final static String dealLimitPxBuyContent = """
        Vui lòng nhập giá giới hạn và số lượng mua, cách nhau bởi dấu phẩy.
        Ví dụ: Nhập 0.001,0.1 có nghĩa là khi giá đạt 0.001 USDT, tự động mua 0.1 SOL
        
        Giá hiện tại: `%s` (Nhấn để sao chép)""";
    public final static String dealLimitPxSellContent = """
        Vui lòng nhập giá giới hạn và số lượng bán, cách nhau bởi dấu phẩy.
        Ví dụ: Nhập 0.001,50 có nghĩa là khi giá đạt 0.001 USDT, tự động bán 50%%
        
        Giá hiện tại: `%s` (Nhấn để sao chép)""";
    public final static String dealLimitRateBuyContent = """
        Vui lòng nhập tỷ lệ thay đổi giá và số lượng mua, cách nhau bởi dấu phẩy. Số dương đại diện cho tăng (chốt lời), số âm đại diện cho giảm (chốt lỗ).
        Ví dụ:
        Nhập 50,0.01 có nghĩa là tăng 50%, mua 0.01 SOL
        Nhập -50,0.01 có nghĩa là giảm 50%, mua 0.01 SOL
        
        Giá hiện tại: `%s` (Nhấn để sao chép)""";
    public final static String dealLimitRateSellContent = """
        Vui lòng nhập tỷ lệ thay đổi giá và số lượng bán, cách nhau bởi dấu phẩy. Số dương đại diện cho tăng (chốt lời), số âm đại diện cho giảm (chốt lỗ).
        Ví dụ:
        Nhập 50,50 có nghĩa là tăng 50%, bán 50%%
        Nhập -50,50 có nghĩa là giảm 50%, bán 50%%
        
        Giá hiện tại: `%s` (Nhấn để sao chép)""";
    public final static String dealLimitCreateSuccess = """
        ✅ %s - Đặt lệnh thành công
        
        Hướng lệnh: %s
        Giá hiện tại: %s USDT
        Giá lệnh: %s USDT
        Số lượng/giá trị lệnh: %s %s
        
        ⚠️ Lệnh sẽ chỉ được thực hiện khi giá đạt mức giá lệnh, do biến động giá hoặc thay đổi số lượng token, giá thực tế có thể có sự sai lệch.
        """;
    public final static String dealLimitOrderKeyBoardFoot = """
        Đặt lệnh:
        Hướng: %s
        Giá token: %s USDT
        Số lượng: %s %s
        Phí ưu tiên: %s SOL
        Thời gian còn lại: %s""";
    public final static String dealOwnedLimitOrderContent = " ⚔\uFE0F Danh sách các lệnh đặt đã cấu hình";
    public final static String dealOwnedLimitOrderNoContent = " Không có lệnh đặt nào đã cấu hình";
    public final static String dealOwnedCloseAllOrderText = "Đóng tất cả lệnh đặt";
    public final static String dealLimitOrderRefreshText = "Làm mới";
    public final static String dealLimitOrderDeleteText = "Xóa lệnh đặt";
    public final static String dealLimitOrderReturnAllListText = "Quay lại";
    public final static String dealLimitOrderCancelAllByTokenText = "Hủy tất cả lệnh đặt của đồng này";
    public final static String dealPumpLaunchFastText = "Bán 100% chỉ với 1 lần nhấn (Chế độ nhanh)";
    public final static String dealPumpLaunchProtectText = "Bán 100% chỉ với 1 lần nhấn (Chế độ bảo vệ)";
    public final static String dealBuyTextPrefix = "Mua ";
    public final static String dealSellTextPrefix = "Bán ";
    public static final String dealImportAmountBuy = "Vui lòng nhập số tiền mua, nhập 0.1 có nghĩa là mua 0.1 SOL, sau khi nhập sẽ tự động gửi lệnh mua\n";
    public static final String dealImportAmountSell = "Vui lòng nhập tỷ lệ bán, nhập 0.5 có nghĩa là bán 50%, sau khi nhập sẽ tự động gửi lệnh bán\n";
    public static final String dealKeyboardHead = """
        \uD83D\uDD04 *Chế độ mua/bán* - %s
        Địa chỉ hợp đồng: ` %s`
        
        """;
    public static final String dealLimitKeyboardHead = """
        \uD83D\uDD04 *Chế độ đặt lệnh* - %s
        Địa chỉ hợp đồng: ` %s`
        
        """;
    public static final String dealSubmitSuccess = "Lệnh giao dịch đã được gửi, vui lòng chờ kết quả giao dịch thành công!\n";
    public static final String dealSuccessContent = """
        ✅ Giao dịch thành công! %s
        Thông tin giao dịch trên chuỗi [Nhấn để xem](%s)
        %s %s -> %s %s\s
        Giá mua/bán trung bình: %s
        
        Số dư ví:
        %s %s
        %s %s
        """;
    public static final String dealSuccessNormalContent = """
        ✅ Giao dịch thành công!
        Thông tin giao dịch trên chuỗi [Nhấn để xem](%s)
        %s %s -> %s %s\s
        Giá mua/bán trung bình: %s
        
        Số dư ví:
        %s %s
        %s %s
        """;
    public static final String dealSuccessLimitContent = """
        Đặt lệnh - %s
        ✅ Giao dịch thành công!
        Thông tin giao dịch trên chuỗi [Nhấn để xem](%s)
        %s %s -> %s %s\s
        Giá mua/bán trung bình: %s
        
        Số dư ví:
        %s %s
        %s %s
        """;
    public static final String dealSuccessIcePart = """
        ✅ Giao dịch thành công! %s
        Thông tin giao dịch trên chuỗi [Nhấn để xem](%s)
        %s %s -> %s %s\s
        Giá mua/bán trung bình: %s
        """;
    public static final String iceDealSuccessContentLast = """
        ✅ Thực hiện chiến lược Iceberg thành công
        Số dư ví:
        %s %s
        %s %s
        """;
    public static final String dealFailLimitPrefix = "Đặt lệnh - %s \n";
    public static final String dealFailContentPrefix = "❌ Giao dịch thất bại! %s \n";
    public static final String dealFailContent = """
        Vui lòng đảm bảo có đủ số dư để thanh toán phí gas, trượt giá và các chi phí khác!""";
    public final static String dealToIcebergBuyLongText = """
        Vui lòng nhập số tiền mua mỗi lần và số lần giao dịch, cách nhau bởi dấu phẩy. Ví dụ: Nhập 0.1,5 có nghĩa là tổng số tiền mua là 0.5 SOL, chia thành 5 lần mua, mỗi lần mua 0.1 SOL, thanh toán 5 lần phí gas (số lần giao dịch tối đa có thể nhập là 10)""";
    public final static String dealToIcebergSellLongText = """
        Vui lòng nhập tỷ lệ phần trăm bán mỗi lần và số lần giao dịch, cách nhau bởi dấu phẩy. Ví dụ: Nhập 0.2,5 có nghĩa là bán 100% số dư, chia thành 5 lần bán, mỗi lần bán 20%, thanh toán 5 lần phí gas (số lần giao dịch tối đa có thể nhập là 10)""";
    public final static String dealToConfirmTwice = """
        Đặt lệnh lớn - Xác nhận lần 2
        
        Số tiền giao dịch: %s SOL
        Chế độ giao dịch: %s
        
        ⚠️ Đối với lệnh lớn, nên sử dụng chế độ bảo vệ hoặc chiến lược Iceberg để chia nhỏ lệnh""";
    public final static String dealTokenInfoTimeAfter = "Đếm ngược: %s ngày %s giờ %s phút nữa sẽ mở giao dịch";
    public final static String dealTokenInfoTimeBefore = "Thời gian tạo: %s ngày %s giờ %s phút trước khi mở giao dịch";

    // ----invite----------------------
    public static final String inviteKeyBoardContent = """
        \uD83D\uDD17 Liên kết mời: %s\s
        \uD83D\uDCB5 Có thể rút: %s SOL (%s SOL đang xử lý)
        \uD83D\uDCB0 Tổng rút: %s SOL   
        \uD83D\uDC65 Tổng số người mời: %s người\s
        \uD83D\uDCD6 Quy tắc:
        1. Mời người khác sử dụng, bạn sẽ kiếm được phí giao dịch của họ mãi mãi, với tỷ lệ %s
        2. Số tiền rút tối thiểu là 0.01 SOL
        """;
    public static final String inviteRefreshText = "Làm mới";
    public static final String inviteWithdrawText = "Rút về ví";
    public static final String inviteChooseWalletHead = "⚙️ Chọn ví bạn muốn rút";
    public static final String inviteChooseWalletOtherText = "Ví khác";
    public static final String inviteChooseWalletReturnText = "Quay lại";
    public static final String inviteWaitWalletInput = """
        ⚙️ Vui lòng nhập địa chỉ nhận SOL lần rút này (chỉ hỗ trợ mạng SOL)
        ⚠️ Vui lòng kiểm tra kỹ địa chỉ, sau khi chuyển tiền không thể hoàn lại, bạn sẽ chịu trách nhiệm về các tổn thất nếu nhập sai địa chỉ!!!""";
    public static final String inviteTransferSuccessText = """
        ✅ Chuyển SOL - Giao dịch thành công!
        Thông tin giao dịch trên chuỗi [Nhấn để xem](%s)
        Chuyển SOL: %s SOL
        """;
    // ------position-----------
    public static final String positionWalletChoose = "\uD83D\uDCC8 Xem vị trí - Vui lòng chọn ví\n\n";
    public static final String positionWalletChooseText = "Ví  ";
    public static final String positionReturnToWalletListText = "Quay lại";
    public static final String positionSwitchWalletText = "Chuyển ví";
    public static final String positionRefreshText = "Làm mới";
    public static final String positionPrePageText = "Trang trước";
    public static final String positionNextPageText = "Trang sau";
    public static final String positionMapIsShowing = "Đang tạo biểu đồ lợi nhuận...";
    // ------scraper----------
    public static final String scraperNewTaskText = "\uD83D\uDD2A Thêm nhiệm vụ mới";
    public static final String scraperStartImportNameMsg = """
        ⚙️ Nhập tên Twitter mà bạn muốn lấy dữ liệu (chỉ cần nhập phần sau @)
        """;
    public static final String scraperStartTwitterErrMsg = """
        Lỗi: Không tìm thấy tên Twitter này, vui lòng nhập lại
        ⚙\uFE0F Nhập tên Twitter mà bạn muốn lấy dữ liệu (chỉ cần nhập phần sau @)""";
    public static final String scraperStartInputAmountMsg = "Vui lòng nhập số tiền, nhập 0.1 có nghĩa là mua 0.1 SOL, sau khi nhập sẽ tạo nhiệm vụ lấy dữ liệu";
    public static final String scraperSendSuccess = """
        ✅ Nhiệm vụ lấy dữ liệu %s - Giao dịch đã được bắt đầu
        \uD83C\uDD94 ID Twitter: %s
        Tên mã thông báo: %s
        Địa chỉ hợp đồng: %s
        Số lần kích hoạt nhiệm vụ hiện tại: %s
        """;
    public static final String scraperRefreshText = "Làm mới";
    public static final String scraperExtraGasText = "Tiền hối lộ gas";
    public static final String scraperCountText = "Số lần thực hiện";
    public static final String scraperCountImportMsg = "Vui lòng nhập số lần thực hiện nhiệm vụ lấy dữ liệu, nhập 5 có nghĩa là thực hiện thành công 5 lần và tự động xóa nhiệm vụ, nhập 0 có nghĩa là không giới hạn số lần, mặc định là 1 lần";
    public static final String scraperFastModeText = "Chế độ nhanh";
    public static final String scraperProtectModeText = "Chế độ bảo vệ";
    public static final String scraperAmountContent = "- - Số tiền mua một lần - -";
    public static String generateDealBuyTextPrefix = "Mua ";
    public static final String scraperRmTaskText = "❌ Xóa nhiệm vụ";
    public static final String scraperNoTask = """
        
        🔪 Không có nhiệm vụ lấy dữ liệu
        
        """;
    public static final String scraperTaskInfoTemplate = """
        \uD83D\uDD2A Nhiệm vụ lấy dữ liệu %s
        
        \uD83C\uDD94 ID Twitter: %s
        ✅ Số lần lấy dữ liệu thành công: %s
        
        \uD83D\uDC5B Số dư ví: %s SOL
        \uD83D\uDD22 Số lần thực hiện nhiệm vụ: %s
        \uD83D\uDCB0 Số tiền mua một lần: %s SOL
        """;
    // ----------setting
    public static final String settingStart = """
        Slippage chế độ nhanh: %s
        Slippage chế độ bảo vệ: %s
        Phí ưu tiên mua: %s SOL
        Phí ưu tiên bán: %s SOL
        Phí ưu tiên săn: %s SOL
        Ví mặc định: %s""";
    public static final String settingFastSlippageText = "Cài đặt slippage - Chế độ nhanh";
    public static final String settingProtectSlippageText = "Cài đặt slippage - Chế độ bảo vệ";
    public static final String settingBuyGasText = "Phí ưu tiên gas mua";
    public static final String settingSellGasText = "Phí ưu tiên gas bán";
    public static final String settingSniperGasText = "Phí ưu tiên gas săn";
    public static final String settingJitoGasText = "Phí ưu tiên chế độ bảo vệ";
    public static final String settingPreferWalletText = "Cài đặt ví mặc định";
    public static final String settingAutoSellText = "Tự động bán";
    public static final String settingAutoSellContent = """
        Khi bật tự động bán, hệ thống sẽ tự động đặt lệnh bán có giới hạn cho toàn bộ token bạn đã mua theo các tham số bạn đã cài đặt.
        Bạn có thể tự do cấu hình tỉ lệ thay đổi giá và tỷ lệ bán ra. Nếu mua nhiều lần cùng một token, hệ thống sẽ tạo ra nhiều lệnh đặt và bạn có thể hủy thủ công trong mục “Lệnh đã cấu hình” từ menu chính.
        """;
    public static final String settingAutoSellSwitchText = "Đã bật";
    public static final String settingAutoSellConfigText = "Thêm cấu hình lệnh đặt";
    public static final String settingAutoSellConfig = """
        Vui lòng nhập tỉ lệ thay đổi giá và tỷ lệ bán tự động, phân cách bằng dấu phẩy.
        Ví dụ:
        Nhập 50,100 có nghĩa là giá tăng 50% và bán 100%
        Nhập -50,100 có nghĩa là giá giảm 50% và bán 100%""";
    public static final String settingForFast = """
        Vui lòng nhập slippage
        Chế độ nhanh, khuyến nghị 10-20, không thấp hơn 5
        Chế độ bảo vệ, khuyến nghị 20-50, đảm bảo tỷ lệ thành công""";
    public static final String settingForProtect = """
        Vui lòng nhập slippage
        Chế độ nhanh, khuyến nghị 10-20, không thấp hơn 5
        Chế độ bảo vệ, khuyến nghị 20-50, đảm bảo tỷ lệ thành công""";
    public static final String settingForBuyGas = """
        Vui lòng nhập phí ưu tiên mua (quyết định tốc độ giao dịch), phạm vi từ 0-1 SOL
        Phí ưu tiên hiện tại là %s SOL
        Giao dịch thông thường khuyến nghị 0.003 SOL, giao dịch nhanh khuyến nghị 0.005 SOL, chế độ nhanh khuyến nghị 0.01 SOL hoặc cao hơn""";
    public static final String settingForSellGas = """
        Vui lòng nhập phí ưu tiên bán (quyết định tốc độ giao dịch), phạm vi từ 0-1 SOL
        Phí ưu tiên hiện tại là %s SOL
        Giao dịch thông thường khuyến nghị 0.003 SOL, giao dịch nhanh khuyến nghị 0.005 SOL, chế độ nhanh khuyến nghị 0.01 SOL hoặc cao hơn""";
    public static final String settingForSniper = """
        Vui lòng nhập phí ưu tiên săn (quyết định tốc độ giao dịch), phạm vi từ 0-1 SOL
        Phí ưu tiên hiện tại là %s SOL""";
    public static final String settingForJito = """
        Vui lòng nhập phí ưu tiên chế độ bảo vệ (quyết định tốc độ giao dịch), phạm vi từ 0-1 SOL
        Phí ưu tiên hiện tại là %s SOL""";
    public static final String settingPreferWalletHead = "⚙️ Cài đặt ví mặc định\n";
    public static final String isModeOnText = "✅ Đã bật tự động bán";
    public static final String isModeOffText = "❌ Đã tắt tự động bán";
    public static String isModeOn(boolean isOn) {
        if(isOn) return isModeOnText;
        else return isModeOffText;
    }
    public static final String settingPreferWalletReturnText = "Quay lại";
    // ----------sniper
    public static final String sniperInputTokenContent = "⚙️ Nhập địa chỉ hợp đồng mà bạn muốn săn\n";
    public static final String sniperInputAmountContent = """
        ⚙️ Nhập chi tiêu tối đa cho mỗi ví
        Định dạng SOL được phép nhập là 0.00, tối đa nhập hai chữ số thập phân""";
    public static final String sniperInputExtraGasContent = """
        ⚙️ Nhập phí gas bạn muốn trả cho tự động săn
        Ý nghĩa: Trả thêm bao nhiêu SOL để hối lộ node và hoàn thành giao dịch nhanh hơn, áp dụng cho tự động săn, giá trị nhập tối thiểu là 0.01
        Định dạng SOL được phép nhập là 0.00, tối đa nhập hai chữ số thập phân
        """;
    public final static String sniperCreateSuccessKeyBoardHead = """
        \uD83D\uDD2B *Tự động săn* - %s
        Địa chỉ hợp đồng: `%s`
        """;
    public final static String sniperKeyboardRefreshText = "Làm mới";
    public final static String sniperSlippageFastModeText = "Slippage - Chế độ nhanh";
    public final static String sniperSlippageProtectModeText = "Slippage - Chế độ bảo vệ";
    public final static String sniperExtraGasText = "Phí hối lộ gas";
    public final static String sniperDeleteText = "Xóa săn này";
    public final static String sniperListNameTextList = "Danh sách săn tự động\n\n";
    public final static String sniperListNameText = "Săn tự động";
    public static String sniperBindButton(Boolean bind) {
        if(bind) return "\uD83D\uDFE2 ";
        return "\uD83D\uDD34 ";
    }
    public static String generateDealBuyText = "Mua ";
    public static final String setFastModeSuccess = "✅ Đặt chế độ nhanh thành công";
    public static final String setProtectModeSuccess = "✅ Đặt chế độ bảo vệ thành công";
    public static final String deleteSnapPlanSuccess = "✅ Xóa cấu hình săn thành công";
    public static final String chooseAtLeastWallet = "❌ Vui lòng chọn ít nhất một ví";
    public static final String sniperCreatedSuccess = "✅ Cấu hình săn tự động thành công";
    public static final String sniperPumpLaunchContent = """
        \uD83D\uDE80 PUMP %s - %s
        Địa chỉ hợp đồng: `%s`
        
        \uD83D\uDCDD Thuộc nhóm: RAY\s
        \uD83D\uDCB8 Giá token: %s USDT
        \uD83D\uDC8E Giá trị tài sản: %s SOL""";
    // ------ start
    public static final String initStart =
            """
                    Chào mừng bạn đến với Duck Sniper Solana Bot của Duck Sniper
    
                    \uD83D\uDCAC Quy tắc sử dụng
                    1. Hãy chắc chắn rằng bạn bảo vệ kỹ càng khóa riêng của ví, Bot không lưu trữ thông tin khóa riêng của bạn
                    2. Trước khi thực hiện bất kỳ giao dịch đầu tư nào, vui lòng tìm hiểu chi tiết về dự án. ❗ Lưu ý quan trọng: Đầu tư có rủi ro mất toàn bộ tài sản
                    3. Chúng tôi khuyến khích bạn sử dụng liên kết mời để mời bạn bè tham gia Duck Sniper, bạn sẽ được hưởng 40% hoa hồng trọn đời \uD83C\uDF7B
                    4. Nếu gặp bất kỳ vấn đề nào, vui lòng tham gia cộng đồng Duck Sniper và liên hệ với quản trị viên để được hỗ trợ: https://t.me/rushduckarmy
    
                    Cảm ơn bạn đã sử dụng và ủng hộ chúng tôi!""";

    public static final String initStartNoWallet = """
        Phát hiện bạn là người dùng mới hoặc đã ngắt kết nối ví, vui lòng đến phần "Ví" để tạo hoặc nhập ví.
        Nếu gặp bất kỳ vấn đề nào, vui lòng tham gia cộng đồng Duck Sniper và liên hệ với quản trị viên để được hỗ trợ: https://t.me/rushduckarmy""";

    public static final String initStartDefaultWallet = """
        Địa chỉ ví: `%s`\s
        Số dư: %s SOL ($%s)
        
        Nhấn nút "Làm mới" để cập nhật số dư hiện tại của bạn. Nếu gặp bất kỳ vấn đề nào, vui lòng tham gia cộng đồng Duck Sniper và liên hệ với quản trị viên để được hỗ trợ: https://t.me/rushduckarmy""";
    public static final String initStartRefreshText = "Làm mới";
    public static final String initStartAutoSniperText = "\uD83D\uDD2B Tự động săn";
    public static final String initStartDealText = "\uD83E\uDD1D Mua/Bán";
    public static final String initStartOwnedSniperText = "⚔️ Đã thiết lập săn";
    public static final String initStartOwnedLimitOrderText = "\uD83D\uDDD2 Đã cấu hình lệnh giới hạn";
    public static final String initStartScraperText = "\uD83D\uDD2A Cạo";
    public static final String initStartPositionText = "\uD83D\uDCC8 Xem vị thế";
    public static final String initStartWalletText = "\uD83D\uDCB0 Ví";
    public static final String initStartSettingText = "⚙️ Cài đặt";
    public static final String initStartInviteText = "\uD83D\uDC65 Mời và kiếm hoa hồng";
    public static final String initStartHelpText = "\uD83D\uDCD6 Trợ giúp";
    public static final String returnToInitStartText = "Quay lại";
    public static final String returnToInitStartText2 = "\uD83C\uDFE0 Menu chính";
    public static final String initStartHelpContent = """
        📖 Ví
    Bạn có thể kết nối ví theo hai cách để bắt đầu giao dịch.
    1. Nhập khóa riêng để kết nối ví đã có
    2. Sử dụng Duck Sniper Bot để tạo ví mới
    Có hai điểm cần lưu ý:
    ⛔️ Không nhập ví chính hoặc ví chứa tài sản lớn
    ⛔️ Không chia sẻ khóa riêng

    📖 Quản lý ví
    Duck Sniper Bot hỗ trợ kết nối tối đa 10 ví.
    Bạn có thể thay đổi ví mặc định, thiết lập biệt danh cho ví, v.v.

    📖 Tự động săn
    Duck Sniper Bot hỗ trợ săn tại các pool Raydium, chỉ cần gửi địa chỉ hợp đồng để thiết lập số tiền và chế độ bảo vệ. Khi dự án mở bán, bot sẽ tự động mua vào. Trong cài đặt, bạn có thể điều chỉnh phí ưu tiên săn để tăng tốc độ, khuyến nghị đặt là 0.01 SOL trở lên để giảm tỷ lệ thất bại và nhanh chóng.

    📖 Giao dịch Pump Token
    pump.fun là nền tảng phát hành token nhanh trên Solana, các dự án mới sau khi hoàn thành bonding curve, sẽ không thêm thanh khoản vào pool Raydium trong thời gian ngắn.
    Nếu bạn muốn giao dịch dự án pump, hãy gửi địa chỉ hợp đồng hoặc liên kết pump cho Duck Sniper Bot để chọn mua/bán.

    📖 Mua/Bán
    Khi bạn cần mua/bán một dự án cụ thể, chỉ cần nhập địa chỉ hợp đồng, hệ thống sẽ tự động nhận diện thông tin dự án. Bao gồm dữ liệu dự án, nhóm người đầu tư, thông tin bảo mật, giúp bạn ra quyết định giao dịch nhanh chóng.
    Giao dịch có thể chọn chế độ bảo vệ hoặc chế độ nhanh, chế độ bảo vệ an toàn hơn, giúp tránh mất tài sản.

    📖 Mua/Bán nhanh
    Nhấn vào số tiền mua/bán, hệ thống sẽ tự động thực hiện giao dịch. Nếu số tiền mặc định của hệ thống không phù hợp với yêu cầu của bạn, nhấn vào tùy chọn cuối cùng Mua x SOL/ Bán x% và nhập số tiền mua tùy chỉnh.

    📖 Lệnh giới hạn
    Duck Sniper Bot hỗ trợ lệnh giới hạn, giúp bạn dễ dàng mua/bán, không cần phải theo dõi thị trường.
    Bạn có thể đặt lệnh theo giá xác định hoặc theo tỷ lệ thay đổi giá, khi giá token đạt giá mục tiêu, giao dịch sẽ được kích hoạt ngay lập tức.
    Gửi địa chỉ hợp đồng và nhấn nút [Lệnh giới hạn] để bắt đầu cài đặt, nhập giá mục tiêu (tỷ lệ thay đổi) và số tiền mua/bán tự động, cách nhau bằng dấu phẩy. Ví dụ, nhập 0.1,1 có nghĩa là khi giá đạt $0.1, sẽ tự động mua 1 SOL. Khi giá đạt mức này, giao dịch sẽ được kích hoạt, giúp bạn mua vào.

    📖 Thực hiện giao dịch
    Hệ thống thường sẽ tự động phát hành ủy quyền sau khi mua/bán, giúp giảm thiểu thời gian chờ.
    Khi giao dịch thành công, bạn sẽ thấy thông báo giao dịch thành công, tức là giao dịch thực tế đã được xác nhận trên blockchain. Bạn có thể xem số tiền giao dịch thực tế, số lượng token đã mua và giá khi giao dịch, cùng số dư ví hiện tại.

    📖 Chiến lược Iceberg
    Chiến lược Iceberg là một chiến lược chia nhỏ các đơn hàng lớn khi giao dịch. Khi thực hiện giao dịch số lượng lớn, để tránh bị bot săn, bạn có thể sử dụng chiến lược này để tự động chia giao dịch thành nhiều đơn hàng nhỏ (phí gas sẽ tăng theo số lượng đơn hàng)

    📖 Xem vị thế
    Hỗ trợ xem tên token hiện tại, giá trị vị thế, sự thay đổi giá trị và thời gian giữ vị thế.

    📖 Mời và kiếm hoa hồng
    Chức năng mời và kiếm hoa hồng, sao chép liên kết mời, chia sẻ cho người dùng mới, bạn sẽ nhận được 40% hoa hồng từ giao dịch của họ (tối thiểu 0.01 SOL có thể rút)

    📖 Cài đặt
    Cài đặt mặc định đủ để đáp ứng nhu cầu giao dịch của người mới. Các tham số giao dịch được khuyến nghị có tỷ lệ thành công cao, tốc độ lên blockchain nhanh và nguy cơ bị săn thấp, người dùng có thể tham khảo sử dụng. Nếu token giao dịch biến động lớn hoặc muốn tăng tốc độ giao dịch, bạn có thể điều chỉnh trượt giá và cài đặt gas.
                                                                                                          
    🏘️ [Cộng đồng chính thức](https://t.me/rushduckarmy)
    🐦 [Liên kết Twitter](https://x.com/DuckSniper_Bot)
        """;
    // ---- wallet
    public static final String walletCreateText = "Tạo ví";
    public static final String walletImportText = "Nhập ví";
    public static final String walletRemoveText = "Ngắt kết nối ví";
    public static final String walletSetNameText = "Cài đặt tên ví";
    public static final String walletExportPriText = "Xuất khóa riêng ví";
    public static final String walletTransferSOLText = "Chuyển SOL";
    public static final String walletListTitle = "Danh sách ví của bạn:\n\n";
    public static final String walletShowContent = """
        Ví của bạn: %s\s
        Địa chỉ ví: ` %s `\s
        Số dư SOL: %s SOL

        """;
    public static final String createWalletStart = """
        Chào mừng bạn đến với Duck Sniper SOL Bot.
        Hệ thống hiện chưa phát hiện bất kỳ thông tin ví nào, có thể bạn là người dùng mới hoặc đã xóa hết ví.
        Để tiếp tục, vui lòng tạo hoặc nhập ví.
        """;
    public static final String createWalletName = "✍️ Hãy đặt tên cho ví của bạn, giới hạn trong 10 ký tự, có thể sử dụng chữ Hán, chữ cái, và số.";
    public static final String createWalletSuccess = """
        ✅ Ví đã được tạo thành công.
        ⚠️ Hãy chắc chắn bảo vệ kỹ càng khóa riêng của bạn. Khóa riêng sẽ không được hiển thị lại.
        \uD83D\uDCA1 Khóa riêng: [%s]
        Ví SOL của bạn đã được thêm thành công, địa chỉ ví là: ` %s `
        """;

    public static final String importWalletKey = "⚙️Nhập khóa riêng ví của bạn\n\n";
    public static final String importWalletSuccess = """
        ✅ Ví đã được nhập thành công
        Đã thêm ví: %s\s
        Số dư: %s SOL""";
    public static final String walletToChooseContent = "⚙️Vui lòng chọn ví";
    public static final String walletToSetNameContent = "⚙️Đặt tên cho ví của bạn, tối đa 10 ký tự, hỗ trợ chữ Hán, chữ cái, và số\n";
    public static final String walletToTransferSolContent = "⚙️Nhập số lượng SOL cần chuyển\n\nĐịnh dạng SOL hợp lệ là 0.0000, tối đa 4 chữ số thập phân";
    public static final String walletTransferInputAddress = """
        ⚙️Nhập địa chỉ nhận SOL chuyển (chỉ hỗ trợ mạng SOL)

        ⚠️ Lưu ý kiểm tra kỹ địa chỉ, sau khi giao dịch được gửi đi sẽ không thể hoàn tác. Mọi thiệt hại do nhập sai địa chỉ sẽ do bạn chịu trách nhiệm!!!""";
    public static final String walletTransferSubmitSuccess = "Giao dịch đã được gửi, vui lòng chờ kết quả giao dịch!\n\n";
    public static final String walletTransferFailText = "❌ Số dư SOL không đủ, chuyển SOL thất bại";
    public static final String walletTransferSuccessText = """
        ✅ Chuyển SOL thành công!
        Thông tin giao dịch trên blockchain [Nhấn để xem](%s)
        SOL chuyển: %s SOL
        Số dư ví: %s SOL""";
    public static final String tokenStaticContentPart1Sniper =
            """
                    *Token*：
                    \uD83D\uDCB0 Giá trị thị trường hiện tại: %s USDT\s
                    \uD83D\uDCB8 Giá token: %s USDT\s
                    \uD83D\uDD1D Tỷ lệ top 10: %s\s
    
                    *Pool*：
                    \uD83C\uDFE6 Số tiền trong pool: %s SOL\s
                    📝 Thuộc về pool: %s %s\s
                    ⏳ %s\s
                    Không có phát hành thêm (%s) Hủy quyền (%s) Đốt pool (%s)\s
    
                    """;
    public static final String tokenStaticContentPart2 = """
        *Nhóm người dùng*：
        \uD83D\uDC65 Số người tham gia: %s\s
        ⚖️ Tỷ lệ lãi/lỗ: %s : %s\s

        """;
    public static final String tokenStaticContentPart3 = """
        *Liên kết*：
        [X](%s) | [Telegram](%s)
        [DexScreener](%s) | [Birdeye](%s) | [Dextools](%s) | [Pump](%s) | [GMGN](%s) \s

        """;
    public static final String tokenWalletContentPart4Sniper = """
        *Săn token*：
        \uD83D\uDC5B Số dư ví: %s SOL
        \uD83D\uDCB0 Số tiền săn: %s SOL
        \uD83D\uDCA8 Phí ưu tiên săn: %s SOL
        """;
    public static final String tokenWalletContentPart4Deal = """
        *Ví*：
        \uD83D\uDCB5 Số dư hiện tại: %s SOL \s
        \uD83D\uDCB1 Giá mua trung bình: %s USDT\s
        \uD83D\uDC8E Giá trị vị thế: %s SOL\s
        \uD83D\uDCB9 Biến động vị thế: %s\s
        """;


    public static final String dealAutoSellNoConfig = "Hiện tại chưa có cấu hình";
    public static final String dealAutoSellConfigTitle = "Đã cấu hình:\n";
    public static final String dealAutoSellConfigContent = "Cấu hình %s: Tăng %s bán %s\n";
    public static final String dealAutoSellConfigCancel = "Hủy cấu hình";
    public static final String initStartLanguageText = "\uD83C\uDF0D Chuyển ngôn ngữ\n";

}
