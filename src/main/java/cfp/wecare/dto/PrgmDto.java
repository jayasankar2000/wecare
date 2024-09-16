package cfp.wecare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrgmDto {
    private String pgmId;
    private String pgmName;
    private Date stDate;
    private Date edDate;
}
