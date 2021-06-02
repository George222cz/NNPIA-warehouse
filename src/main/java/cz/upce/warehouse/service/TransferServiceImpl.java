package cz.upce.warehouse.service;

import cz.upce.warehouse.dto.TransferStateEnum;
import cz.upce.warehouse.entity.Transfer;
import cz.upce.warehouse.entity.TransferItem;
import cz.upce.warehouse.entity.User;
import cz.upce.warehouse.repository.TransferRepository;
import cz.upce.warehouse.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TransferServiceImpl {

    private final TransferFormService transferFormService;

    private final UserRepository userRepository;

    private final TransferRepository transferRepository;

    public TransferServiceImpl(TransferFormService transferFormService, UserRepository userRepository, TransferRepository transferRepository) {
        this.transferFormService = transferFormService;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
    }

    public List<Transfer>  getAllTransfers(String state){
        if(state!=null && !state.isEmpty()){
            return transferRepository.findAllByState(TransferStateEnum.valueOf(state));
        }
        return transferRepository.findAll();
    }

    public ResponseEntity<?> changeTransferState(Long transferId, String state) throws NoSuchElementException{
        Optional<Transfer> byId = transferRepository.findById(transferId);
        if(byId.isPresent()){
            Transfer transfer = byId.get();
            if(transfer.getState()==TransferStateEnum.DONE || transfer.getState()==TransferStateEnum.CANCELED){
                return ResponseEntity.badRequest().body("You cannot change this state!");
            }
            TransferStateEnum stateEnum = TransferStateEnum.valueOf(state);
            if(stateEnum==TransferStateEnum.DONE){
                for (TransferItem transferItem : transfer.getTransferItems()) {
                    if(transferItem.getProduct().getAmount()<=transferItem.getAmount()){
                        return ResponseEntity.badRequest().body("There is not enough amount of the product: "+transferItem.getProduct().getProductName());
                    }
                }
                for (TransferItem transferItem : transfer.getTransferItems()) {
                    transferItem.getProduct().setAmount(transferItem.getProduct().getAmount()-transferItem.getAmount());
                }
            }
            transfer.setState(stateEnum);
            transferRepository.save(transfer);
            return ResponseEntity.ok().build();
        }else{
            throw new NoSuchElementException("Transfer with ID: " + transferId + " was not found!");
        }
    }

    public ResponseEntity<Object> confirmTransferForm(Long userId, String address) throws NoSuchElementException{
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            transferFormService.confirm(user.get(), address.replaceAll("^\"|\"$", ""));
            return ResponseEntity.ok().build();
        }else{
            throw new NoSuchElementException("User with ID: " + userId + " was not found!");
        }
    }


}
