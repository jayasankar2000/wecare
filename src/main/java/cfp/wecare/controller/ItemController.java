package cfp.wecare.controller;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping(value = "/getAllItems")
    public List<ItemDto> getItems() {
        return itemService.getItems();
    }
}
