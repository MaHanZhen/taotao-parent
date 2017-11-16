package com.taotao.rest.service.impl;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;

    @Override
    public TaotaoResult syncContent(String key) {
        try {
            jedisClient.hdel(INDEX_CONTENT_REDIS_KEY, key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(INDEX_CONTENT_REDIS_KEY + "/" + key + "/" + "删除失败");
        }
        return TaotaoResult.ok();
    }
}
