package com.lixuanchen.wschool.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    //配置了swagger的Docket的bean实例
    public Docket getDocket() {
        //添加head参数start
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tmpPar = new ParameterBuilder();
        tmpPar.name("userTempId")
                .description("临时用户ID")
                .defaultValue("1")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false);
        pars.add(tmpPar.build());
        //添加head参数end

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("chenAPI")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lixuanchen.wschool.controller"))
                .build()
                .globalOperationParameters(pars);
    }

    //配置swagger信息：=apiinfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact contact = new Contact("本少第一帅", "https://blog.csdn.net/m0_56988741", "1152849733@qq.com");
        return new ApiInfo("我的swaggerAPI文档",
                "这个人很帅, 什么描述也没留下",
                "v1.0",
                "https://mp.csdn.net/console/home",
                contact, "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

}
