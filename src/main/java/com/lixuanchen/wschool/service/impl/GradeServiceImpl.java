package com.lixuanchen.wschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixuanchen.wschool.mapper.GradeMapper;
import com.lixuanchen.wschool.pojo.Grade;
import com.lixuanchen.wschool.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(timeout = 10)
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
}
