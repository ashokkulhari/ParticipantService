package com.abc.model;

import lombok.Data;

@Data
public class ResponseVo {

	
	private Object user;
	private String errorMessage;
	private String errocode;
	private Boolean success;
}
