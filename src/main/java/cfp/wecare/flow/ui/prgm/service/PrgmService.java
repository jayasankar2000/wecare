package cfp.wecare.flow.ui.prgm.service;

import cfp.wecare.dto.PrgmDto;
import org.springframework.stereotype.Service;

import java.util.List;
public interface PrgmService {
    public List<PrgmDto> getPrgms();

    public PrgmDto getPrgm(String prgmId);

    public PrgmDto savePrgm(PrgmDto prgmDto);

    public PrgmDto updateProgram(String PrgmId, PrgmDto prgmDto);

    public void deleteProgram(String prgmId);
}
