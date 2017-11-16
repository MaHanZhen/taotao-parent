package com.taotao.sso.service.impl;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbUserMapper userMapper;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String content, Integer type) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //对数据进行校验：1、2、3分别代表username、phone、email
        if (1 == type) {
            criteria.andUsernameEqualTo(content);
        } else if (2 == type) {
            criteria.andPhoneEqualTo(content);
        } else {
            criteria.andEmailEqualTo(content);
        }

        List<TbUser> list = userMapper.selectByExample(example);
        if (null == list || 0 == list.size()) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult createUser(TbUser user) {

        user.setCreated(new Date());
        user.setUpdated(new Date());

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult userLogin(String username, String password, HttpServletRequest request, HttpServletResponse response) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser> list = userMapper.selectByExample(example);

        if (null == list || list.size() == 0) {
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        TbUser user = list.get(0);

        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return TaotaoResult.build(400, "用户名或密码错误");
        }

        user.setPassword(null);

        String token = UUID.randomUUID().toString();
        String key = REDIS_USER_SESSION_KEY + ":" + token;
        jedisClient.set(key, JsonUtils.objectToJson(user));
        jedisClient.expire(key, SSO_SESSION_EXPIRE);

        CookieUtils.setCookie(request, response, "TT_TOKEN", token);

        return TaotaoResult.ok(token);
    }


    @Override
    public TaotaoResult getUserByToken(String token) {
        String key = REDIS_USER_SESSION_KEY + ":" + token;
        String json = jedisClient.get(key);
        if (StringUtils.isEmpty(json)) {
            return TaotaoResult.build(400, "此session已经过期，请重新登录");
        }
        jedisClient.expire(key, SSO_SESSION_EXPIRE);
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
    }

    @Override
    public TaotaoResult userLogout(String token, HttpServletRequest request, HttpServletResponse response) {
        String key = REDIS_USER_SESSION_KEY + ":" + token;
        jedisClient.get(key);
        CookieUtils.deleteCookie(request, response, "TT_TOKEN");
        return TaotaoResult.ok();
    }
}
