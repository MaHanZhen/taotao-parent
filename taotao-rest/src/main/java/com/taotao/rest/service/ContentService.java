package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

public interface ContentService {
    public TaotaoResult getContentList(Long cid) throws Exception;
}
