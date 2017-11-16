package com.taotao.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;

    @Override
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) throws Exception {
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();

        PageHelper.startPage(page, rows);
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        Page<TbContent> tbContents = (Page<TbContent>) contentMapper.selectByExample(contentExample);

        easyUIDataGridResult.setRows(tbContents);
        easyUIDataGridResult.setTotal(tbContents.getTotal());

        return easyUIDataGridResult;
    }

    @Override
    public TaotaoResult saveContent(TbContent content) throws Exception {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);

        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("缓存同步失败");
            TaotaoResult.build(500,e.getStackTrace().toString());
        }
        return TaotaoResult.ok();
    }
}
