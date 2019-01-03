package com.amt.wechat.form;

import com.amt.wechat.model.poster.SequencePoster;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-26 16:00
 * @version 1.0
 */
public class SeqPosterWrapper implements Serializable {
    private static final long serialVersionUID = 2193546507540652130L;
    private int posterListSize;

    private List<SequencePoster> posterList;

    public SeqPosterWrapper(int posterListSize, List<SequencePoster> posterList) {
        this.posterListSize = posterListSize;
        this.posterList = posterList;
    }

    public int getPosterListSize() {
        return posterListSize;
    }

    public void setPosterListSize(int posterListSize) {
        this.posterListSize = posterListSize;
    }

    public List<SequencePoster> getPosterList() {
        return posterList;
    }

    public void setPosterList(List<SequencePoster> posterList) {
        this.posterList = posterList;
    }
}