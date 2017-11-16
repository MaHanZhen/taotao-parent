package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id", defaultValue = "0") long parentId) throws Exception {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult addNode(long parentId, String name) throws Exception {
        return contentCategoryService.addNode(parentId, name);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteNode(long id) throws Exception {
        return contentCategoryService.deleteNode(id);
    }

    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateNode(long id, String name) throws Exception {
        return contentCategoryService.updateNode(id, name);
    }
}
