package com.stripe.ach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSourceCollection;

@Service
public class SourceService {

	@Value("${stripe.api.key}")
	private String apiKey;

	public PaymentSourceCollection retrieveCustomerSource(String customerID) throws StripeException {

		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		PaymentSourceCollection paymentSourceCollection = customer.getSources();
		return paymentSourceCollection;

	}

}
