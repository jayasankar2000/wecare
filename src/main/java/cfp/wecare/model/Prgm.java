package cfp.wecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prgm {
    @Id
    private String pgmId;
    private String pgmName;
    private Date stDate;
    private Date edDate;
}
