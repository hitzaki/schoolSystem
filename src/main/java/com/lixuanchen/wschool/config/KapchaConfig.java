package com.lixuanchen.wschool.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KapchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        //是否有边框
        properties.setProperty(Constants.KAPTCHA_BORDER,"yes");
        //验证码文本颜色
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,"blue");
        //验证码图片宽度
        properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,"90");
        //验证码图片高度
        properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,"35");
        //文本字符大小
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,"30");
        //验证码session的值
        properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY,"kaptchaCode");
        //验证码文本长度
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        //字体
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, "宋体,楷体,微软雅黑");

        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
