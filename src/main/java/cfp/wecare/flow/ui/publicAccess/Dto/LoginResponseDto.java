package cfp.wecare.flow.ui.publicAccess.Dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String login;
    private String accessToken;
}
