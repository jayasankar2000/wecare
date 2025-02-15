package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrgService {
    public List<OrgDto> getAllOrgs();

    OrgDto getOrg(String orgId);

    OrgDto create(OrgDto orgDto);

    OrgDto update(String orgId, OrgDto orgDto);

    void deleteOrg(String orgId);
}
