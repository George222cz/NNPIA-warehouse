package cz.upce.warehouse.service;

import cz.upce.warehouse.entity.*;
import cz.upce.warehouse.model.TransferStateEnum;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.TransferItemRepository;
import cz.upce.warehouse.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@SessionScope
public class TransferFormServiceImpl implements TransferFormService {

    private final Map<Product,Integer> transferForm;

    private final ProductRepository productRepository;
    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;

    public TransferFormServiceImpl(ProductRepository productRepository, TransferRepository transferRepository, TransferItemRepository transferItemRepository) {
        this.productRepository = productRepository;
        this.transferRepository = transferRepository;
        this.transferItemRepository = transferItemRepository;
        transferForm = new HashMap<>();
    }

    @Override
    public void add(Long id) {
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if(transferForm.containsKey(product)){
            transferForm.replace(product,transferForm.get(product)+1);
        }else{
            transferForm.put(product,1);
        }
    }

    @Override
    public void remove(Long id) {
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if(transferForm.containsKey(product)){
            if(transferForm.get(product) > 1){
                transferForm.replace(product, transferForm.get(product) -1);
            }else{
                transferForm.remove(product);
            }
        }
    }

    @Override
    public Map<Product, Integer> getTransferForm() {
        return transferForm;
    }

    @Override
    public void confirm(User user, String address) {
        Transfer transfer = new Transfer();
        transfer.setUser(user);
        transfer.setAddress(address);
        transfer.setState(TransferStateEnum.PENDING);
        transfer.setCreated(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        transferRepository.save(transfer);

        for (Map.Entry<Product, Integer> entry : transferForm.entrySet()) {
            TransferItem transferItem = new TransferItem();
            transferItem.setTransfer(transfer);
            transferItem.setProduct(entry.getKey());
            transferItem.setAmount(entry.getValue());
            transferItemRepository.save(transferItem);
        }
        transferForm.clear();
    }
}
