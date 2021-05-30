package cz.upce.warehouse.controller;

import cz.upce.warehouse.entity.Transfer;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.dto.TransferStateEnum;
import cz.upce.warehouse.repository.TransferRepository;
import cz.upce.warehouse.repository.UserRepository;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transfer")
public class TransferController {

    private final TransferFormService transferFormService;

    private final UserRepository userRepository;

    private final TransferRepository transferRepository;

    public TransferController(TransferFormService transferFormService, UserRepository userRepository, TransferRepository transferRepository) {
        this.transferFormService = transferFormService;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleException(NoSuchElementException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public List<Transfer> getAllTransfers(){
        return transferRepository.findAll();
    }

    @PutMapping("/change-state/{transferId}/{state}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeTransferState(@PathVariable Long transferId, @PathVariable String state){
        Optional<Transfer> byId = transferRepository.findById(transferId);
        if(byId.isPresent()){
            Transfer transfer = byId.get();
            transfer.setState(TransferStateEnum.valueOf(state));
            transferRepository.save(transfer);
            return ResponseEntity.ok().build();
        }else{
            throw new NoSuchElementException("Transfer with ID: " + transferId + " was not found!");
        }
    }

    @PostMapping("/confirm/{userId}/{address}")
    @PreAuthorize("hasRole('ROLE_WAREHOUSEMAN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> confirmTransferForm(@PathVariable Long userId, @PathVariable String address){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            transferFormService.confirm(user.get(), address);
            return ResponseEntity.ok().build();
        }else{
            throw new NoSuchElementException("User with ID: " + userId + " was not found!");
        }
    }

}
