package cfp.wecare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private String itemId;
    private String itemName;
    private Integer itemPrice;
    private Integer quantity;
}
