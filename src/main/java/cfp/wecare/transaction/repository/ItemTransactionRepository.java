package cfp.wecare.transaction.repository;

import cfp.wecare.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemTransactionRepository extends JpaRepository<Item, String> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE item SET payment_status :=paymentStatus where item_id in (:=itemIds)", nativeQuery = true)
    int updateItemsPaymentStatus(@Param("itemIds") List<String> itemIds, @Param("paymentStatus") String paymentStatus);
}
