package com.amt.wechat.dao.member;

import com.amt.wechat.model.member.MemberCardData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-10 14:27
 * @version 1.0
 */
@Repository("memberDao")
@Mapper
public interface MemberDao {

    @Select("SELECT * FROM card_items ORDER BY showSeq ASC")
    public List<MemberCardData> getMemberCardList();


    @Select("SELECT * FROM card_items WEHRE id=#{id} LIMIT 1")
    public MemberCardData getMemberCardData(int memberCardId);
}
