package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
    public EasyUIDataGridResult getContentList(long categoryId,int page,int rows) throws Exception;

    public TaotaoResult saveContent(TbContent content) throws Exception;
}
