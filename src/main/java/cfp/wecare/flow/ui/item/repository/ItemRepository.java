package cfp.wecare.flow.ui.item.repository;

import cfp.wecare.model.Item;
import cfp.wecare.model.Org;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemRepository extends JpaRepository<Item, String> {

    List<Item> findByOrg(Org org);
}
