package com.lixuanchen.wschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixuanchen.wschool.mapper.AdminMapper;
import com.lixuanchen.wschool.pojo.Admin;
import com.lixuanchen.wschool.pojo.LoginForm;
import com.lixuanchen.wschool.service.AdminService;
import com.lixuanchen.wschool.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(timeout = 10)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("name", loginForm.getUsername())//密码要转成密文
                .eq("password", MD5.encrypt(loginForm.getPassword()));
        return baseMapper.selectOne(wrapper);
    }
}
