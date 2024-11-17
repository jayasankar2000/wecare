package cfp.wecare.flow.ui.org.controller;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrgController {

    @Autowired
    private OrgService orgService;

    @GetMapping(value = "/getOrgs")
    public List<OrgDto> getAllOrgs() {
        return orgService.getAllOrgs();
    }

    @GetMapping(value = "/getOrg/{orgId}")
    public ResponseEntity<OrgDto> getOrg(@PathVariable String orgId) {
        return ResponseEntity.ok(orgService.getOrg(orgId));
    }

    @PostMapping(value = "/admin/org/create")
    public ResponseEntity<OrgDto> createOrg(@RequestParam OrgDto orgDto) {
        OrgDto newOrg = orgService.create(orgDto);
        return ResponseEntity.ok(newOrg);
    }

    @PutMapping(value = "/admin/org/update")
    public ResponseEntity<OrgDto> updateOrg(@RequestParam OrgDto orgDto) {
        OrgDto updated = orgService.update(orgDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/admin/org/delete/{orgId}")
    public ResponseEntity<String> deteleOrg(@PathVariable String orgId) {
        orgService.deleteOrg(orgId);
        return ResponseEntity.ok("Organization deleted successfully");
    }
}
