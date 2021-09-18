package com.stripe.ach.stripeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.ach.service.SourceService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentSourceCollection;

@RestController
@RequestMapping("/stripe/source")
public class SourceController {
	@Autowired
	private SourceService sourceService;

	@GetMapping("/{customerID}")
	private ResponseEntity<ModelMap> retrieveCustomerSources(
			@PathVariable(value = "customerID", required = true) String customerID) throws StripeException {
		PaymentSourceCollection response = sourceService.retrieveCustomerSource(customerID);
		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("Sources", response));

	}
}
