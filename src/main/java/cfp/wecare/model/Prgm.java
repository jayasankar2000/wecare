package cfp.wecare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_prgm")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prgm {
    @Id
    private String pgmId;
    private String pgmName;
    private Date stDate;
    private Date edDate;
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Org> orgs;
}
