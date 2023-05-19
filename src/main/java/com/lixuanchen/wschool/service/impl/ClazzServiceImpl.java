package com.lixuanchen.wschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lixuanchen.wschool.mapper.ClazzMapper;
import com.lixuanchen.wschool.pojo.Clazz;
import com.lixuanchen.wschool.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(timeout = 10)
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
}
