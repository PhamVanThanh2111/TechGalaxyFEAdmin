package iuh.fit.se.techgalaxy.frontend.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PaymentResponse {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class VNPayResponseCreate {
        private String code;
        private String message;
        private String paymentUrl;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class VNPayResponse {

        private String txnRef;               // Mã giao dịch thanh toán
        private String amount;               // Số tiền thanh toán
        private String orderInfo;            // Thông tin đơn hàng
        private String responseCode;         // Mã phản hồi từ VNPAY
        private String transactionNo;        // Mã giao dịch tại VNPAY
        private String bankCode;             // Mã ngân hàng thanh toán
        private String cardType;             // Loại thẻ (Visa, Mastercard, etc.)
        private String payDate;              // Thời gian thanh toán
        private String transactionStatus;    // Tình trạng giao dịch
        private String secureHash;           // Chữ ký bảo mật từ VNPAY
        private String signValue;            // Chữ ký tính toán trên server để kiểm tra tính hợp lệ
        private String bankTranNo;           // Mã giao dịch tại ngân hàng
    }
}
