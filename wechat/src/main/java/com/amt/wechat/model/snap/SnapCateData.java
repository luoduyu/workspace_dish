package com.amt.wechat.model.snap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;


/**
 * Copyright (c) 2019 by CANSHU
 *
 *  抢购分类
 *
 * table:snap_cate
 *
 * @author adu Create on 2019-01-28 11:36
 * @version 1.0
 */
public class SnapCateData implements Serializable{
	private static final long serialVersionUID = -719966694039484975L;

	private Integer id;

	/**
	 * 标题
	 */
	private String name;

	/**
	 * 是否可用;0:不可用;1:可用;
	 */
	private Integer isEnabled;

	/**
	 * 封面图片地址
	 */
	private String coverImg;

	/**
	 * 描述
	 */
	private String drcp;


	/**
	 * 标签
	 */
	private String tags;

	/**
	 * 抢购时间段
	 */
	private JSONArray timeFrames;

	/**
	 * 显示顺序
	 */
	private Integer showSeq;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getDrcp() {
		return drcp;
	}

	public void setDrcp(String drcp) {
		this.drcp = drcp;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public JSONArray getTimeFrames() {
		return timeFrames;
	}

	public void setTimeFrames(JSONArray timeFrames) {
		this.timeFrames = timeFrames;
	}

	public Integer getShowSeq() {
		return showSeq;
	}

	public void setShowSeq(Integer showSeq) {
		this.showSeq = showSeq;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}