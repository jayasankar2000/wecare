package cfp.wecare.transaction.controller;

import cfp.wecare.dto.ItemDto;
import cfp.wecare.flow.ui.item.exception.ItemException;
import cfp.wecare.model.Item;
import cfp.wecare.transaction.dto.ItemDetailsDto;
import cfp.wecare.transaction.dto.ItemDetailsResponseDto;
import cfp.wecare.transaction.dto.PaymentResponseDto;
import cfp.wecare.transaction.exception.ItemDetailsException;
import cfp.wecare.transaction.service.FilterSelectedItemsService;
import cfp.wecare.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private FilterSelectedItemsService filterSelectedItemsService;
    @Autowired
    private TransactionService transactionService;


    @PostMapping(value = "/transaction/filter-unpaid")
    public ResponseEntity<ItemDetailsResponseDto> confirmSelectedItems(@RequestBody ItemDetailsDto itemDetailsDto) {
        try {
            if (!isRequestValid(itemDetailsDto)) {
                throw new ItemDetailsException(HttpStatus.BAD_REQUEST, "Request should not be null/Empty.");
            }
            return ResponseEntity.ok(filterSelectedItemsService.itemAvailabilityResolver(itemDetailsDto));
        } catch (ItemDetailsException exp) {
            throw exp;
        } catch (Exception e) {
            throw new ItemException(HttpStatus.INTERNAL_SERVER_ERROR, "Processing the selected items failed due to internal error. Please try again");
        }
    }

    @PostMapping(value = "/transaction/confirm-payment")
    public ResponseEntity<PaymentResponseDto> confirmPayment(@RequestBody List<ItemDto> items) {
        try {
            if (CollectionUtils.isEmpty(items)) {
                throw new ItemDetailsException(HttpStatus.BAD_REQUEST, "The given list of items is empty");
            }
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.ok(transactionService.confirmPayment(items, username));
        } catch (ItemDetailsException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ItemDetailsException(HttpStatus.INTERNAL_SERVER_ERROR, "The transaction was not completed. Please try again");
        }
    }


    private Boolean isRequestValid(ItemDetailsDto itemDetailsDto) {
        return itemDetailsDto != null && !CollectionUtils.isEmpty(itemDetailsDto.getItems());
    }


}
