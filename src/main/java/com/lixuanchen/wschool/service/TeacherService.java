package com.lixuanchen.wschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lixuanchen.wschool.pojo.LoginForm;
import com.lixuanchen.wschool.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);
}
