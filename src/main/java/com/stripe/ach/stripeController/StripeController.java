package com.stripe.ach.stripeController;

import java.util.Map;

import org.json.simple.JSONObject;
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

import com.stripe.ach.service.CustomerService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@RestController
@RequestMapping("/stripe")
public class StripeController {

	@Autowired
	private CustomerService customerService;

	/**
	 * curl https://api.stripe.com/v1/customers \ -u
	 * sk_test_51JaOd9BDr2ZZ1EF8koLQvvA0ACvfSQQUErdhNOomXix9u12x6yB1vpmtjENh4L4EeQ9YVtGQnDa1cisJpFcs2a1y00DNDZBV0h:
	 * \ -d description="My First Test Customer (created for API docs)"
	 * 
	 * @param customer
	 * @return
	 * @throws StripeException
	 */
	@PostMapping("/customer")
	public ResponseEntity<?> createCustomer(@RequestBody Map<String, Object> customer) throws StripeException {
		Customer customerResponse = customerService.createCustomer(customer);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ModelMap().addAttribute("CustomerID", customerResponse.getId()));
	}

	/**
	 * curl https://api.stripe.com/v1/customers/cus_KFESln4oaLxefG \ -u
	 * sk_test_51JaOd9BDr2ZZ1EF8koLQvvA0ACvfSQQUErdhNOomXix9u12x6yB1vpmtjENh4L4EeQ9YVtGQnDa1cisJpFcs2a1y00DNDZBV0h:
	 * 
	 * @param customerId
	 * @return
	 * @throws StripeException
	 * @throws ParseException
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<?> retrieveCustomer(@PathVariable(value = "customerId", required = true) String customerId)
			throws StripeException, ParseException {
		JSONObject customerResponse = customerService.retrieveCustomer(customerId);
		return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
	}

	@PostMapping("/customer/{customerId}")
	public ResponseEntity<?> addCardDetailsCustomer(
			@PathVariable(value = "customerId", required = true) String customerId,
			@RequestBody Map<String, Object> cardDetails) throws StripeException, ParseException {
		String customerResponse = customerService.addCardDetailsCustomer(customerId, cardDetails);
		return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
	}
}