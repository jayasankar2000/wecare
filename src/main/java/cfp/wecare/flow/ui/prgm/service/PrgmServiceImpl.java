package cfp.wecare.flow.ui.prgm.service;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.repository.PrgmRepository;
import cfp.wecare.model.Prgm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
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
        Prgm prgm = prgmRepository.findById(prgmId).orElseThrow(() -> new PrgmException(HttpStatus.NOT_FOUND, "Program not found"));
        return mapperToDto(prgm);
    }

    @Override
    public PrgmDto savePrgm(PrgmDto prgmDto) {
        System.out.println("in save Prgm");
        try {
            Prgm prgm = prgmRepository.save(modelMapper.map(prgmDto, Prgm.class));
            return mapperToDto(prgm);
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create a program " + e.getMessage());
        }
    }

    @Override
    public PrgmDto updateProgram(String prgmId, PrgmDto prgmDto) {
        try {
            Prgm prgm = modelMapper.map(prgmDto, Prgm.class);
            prgm.setPgmId(prgmId);
            return mapperToDto(prgmRepository.save(prgm));
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update program " + e.getMessage());
        }
    }

    @Override
    public void deleteProgram(String prgmId) {
        try {
            prgmRepository.deleteById(prgmId);
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete program " + e.getMessage());
        }
    }

    private PrgmDto mapperToDto(Prgm prgm) {
        return modelMapper.map(prgm, PrgmDto.class);
    }
}
