package com.amt.wechat.service;

import com.amt.wechat.model.poster.SequencePoster;
import com.amt.wechat.service.poster.IPosterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-26 13:21
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PosterServiceTests {

    private @Resource IPosterService posterService;

    @Test
    public void recommendPosterListTest() {
        List<SequencePoster> list = posterService.getRecommendPosterList();
        list.stream().forEach((e) ->{
            System.out.println(e.toString());
        });
    }
}
