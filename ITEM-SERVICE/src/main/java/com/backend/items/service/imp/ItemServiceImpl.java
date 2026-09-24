package com.backend.items.service.imp;

import com.backend.items.dto.ItemRequest;
import com.backend.items.dto.ItemResponse;
import com.backend.items.dto.ProductResponse;
import com.backend.items.entity.Item;
import com.backend.items.exception.DuplicateItemException;
import com.backend.items.exception.ResourceNotFoundException;
import com.backend.items.repository.ItemRepository;
import com.backend.items.service.ItemService;
import com.backend.items.service.ProductServiceFeign;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final ProductServiceFeign productServiceFeign;

    public ItemServiceImpl(ItemRepository itemRepository, ModelMapper modelMapper, ProductServiceFeign productServiceFeign) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.productServiceFeign = productServiceFeign;
        configMapper();

    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(item -> modelMapper.map(item, ItemResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long id) {
        return itemRepository.findById(id)
                .map(item -> modelMapper.map(item, ItemResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    @Override
    @Transactional
    public ItemResponse createItem(ItemRequest itemRequest) {

        itemRepository.findBySerialNumber(itemRequest.getSerialNumber())
                .ifPresent(item -> {
                    throw new DuplicateItemException("Item with serial number " + itemRequest.getSerialNumber() + " already exists");
                });

        itemRequest.setTotalPrice(itemRequest.getUnitPrice() * itemRequest.getQuantity());
        Item itemSaved = itemRepository.save(modelMapper.map(itemRequest, Item.class));

        return modelMapper.map(itemSaved, ItemResponse.class);
    }

    @Override
    public ItemResponse createItemByProductId(Long productId, ItemRequest itemRequest) {

        ProductResponse searchedProductResponse = productServiceFeign.getProductById(productId);

        if (searchedProductResponse == null) {
            throw new ResourceNotFoundException("ProductResponse not found with id: " + productId);
        }

        itemRepository.findBySerialNumber(itemRequest.getSerialNumber())
                .ifPresent(item -> {
                    throw new DuplicateItemException("Item with serial number " + itemRequest.getSerialNumber() + " already exists");
                });

        Item itemToSave = modelMapper.map(itemRequest, Item.class);
        itemToSave.setProductId(searchedProductResponse.getId());
        itemToSave.setUnitPrice(searchedProductResponse.getPrice());
        itemToSave.setTotalPrice(searchedProductResponse.getPrice() * itemRequest.getQuantity());

        return modelMapper.map(itemRepository.save(itemToSave), ItemResponse.class);


    }

    @Override
    public ItemResponse updateItemByProductId(Long productId, Long itemId, ItemRequest itemRequest) {
        ProductResponse searchedProductResponse = productServiceFeign.getProductById(productId);

        if (searchedProductResponse == null) {
            throw new ResourceNotFoundException("ProductResponse not found with id: " + productId);
        }

        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setSerialNumber(itemRequest.getSerialNumber());
                    item.setQuantity(itemRequest.getQuantity());
                    item.setProductId(searchedProductResponse.getId());
                    item.setUnitPrice(searchedProductResponse.getPrice());
                    item.setTotalPrice(searchedProductResponse.getPrice() * itemRequest.getQuantity());
                    return modelMapper.map(itemRepository.save(item), ItemResponse.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));
    }


    @Override
    @Transactional
    public ItemResponse updateItem(Long id, ItemRequest itemRequest) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setSerialNumber(itemRequest.getSerialNumber());
                    item.setQuantity(itemRequest.getQuantity());
                    item.setProductId(itemRequest.getProductId());
                    Item updatedItem = itemRepository.save(item);
                    return modelMapper.map(updatedItem, ItemResponse.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {

        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Item not found with id: " + id);
        }


    }

    public void configMapper() {
        modelMapper.typeMap(ItemRequest.class, Item.class)
                .addMappings(mapper -> {
                    mapper.skip(Item::setId);
                    //mapper.skip(Item::setTotalPrice);
                });
    }

}
