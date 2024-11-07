package cfp.wecare.Repository;

import cfp.wecare.model.OrgItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgItemRepository extends JpaRepository<OrgItem, String> {
}
