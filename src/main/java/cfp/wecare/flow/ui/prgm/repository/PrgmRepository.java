package cfp.wecare.flow.ui.prgm.repository;

import cfp.wecare.model.Prgm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrgmRepository extends CrudRepository<Prgm, String> {
}
