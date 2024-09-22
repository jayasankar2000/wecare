package cfp.wecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private String itemId;
    private String itemName;
    private Integer itemPrice;
    private Integer quantity;
}
