package com.stripe.ach.service.ach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.ach.model.BankAccount;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Token;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentSourceCollectionCreateParams;
import com.stripe.param.TokenCreateParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ACHService {
	@Value("${stripe.api.key}")
	private String apiKey;

	public String addBankAccount(String customerID, BankAccount bankAccount)
			throws StripeException, JsonProcessingException, ParseException {

		log.info("Adding bankAccount details {} to customer with ID : {}", bankAccount, customerID);
		Stripe.apiKey = apiKey;

		Customer customer = Customer.retrieve(customerID);

		TokenCreateParams tokenCreateParams = TokenCreateParams.builder()
				.setBankAccount(TokenCreateParams.BankAccount.builder().setCountry(bankAccount.getCountry())
						.setCurrency(bankAccount.getCurrency()).setAccountHolderName(bankAccount.getAccountHolderName())
						.setAccountHolderType(TokenCreateParams.BankAccount.AccountHolderType.COMPANY)
						.setAccountNumber(bankAccount.getAccountNumber())
						.setRoutingNumber(bankAccount.getRoutingNumber()).build())
				.build();

		Token token = Token.create(tokenCreateParams);

		/**
		 * Prevent adding a bank account twice.
		 */
		PaymentSourceCollection sources = customer.getSources();
		for (PaymentSource ps : sources.getData()) {
			JSONObject obj = (JSONObject) new JSONParser().parse(new ObjectMapper().writeValueAsString(ps));
			if ("bank_account".equals(obj.get("object"))) {
				com.stripe.model.BankAccount ba = (com.stripe.model.BankAccount) ps;
				if (ba.getFingerprint().equals(token.getBankAccount().getFingerprint())) {
					return "BankAccount Already Exist";
				}
			}
		}
		PaymentSourceCollectionCreateParams paymentSourceCollectionCreateParams = PaymentSourceCollectionCreateParams
				.builder().setSource(token.getId()).build();

		PaymentSource paymentSource = customer.getSources().create(paymentSourceCollectionCreateParams);

		log.info("-----PaymentSource Added ie. Bank Account for CustomerID : {} is {}", customerID, paymentSource);
		return paymentSource.getId();
	}

	public String verifyBankAccount(String customerID, String bankAccountID) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		PaymentSource source = customer.getSources().retrieve(bankAccountID);

		com.stripe.model.BankAccount ba = (com.stripe.model.BankAccount) source;
		ArrayList<Integer> amounts = new ArrayList<>();
		amounts.add(32);
		amounts.add(45);

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("amounts", amounts);
		com.stripe.model.BankAccount verifiedBankAccount = ba.verify(param);

		return verifiedBankAccount.getId();
	}

	public String makeBankAccountDefaultSource(String customerID, String bankAccountID) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		CustomerUpdateParams customerUpdateParams = CustomerUpdateParams.builder().setDefaultSource(bankAccountID)
				.build();
		Customer updatedCustomer = customer.update(customerUpdateParams);
		return updatedCustomer.getDefaultSource();
	}

	/*
	 * 
	 * Amount should be amount * 100
	 */
	public String bankCharge(String customerID) throws StripeException {
		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		ChargeCreateParams params = ChargeCreateParams.builder().setAmount(500L).setCurrency("usd")
				.setCustomer(customer.getId()).build();
		Charge charge = Charge.create(params);

		return charge.getId();
	}
}
