package cz.upce.warehouse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.upce.warehouse.dto.TransferItemDto;
import cz.upce.warehouse.entity.User;

import java.util.List;

public interface TransferFormService {

    void add(Long id);

    void remove(Long id);

    List<TransferItemDto> getTransferForm();

    void confirm(User user, String address);

    void update(Long productId, Integer amount);
}
