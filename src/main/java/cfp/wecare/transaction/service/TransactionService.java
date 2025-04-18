package cfp.wecare.transaction.service;

import cfp.wecare.Repository.UserRepository;
import cfp.wecare.dto.ItemDto;
import cfp.wecare.model.User;
import cfp.wecare.transaction.dto.PaymentRequestDto;
import cfp.wecare.transaction.dto.PaymentResponseDto;
import cfp.wecare.transaction.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    public PaymentResponseDto confirmPayment(List<ItemDto> itemDtoList, String userName) {
        try {
            validateRequest(itemDtoList, userName);
            Optional<User> user = userRepository.findByUserName(userName);
            String userId = user.get().getUserId();
            List<String> itemIds = itemDtoList.stream()
                    .filter(Objects::nonNull)
                    .map(ItemDto::getItemId)
                    .toList();
            Integer amount = itemDtoList.stream()
                    .filter(Objects::nonNull)
                    .mapToInt(ItemDto::getItemPrice)
                    .sum();
            PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
                    .items(itemIds)
                    .user(userName)
                    .totalAmount(amount)
                    .build();
            ResponseEntity<PaymentResponseDto> paymentResponse = restTemplate.postForEntity("", paymentRequest, PaymentResponseDto.class);
            if (!paymentResponse.hasBody()) {
                throw new TransactionException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment failed with internal server error");
            }
            return paymentResponse.getBody();
        } catch (TransactionException | RestClientException e) {
            throw new TransactionException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment failed with Internal server error");
        }
    }

    private void validateRequest(List<ItemDto> itemDtoList, String userName) {
        if (CollectionUtils.isEmpty(itemDtoList) || !StringUtils.hasText(userName)) {
            throw new TransactionException(HttpStatus.BAD_REQUEST, "The list of items or username is empty");
        }
    }
}
