package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.model.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgServiceImpl implements OrgService {
    @Autowired
    private OrgRepository orgRepository;

    @Override
    public List<OrgDto> getAllOrgs() {
        List<Org> orgs = (List<Org>) orgRepository.findAll();
        return orgs.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
    }

    public OrgDto mapper(Org org) {
        return OrgDto.builder()
                .orgId(org.getOrgId())
                .orgName(org.getOrgName())
                .orgAdmin(org.getOrgAdmin())
                .address(org.getAddress())
                .description(org.getDescription())
                .build();
    }
}
