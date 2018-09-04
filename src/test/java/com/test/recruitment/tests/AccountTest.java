package com.test.recruitment.tests;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

/**
 * Account test
 * 
 * @author A525125
 *
 */
public class AccountTest extends AbstractTest {

	@Test
	public void getAccounts() throws Exception {
		mockMvc.perform(get("/accounts")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements", is(2)))
				.andExpect(jsonPath("$.content[0].type", is("SAVING")))
				.andExpect(jsonPath("$.content[0].balance", is(4210.42)));
	}

	@Test
	public void getAccountDetails() throws Exception {
		mockMvc.perform(get("/accounts/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.active", is(true)))
				.andExpect(jsonPath("$.type", is("SAVING")))
				.andExpect(jsonPath("$.balance", is(4210.42)));
	}

	@Test
	public void getAccountDetailsOnUnexistingAccount() throws Exception {
		mockMvc.perform(get("/accounts/test")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorCode", is("NOT_FOUND_ACCOUNT")));
	}

	@Test
	public void getAccountsWithPaging() throws Exception {
		mockMvc.perform(get("/accounts?size=1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements", is(1)));
	}

}
