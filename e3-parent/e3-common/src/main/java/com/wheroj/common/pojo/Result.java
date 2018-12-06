package com.wheroj.common.pojo;

import java.io.Serializable;

public class Result<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int status;
	
	private String msg;
	
	private T data;
	
	public Result(int status, String msg, T data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public Result(T data) {
		super();
		this.status = 200;
		this.msg = "";
		this.data = data;
	}

	public static <K> Result<K> ok(K data) {
		return new Result<K>(data);
	}
	
	public static Result<?> error(int status, String msg) {
		return new Result<>(status, msg, null);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
