package cfp.wecare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrgmDto {
    private String pgmId;
    private String pgmName;
    private LocalDate stDate;
    private LocalDate edDate;
}
