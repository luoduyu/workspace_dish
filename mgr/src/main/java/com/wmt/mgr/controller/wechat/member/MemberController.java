package com.wmt.mgr.controller.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.controller.base.BaseController;
import com.wmt.mgr.dao.wechat.member.CardDao;
import com.wmt.mgr.domain.annotation.GetMappingEx;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.form.wechat.member.CardForm;
import com.wmt.mgr.model.member.CardData;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import com.wmt.mgr.service.wechat.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 会员相关业务
 *
 * @author adu Create on 2019-02-23 17:02
 * @version 1.0
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class MemberController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    private @Resource CardDao cardDao;
    private @Resource MemberService memberService;

    @GetMappingEx(value = "/mgr/m/card/list",funcName = "会员卡浏览",module = MgrModules.MEMBER)
    public BizPacket cardList(){
        List<CardData>  list= cardDao.getCardList();
        return BizPacket.success(list);
    }


    @PostMappingEx(value = "/mgr/m/card/add",funcName = "会员卡添加",module = MgrModules.MEMBER)
    public BizPacket cardAdd(@RequestBody @Valid CardForm cardForm, BindingResult result){
        if(cardForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"cardForm参数必须!");
        }
        if(result.hasErrors()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),result.getFieldError().getDefaultMessage());
        }

        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardAdd(mgrUserData,cardForm);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/m/card/edit",funcName = "会员卡编辑",module = MgrModules.MEMBER)
    public BizPacket cardEdit(@RequestBody @Valid CardForm cardForm, BindingResult result){
        if(cardForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"cardForm参数必须!");
        }
        if(cardForm.getId() == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "CardForm.id参数必须!");
        }
        if(result.hasErrors()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),result.getFieldError().getDefaultMessage());
        }

        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardEdit(mgrUserData,cardForm);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/m/card/rm",funcName = "会员卡删除",module = MgrModules.MEMBER)
    public BizPacket cardRM(@RequestParam("cardId") Integer cardId){
        if(cardId == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "cardId参数必须!");
        }
        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardRM(mgrUserData,cardId);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/m/card/seq/upd",funcName = "会员卡显示顺序调整",module = MgrModules.MEMBER)
    public BizPacket cardShowSeqUpdate(@RequestParam("cardId") Integer cardId,@RequestParam("showSeq")Integer showSeq){
        if(cardId == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "cardId参数必须!");
        }
        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardShowSeqUpdate(mgrUserData,cardId,showSeq);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }
}