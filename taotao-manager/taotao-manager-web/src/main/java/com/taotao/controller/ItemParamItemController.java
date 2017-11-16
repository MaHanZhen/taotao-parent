package com.taotao.controller;

import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;
    @RequestMapping("/showItem/{itemId}")
    public String showItem(@PathVariable long itemId, Model model) throws Exception{
        String table = itemParamItemService.getItemParamItemByItemId(itemId);
        model.addAttribute("itemParam",table);
        return "itemParam";
    }
}
