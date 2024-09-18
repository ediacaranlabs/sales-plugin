	 $('#payment_form').bootstrapValidator('addField', 'payment.paymentType', {
			validators : {
				notEmpty : {
					message : messages.cart.payment_form.required_payment_type
				}
			}
	 });	
