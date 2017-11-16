package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    public List<EasyUITreeNode> getContentCategoryList(long parentId);

    public TaotaoResult addNode(long parentId, String name) throws Exception;

    public TaotaoResult deleteNode( long id) throws Exception;

    public TaotaoResult updateNode(long id, String name) throws Exception;
}
