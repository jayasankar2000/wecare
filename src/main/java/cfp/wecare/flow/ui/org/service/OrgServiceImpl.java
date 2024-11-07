package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.model.Org;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public OrgDto mapperToDto(Org org) {
        return modelMapper.map(org, OrgDto.class);
    }
}
