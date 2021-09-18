package com.stripe.ach.stripeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.ach.model.CreateCard;
import com.stripe.ach.service.CardServiceCustom;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/stripe/card")
public class CardController {
	@Autowired
	private CardServiceCustom cardService;

	@PostMapping("/{customerID}")
	private ResponseEntity<ModelMap> retrieveCustomerSources(@RequestBody CreateCard card,
			@PathVariable(value = "customerID", required = true) String customerID) throws StripeException {
		String response = cardService.addCardToCustomer(customerID, card);
		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("ChargeId", response));

	}
}
