package cfp.wecare.dto;

import cfp.wecare.model.Item;
import cfp.wecare.model.Prgm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgDto {
    private String orgId;
    private String orgName;
    private String address;
    private String description;
    private String orgAdmin;
    @JsonManagedReference
    private List<ItemDto> items;
    @JsonBackReference
    private PrgmDto prgmDto;
}
