package cfp.wecare.flow.ui.item.repository;

import cfp.wecare.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
@Service
public interface ItemRepository extends CrudRepository<Item, String> {

}
