package iuh.fit.se.techgalaxy.frontend.admin.dto.request;

import iuh.fit.se.techgalaxy.frontend.admin.entities.ProductVariantDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    String orderCode;
    String paymentInfo;
    String shippingAddress;
    String orderNumber;
    String symbol;
    String invoiceDate;
    String invoiceNumber;
    String customerName;
    String taxCode;
    String searchCode;
    List<ProductVariantDetail> productVariantDetails;
}
