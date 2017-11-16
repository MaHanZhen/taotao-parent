package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public TaotaoResult getItemBaseInfo(long itemId) {

        String key = REDIS_ITEM_KEY+":"+itemId+":base";

        try {
            String json = jedisClient.get(key);
            if(!StringUtils.isEmpty(json)){
                TbItem item = JsonUtils.jsonToPojo(json,TbItem.class);
                return  TaotaoResult.ok(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        try {
            jedisClient.set(key, JsonUtils.objectToJson(item));
            jedisClient.expire(key,REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok(item);
    }

    @Override
    public TaotaoResult getItemDecs(long itemId) {

        String key = REDIS_ITEM_KEY+":"+itemId+":decs";

        try {
            String json = jedisClient.get(key);
            if(!StringUtils.isEmpty(json)){
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json,TbItemDesc.class);
                return  TaotaoResult.ok(itemDesc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemDesc item = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            jedisClient.set(key, JsonUtils.objectToJson(item));
            jedisClient.expire(key,REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok(item);
    }

    @Override
    public TaotaoResult getItemParam(long itemId) {

        String key = REDIS_ITEM_KEY+":"+itemId+":param";

        try {
            String json = jedisClient.get(key);
            if(!StringUtils.isEmpty(json)){
                TbItemParamItem itemDesc = JsonUtils.jsonToPojo(json,TbItemParamItem.class);
                return  TaotaoResult.ok(itemDesc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        TbItemParamItemExample paramItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = paramItemExample.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(paramItemExample);

        if(list!=null&&list.size()>0){
            TbItemParamItem paramItem =list.get(0);
            try {
                jedisClient.set(key, JsonUtils.objectToJson(paramItem));
                jedisClient.expire(key,REDIS_ITEM_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
            return TaotaoResult.ok(paramItem);
        }
        return TaotaoResult.build(400,"无此商品规格");

    }
}
