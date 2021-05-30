package cz.upce.warehouse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/transfer/form")
public class TransferFormController {

    private final TransferFormService transferFormService;

    public TransferFormController(TransferFormService transferFormService) {
        this.transferFormService = transferFormService;
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleException(){
        return ResponseEntity.badRequest().body("ERROR: json processing exception!");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public String getTransferForm() throws JsonProcessingException {
        return transferFormService.getTransferFormJSON();
    }

    @PostMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public String addProductToTransfer(@PathVariable("productId") Long productId) throws JsonProcessingException {
        transferFormService.add(productId);
        return transferFormService.getTransferFormJSON();
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public String removeProductFromTransfer(@PathVariable("productId") Long productId) throws JsonProcessingException {
        transferFormService.remove(productId);
        return transferFormService.getTransferFormJSON();
    }

    @PutMapping("{productId}/{amount}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateProductToTransfer(@PathVariable("productId") Long productId, @PathVariable("amount") Integer amount) throws JsonProcessingException {
        transferFormService.update(productId,amount);
        return transferFormService.getTransferFormJSON();
    }
}
