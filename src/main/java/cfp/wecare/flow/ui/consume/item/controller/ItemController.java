package cfp.wecare.flow.ui.consume.item.controller;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.consume.item.exception.ItemException;
import cfp.wecare.flow.ui.consume.item.service.ItemService;
import cfp.wecare.util.ExceptionResponseObject;
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

    @ExceptionHandler(value = ItemException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponseObject> exceptionHandler(ItemException ex, WebRequest request) {
        ExceptionResponseObject object = ExceptionResponseObject.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .httpStatusCode(ex.getHttpStatus().value())
                .build();
        return new ResponseEntity<ExceptionResponseObject>(object, ex.getHttpStatus());
    }

}
