package cfp.wecare.service;

import cfp.wecare.Repository.PrgmRepository;
import cfp.wecare.dto.PrgmDto;
import cfp.wecare.model.Prgm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PrgmServiceImpl implements PrgmService {

    @Autowired
    private PrgmRepository prgmRepository;

    @Override
    public List<PrgmDto> getPrgms() {
        List<Prgm> prgms = (List<Prgm>) prgmRepository.findAll();
        return prgms.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
    }

    private PrgmDto mapper(Prgm prgm) {
        return PrgmDto.builder()
                .pgmId(prgm.getPgmId())
                .pgmName(prgm.getPgmName())
                .edDate(prgm.getEdDate())
                .stDate(prgm.getStDate())
                .build();
    }
}
