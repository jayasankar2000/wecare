package cfp.wecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_org_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgItem {
    @Id
    private String orgItemId;
    private String orgId;
    private String itemId;
}
