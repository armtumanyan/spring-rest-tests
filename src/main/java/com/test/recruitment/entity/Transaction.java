package com.test.recruitment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Transaction entity
 * 
 * @author A525125
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 706690724306325415L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "account_id")
	private String accountId;

	@Column(name = "number")
	private String number;

	@Column(name = "balance")
	private BigDecimal balance;
}
