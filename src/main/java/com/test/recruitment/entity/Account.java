package com.test.recruitment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Account entity
 * 
 * @author A525125
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account implements Serializable {

	private static final long serialVersionUID = -3548441891975414771L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "number")
	private String number;

	@Column(name = "type")
	private String type;

	@Column(name = "balance")
	private BigDecimal balance;

	@Column(name = "creation_date")
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Column(name = "is_active")
	private boolean isActive;
}
