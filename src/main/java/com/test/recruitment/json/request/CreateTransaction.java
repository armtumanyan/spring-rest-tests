package com.test.recruitment.json.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.test.recruitment.json.AbstractTransaction;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransaction extends AbstractTransaction implements Serializable {
}
