package cfp.wecare.Repository;

import cfp.wecare.model.Org;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgRepository extends CrudRepository<Org, String> {
}
