package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

public interface RedisService {
    public TaotaoResult syncContent(String key);
}
