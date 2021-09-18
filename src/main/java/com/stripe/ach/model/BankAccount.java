package com.stripe.ach.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.stripe.net.ApiRequestParams;
import com.stripe.param.TokenCreateParams.BankAccount.AccountHolderType;

import lombok.Data;

@Data
public class BankAccount {
	/**
	 * The name of the person or business that owns the bank account.This field is
	 * required when attaching the bank account to a {@code Customer} object.
	 */
	@JsonProperty("account_holder_name")
	String accountHolderName;
	/**
	 * The type of entity that holds the account. It can be {@code company} or
	 * {@code individual}. This field is required when attaching the bank account to
	 * a {@code Customer} object.
	 */
	@JsonProperty("account_holder_type")
	AccountHolderType accountHolderType;
	/**
	 * The account number for the bank account, in string form. Must be a checking
	 * account.
	 */
	@JsonProperty("account_number")
	String accountNumber;
	/**
	 * The country in which the bank account is located.
	 */
	@JsonProperty("country")
	String country;
	/**
	 * The currency the bank account is in. This must be a country/currency pairing
	 * that <a href="docs/payouts">Stripe supports.</a>
	 */
	@JsonProperty("currency")
	String currency;
	/**
	 * Map of extra parameters for custom features not available in this client
	 * library. The content in this map is not serialized under this field's
	 * {@code @SerializedName} value. Instead, each key/value pair is serialized as
	 * if the key is a root-level field (serialized) name in this param object.
	 * Effectively, this map is flattened to its parent instance.
	 */
	@JsonProperty(ApiRequestParams.EXTRA_PARAMS_KEY)
	Map<String, Object> extraParams;
	/**
	 * The routing number, sort code, or other country-appropriateinstitution number
	 * for the bank account. For US bank accounts, this is required and should bethe
	 * ACH routing number, not the wire routing number. If you are providing an IBAN
	 * for{@code account_number}, this field is not required.
	 */
	@JsonProperty("routing_number")
	String routingNumber;

}
