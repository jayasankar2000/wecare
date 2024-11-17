package cfp.wecare.dto;

import cfp.wecare.model.Item;
import cfp.wecare.model.Prgm;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgDto {
    private String orgId;
    private String orgName;
    private String address;
    private String description;
    private String orgAdmin;
    private List<Item> items;
    private Prgm program;
}
