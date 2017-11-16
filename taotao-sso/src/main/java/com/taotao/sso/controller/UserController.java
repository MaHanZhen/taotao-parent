package com.taotao.sso.controller;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.utils.CallBackUtil;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback){
        TaotaoResult taotaoResult = null;

        //参数有效性校验
        if (StringUtils.isBlank(param)) {
            taotaoResult = TaotaoResult.build(400, "校验内容不能为空");
        }
        if (type == null) {
            taotaoResult = TaotaoResult.build(400, "校验内容类型不能为空");
        }
        if (type != 1 && type != 2 && type != 3 ) {
            taotaoResult = TaotaoResult.build(400, "校验内容类型错误");
        }

        if(null !=taotaoResult){
            if(!StringUtils.isEmpty(callback)){
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
                mappingJacksonValue.setJsonpFunction(callback);
                return  mappingJacksonValue;
            }else{
                return  taotaoResult;
            }
        }

        //调用服务

        try {
            taotaoResult = userService.checkData(param,type);
        }catch (Exception e){
            e.printStackTrace();
            taotaoResult = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }

        return CallBackUtil.jsonpCallBack(callback,taotaoResult);
    }

    //创建用户
    @RequestMapping("/register")
    @ResponseBody
    public TaotaoResult createUser(TbUser user) {

        try {
            TaotaoResult result = userService.createUser(user);
            return result;
        } catch (Exception e) {
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    //用户登录
    @RequestMapping(value="/login", method= RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            TaotaoResult result = userService.userLogin(username, password,request,response);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }

    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback){
        TaotaoResult taotaoResult = null;
        try {
            taotaoResult = userService.getUserByToken(token);
        }catch (Exception e){
            e.printStackTrace();
            taotaoResult = TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }

        return CallBackUtil.jsonpCallBack(callback,taotaoResult);
    }

    @RequestMapping("/logout/{token}")
    @ResponseBody
    public Object userLogout(@PathVariable String token,String callback, HttpServletRequest request, HttpServletResponse response){
        TaotaoResult taotaoResult = null;
        try {
            taotaoResult = userService.userLogout(token,request,response);
        }catch (Exception e){
            e.printStackTrace();
            taotaoResult = TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }

        return CallBackUtil.jsonpCallBack(callback,taotaoResult);
    }



}
