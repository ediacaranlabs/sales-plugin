package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.payment.InvalidDataPaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.pub.CartPubResource;
import br.com.uoutec.community.ediacaran.sales.pub.CartPubResourceMessages;
import br.com.uoutec.community.ediacaran.sales.registry.ClientExistsRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.EmptyOrderException;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.UnavailableProductException;
import br.com.uoutec.community.ediacaran.system.error.ErrorMapping;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class CartErrorsPluginInstaller {

	public void install() throws Throwable {

		ErrorMapping errorMapping = EntityContextPlugin.getEntity(ErrorMapping.class);
		
		//Action index

		errorMapping.registerError(CartPubResource.class, "index", "load", (type, action, error, locale, t)-> {
			
			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.index.error.generic_error, 
							locale
					);	
		});
		
		//Action paymentType
		
		errorMapping.registerError(CartPubResource.class, "paymentType", "paymentLoad", (type, action, error, locale, t)-> {
			return t.getMessage();
		});
		
		//updateUnits
		
		errorMapping.registerError(CartPubResource.class, "updateUnits", "loadProduct", (type, action, error, locale, t)-> {

			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.update_units.error.invalid_index, 
							locale
					);
		});

		errorMapping.registerError(CartPubResource.class, "updateUnits", "updateQuantity", (type, action, error, locale, t)-> {
			
			String message = t instanceof ProductTypeHandlerException? t.getMessage() : null;
			message        = t instanceof MaxItensException? t.getMessage() : error;
			
			if(t.getMessage() == null){
				I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
				return i18nRegistry
						.getString(
								CartPubResourceMessages.RESOURCE_BUNDLE,
								CartPubResourceMessages.update_units.error.update_qty, 
								locale
						);
				
			}
			
			return message;
		});

		//add
		
		errorMapping.registerError(CartPubResource.class, "add", "loadProductData", (type, action, error, locale, t)-> {
			
			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.add.error.product_not_found, 
							locale
					);
		});

		errorMapping.registerError(CartPubResource.class, "add", "productNotFound", (type, action, error, locale, t)-> {

			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.add.error.product_not_found, 
							locale
					);
			
		});

		errorMapping.registerError(CartPubResource.class, "add", "addProduct", (type, action, error, locale, t)-> {

			String message = t instanceof ProductTypeHandlerException? t.getMessage() : null;
			message        = t instanceof MaxItensException? t.getMessage() : error;
			
			if(t.getMessage() == null){
				I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
				return i18nRegistry
						.getString(
								CartPubResourceMessages.RESOURCE_BUNDLE,
								CartPubResourceMessages.add.error.add_item, 
								locale
						);
			}

			return message;
			
		});

		//remove
		
		errorMapping.registerError(CartPubResource.class, "remove", "loadProductData", (type, action, error, locale, t)-> {
			
			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.remove.error.invalid_index, 
							locale
					);
			
		});

		errorMapping.registerError(CartPubResource.class, "remove", "removeProduct", (type, action, error, locale, t)-> {

			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.remove.error.remove, 
							locale
					);
			
		});
		
		//checkout

		errorMapping.registerError(CartPubResource.class, "checkout", "emptyCart", (type, action, error, locale, t)-> {

			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.checkout.error.cart_empty, 
							locale
					);
			
		});

		errorMapping.registerError(CartPubResource.class, "checkout", "loadUserData", (type, action, error, locale, t)-> {
			
			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.checkout.error.invalid_data, 
							locale
					);

		});

		errorMapping.registerError(CartPubResource.class, "checkout", "loadPaymentData", (type, action, error, locale, t)-> {

			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							CartPubResourceMessages.checkout.error.invalid_data, 
							locale
					);
			
		});
		
		errorMapping.registerError(CartPubResource.class, "checkout", "checkout", (type, action, error, locale, t)-> {
			
			String messageKey;
			
			if(t instanceof EmptyOrderException){
				messageKey = CartPubResourceMessages.checkout.error.cart_empty;
			}
			if(t instanceof PaymentGatewayException){
				messageKey = CartPubResourceMessages.checkout.error.payment_gateway_load_fail;
			}
			else
			if(t instanceof UnavailableProductException){
				messageKey = CartPubResourceMessages.checkout.error.cart_item_unavailable;
			}
			else
			if(t instanceof OrderRegistryException){
				
				if(t.getCause() instanceof ClientExistsRegistryException) {
					messageKey = CartPubResourceMessages.checkout.error.user_exists;
				}
				else {
					messageKey = CartPubResourceMessages.checkout.error.mount_order_fail;
				}
			}
			else
			if(t instanceof InvalidDataPaymentGatewayException){
				messageKey = CartPubResourceMessages.checkout.error.invalid_data;
			}
			else{
				messageKey = CartPubResourceMessages.checkout.error.registry_order_fail;
			}
			
			I18nRegistry i18nRegistry = EntityContextPlugin.getEntity(I18nRegistry.class);
			return i18nRegistry
					.getString(
							CartPubResourceMessages.RESOURCE_BUNDLE,
							messageKey, 
							locale
					);
			
		});		
	}
	
	public void uninstall() throws Throwable {

		ErrorMapping errorMapping = EntityContextPlugin.getEntity(ErrorMapping.class);
		
		//Action index
		
		errorMapping.removeError(CartPubResource.class, "index", "load");

		//Action paymentType
		
		errorMapping.removeError(CartPubResource.class, "paymentType", "paymentLoad");

		//updateUnits
		
		errorMapping.removeError(CartPubResource.class, "updateUnits", "loadProduct");
		errorMapping.removeError(CartPubResource.class, "updateUnits", "updateQuantity");

		//add
		
		errorMapping.removeError(CartPubResource.class, "add", "loadProductData");
		errorMapping.removeError(CartPubResource.class, "add", "productNotFound");
		errorMapping.removeError(CartPubResource.class, "add", "addProduct");

		//remove
		
		errorMapping.removeError(CartPubResource.class, "remove", "loadProductData");
		errorMapping.removeError(CartPubResource.class, "remove", "removeProduct");
		
		//checkout

		errorMapping.removeError(CartPubResource.class, "checkout", "emptyCart");
		errorMapping.removeError(CartPubResource.class, "checkout", "loadUserData");
		errorMapping.removeError(CartPubResource.class, "checkout", "loadPaymentData");
		errorMapping.removeError(CartPubResource.class, "checkout", "checkout");
		
	}
	
	
}
