package cfp.wecare.flow.ui.org.repository;

import cfp.wecare.model.Org;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgRepository extends CrudRepository<Org, String> {
    @Query(value = "SELECT o from Org o where o.program.pgmId =:pgmId")
    List<Org> findAllByProgram(@Param("pgmId") String pgmId);
}
