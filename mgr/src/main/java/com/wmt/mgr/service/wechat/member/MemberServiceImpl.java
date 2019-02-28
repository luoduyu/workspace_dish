package com.wmt.mgr.service.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.dao.wechat.member.CardDao;
import com.wmt.mgr.form.wechat.member.CardForm;
import com.wmt.mgr.model.member.CardData;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

    private @Resource  CardDao cardDao;

    @Override
    public BizPacket cardAdd(MgrUserData admin, CardForm form) {
        Integer total = cardDao.countCard();
        CardData card = createCard(form,total == null? 1:total+1);
        cardDao.addCardData(card);
        return BizPacket.success(card.getId());
    }
    private CardData createCard(CardForm form,int showSeq){
        CardData card = new CardData();
        card.setDuration(form.getDuration());
        card.setDurationUnit(form.getDurationUnit());
        card.setMainRecmd(form.getMainRecmd());
        card.setName(form.getName());
        card.setNewDiscount(form.getNewDiscount());
        card.setPrice(form.getPrice());
        card.setShowSeq(showSeq);
        return card;
    }

    @Override
    public BizPacket cardEdit(MgrUserData admin, CardForm form) {
        CardData card = cardDao.getCardData(form.getId());
        if(card == null){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"不存在的会员卡!id="+form.getId());
        }

        card.setName(form.getName());
        card.setPrice(form.getPrice());
        card.setNewDiscount(form.getNewDiscount());
        card.setMainRecmd(form.getMainRecmd());
        card.setDurationUnit(form.getDurationUnit());
        card.setDuration(form.getDuration());
        if(form.getShowSeq() != null){
            card.setShowSeq(form.getShowSeq());
        }

        cardDao.updateCardData(card);
        return BizPacket.success();
    }

    @Override
    public BizPacket cardRM(MgrUserData admin, int id) {
        cardDao.removeCard(id);
        return BizPacket.success();
    }


    @Override
    public BizPacket cardSuggest(MgrUserData admin, Integer cardId, Integer flag) {
        CardData card = cardDao.getCardData(cardId);
        if(card == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value()," 无此会员卡!");
        }
        card.setMainRecmd(flag);
        cardDao.updateCardData(card);
        return BizPacket.success();
    }

    @Override
    public BizPacket cardDetail(MgrUserData admin, int id) {
        CardData cardData = cardDao.getCardData(id);
        return BizPacket.success(cardData);
    }

    @Override
    public BizPacket cardShowSeqUpdate(MgrUserData admin, int cardId, int showSeq) {
        cardDao.updateCardDataShowSeq(showSeq,cardId);
        return BizPacket.success();
    }
}
