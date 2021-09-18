package com.stripe.ach.stripeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.ach.service.PaymentService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/stripe/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/{customerID}/{cardID}")
	private ResponseEntity<ModelMap> retrieveCustomerSources(
			@PathVariable(value = "customerID", required = true) String customerID,
			@PathVariable(value = "cardID", required = true) String cardID) throws StripeException {
		String response = paymentService.payUsingParticularCard(customerID, cardID);
		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("ChargeId", response));

	}

}
