package com.taotao.search.mapper;

import com.taotao.search.pojo.Item;

import java.util.List;

public interface ItemMapper {
    public List<Item> getItemList() throws Exception;
}
