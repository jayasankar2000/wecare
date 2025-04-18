package cfp.wecare.transaction.service;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.item.repository.ItemRepository;
import cfp.wecare.model.Item;
import cfp.wecare.transaction.dto.ItemAvailabilitDetailsDto;
import cfp.wecare.transaction.dto.ItemDetailsDto;
import cfp.wecare.transaction.dto.ItemDetailsResponseDto;
import cfp.wecare.transaction.exception.ItemDetailsException;
import cfp.wecare.transaction.model.PaymentStatus;
import cfp.wecare.transaction.repository.ItemTransactionRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cfp.wecare.transaction.model.PaymentStatus.PAID;
import static cfp.wecare.transaction.model.PaymentStatus.WAITING_TO_PAY;

@Service
public class FilterSelectedItemsService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTransactionRepository itemTransactionRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ItemDetailsResponseDto itemAvailabilityResolver(ItemDetailsDto itemDetailsDtos) {
        List<String> items = itemDetailsDtos.getItems();
        if (CollectionUtils.isEmpty(items)) {
            throw new ItemDetailsException(HttpStatus.BAD_REQUEST, "The Item list should not be empty");
        }
        List<Item> dbItems = itemRepository.findAllById(items);
        return getItemAvailabilityList(dbItems);
    }

    private ItemDetailsResponseDto getItemAvailabilityList(List<Item> dbItems) {
        Integer totalAmount = 0;
        String availableMessage = "These are the availabe items";
        String unAvailableMessage = "These are the unavailabe items";
        List<Item> availableItems = new ArrayList<>();
        List<Item> unAvailableItems = new ArrayList<>();
        for (Item item : dbItems) {
            if (isItemPaidOrWaitingToPay(item)) {
                unAvailableItems.add(item);
            } else {
                availableItems.add(item);
                totalAmount += item.getItemPrice();
            }
        }
        List<String> itemIds = availableItems.stream()
                .map(Item::getItemId)
                .toList();
        Integer affectedRows = itemTransactionRepository.updateItemsPaymentStatus(itemIds, WAITING_TO_PAY.name());
        List<ItemDto> availableItemDtos = getItemDtos(availableItems);
        List<ItemDto> unavailableItemsDto = getItemDtos(unAvailableItems);
        return getItemDetailsResponseDto(availableMessage, availableItemDtos, totalAmount, unavailableItemsDto, unAvailableMessage);
    }

    private static ItemDetailsResponseDto getItemDetailsResponseDto(String availableMessage, List<ItemDto> availableItemDtos, Integer totalAmount, List<ItemDto> unavailableItemsDto, String unAvailableMessage) {
        ItemAvailabilitDetailsDto availableItemsDetails = ItemAvailabilitDetailsDto.builder()
                .message(availableMessage)
                .items(availableItemDtos)
                .totalAmount(totalAmount)
                .build();
        ItemAvailabilitDetailsDto unAvailableItemDetails = ItemAvailabilitDetailsDto.builder()
                .items(unavailableItemsDto)
                .message(unAvailableMessage)
                .build();
        return ItemDetailsResponseDto.builder().itemDetails(Arrays.asList(availableItemsDetails, unAvailableItemDetails))
                .build();
    }

    private List<ItemDto> getItemDtos(List<Item> availableItems) {
        return availableItems.stream()
                .map(item -> {
                    return modelMapper.map(item, ItemDto.class);
                })
                .toList();
    }


    private Boolean isItemPaidOrWaitingToPay(Item item) {
        return item.getPaymentStatus() != PAID && item.getPaymentStatus() != WAITING_TO_PAY;
    }
}
