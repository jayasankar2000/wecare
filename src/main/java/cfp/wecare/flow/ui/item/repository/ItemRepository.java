package cfp.wecare.flow.ui.item.repository;

import cfp.wecare.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query(value = "SELECT item from Item item where item.org.orgId =:orgId")
    List<Item> findByOrgId(@Param("orgId") String orgId);
}
