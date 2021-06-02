package cz.upce.warehouse.controller;

import cz.upce.warehouse.entity.Transfer;
import cz.upce.warehouse.service.TransferServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/transfer")
public class TransferController {

    private final TransferServiceImpl transferService;

    public TransferController(TransferServiceImpl transferService) {
        this.transferService = transferService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping(value = {"/","/{state}"})
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public List<Transfer> getAllTransfers(@PathVariable(required = false) String state){
        return transferService.getAllTransfers(state);
    }

    @PutMapping("/change-state/{transferId}/{state}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeTransferState(@PathVariable Long transferId, @PathVariable String state){
        return transferService.changeTransferState(transferId,state);
    }

    @PostMapping("/confirm/{userId}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> confirmTransferForm(@PathVariable Long userId, @RequestBody String address){
        return transferService.confirmTransferForm(userId,address);
    }

}
