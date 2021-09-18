package com.stripe.ach.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.ach.model.CreateCard;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import com.stripe.model.PaymentSourceCollection;
import com.stripe.model.Token;
import com.stripe.param.PaymentSourceCollectionCreateParams;
import com.stripe.param.TokenCreateParams;

@Service
public class CardServiceCustom {
	@Value("${stripe.api.key}")
	private String apiKey;

	public String addCardToCustomer(String customerID, CreateCard card) throws StripeException {

		Stripe.apiKey = apiKey;
		Customer customer = Customer.retrieve(customerID);

		TokenCreateParams tokenCreateParm = TokenCreateParams.builder()
				.setCard(TokenCreateParams.Card.builder().setNumber(card.getCardNumber())
						.setExpMonth(card.getExpMonth()).setExpYear(card.getExYear()).setCvc(card.getCvc()).build())
				.build();
		Token token = Token.create(tokenCreateParm);

		PaymentSourceCollection sources = customer.getSources();
		for (PaymentSource ps : sources.getData()) {

			Card cd = (Card) ps;
			if (cd.getFingerprint().equals(token.getCard().getFingerprint())) {
				return "Card Already Exist";
			}
		}
		PaymentSourceCollectionCreateParams paymentSourceCollectionCreateParams = PaymentSourceCollectionCreateParams
				.builder().setSource(token.getId()).build();
		PaymentSource ps = customer.getSources().create(paymentSourceCollectionCreateParams);
		return ps.getId();
	}
}
