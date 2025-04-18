package cfp.wecare.transaction.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaymentResponseDto {
    private String transactionId;
    private String transactionStatus;
    private Integer errorCode;
    private String user;
    private List<String> items;
}
