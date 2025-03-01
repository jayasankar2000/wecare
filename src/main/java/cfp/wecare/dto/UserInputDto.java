package cfp.wecare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInputDto implements Serializable {
    private String userId;
    private String userName;
    private String password;
    private String role;
}
