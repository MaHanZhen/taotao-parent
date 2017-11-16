package com.taotao.service.impl;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.service.ItemParamService;

import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

	@Autowired
	private TbItemParamMapper itemParamMapper;
	@Override
	public EasyUIDataGridResult getItemParamList(int page, int rows) throws Exception {
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();

		PageHelper.startPage(page,rows);
		TbItemParamExample example = new TbItemParamExample();
		Page<TbItemParam> items = (Page<TbItemParam>) itemParamMapper.selectByExampleWithBLOBs(example);
		easyUIDataGridResult.setTotal(items.getTotal());
		easyUIDataGridResult.setRows(items);
		return easyUIDataGridResult;	
	}

	@Override
	public TaotaoResult getItemParamByCid(Long cid) throws Exception {
		TbItemParamExample example = new TbItemParamExample();
		TbItemParamExample.Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> paramList = itemParamMapper.selectByExampleWithBLOBs(example);
		if(paramList!=null&&paramList.size()>0){
			return TaotaoResult.ok(paramList.get(0));
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult saveItemParam(TbItemParam itemParam) throws Exception {
		itemParamMapper.insert(itemParam);
		return TaotaoResult.ok();
	}

}
