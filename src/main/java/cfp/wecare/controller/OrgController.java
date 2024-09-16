package cfp.wecare.controller;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrgController {

    @Autowired
    private OrgService orgService;

    @GetMapping(value = "/getAllOrgs")
    public List<OrgDto> getAllOrgs() {
        return orgService.getAllOrgs();
    }
}
