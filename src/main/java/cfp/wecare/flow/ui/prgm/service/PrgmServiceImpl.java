package cfp.wecare.flow.ui.prgm.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.prgm.Exception.PrgmException;
import cfp.wecare.flow.ui.prgm.repository.PrgmRepository;
import cfp.wecare.model.Org;
import cfp.wecare.model.Prgm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class PrgmServiceImpl implements PrgmService {

    @Autowired
    private PrgmRepository prgmRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PrgmDto> getPrgms() {
        try {
            List<Prgm> prgms = (List<Prgm>) prgmRepository.findAll();
            return prgms.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapperToDto)
                    .collect(toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching Programs failed with internal error");
        }
    }

    @Override
    public PrgmDto getPrgm(String prgmId) {
        try {
            Optional<Prgm> prgm = prgmRepository.findById(prgmId);
            if (prgm.isEmpty()) {
                throw new PrgmException(HttpStatus.NOT_FOUND, "Program not found");
            }
            Prgm dbPrgm = prgm.get();
            List<Org> orgs = dbPrgm.getOrgs();
            List<OrgDto> orgDtos = new ArrayList<>();
            for (Org org : orgs) {
                orgDtos.add(modelMapper.map(org, OrgDto.class));
            }
            return PrgmDto.builder()
                    .pgmId(dbPrgm.getPgmId())
                    .pgmName(dbPrgm.getPgmName())
                    .stDate(dbPrgm.getStDate())
                    .edDate(dbPrgm.getEdDate())
                    .orgs(orgDtos)
                    .build();
        } catch (PrgmException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e);
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching program details failed with internal error");
        }
    }

    @Override
    public PrgmDto savePrgm(PrgmDto prgmDto) {
        try {
            Prgm prgm = modelMapper.map(prgmDto, Prgm.class);
            prgm.setPgmId(UUID.randomUUID().toString());
            Prgm savedPrgm = prgmRepository.save(prgm);
            return mapperToDto(savedPrgm);
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create a program " + e.getMessage());
        }
    }

    @Override
    public PrgmDto updateProgram(String prgmId, PrgmDto prgmDto) {
        try {
            Optional<Prgm> optionalPrgm = prgmRepository.findById(prgmId);
            if (optionalPrgm.isEmpty()) {
                throw new PrgmException(HttpStatus.NOT_FOUND, "The program does not exist");
            }
            Prgm prgm = optionalPrgm.get();
            prgm.setPgmId(prgmDto.getPgmId());
            prgm.setPgmName(prgmDto.getPgmName());
            prgm.setStDate(prgmDto.getStDate());
            prgm.setEdDate(prgmDto.getEdDate());
            Prgm savedPrgm = prgmRepository.save(prgm);
            return modelMapper.map(savedPrgm, PrgmDto.class);
        } catch (PrgmException exception) {
            throw exception;
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update program " + e.getMessage());
        }
    }

    @Override
    public void deleteProgram(String prgmId) {
        try {
            if (!prgmRepository.existsById(prgmId)) {
                throw new PrgmException(HttpStatus.NOT_FOUND, "The program does not exist");
            }
            prgmRepository.deleteById(prgmId);
        } catch (PrgmException exception) {
            throw exception;
        } catch (Exception e) {
            throw new PrgmException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete program " + e.getMessage());
        }
    }

    private PrgmDto mapperToDto(Prgm prgm) {
        return modelMapper.map(prgm, PrgmDto.class);
    }
}
