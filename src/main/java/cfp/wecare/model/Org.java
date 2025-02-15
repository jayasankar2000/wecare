package cfp.wecare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_org")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Org {
    @Id
    private String orgId;
    private String orgName;
    private String address;
    private String description;
    private String orgAdmin;
    @ManyToOne
    @JoinColumn(name = "Prgm_Id", nullable = false)
    private Prgm program;
    @OneToMany(mappedBy = "org", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
}
