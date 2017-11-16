package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;

public interface ItemParamService {

	public EasyUIDataGridResult getItemParamList(int page, int rows) throws Exception;

	public TaotaoResult getItemParamByCid(Long cid) throws  Exception;

	public TaotaoResult saveItemParam(TbItemParam itemParam) throws  Exception;
}
