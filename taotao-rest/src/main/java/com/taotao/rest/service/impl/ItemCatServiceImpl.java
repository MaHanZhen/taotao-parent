package com.taotao.rest.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public CatResult getItemCatList() throws Exception {
        CatResult catResult = new CatResult();
        catResult.setData(getCatList((long) 0));
        return catResult;
    }

    public List getCatList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);
        List result = new ArrayList();
        int index = 0;
        for (TbItemCat tbItemCat : tbItemCats) {
            index++;
            if (tbItemCat.getIsParent()) {
                CatNode catNode = new CatNode();

                if (parentId == 0) {
                    catNode.setName("<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
                } else {
                    catNode.setName(tbItemCat.getName());
                }

                catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
                //开始递归
                catNode.setItem(getCatList(tbItemCat.getId()));

                result.add(catNode);

            } else {
                result.add("/products/"+tbItemCat.getId()+".html|" + tbItemCat.getName());
            }

            if(parentId ==0 && index>=14){
                break;
            }
        }
        return result;
    }
}
