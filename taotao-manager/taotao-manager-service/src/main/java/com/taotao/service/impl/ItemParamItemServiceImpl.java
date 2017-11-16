package com.taotao.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TableUtils;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Override
    public String getItemParamItemByItemId(long itemId) throws Exception {
        TbItemParamItemExample itemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = itemParamItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);

        List<TbItemParamItem> tbItemParamItems = itemParamItemMapper.selectByExampleWithBLOBs(itemParamItemExample);
        if (tbItemParamItems == null || tbItemParamItems.size() == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        List<Map> paramDateList = JsonUtils.jsonToList(tbItemParamItems.get(0).getParamData(), Map.class);

        return TableUtils.getTableByMap(paramDateList);
    }
}
