package cz.upce.warehouse.controller;

import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.repository.UserRepository;
import cz.upce.warehouse.service.TransferFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transfer")
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferFormService transferFormService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/confirm/{userId}/{address}")
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
