package cfp.wecare.flow.ui.org.service;

import cfp.wecare.dto.OrgDto;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.model.Org;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrgServiceImplTest {

    @Mock
    private OrgRepository orgRepository;
    @Spy
    private ModelMapper modelMapper;
    @InjectMocks
    private OrgServiceImpl orgService;

    @Test
    public void shouldGetAllOrgs() {
        List<Org> orgs = new ArrayList<>();
        Org org = Org.builder()
                .orgId(UUID.randomUUID().toString())
                .orgName("org1")
                .orgAdmin("Org1 admin")
                .items(null)
                .description("description")
                .address("address")
                .build();
        orgs.add(org);
        Mockito.doReturn(orgs).when(orgRepository).findAll();
        List<OrgDto> returnedOrg = orgService.getAllOrgs();
        Mockito.verify(orgRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(returnedOrg.getFirst().getOrgName(), org.getOrgName());
    }
}
