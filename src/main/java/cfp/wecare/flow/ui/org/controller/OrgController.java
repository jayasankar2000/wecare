package cfp.wecare.flow.ui.org.controller;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.Exception.OrgException;
import cfp.wecare.flow.ui.org.service.OrgService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrgController {

    @Autowired
    private OrgService orgService;

    @GetMapping(value = "/org")
    public List<OrgDto> getAllOrgs() {
        try {
            return orgService.getAllOrgs();
        } catch (Exception e) {
            if (e instanceof OrgException ex) {
                throw ex;
            }
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching organizations failed with internal error");
        }

    }

    @GetMapping(value = "/org/{orgId}")
    public ResponseEntity<OrgDto> getOrg(@PathVariable String orgId) {
        try {
            return ResponseEntity.ok(orgService.getOrg(orgId));
        } catch (Exception e) {
            if (e instanceof OrgException ex) {
                throw ex;
            }
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching Organization details failed with internal server error");
        }
    }

    @GetMapping(value = "/org/{prgmId}")
    public ResponseEntity<List<OrgDto>> getOrgsByProgram(@PathVariable String prgmId) {
        try {
            return ResponseEntity.ok(orgService.findOrgsByPrgm(prgmId));
        } catch (OrgException e) {
            throw e;
        } catch (Exception e) {
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/org-admin/org/create")
    public ResponseEntity<OrgDto> createOrg(@RequestBody OrgDto orgDto) {
        try {
            if (orgDto == null || orgDto.getPrgmDto() == null || !StringUtils.hasText(orgDto.getOrgName())) {
                throw new OrgException(HttpStatus.BAD_REQUEST, "Organization details and Program details are mandatory");
            }
            return ResponseEntity.ok(orgService.create(orgDto));
        } catch (Exception e) {
            if (e instanceof OrgException ex) {
                throw ex;
            }
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Addition of organization failed with internal error.");
        }
    }

    @PutMapping(value = "/org-admin/org/update/{orgId}")
    public ResponseEntity<OrgDto> updateOrg(@PathVariable String orgId, @RequestBody OrgDto orgDto) {
        try {
            if (orgDto == null || !StringUtils.hasText(orgDto.getOrgId()) || !StringUtils.hasText(orgDto.getOrgName())) {
                throw new OrgException(HttpStatus.BAD_REQUEST, "Organization details are mandatory");
            }
            return ResponseEntity.ok(orgService.update(orgId, orgDto));
        } catch (Exception e) {
            if (e instanceof OrgException ex) {
                throw ex;
            }
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Updating the organization failed with Internal error");
        }
    }

    @DeleteMapping(value = "/org-admin/org/delete/{orgId}")
    public ResponseEntity<String> deteleOrg(@PathVariable String orgId) {
        try {
            orgService.deleteOrg(orgId);
            return ResponseEntity.ok("Organization deleted successfully");
        } catch (Exception e) {
            if (e instanceof OrgException ex) {
                throw ex;
            }
            throw new OrgException(HttpStatus.INTERNAL_SERVER_ERROR, "Deleting Organization failed with Internal error");
        }
    }

    @ExceptionHandler(value = OrgException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(OrgException exception) {
        ExceptionResponseObject object = ExceptionResponseObject.builder()
                .httpStatusCode(exception.getHttpStatus().value())
                .message(exception.getMessage())
                .httpStatus(exception.getHttpStatus())
                .build();
        return new ResponseEntity<>(object, exception.getHttpStatus());
    }
}
