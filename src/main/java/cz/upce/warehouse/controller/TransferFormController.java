package cz.upce.warehouse.controller;

import cz.upce.warehouse.entity.Product;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/transfer/form")
@CrossOrigin
public class TransferFormController {

    @Autowired
    private TransferFormService transferFormService;

    @GetMapping
    public Map<Product, Integer> getTransferForm(){
        return transferFormService.getTransferForm();
    }

    @PostMapping("{productId}")
    public Map<Product, Integer> addProductToCart(@PathVariable("productId") Long productId) {
        transferFormService.add(productId);
        return transferFormService.getTransferForm();
    }

    @DeleteMapping("{productId}")
    public Map<Product, Integer> removeProductFromCart(@PathVariable("productId") Long productId) {
        transferFormService.remove(productId);
        return transferFormService.getTransferForm();
    }
}
