package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.model.Org;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrgDto> getAllOrgs() {
        List<Org> orgs = (List<Org>) orgRepository.findAll();
        return orgs.stream()
                .map(this::mapperToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrgDto getOrg(String orgId) {
        Optional<Org> org = orgRepository.findById(orgId);
        if(org.isPresent()){
            Org org1 = org.get();
            return mapperToDto(org1);
        }
        return null;
    }

    @Override
    public OrgDto create(OrgDto orgDto) {
        Org newOrg = Org.builder()
                .orgId(UUID.randomUUID().toString())
                .orgName(orgDto.getOrgName())
                .build();
        Org createdOrg = orgRepository.save(newOrg);
        return mapperToDto(createdOrg);
    }

    @Override
    public OrgDto update(OrgDto orgDto) {
        Optional<Org> org = orgRepository.findById(orgDto.getOrgId());
        if(org.isPresent()){
            Org toSave = Org.builder()
                    .orgId(orgDto.getOrgId())
                    .items(orgDto.getItems())
                    .orgAdmin(orgDto.getOrgAdmin())
                    .address(orgDto.getAddress())
                    .program(orgDto.getProgram())
                    .description(orgDto.getDescription())
                    .items(orgDto.getItems())
                    .build();
            Org save = orgRepository.save(toSave);
            return mapperToDto(save);
        }
        return null;
    }

    @Override
    public void deleteOrg(String orgId) {
        orgRepository.deleteById(orgId);
    }

    public OrgDto mapperToDto(Org org) {
        return modelMapper.map(org, OrgDto.class);
    }
}
