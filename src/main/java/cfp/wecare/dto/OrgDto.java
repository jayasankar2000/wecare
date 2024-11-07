package cfp.wecare.dto;

import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
