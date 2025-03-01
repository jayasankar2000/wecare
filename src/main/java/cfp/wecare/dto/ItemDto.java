package cfp.wecare.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
}
