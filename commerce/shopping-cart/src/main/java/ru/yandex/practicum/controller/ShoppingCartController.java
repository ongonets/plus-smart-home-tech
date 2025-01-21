package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService service;

    @GetMapping
    public ShoppingCartDto findShoppingCart(@RequestParam String username) {
        return service.findShoppingCarte(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam String username,
                                                    @RequestBody Map<UUID, Integer> products) {
        log.info("Received request to add products to shopping cart of user: {}", username);
        return service.addProductToShoppingCart(username, products);
    }

    @DeleteMapping
    public void deactivateShoppingCart(@RequestParam String username) {
        log.info("Received request to deactivate shopping cart of user: {}", username);
        service.deactivateShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProductInShoppingCart(@RequestParam String username,
                                                       @RequestBody List<UUID> products) {
        log.info("Received request to remove products in shopping cart of user: {}", username);
        return service.removeProductToShoppingCart(username, products);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
            @RequestBody ChangeProductQuantityRequest request) {
        log.info("Received request to change product quantity in shopping cart of user: {}", username);
        return service.updateProductQuantity(username, request);
    }

    @PostMapping("/booking")
    public BookedProductsDto bookedProductFromShoppingCart(@RequestParam String username) {
        log.info("Received request to book products in shopping cart of user: {}", username);
        return service.bookProductFromShoppingCart(username);
    }
}
