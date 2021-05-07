package cz.upce.warehouse.service;

import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.entity.User;

import java.util.Map;

public interface TransferFormService {

    void add(Long id);

    void remove(Long id);

    Map<Product, Integer> getTransferForm();

    void confirm(User user, String address);
}
