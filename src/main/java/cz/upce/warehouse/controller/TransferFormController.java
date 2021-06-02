package cz.upce.warehouse.controller;

import cz.upce.warehouse.dto.TransferItemDto;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/transfer/form")
public class TransferFormController {

    private final TransferFormService transferFormService;

    public TransferFormController(TransferFormService transferFormService) {
        this.transferFormService = transferFormService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public List<TransferItemDto> getTransferForm() {
        return transferFormService.getTransferForm();
    }

    @PostMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public List<TransferItemDto> addProductToTransfer(@PathVariable("productId") Long productId) {
        transferFormService.add(productId);
        return transferFormService.getTransferForm();
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public List<TransferItemDto> removeProductFromTransfer(@PathVariable("productId") Long productId) {
        transferFormService.remove(productId);
        return transferFormService.getTransferForm();
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TransferItemDto> updateProductToTransfer(@RequestBody TransferItemDto transferItemDto) {
        transferFormService.update(transferItemDto.getProductId(),transferItemDto.getAmount());
        return transferFormService.getTransferForm();
    }
}
