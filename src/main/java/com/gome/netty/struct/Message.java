package com.gome.netty.struct;

import java.io.Serializable;

/** 
 * 类说明.
 * netty当中传输的消息
 * <pre>
 * 修改日期        修改人    修改原因
 * 2015年10月28日    周亮亮    新建
 * </pre>
 */

public class Message implements Serializable{
	
	private static final long serialVersionUID = -974314276881698207L;
	//消息类型
	private int messageType;
	//状态
	private int status;
	//用来检测有效性
	private String md5Check;
	
	private String channelId;
	
	private String content;

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMd5Check() {
		return md5Check;
	}

	public void setMd5Check(String md5Check) {
		this.md5Check = md5Check;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
}
