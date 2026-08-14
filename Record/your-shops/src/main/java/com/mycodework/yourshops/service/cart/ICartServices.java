package com.mycodework.yourshops.service.cart;

import com.mycodework.yourshops.model.Cart;
import com.mycodework.yourshops.model.User;


import java.math.BigDecimal;

public interface ICartServices {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);



    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}

