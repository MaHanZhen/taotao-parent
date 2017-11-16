package com.taotao.portal.service.impl;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TableUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {


    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ERST_ITEM_INFO_URL}")
    private String ERST_ITEM_INFO_URL;
    @Value("${ERST_DESC_INFO_URL}")
    private String ERST_DESC_INFO_URL;
    @Value("${ERST_PARAM_INFO_URL}")
    private String ERST_PARAM_INFO_URL;

    @Override
    public ItemInfo getItemById(Long itemId) {

        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + ERST_ITEM_INFO_URL + itemId);
            if (!StringUtils.isEmpty(json)) {
                TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, ItemInfo.class);
                if (taotaoResult.getStatus() == 200) {
                    ItemInfo itemInfo = (ItemInfo) taotaoResult.getData();
                    return itemInfo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getItemDesc(Long itemId) {

        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + ERST_DESC_INFO_URL + itemId);
            if (!StringUtils.isEmpty(json)) {
                TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);
                if (taotaoResult.getStatus() == 200) {
                    TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
                    return itemDesc.getItemDesc();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String getItemParam(Long itemId) {
        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + ERST_PARAM_INFO_URL + itemId);
            //把json转换成java对象
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
            if (taotaoResult.getStatus() == 200) {
                TbItemParamItem itemParamItem = (TbItemParamItem) taotaoResult.getData();
                String paramData = itemParamItem.getParamData();
                //生成html
                // 把规格参数json数据转换成java对象
                List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);

                //返回html片段
                return TableUtils.getTableByMap(jsonList);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

}