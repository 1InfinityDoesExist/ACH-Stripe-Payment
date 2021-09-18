package com.stripe.ach.stripeController.ach;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.ach.model.BankAccount;
import com.stripe.ach.service.ach.ACHService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/ach/bank-account")
public class ACHController {

	@Autowired
	private ACHService achService;

	@PostMapping("/{customerID}")
	public ResponseEntity<?> addBankAccount(@RequestBody BankAccount bankAccount,
			@PathVariable(value = "customerID", required = true) String customerID)
			throws StripeException, JsonProcessingException, ParseException {

		String response = achService.addBankAccount(customerID, bankAccount);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("PaymentSourceId", response));

	}

}
