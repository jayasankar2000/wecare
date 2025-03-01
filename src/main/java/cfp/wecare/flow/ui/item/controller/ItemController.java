package cfp.wecare.flow.ui.item.controller;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.item.exception.ItemException;
import cfp.wecare.flow.ui.item.service.ItemService;
import cfp.wecare.util.ExceptionResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;

    @GetMapping(value = "/items")
    public ResponseEntity<List<ItemDto>> getAllItems() {
        try {
            return ResponseEntity.ok(itemService.getItems());
        } catch (Exception e) {
            if (e instanceof ItemException ex) {
                throw ex;
            }
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching All items failed with Internal error");
        }
    }

    @GetMapping(value = "/{orgId}/items")
    public ResponseEntity<List<ItemDto>> getOrgItems(@PathVariable String orgId) {
        try {
            return ResponseEntity.ok(itemService.getOrgItem(orgId));
        } catch (Exception e) {
            if (e instanceof ItemException ex) {
                throw ex;
            }
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching items for the Organization failed with Internal error");
        }
    }

    @PostMapping(value = "/org-admin/item/add")
    public int saveItems(@RequestParam MultipartFile file, @RequestParam String orgId) {
        try {
            return itemService.saveItems(file, orgId);
        } catch (Exception ex) {
            if (ex instanceof ItemException itemException) {
                throw itemException;
            }
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
    }

    @PutMapping(value = "/org-admin/update/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable String itemId, @RequestBody ItemDto itemDto) {
        try {
            return ResponseEntity.ok(itemService.update(itemId, itemDto));
        } catch (Exception e) {
            if (e instanceof ItemException exception) {
                throw exception;
            }
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Updating Item failed with Internal error");
        }
    }

    @DeleteMapping(value = "/org-admin/detele/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable String itemId) {
        try {
            itemService.deleteItem(itemId);
            return ResponseEntity.ok("Item Deleted successfully");
        } catch (Exception e) {
            if (e instanceof ItemException ex) {
                throw ex;
            }
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Deleting the Item failed with Internal error");
        }
    }

    @ExceptionHandler(value = ItemException.class)
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(ItemException ex, WebRequest request) {
        ExceptionResponseObject object = ExceptionResponseObject.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .httpStatusCode(ex.getHttpStatus().value())
                .build();
        return new ResponseEntity<>(object, ex.getHttpStatus());
    }

}
