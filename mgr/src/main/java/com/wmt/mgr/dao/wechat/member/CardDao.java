package com.wmt.mgr.dao.wechat.member;

import com.wmt.mgr.model.member.CardData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-23 17:05
 * @version 1.0
 */
@Repository("cardDao")
@Mapper
public interface CardDao {

    @Insert("INSERT INTO card_items(name,durationUnit, duration,price,showSeq,mainRecmd,newDiscount)VALUES(#{name},#{durationUnit},#{duration},#{price},#{showSeq},#{mainRecmd},#{newDiscount})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addCardData(CardData cardData);


    @Delete("DELETE FROM card_items WHERE id =#{cardId}")
    public void removeCard(int cardId);

    @Delete("UPDATE card_items SET name =#{name},durationUnit =#{durationUnit},duration=#{duration},price =#{price},showSeq=#{showSeq},mainRecmd=#{mainRecmd},newDiscount = #{newDiscount} WHERE id = #{id}")
    public void updateCardData(CardData cardData);

    @Delete("UPDATE card_items SET showSeq=#{showSeq} WHERE id = #{cardId}")
    public void updateCardDataShowSeq(int showSeq,int cardId);


    @Select("SELECT * FROM card_items ORDER BY showSeq ASC LIMIT 100")
    public List<CardData> getCardList();

    @Select("SELECT count(*) FROM card_items")
    public Integer countCard();


    @Select("SELECT * FROM card_items WHERE id=#{id} LIMIT 1")
    public CardData getCardData(int id);


    @Select("SELECT * FROM card_items WHERE durationUnit=#{durationUnit} AND duration=#{duration} LIMIT 1")
    public CardData getCardDataByDur(String  durationUnit,int duration);

}
