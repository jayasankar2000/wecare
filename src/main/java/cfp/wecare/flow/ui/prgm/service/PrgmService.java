package cfp.wecare.flow.ui.prgm.service;

import cfp.wecare.dto.PrgmDto;

import java.util.List;

public interface PrgmService {
    public List<PrgmDto> getPrgms();

    public PrgmDto getPrgm(String prgmId);
}
