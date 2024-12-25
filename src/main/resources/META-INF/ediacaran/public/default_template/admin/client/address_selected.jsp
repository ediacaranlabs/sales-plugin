<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<ed:row>
	<ed:col>
		<span formgroup="client">
			<ec:select label="#{form.selected_billing_address.label}" name="selectedBillingAddress" bundle="${messages}">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.name} - ${vars.client.zip} (<fmt:message key="form.selected_billing_address.default_suffix" bundle="${messages}"/>)</ec:option>
				<c:forEach items="${vars.addresses}" var="address">
					<ec:option value="${address.protectedID}" selected="${vars.client.selectedBillingAddress == address.id}">${address.addressLine1} ${address.addressLine2} ${address.city} ${address.region} ${address.country.name} - ${address.zip}</ec:option>
				</c:forEach>
				<ec:option value="new">New address</ec:option>
				<ec:event type="change">
					var $form = $event.source.getForm();
					
					$form.updateFieldIndex();
					$form.updateFieldNames();
					
					var $selectedBillingAddress = $form.getField('client.selectedBillingAddress');
					
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
		<span formgroup="client">
			<ec:select label="#{form.selected_shipping_address.label}" name="selectedShippingAddress" bundle="${messages}">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.name} - ${vars.client.zip} (<fmt:message key="form.selected_shipping_address.default_suffix" bundle="${messages}"/>)</ec:option>
				<c:forEach items="${vars.addresses}" var="address">
					<ec:option value="${address.protectedID}" selected="${vars.client.selectedShippingAddress == address.id}">${address.addressLine1} ${address.addressLine2} ${address.city} ${address.region} ${address.country.name} - ${address.zip}</ec:option>
				</c:forEach>
				<ec:option value="new">New address</ec:option>
				<ec:event type="change">
					var $form = $event.source.getForm();
				
					$form.updateFieldIndex();
					$form.updateFieldNames();
				
					var $selectedBillingAddress = $form.getField('client.selectedShippingAddress');
					
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