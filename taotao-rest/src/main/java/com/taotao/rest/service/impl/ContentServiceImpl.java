package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;

    @Override
    public TaotaoResult getContentList(Long cid) throws Exception {
        String jedisEesult = null;
        try {
            jedisEesult = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, cid.toString());
            if (!StringUtils.isEmpty(jedisEesult)) {
                List<TbContent> tbContents = JsonUtils.jsonToList(jedisEesult, TbContent.class);
                return TaotaoResult.ok(tbContents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExample(example);

        try {
            if (StringUtils.isEmpty(jedisEesult)) {
                jedisClient.hset(INDEX_CONTENT_REDIS_KEY, cid.toString(), JsonUtils.objectToJson(tbContents));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok(tbContents);
    }
}
