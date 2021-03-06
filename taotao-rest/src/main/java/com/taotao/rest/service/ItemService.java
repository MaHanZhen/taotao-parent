package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

public interface ItemService {

    public TaotaoResult getItemBaseInfo(long itemId);

    public TaotaoResult getItemDecs(long itemId);

    public TaotaoResult getItemParam(long itemId);
}
