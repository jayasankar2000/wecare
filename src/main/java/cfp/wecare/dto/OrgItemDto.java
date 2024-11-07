package cfp.wecare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgItemDto {
    private String orgItemId;
    private String orgId;
    private String itemId;
}
