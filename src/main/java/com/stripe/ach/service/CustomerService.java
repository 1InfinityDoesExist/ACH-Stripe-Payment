package com.stripe.ach.service;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.stripe.model.Token;
import com.stripe.param.CustomerCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	@Value("${stripe.api.key}")
	private String apiKey;

	public Customer createCustomer(Map<String, Object> customerParams) throws StripeException {
		Stripe.apiKey = apiKey;

		CustomerCreateParams params = CustomerCreateParams.builder().setEmail("jenny.rosen@example.com")
				.setPaymentMethod("pm_1FWS6ZClCIKljWvsVCvkdyWg").setInvoiceSettings(CustomerCreateParams.InvoiceSettings
						.builder().setDefaultPaymentMethod("pm_1FWS6ZClCIKljWvsVCvkdyWg").build())
				.build();
		Customer customer = Customer.create(customerParams);
		log.info("-----Customer Id :{}", customer.getId());
		return customer;

	}

	public JSONObject retrieveCustomer(String customerId) throws StripeException, ParseException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerId);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		log.info("Customer from Stripe with Id : {} is {}", customerId, customer);

		return (JSONObject) new JSONParser().parse(gson.toJson(customer));
	}

	/**
	 * If the card’s owner has no default card, then the new card will become the
	 * default.
	 * 
	 * @param customerId
	 * @param cardDetails
	 * @return
	 * @throws StripeException
	 */
	public String addCardDetailsCustomer(String customerId, Map<String, Object> cardDetails) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerId);

		Map<String, Object> tokenParam = new HashMap<String, Object>();
		tokenParam.put("card", cardDetails);

		Token token = Token.create(tokenParam);
		log.info("Token : {}", token);

		Map<String, Object> source = new HashMap<String, Object>();
		source.put("source", token.getId());

		PaymentSource paymentSource = customer.getSources().create(source);

		return paymentSource.getId();
	}

}
