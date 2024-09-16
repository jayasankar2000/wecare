package cfp.wecare.service;

import cfp.wecare.Repository.ItemRepository;
import cfp.wecare.dto.ItemDto;
import cfp.wecare.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems() {
        List<Item> items = (List<Item>) itemRepository.findAll();
        return items.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
    }

    private ItemDto mapper(Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .quantity(item.getQuantity())
                .build();
    }
}
