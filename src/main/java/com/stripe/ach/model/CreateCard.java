package com.stripe.ach.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateCard {

	@JsonProperty("number")
	String cardNumber;

	@JsonProperty("exp_month")
	String expMonth;

	@JsonProperty("exp_year")
	String exYear;

	@JsonProperty("cvc")
	String cvc;

}
