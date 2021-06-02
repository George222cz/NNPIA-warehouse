package cz.upce.warehouse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.upce.warehouse.dto.TransferItemDto;
import cz.upce.warehouse.entity.*;
import cz.upce.warehouse.dto.TransferStateEnum;
import cz.upce.warehouse.repository.ProductRepository;
import cz.upce.warehouse.repository.TransferItemRepository;
import cz.upce.warehouse.repository.TransferRepository;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.stereotype.Service;

import javax.transaction.TransactionScoped;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@TransactionScoped
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
    public List<TransferItemDto> getTransferForm() {
        List<TransferItemDto> list = new ArrayList<>();
        for (Map.Entry<Product, Integer> productAmountEntry : transferForm.entrySet()) {
            TransferItemDto itemDto = new TransferItemDto();
            itemDto.setProductName(productAmountEntry.getKey().getProductName());
            itemDto.setAmount(productAmountEntry.getValue());
            itemDto.setProductId(productAmountEntry.getKey().getId());
            list.add(itemDto);
        }
        return list;
    }

    @Override
    public void confirm(User user, String address) {
        Transfer transfer = new Transfer();
        transfer.setUser(user);
        transfer.setAddress(address);
        transfer.setState(TransferStateEnum.PENDING);
        transfer.setCreated(new Date());
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

    @Override
    public void update(Long productId, Integer amount) {
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        if(transferForm.containsKey(product)){
            transferForm.replace(product,amount);
        }else{
            transferForm.put(product,amount);
        }
    }


}
