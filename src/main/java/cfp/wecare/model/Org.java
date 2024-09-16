package cfp.wecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Org {
    @Id
    private String orgId;
    private String orgName;
    private String address;
    @Lob
    private String description;
    private String orgAdmin;
}
