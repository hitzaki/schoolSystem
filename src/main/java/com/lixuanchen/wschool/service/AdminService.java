package com.lixuanchen.wschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lixuanchen.wschool.pojo.Admin;
import com.lixuanchen.wschool.pojo.LoginForm;


public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);
}
