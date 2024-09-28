package cfp.wecare.flow.ui.prgm.service;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.repository.PrgmRepository;
import cfp.wecare.model.Prgm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrgmServiceImpl implements PrgmService {

    @Autowired
    private PrgmRepository prgmRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PrgmDto> getPrgms() {
        List<Prgm> prgms = (List<Prgm>) prgmRepository.findAll();
        return prgms.stream()
                .map(this::mapperToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PrgmDto getPrgm(String prgmId) {
        Prgm prgm = prgmRepository.findById(prgmId).orElse(null);
        if (prgm != null) {
            return mapperToDto(prgm);
        }
        throw new PrgmException(HttpStatus.NOT_FOUND, "Program not found");
    }

    private PrgmDto mapperToDto(Prgm prgm) {
        return modelMapper.map(prgm, PrgmDto.class);
    }
}
