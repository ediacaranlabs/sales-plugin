$.AppContext.sales = {};

$.AppContext.sales.context = "";
 
$.AppContext.sales.updateProducts = function(){
	$.AppContext.utils.updateContentByID($.AppContext.sales.context + '/cart/products', 'product_content');
};

$.AppContext.sales.updatePaymentDetails = function(){
	$.AppContext.utils.updateContentByID($.AppContext.sales.context + '/cart/payment-details', 'cart_payment_details');
};
	
$.AppContext.sales.updatePaymentForm = function(){
		var $form = $.AppContext.utils.getById('payment_form');
		var $paymentType = $form.getField('payment.paymentType');
		var $paymentTypeValue = $paymentType.getValue();
		
		$form.submit(
			false, 
			$.AppContext.sales.context + "/cart/payment-type/" + $paymentTypeValue, 
			"payment_form_area"
		);
};

$.AppContext.onload(function(){

	$.AppContext.addLoadListener('updateUnits', '^.*' + $.AppContext.sales.context + '/cart/units/.*$', {
		
		after: function (){
			$.AppContext.sales.updatePaymentDetails();
		}
	
	});
	
});



/*
function updateProducts(){
	$.AppContext.loadContent('#product_content', '/cart/products');
}

function updatePaymentDetails(){
	if($('#cart_payment_details').length){
		$.AppContext.loadContent('#cart_payment_details', '/cart/payment-details');
	}
}

function updateProductContent($resource){
	if($('#product_content').length){
		$.AppContext.loadContent('#product_content', $resource);
	}
}

function updateProductQty($resource, $field){
	
	if($('#product_content').length){
		var $unitsField   = $("#product_content :input[name='" + $field + "']");
		var $units        = $unitsField.val();
		var $fullResource = $resource + "/" + $units;
		
		$.AppContext.loadContent('#product_content', $fullResource);
	}
}

function updatePaymentForm(){
	var $type = $('#payment_form input[name="payment.paymentType"]:checked').val();
	var $resource = "/cart/payment-type?code=" + encodeURI($type);
	$.AppContext.loadContent('#payment_form_area', $resource);
}
*/