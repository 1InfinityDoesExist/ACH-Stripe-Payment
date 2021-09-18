package com.stripe.ach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.param.ChargeCreateParams;

@Service
public class PaymentService {

	@Value("${stripe.api.key}")
	private String apiKey;

	public String payUsingParticularCard(String customerID, String cardID) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		ChargeCreateParams params = ChargeCreateParams.builder().setAmount(8500L).setCurrency("usd")
				.setDescription("Pay using a particular card").setCustomer(customer.getId()).setSource(cardID).build();
		Charge charge = Charge.create(params);

		return charge.getId();
	}
}
