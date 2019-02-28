package com.wmt.mgr.controller.base;

import com.wmt.commons.domain.oss.AliOSSUtil;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Iterator;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 10:12
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ImgController {
    private static Logger logger = LoggerFactory.getLogger(ImgController.class);

    private static final int width = 63;
    private static final int height = 37;

    @RequestMapping(value = "/mgr/img/code")
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        SecureRandom random = new SecureRandom();

        // 设置response头信息
        // 禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);

        // 产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();

        // Graphics类的样式
        g.setColor(getRandColor(200, 250));
        g.setFont(new Font("Times New Roman", 0, 28));
        g.fillRect(0, 0, width, height);

        // 绘制干扰线
        for (int i = 0; i < 40; i++) {
            g.setColor(getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        // 绘制字符
        String strCode = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 28);
        }

        // 将字符保存到session中用于前端的验证
        session.setAttribute(Constants.MGR_IMG_CODE, strCode);
        System.out.println("生成code=" + strCode);
        g.dispose();

        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
    }

    // 创建颜色
    private static Color getRandColor(int fc, int bc) {
        SecureRandom random = new SecureRandom();
        if (fc > 255) {
            fc = 255;
        }

        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }


    /*
     * 采用file.Transto 来保存上传的文件
     */
    @PostMappingEx(value = "/mgr/img/upload",funcName = "图片上传",module = MgrModules.BASIC)
    public BizPacket fileUpload2(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.getSize()<=0) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"空文件!");
        }

        try(InputStream is = file.getInputStream()){
            logger.info("上传文件,originalFilename={},size={},contentType={},name={}",file.getOriginalFilename(),file.getSize(),file.getContentType(),file.getName());
            String url = AliOSSUtil.putBannerImg(file.getInputStream());
            return BizPacket.success(url);
        }
    }

    /*
     *采用spring提供的上传文件的方法
     */
    @PostMappingEx(value = "/mgr/img/upload2",funcName = "图片上传",module = MgrModules.BASIC)
    public BizPacket springUpload(HttpServletRequest request) throws IllegalStateException, IOException {
        // 将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        // 检查form中是否有enctype="multipart/form-data"
        if (!multipartResolver.isMultipart(request)) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "参数错误!");
        }

        // 将request变成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 获取multiRequest 中所有的文件名
        Iterator iter = multiRequest.getFileNames();

        // 遍历所有文件
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next().toString());
            if (file == null || file.getSize()<=0) {
                continue;
            }
            try(InputStream is = file.getInputStream()){
                logger.info("上传文件,originalFilename={},size={},contentType={},name={}",file.getOriginalFilename(),file.getSize(),file.getContentType(),file.getName());
                String url = AliOSSUtil.putBannerImg(is);
                return BizPacket.success(url);
            }
        }
        return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "参数错误!");
    }
}