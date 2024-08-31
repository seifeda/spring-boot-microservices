package com.wegagenspringdemo.inventory_service.controller;

import com.wegagenspringdemo.inventory_service.dto.InventoryResponse;
import com.wegagenspringdemo.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {

       return inventoryService.isInStock(skuCode);
    }

}
