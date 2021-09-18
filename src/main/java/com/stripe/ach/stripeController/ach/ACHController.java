package com.stripe.ach.stripeController.ach;

import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/verify/{customerID}/{bankAccountID}")
	public ResponseEntity<?> verifyBankAccount(
			@PathVariable(value = "bankAccountID", required = true) String bankAccountID,
			@PathVariable(value = "customerID", required = true) String customerID)
			throws StripeException, JsonProcessingException, ParseException {

		String response = achService.verifyBankAccount(customerID, bankAccountID);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("response", response));

	}

	@GetMapping("/change-default-source/{customerID}/{bankAccountID}")
	public ResponseEntity<?> makeBankAccountDefaultSource(
			@PathVariable(value = "bankAccountID", required = true) String bankAccountID,
			@PathVariable(value = "customerID", required = true) String customerID)
			throws StripeException, JsonProcessingException, ParseException {
		String response = achService.makeBankAccountDefaultSource(customerID, bankAccountID);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("response", response));
	}

	@PostMapping("/charge/{customerID}")
	public ResponseEntity<?> bankCharge(@RequestBody Map<String, Object> payment,
			@PathVariable(value = "customerID", required = true) String customerID)
			throws StripeException, JsonProcessingException, ParseException {

		String response = achService.bankCharge(customerID);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("response", response));
	}
}
