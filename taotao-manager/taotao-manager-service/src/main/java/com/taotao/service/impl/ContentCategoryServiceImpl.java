package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Autowired
    private TbContentMapper contentMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        criteria.andStatusEqualTo(1);
        List<TbContentCategory> contentCategories = contentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
        for (TbContentCategory contentCategory : contentCategories) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setState(contentCategory.getIsParent() ? "closed" : "open");
            easyUITreeNodes.add(easyUITreeNode);
        }
        return easyUITreeNodes;
    }

    @Override
    public TaotaoResult addNode(long parentId, String name) throws Exception {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);

        contentCategory.setIsParent(false);
        contentCategory.setStatus(1);

        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        contentCategory.setSortOrder(1);
        contentCategoryMapper.insert(contentCategory);

        TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);

        if (!parentNode.getIsParent()) {
            parentNode.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult deleteNode(long id) throws Exception {

        TbContentCategory deleteCategory = contentCategoryMapper.selectByPrimaryKey(id);
        deleteCategory.setStatus(2);
        contentCategoryMapper.updateByPrimaryKey(deleteCategory);

        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria contentCategoryCriteria = example.createCriteria();
        contentCategoryCriteria.andParentIdEqualTo(deleteCategory.getParentId());
        List<TbContentCategory> contentCategories = contentCategoryMapper.selectByExample(example);

        boolean isParent = false;
        if (contentCategories != null && contentCategories.size() > 0) {
            for (TbContentCategory contentCategory : contentCategories) {
                if (contentCategory.getStatus() != 2) {
                    isParent = true;
                    break;
                }
            }
        }

        if (!isParent) {
            TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(deleteCategory.getParentId());
            contentCategory.setIsParent(isParent);
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateNode(long id, String name) throws Exception {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return TaotaoResult.ok();
    }
}
