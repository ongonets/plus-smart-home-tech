package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartState;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;

    @Override
    public ShoppingCartDto findShoppingCarte(String username) {
        validateUsername(username);
        return mapper.map(getShoppingCart(username));
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Integer> products) {
        validateUsername(username);
        ShoppingCart shoppingCart = getShoppingCart(username);
        Map<UUID, Integer> oldProducts = shoppingCart.getProducts();
        oldProducts.putAll(products);
        repository.save(shoppingCart);
        log.info("Add products to shopping cart ID: {}", shoppingCart.getId());
        return mapper.map(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProductToShoppingCart(String username, List<UUID> products) {
        validateUsername(username);
        ShoppingCart shoppingCart = getShoppingCart(username);
        Map<UUID, Integer> oldProducts = shoppingCart.getProducts();
        products.forEach(oldProducts::remove);
        repository.save(shoppingCart);
        log.info("Remove products from shopping cart ID: {}", shoppingCart.getId());
        return mapper.map(shoppingCart);
    }


    @Override
    public void deactivateShoppingCart(String username) {
        validateUsername(username);
        Optional<ShoppingCart> shoppingCartOpt = repository.findByUsernameAndState(username, ShoppingCartState.ACTIVE);
        if (shoppingCartOpt.isEmpty()) {
            return;
        }
        ShoppingCart shoppingCart = shoppingCartOpt.get();
        shoppingCart.setState(ShoppingCartState.DEACTIVATE);
        repository.save(shoppingCart);
        log.info("Deactivated shopping cart ID: {}", shoppingCart.getId());
    }

    @Override
    public ShoppingCartDto updateProductQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);
        ShoppingCart shoppingCart = getShoppingCart(username);
        Map<UUID, Integer> oldProducts = shoppingCart.getProducts();
        oldProducts.put(request.getProductId(), request.getNewQuantity());
        repository.save(shoppingCart);
        log.info("Update product quantity in shopping cart ID: {}", shoppingCart.getId());
        return mapper.map(shoppingCart);
    }

    @Override
    public BookedProductsDto bookProductFromShoppingCart(String username) {
        validateUsername(username);
        return null;
    }

    private ShoppingCart getShoppingCart(String username) {
        Optional<ShoppingCart> shoppingCartOpt = repository.findByUsernameAndState(username, ShoppingCartState.ACTIVE);
        if (shoppingCartOpt.isPresent()) {
            return shoppingCartOpt.get();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUsername(username);
        shoppingCart.setProducts(new HashMap<>());
        shoppingCart.setState(ShoppingCartState.ACTIVE);
        return repository.save(shoppingCart);
    }

    private void validateUsername(String username) {
        if (!username.equals(username)) {
            throw new NotAuthorizedUserException(String.format("Not authorized user: %s", username));
        }
    }
}
