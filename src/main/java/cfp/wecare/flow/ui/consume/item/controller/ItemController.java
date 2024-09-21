package cfp.wecare.flow.ui.consume.item.controller;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.consume.item.exception.ItemException;
import cfp.wecare.flow.ui.consume.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping(value = "/getAllItems")
    public List<ItemDto> getItems() {
        return itemService.getItems();
    }

    @PostMapping(value = "/saveItems")
    public int saveItems(@RequestParam MultipartFile file) {
        try {
            return itemService.saveItems(file);
        } catch (ItemException e) {
            throw e;
        } catch (Exception ex) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @ExceptionHandler
    public ResponseEntity exceptionHandler(ItemException ex, WebRequest request) {
        return ResponseEntity.status(ex.getHttpStatus()).build();
    }

}
