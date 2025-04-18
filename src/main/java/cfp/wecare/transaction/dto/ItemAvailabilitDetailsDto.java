package cfp.wecare.transaction.dto;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemAvailabilitDetailsDto {
    private List<ItemDto> items;
    private Integer totalAmount;
    private String message;
}
