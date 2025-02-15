package cfp.wecare.flow.ui.item.service;

import cfp.wecare.Repository.OrgItemRepository;
import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.item.exception.ItemException;
import cfp.wecare.flow.ui.item.repository.ItemRepository;
import cfp.wecare.flow.ui.org.repository.OrgRepository;
import cfp.wecare.model.Item;
import cfp.wecare.model.Org;
import cfp.wecare.model.OrgItem;
import cfp.wecare.util.Constants;
import cfp.wecare.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static cfp.wecare.flow.ui.Constants.FAILED_TO_PARSE_EXCEL;
import static cfp.wecare.flow.ui.Constants.NOT_AN_EXCEL;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private OrgItemRepository orgItemRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrgRepository orgRepository;

    Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public List<ItemDto> getItems() {
        try {
            List<Item> items = (List<Item>) itemRepository.findAll();
            return items.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapperToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching all Items failed with Internal error");
        }
    }

    @Override
    @Transactional
    public int saveItems(MultipartFile multipartFile, String orgId) {
        logger.info("saveItems :");
        if (!excelUtil.isTypeExcel(multipartFile)) {
            List<Item> itemsToSave = null;
            try {
                itemsToSave = excelToItem(multipartFile.getInputStream());
            } catch (IOException e) {
                logger.warn(FAILED_TO_PARSE_EXCEL + e.getMessage());
                throw new ItemException(HttpStatus.BAD_REQUEST, FAILED_TO_PARSE_EXCEL + e.getMessage());
            }
            List<OrgItem> orgItems = itemsToSave.stream()
                    .map(item -> {
                        OrgItem orgItem = OrgItem.builder()
                                .orgItemId(UUID.randomUUID().toString())
                                .itemId(item.getItemId())
                                .orgId(orgId).build();
                        return orgItem;
                    }).collect(Collectors.toList());
            List<Item> savedItems = (List<Item>) itemRepository.saveAll(itemsToSave);
            orgItemRepository.saveAll(orgItems);
            logger.info("Saved Items :" + savedItems);
            return savedItems.size();
        } else {
            logger.warn(NOT_AN_EXCEL);
            throw new ItemException(HttpStatus.BAD_REQUEST, NOT_AN_EXCEL);
        }
    }

    @Override
    public List<ItemDto> getOrgItem(String orgId) {
        try {
            Optional<Org> org = orgRepository.findById(orgId);
            if (org.isEmpty()) {
                throw new ItemException(HttpStatus.NOT_FOUND, "The Organization is not present");
            }
            List<Item> items = itemRepository.findByOrg(org.get());
            return items.stream()
                    .filter(Objects::nonNull)
                    .map(this::mapperToDto)
                    .collect(Collectors.toList());
        } catch (ItemException e) {
            throw e;
        } catch (Exception ex) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Fetching Items for the Organization failed with Internal error");
        }
    }

    @Override
    public void deleteItem(String itemId) {
        try {
            if (itemRepository.findById(itemId).isEmpty()) {
                throw new ItemException(HttpStatus.NOT_FOUND, "The Item does not exist");
            }
            itemRepository.deleteById(itemId);
        } catch (ItemException e) {
            throw e;
        } catch (Exception e) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Deleting the item failed with Internal error");
        }
    }

    @Override
    public ItemDto update(String itemId, ItemDto itemDto) {
        try {
            Optional<Item> item = itemRepository.findById(itemId);
            if (item.isEmpty()) {
                throw new ItemException(HttpStatus.NOT_FOUND, "Could not find the Item");
            }
            Item newItem = item.get();
            newItem.setItemName(itemDto.getItemName());
            newItem.setItemPrice(itemDto.getItemPrice());
            newItem.setQuantity(itemDto.getQuantity());
            return mapperToDto(itemRepository.save(newItem));
        } catch (ItemException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update Item, failed with Internal error");
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

    private ItemDto mapperToDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }
}
