package com.abc.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
@Data
public class ParticipantDTO {

	private Integer active;

	private String email;

	private String firstname;

//	@Temporal(TemporalType.TIMESTAMP)
//	private Date inserted;

	private String lastname;

	private String refid;

	private String src;

	private String type;
}
