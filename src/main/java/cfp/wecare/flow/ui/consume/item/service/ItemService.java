package cfp.wecare.flow.ui.consume.item.service;

import cfp.wecare.dto.ItemDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    List<ItemDto> getItems();

    int saveItems(MultipartFile multipartFile) throws IOException;
}
