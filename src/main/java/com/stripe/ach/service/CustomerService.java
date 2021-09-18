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
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.stripe.model.Source;
import com.stripe.model.Token;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentSourceCollectionCreateParams;
import com.stripe.param.TokenCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	@Value("${stripe.api.key}")
	private String apiKey;

	public Customer createCustomer(Map<String, Object> customerParams) throws StripeException {
		Stripe.apiKey = apiKey;

		CustomerCreateParams params = CustomerCreateParams.builder().setEmail("jenny.rosen@example.com").build();
		Customer customer = Customer.create(params);
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

	/**
	 * This method will deduct money from the default card. or source (the default
	 * one).
	 * 
	 * @param customerId
	 * @param cardDetails
	 * @return
	 * @throws StripeException
	 */
	public String payment(String customerId, Map<String, Object> cardDetails) throws StripeException {

		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerId);

		ChargeCreateParams params = ChargeCreateParams.builder().setAmount(500L).setCurrency("usd")
				.setCustomer(customer.getId()).build();
		Charge charge = Charge.create(params);

		System.out.println(charge);
		return "";

	}

	/**
	 * 
	 * Tokenization is the process Stripe uses to collect sensitive card or bank
	 * account details, or personally identifiable information (PII), directly from
	 * your customers in a secure manner. A token representing this information is
	 * returned to your server to use.
	 * 
	 * 
	 * 
	 * @return
	 * 
	 * @throws StripeException
	 */
	public String paymentUsingCard(String customerId, Map<String, Object> cardDetails) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerId);

		TokenCreateParams tokenParam = TokenCreateParams.builder()
				.setCard(TokenCreateParams.Card.builder().putAllExtraParam(cardDetails).build()).build();

		Token token = Token.create(tokenParam);
		log.info("Token : {}", token);

		PaymentSourceCollectionCreateParams pscc = PaymentSourceCollectionCreateParams.builder()
				.setSource(token.getId()).build();

		customer.getSources().create(pscc);

		ChargeCreateParams params = ChargeCreateParams.builder().setAmount(500L).setCurrency("usd")
				.setDescription("Testing Purpose").setStatementDescriptor("Still testing").setCustomer(customer.getId())
				.build();
		Charge charge = Charge.create(params);

		log.info("Charge ID : {}", charge.getId());

		return "Paid";
	}
}
