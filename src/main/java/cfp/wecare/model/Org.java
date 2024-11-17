package cfp.wecare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_org")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Org {
    @Id
    private String orgId;
    private String orgName;
    private String address;
    @Lob
    private String description;
    private String orgAdmin;
    @ManyToOne
    @JoinColumn(name = "Prgm_Id", nullable = false)
    private Prgm program;
    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
}
