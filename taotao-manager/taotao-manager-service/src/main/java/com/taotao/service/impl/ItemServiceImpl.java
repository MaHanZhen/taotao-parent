package com.taotao.service.impl;

import java.util.Date;

import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Override
    public TbItem getItemById(Long itemId) throws Exception {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) throws Exception {
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        PageHelper.startPage(page, rows);
        Page<TbItem> items = (Page<TbItem>) itemMapper.selectByExample(null);
        easyUIDataGridResult.setTotal(items.getTotal());
        easyUIDataGridResult.setRows(items);
        return easyUIDataGridResult;
    }

    @Override
    public TaotaoResult createItem(TbItem tbItem, String decs,String itemParams) throws Exception {
        long id = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        itemMapper.insert(tbItem);

        TaotaoResult result = createItemDecs(id, decs);
        if (result.getStatus() != 200) {
            throw new Exception();
        }

        result = createItemParamItem(id,itemParams);

        return TaotaoResult.ok();
    }

    private TaotaoResult createItemParamItem(Long itemId, String itemParams) {
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        itemParamItemMapper.insert(itemParamItem);

        return TaotaoResult.ok();
    }

    private TaotaoResult createItemDecs(Long id, String desc) throws Exception {
        TbItemDesc record = new TbItemDesc();
        record.setItemId(id);
        record.setItemDesc(desc);
        record.setCreated(new Date());
        record.setUpdated(new Date());
        itemDescMapper.insert(record);
        return TaotaoResult.ok();
    }

}
