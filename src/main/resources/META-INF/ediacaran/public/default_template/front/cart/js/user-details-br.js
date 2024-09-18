	 $('#payment_form').bootstrapValidator('addField', 'customer.firstName', {
			validators : {
				notEmpty : {
					message : messages.user_details.name_empty
				},
				regexp : {
					regexp : regexPatterns.NAME_FORMAT,
					message : messages.user_details.name
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.name_len
				}

			}
	 });

	 $('#payment_form').bootstrapValidator('addField', 'customer.lastName', {
			validators : {
				notEmpty : {
					message : messages.user_details.name_empty
				},
				regexp : {
					regexp : regexPatterns.NAME_FORMAT,
					message : messages.user_details.name
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.name_len
				}

			}
     });
	 
	 $('#payment_form').bootstrapValidator('addField', 'customer.address', {
			validators : {
				notEmpty : {
					message : messages.user_details.address_empty
				},
				regexp : {
					regexp : regexPatterns.ADDRESS_FORMAT,
					message : messages.user_details.address_regex
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.address_len
				}

			}
     });

	 $('#payment_form').bootstrapValidator('addField', 'customer.city', {
			validators : {
				notEmpty : {
					message : messages.user_details.city_empty
				},
				regexp : {
					regexp : regexPatterns.ADDRESS_FORMAT,
					message : messages.user_details.city_regex
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.city_len
				}

			}
	 });

	 $('#payment_form').bootstrapValidator('addField', 'customer.region', {
			validators : {
				regexp : {
					regexp : regexPatterns.NAME_FORMAT,
					message : messages.user_details.region_regex
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.region_len
				}

			}
	 });
	 
	 $('#payment_form').bootstrapValidator('addField', 'customer.country', {
			validators : {
				notEmpty : {
					message : messages.user_details.country_empty
				}
			}
	 });
	 
	 $('#payment_form').bootstrapValidator('addField', 'customer.zip', {
			validators : {
				notEmpty : {
					message : messages.user_details.zip_empty
				},
				regexp : {
					regexp : /^[0-9]{3,}$/,
					message : messages.user_details.zip_regex
				},
				stringLength : {
					min : 3,
					max : 60,
					message : messages.user_details.zip_len
				}

			}
	 });

	 $('#payment_form').bootstrapValidator('addField', 'customer.phone', {
			validators : {
				regexp : {
					regexp : /^[0-9]+$/,
					message : messages.user_details.phone_regex
				},
				stringLength : {
					min : 3,
					max : 16,
					message : messages.user_details.phone_len
				}

			}
	 });


	 $('#payment_form').bootstrapValidator('addField', 'customer.document', {
			validators : {
				callback : {
					message : messages.user_details.document_invalid,
					callback : function(value, validator) {
						return !value && ignoreValidation? true : Util.isValidDocument(value);
					}
				}
			}
	 });