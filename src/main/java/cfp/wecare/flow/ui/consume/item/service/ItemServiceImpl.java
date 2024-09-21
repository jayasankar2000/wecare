package cfp.wecare.flow.ui.consume.item.service;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.consume.item.exception.ItemException;
import cfp.wecare.flow.ui.consume.item.repository.ItemRepository;
import cfp.wecare.model.Item;
import cfp.wecare.util.Constants;
import cfp.wecare.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static cfp.wecare.flow.ui.consume.Constants.FAILED_TO_PARSE_EXCEL;
import static cfp.wecare.flow.ui.consume.Constants.NOT_AN_EXCEL;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ExcelUtil excelUtil;

    @Override
    public List<ItemDto> getItems() {
        List<Item> items = (List<Item>) itemRepository.findAll();
        return items.stream()
                .map(this::mapper)
                .collect(Collectors.toList());
    }

    @Override
    public int saveItems(MultipartFile multipartFile) {
        if (excelUtil.isTypeExcel(multipartFile)) {
            List<Item> itemsToSave = null;
            try {
                itemsToSave = excelToItem(multipartFile.getInputStream());
            } catch (IOException e) {
                throw new ItemException(HttpStatus.BAD_REQUEST, FAILED_TO_PARSE_EXCEL + e.getMessage());
            }
            List<Item> savedItems = (List<Item>) itemRepository.saveAll(itemsToSave);
            return savedItems.size();
        } else {
            throw new ItemException(HttpStatus.BAD_REQUEST, NOT_AN_EXCEL);
        }
    }

    private List<Item> excelToItem(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(Constants.SHEET);
        Iterator<Row> rows = sheet.iterator();
        List<Item> items = new ArrayList<>();
        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            Iterator<Cell> cellInRow = currentRow.iterator();
            Item item = new Item();
            item.setItemId(UUID.randomUUID().toString());
            int cellIndx = 0;
            while (cellInRow.hasNext()) {
                Cell currentCell = cellInRow.next();
                switch (cellIndx) {
                    case 1:
                        item.setItemName(currentCell.getStringCellValue());
                        break;
                    case 2:
                        item.setItemPrice((int) currentCell.getNumericCellValue());
                        break;
                    case 3:
                        item.setQuantity((int) currentCell.getNumericCellValue());
                        break;
                    default:
                        break;
                }
                cellIndx++;
            }
            items.add(item);
        }
        return items;
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
