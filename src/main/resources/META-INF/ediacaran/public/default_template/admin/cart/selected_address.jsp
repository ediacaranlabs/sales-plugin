<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>
<ed:row>
	<ed:col>
	<h5>Billing address</h5>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<span formgroup="selectedBillingAddress">
			<ec:select name="protectedID">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.isoAlpha3} - ${vars.client.zip} (Default Address)</ec:option>
				<c:if test="${!empty vars.billingAddress}">
					<ec:option value="${vars.billingAddress.protectedID}">${vars.billingAddress.addressLine1} ${vars.billingAddress.addressLine2} ${vars.billingAddress.city} ${vars.billingAddress.region} ${vars.billingAddress.country.isoAlpha3} - ${vars.billingAddress.zip}</ec:option>
				</c:if>
				<ec:option value="new">New address</ec:option>
					<ec:event type="change">
						var $form = $event.source.getForm();
						
						$form.updateFieldIndex();
						$form.updateFieldNames();
						
						var $selectedBillingAddress = $form.getField('selectedBillingAddress.protectedID');
						
						if($selectedBillingAddress.getValue() === 'new'){
							$.AppContext.utils.updateContentByID("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/address/simplified", "new_billing_address");
						}
						else{
							$.AppContext.utils.content.update("new_billing_address", "");
						}
					</ec:event>
			</ec:select>
		</span>			
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<span id="new_billing_address" formgroup="billingAddress"></span>
	</ed:col>
</ed:row>

<ed:row>
	<ed:col>
	<h5>Shipping address</h5>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<span formgroup="selectedShippingAddress">
			<ec:select name="protectedID">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.isoAlpha3} - ${vars.client.zip} (Default Address)</ec:option>
				<c:forEach items="${vars.shippingAddresses}" var="shippingAddress">
					<ec:option value="${shippingAddress.protectedID}">${shippingAddress.addressLine1} ${shippingAddress.addressLine2} ${shippingAddress.city} ${shippingAddress.region} ${shippingAddress.country.isoAlpha3} - ${shippingAddress.zip}</ec:option>
				</c:forEach>
				<ec:option value="new">New address</ec:option>
					<ec:event type="change">
						var $form = $event.source.getForm();
					
						$form.updateFieldIndex();
						$form.updateFieldNames();
					
						var $selectedBillingAddress = $form.getField('selectedShippingAddress.protectedID');
						
						if($selectedBillingAddress.getValue() === 'new'){
							$.AppContext.utils.updateContentByID("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/address/simplified", "new_shipping_address");
						}
						else{
							$.AppContext.utils.content.update("new_shipping_address", "");
						}
					</ec:event>
			</ec:select>
		</span>			
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<span id="new_shipping_address" formgroup="shippingAddress"></span>
	</ed:col>
</ed:row>