package com.taotao.portal.service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response);

    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) ;

    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response);
}
