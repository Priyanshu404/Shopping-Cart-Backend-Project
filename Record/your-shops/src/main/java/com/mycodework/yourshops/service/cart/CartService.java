package com.mycodework.yourshops.service.cart;

import com.mycodework.yourshops.exceptions.ResourcesNotFoundException;
import com.mycodework.yourshops.model.Cart;
import com.mycodework.yourshops.model.User;
import com.mycodework.yourshops.repository.CartItemRepository;
import com.mycodework.yourshops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CartService implements ICartServices{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow (() -> new ResourcesNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }


    @Transactional
    @Override
    public void clearCart(Long id) {
     Cart cart = getCart(id);
     cartItemRepository.deleteAllByCartId(id);
     cart.getItems().clear();
     cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);

        return cart.getTotalAmount();
    }


    @Override
    public Cart initializeNewCart(User user) {
      return Optional.ofNullable(getCartByUserId(user.getId()))
              .orElseGet(() ->{
                  Cart cart = new Cart();
                  cart.setUser(user);
                  return cartRepository.save(cart);
              });
    }

    @Override
    public Cart getCartByUserId(Long userId) {

        return cartRepository.findByUserId(userId);
    }
}
