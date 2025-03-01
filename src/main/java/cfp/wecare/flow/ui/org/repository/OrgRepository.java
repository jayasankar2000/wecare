package cfp.wecare.flow.ui.org.repository;

import cfp.wecare.model.Org;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgRepository extends CrudRepository<Org, String> {
    List<Org> findAllByProgram(String prgmId);
}
