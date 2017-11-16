package com.taotao.portal.service.impl;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Value("${SSO_BASE_URL}")
    public String SSO_BASE_URL;
    @Value("${SSO_USER_TOKEN}")
    public String SSO_USER_TOKEN;
    @Value("${SSO_PAGE_LOGIN}")
    public String SSO_PAGE_LOGIN;



    @Override
    public TbUser getUserByToken(String token) {
        try {
           String json =  HttpClientUtil.doGet(SSO_BASE_URL+SSO_USER_TOKEN+token);

            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json,TbUser.class);
            if (taotaoResult.getStatus() == 200) {
                TbUser user = (TbUser) taotaoResult.getData();
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }
}
