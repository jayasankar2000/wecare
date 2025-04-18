package cfp.wecare.dto;

import cfp.wecare.transaction.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private String itemId;
    private String itemName;
    private Integer itemPrice;
    private Integer quantity;
    @JsonBackReference
    private OrgDto orgDto;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
