package com.taotao.rest.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/content")
@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @Autowired
    private JedisClient jedisClient;
    @RequestMapping("/list/{cid}")
    @ResponseBody
    public TaotaoResult getContentList(@PathVariable long cid) {



        TaotaoResult result = null;
        try {
            result = contentService.getContentList(cid);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, e.getMessage());
        }


        return result;
    }
}
