package cfp.wecare.flow.ui.consume.prgm.controller;

import cfp.wecare.dto.PrgmDto;
import cfp.wecare.flow.ui.consume.prgm.service.PrgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrgmController {

    @Autowired
    private PrgmService prgmService;

    @GetMapping(value = "/allPrgms")
    public List<PrgmDto> getAllPrgms() {
        return prgmService.getPrgms();
    }
}
