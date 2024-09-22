package cfp.wecare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "t_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String userId;
    private String userName;
    private String userType;
}
