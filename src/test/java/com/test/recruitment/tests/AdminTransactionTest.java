package com.test.recruitment.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

/**
 * Account test
 * 
 * @author A525125
 *
 */
public class AdminTransactionTest extends AbstractTest {

	@Test
	@Transactional
	public void createTransaction() throws Exception {
		String request = getRequest("createOk");

		mockMvc.perform(
				post("/accounts/1/transactions").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isCreated());
	}

	@Test
	public void createTransactionBadRequest() throws Exception {
		String request = getRequest("createBadRequest");

		mockMvc.perform(
				post("/accounts/1/transactions").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateTransaction() throws Exception {
		String request = getRequest("updateOk");

		mockMvc.perform(
				put("/accounts/1/transactions/3").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isNoContent());
	}

	@Test
	public void updateTransactionWhichNotBelongToTheAccount() throws Exception {
		String request = getRequest("updateOk");

		mockMvc.perform(
				put("/accounts/2/transactions/3").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isForbidden());
	}

	@Test
	public void updateUnexistingTransaction() throws Exception {
		String request = getRequest("updateOk");

		mockMvc.perform(
				put("/accounts/1/transactions/8").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateTransactionBadRequest() throws Exception {
		String request = "test";

		mockMvc.perform(
				put("/accounts/1/transactions/3").contentType(
						MediaType.APPLICATION_JSON).content(request))
				.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void deleteTransaction() throws Exception {
		mockMvc.perform(delete("/accounts/1/transactions/1")).andExpect(
				status().isNoContent());
	}

	@Test
	public void deleteTransactionWhichNotBelongToTheAccount() throws Exception {
		mockMvc.perform(delete("/accounts/2/transactions/2")).andExpect(
				status().isForbidden());
	}

	@Test
	public void deleteUnexistingTransaction() throws Exception {
		mockMvc.perform(delete("/accounts/1/transactions/99")).andExpect(
				status().isNotFound());
	}

	/**
	 * Get json request from test file
	 * 
	 * @param name
	 *            the filename
	 * @return the request
	 * @throws IOException
	 */
	private String getRequest(String name) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("json/" + name + ".json"), writer);
		return writer.toString();
	}

}
