package com.wmt.mgr.controller.mgr.user;


import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.controller.base.BaseController;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.form.mgr.user.UserForm;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import com.wmt.mgr.service.mgr.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Copyright (c) 2019 by CANSHU
 *  角色/权限/帐户
 * @author adu Create on 2019-02-23 11:43
 * @version 1.0
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class UserController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private @Resource UserService userService;

    @PostMappingEx(value = "/mgr/user/list",funcName = "帐户列表",module = MgrModules.USER)
    public BizPacket userList(String name,String mobile,Integer isEnabled,Integer index,Integer pageSize){
        if(isEnabled != null){
            if(isEnabled != 0 && isEnabled != 1 && isEnabled != -1){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数isEnabled错误!");
            }
        }
        if(index == null || index <0 || index >= Integer.MAX_VALUE){
            index = 0;
        }
        if(pageSize == null || pageSize <0 || pageSize >= Integer.MAX_VALUE){
            pageSize = 20;
        }

        return userService.userList(name,mobile,isEnabled,index,pageSize);
    }


    @PostMappingEx(value = "/mgr/user/add",funcName = "帐户添加",module = MgrModules.USER,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket userList(@RequestBody @Valid UserForm userForm, BindingResult result){
        if(userForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"userForm参数必须!");
        }
        if(result.hasErrors()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),result.getFieldError().getDefaultMessage());
        }

        MgrUserData mgrUserData = getUser();
        try {
            return userService.userAdd(mgrUserData, userForm);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @PostMappingEx(value = "/mgr/user/edit",funcName = "帐户编辑",module = MgrModules.USER,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket userEdit(@RequestBody @Valid UserForm userForm, BindingResult result) {
        if (userForm == null) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "userForm参数必须!");
        }
        if(userForm.getId() == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "user.id参数必须!");
        }
        if(result.hasErrors()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),result.getFieldError().getDefaultMessage());
        }

        MgrUserData admin = getUser();
        try {
            userService.userEdit(admin,userForm);
            return BizPacket.success();

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @PostMappingEx(value = "/mgr/user/detail",funcName = "人员详细信息",module = MgrModules.USER)
    public BizPacket userDetail(@RequestParam("mgrUid") Integer mgrUid){
        if(mgrUid == null || mgrUid <=0 || mgrUid >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "mgrUId参数必须!");
        }

        MgrUserData admin = getUser();
        try {
            return userService.userDetail(admin,mgrUid);

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}