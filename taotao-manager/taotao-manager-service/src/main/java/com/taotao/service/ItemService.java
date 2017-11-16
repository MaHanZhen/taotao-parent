package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

	public TbItem getItemById(Long itemId) throws Exception;
	
	public EasyUIDataGridResult getItemList(int page, int rows) throws Exception;

	public TaotaoResult createItem(TbItem tbItem, String desc,String itemParams) throws Exception;
}
