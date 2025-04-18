package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.org.Exception.OrgException;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.flow.ui.prgm.repository.PrgmRepository;
import cfp.wecare.model.Org;
import cfp.wecare.model.Prgm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PrgmRepository prgmRepository;

    @Override
    public List<OrgDto> getAllOrgs() {
        try {
            List<Org> orgs = (List<Org>) orgRepository.findAll();
            List<OrgDto> orgDtos = new ArrayList<>();
            for (Org org : orgs) {
                Prgm prgm = org.getProgram();
                orgDtos.add(orgToDtoMapper(org, prgm));
            }
            return orgDtos;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching Organizations failed with internal error");
        }
    }

    @Override
    public OrgDto getOrg(String orgId) {
        try {
            Optional<Org> org = orgRepository.findById(orgId);
            if (org.isPresent()) {
                return orgToDtoMapper(org.get(), org.get().getProgram());
            }
            throw new OrgException(HttpStatus.NOT_FOUND, "Organization Not found");
        } catch (OrgException ex) {
            throw ex;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching organization details failed with internal server error");
        }
    }

    @Override
    public OrgDto create(OrgDto orgDto) {
        try {
            Org org = modelMapper.map(orgDto, Org.class);
            org.setOrgId(UUID.randomUUID().toString());
            Optional<Prgm> prgm = prgmRepository.findById(orgDto.getPrgmDto().getPgmId());
            if (prgm.isEmpty()) {
                throw new OrgException(HttpStatus.BAD_REQUEST, "The program does not exist");
            }
            Prgm dbPrgm = prgm.get();
            org.setProgram(dbPrgm);
            return orgToDtoMapper(orgRepository.save(org), dbPrgm);
        } catch (OrgException exception) {
            throw exception;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public OrgDto update(String orgId, OrgDto orgDto) {
        try {
            Optional<Org> org = orgRepository.findById(orgId);
            if (org.isEmpty()) {
                throw new OrgException(HttpStatus.NOT_FOUND, "The organization is not present");
            }
            Org newOrg = org.get();
            newOrg.setOrgName(orgDto.getOrgName());
            newOrg.setOrgAdmin(orgDto.getOrgAdmin());
            newOrg.setAddress(orgDto.getAddress());
//            newOrg.setDescription(orgDto.getDescription());
//            Optional<Prgm> prgm = prgmRepository.findById(org.get().getProgram().getPgmId());
//            prgm.ifPresent(newOrg::setProgram);
            return modelMapper.map(orgRepository.save(newOrg), OrgDto.class);
        } catch (OrgException e) {
            throw e;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating the Organization");
        }
    }

    @Override
    public void deleteOrg(String orgId) {
        try {
            if (orgRepository.findById(orgId).isEmpty()) {
                throw new OrgException(HttpStatus.NOT_FOUND, "The Organization is not present");
            }
            orgRepository.deleteById(orgId);
        } catch (OrgException ex) {
            throw ex;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Deleting Organization failed with Internal error");
        }
    }

    @Override
    public List<OrgDto> findOrgsByPrgm(String prgmId) {
        try {
            List<Org> orgs = orgRepository.findAllByProgram(prgmId);
            List<OrgDto> orgDtos = new ArrayList<>();
            for (Org org : orgs) {
                orgDtos.add(orgToDtoMapper(org, org.getProgram()));
            }
            return orgDtos;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Getting organizations for this program failed with internal error");
        }
    }

    private OrgDto orgToDtoMapper(Org org, Prgm prgm) {
        PrgmDto prgmDto = modelMapper.map(prgm, PrgmDto.class);
        OrgDto savedOrgDto = modelMapper.map(org, OrgDto.class);
        savedOrgDto.setPrgmDto(prgmDto);
        return savedOrgDto;
    }
}
