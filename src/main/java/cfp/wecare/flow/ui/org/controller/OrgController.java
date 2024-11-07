package cfp.wecare.flow.ui.org.controller;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrgController {

    @Autowired
    private OrgService orgService;

    @GetMapping(value = "/getAllOrgs")
    public List<OrgDto> getAllOrgs() {
        return orgService.getAllOrgs();
    }
}
