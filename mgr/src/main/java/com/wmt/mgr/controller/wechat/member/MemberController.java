package com.wmt.mgr.controller.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
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
import org.springframework.http.MediaType;
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


    @PostMappingEx(value = "/mgr/m/card/add",funcName = "会员卡添加",module = MgrModules.MEMBER,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @PostMappingEx(value = "/mgr/m/card/edit",funcName = "会员卡编辑",module = MgrModules.MEMBER,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket cardEdit(@RequestBody @Valid CardForm cardForm, BindingResult result){
        if(cardForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"无参数!");
        }
        if(cardForm.getId() == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "id参数必须!");
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

    @PostMappingEx(value = "/mgr/m/card/suggest",funcName = "会员卡推荐设置",module = MgrModules.MEMBER,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket cardSuggest(Integer cardId, Integer flag){
        if(cardId == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"cardId参数必须!");
        }
        if(flag== null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "flag参数必须!");
        }
        if(flag != 1 && flag != 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "flag参数错误");
        }

        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardSuggest(mgrUserData,cardId, flag);

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

    @PostMappingEx(value = "/mgr/m/card/detail",funcName = "会员卡详情",module = MgrModules.MEMBER)
    public BizPacket cardDetail(@RequestParam("cardId") Integer cardId){
        if(cardId == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "cardId参数必须!");
        }
        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardDetail(mgrUserData,cardId);

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
        if(showSeq <0 || showSeq>= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "showSeq参数范围:0-65536 !");
        }
        MgrUserData mgrUserData = getUser();
        try {
            return memberService.cardShowSeqUpdate(mgrUserData,cardId,showSeq);

        } catch (Exception ex) {
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/m/member/list", funcName = "会员管理列表", module = MgrModules.MEMBER)
    public BizPacket queryMemberList(String branchName, String poiUserMobile,
                                     String startTime, String endTime,
                                     Integer index, Integer pageSize){

        if(startTime != null && startTime.length() != 0 && endTime != null && endTime.length() != 0){
            if(startTime.compareTo(endTime) > 0){
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"查询请求开始时间大于结束时间");
            }
        }

        if(branchName != null){
            branchName = branchName.trim();
        }
        if(poiUserMobile != null){
            poiUserMobile = poiUserMobile.trim();
        }

        if(pageSize == null || pageSize <= 0 || pageSize >= Integer.MAX_VALUE){
            pageSize = Constants.PAGESIZE;
        }

        if(index == null || index <= 0 || index >= Integer.MAX_VALUE){
            index = Constants.INDEX;
        }
        return memberService.getMemberList(branchName,poiUserMobile,startTime,endTime,index,pageSize);
    }

    @PostMappingEx(value = "/mgr/m/member/history",funcName = "会员充值历史记录", module = MgrModules.MEMBER)
    public BizPacket queryMemberHistory(String poiId){
        if(poiId == null || poiId.length() == 0){
            return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"店铺Id不能为空poiId="+poiId);
        }
        return memberService.getMemberHistory(poiId);
    }
}