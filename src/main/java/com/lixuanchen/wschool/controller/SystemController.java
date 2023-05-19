package com.lixuanchen.wschool.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lixuanchen.wschool.pojo.Admin;
import com.lixuanchen.wschool.pojo.LoginForm;
import com.lixuanchen.wschool.pojo.Student;
import com.lixuanchen.wschool.pojo.Teacher;
import com.lixuanchen.wschool.service.AdminService;
import com.lixuanchen.wschool.service.StudentService;
import com.lixuanchen.wschool.service.TeacherService;
import com.lixuanchen.wschool.util.JwtHelper;
import com.lixuanchen.wschool.util.MD5;
import com.lixuanchen.wschool.util.Result;
import com.lixuanchen.wschool.util.ResultCodeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("sms/system")
public class SystemController {
    //____________________用户修改自己的密码___________________
    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token, @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        if(JwtHelper.isExpiration(token)){//token过期
            return Result.fail().message("token失效!");
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        System.out.println(userType);
        // 将明文密码转换为暗文
        oldPwd=MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);
        switch (userType){
            case 1:
                UpdateWrapper<Admin> wrapper1 = new UpdateWrapper<>();
                wrapper1.set("password", newPwd).eq("id", userId).eq("password", oldPwd);
                return adminService.update(wrapper1)? Result.ok(): Result.fail();
            case 2:
                UpdateWrapper<Student> wrapper2 = new UpdateWrapper<>();
                wrapper2.set("password", newPwd).eq("id", userId).eq("password", oldPwd);
                return studentService.update(wrapper2)? Result.ok(): Result.fail();
            case 3:
                UpdateWrapper<Teacher> wrapper3 = new UpdateWrapper<>();
                wrapper3.set("password", newPwd).eq("id", userId).eq("password", oldPwd);
                return teacherService.update(wrapper3)? Result.ok(): Result.fail();
            default: return Result.fail();
        }
    }

    //________________________图片上传________________________
    String[] suffixs = new String[]{".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};

    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(HttpServletRequest request, @RequestPart("multipartFile") MultipartFile image){
        if(image.getSize()>20971520) return Result.fail().message("图片不允许超过20MB");

        String fileName = image.getOriginalFilename();
        fileName = fileName.substring(fileName.lastIndexOf('.'));
        //接下来判断是否符合文件格式
        boolean flag = true;
        for(String str : suffixs){
            if(str.equals(fileName)) {flag = false; break;}
        }
        if(flag) return Result.fail().message("文件不符合图片的格式");
        fileName = UUID.randomUUID().toString().replace("-", "") + fileName;
        try {
            image.transferTo(new File(request.getServletContext().getRealPath("upload") + File.separator + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok("upload/"+fileName);
    }


        //________________________登录验证及信息回传________________________
    //________________________登录信息验证
    @Resource
    public AdminService adminService;
    @Resource
    public StudentService studentService;
    @Resource
    public TeacherService teacherService;

    @ApiOperation("登录请求验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession session){
        //_____________________验证码校验
        String sessionVerify = (String)session.getAttribute("VerifyCode");
        String Verify = loginForm.getVerifiCode();
        if("".equals(sessionVerify) || null ==sessionVerify)
            return Result.fail().message("验证码失效，请刷新后重试");
        if(!sessionVerify.equals(Verify))
            return Result.fail().message("验证码填写错误，请刷新重试");
        session.removeAttribute("VerifyCode");//移除验证码

        //________根据不同表校验身份
        Map<String,Object> map=new HashMap<>();
        try {//检测getId可能产生的空指针异常.
            switch(loginForm.getUserType()){
                case 1:
                    Admin admin = adminService.login(loginForm);
                    if(admin==null) return Result.fail().message("用户名/密码有误");
                    map.put("token", JwtHelper.createToken((long)admin.getId(), 1));
                    break;
                case 2:
                    Student student = studentService.login(loginForm);
                    if(student==null) return Result.fail().message("用户名/密码有误");
                    map.put("token", JwtHelper.createToken((long)student.getId(), 2));
                    break;
                case 3:
                    Teacher teacher = teacherService.login(loginForm);
                    if(teacher==null) return Result.fail().message("用户名/密码有误");
                    map.put("token", JwtHelper.createToken((long)teacher.getId(), 3));
                    break;
                default: return Result.fail().message("找不到用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(map);
    }

    //________________________用户信息回传,前端跳转到首页________________________
    @GetMapping("/getInfo")
    public Result returnInfo(@RequestHeader("token") String token){
        //验证token是否到期
        if(JwtHelper.isExpiration(token)) return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        //解析token,获取用户id与用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new HashMap<>();
        map.put("userType", userType);
        //查找对应的user实体类
        switch(userType){
            case 1:
                map.put("user", adminService.getById(userId)); break;
            case 2:
                map.put("user", studentService.getById(userId)); break;
            case 3:
                map.put("user", teacherService.getById(userId)); break;
            default: return Result.fail().message("登录信息被篡改");
        }
        return Result.ok(map);
    }

    //________________________验证码图片________________________
    @Resource
    private DefaultKaptcha kaptcha;

    @GetMapping("/getVerifiCodeImage")
    public void verifyCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //响应立即过期
        response.setDateHeader("Expires", 0);
        //不缓存任何图片数据
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.setHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/png");
        //生成验证码字符文本
        String verifyCode = kaptcha.createText();
        request.getSession().setAttribute("VerifyCode", verifyCode);
        BufferedImage image = kaptcha.createImage(verifyCode);//创建验证图片
        ServletOutputStream stream = response.getOutputStream();
        ImageIO.write(image, "png", stream);
        stream.flush();
        stream.close();
    }
}
