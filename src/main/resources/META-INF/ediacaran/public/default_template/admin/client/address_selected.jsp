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
			</ec:select>		
		</span>			
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
			</ec:select>		
		</span>			
	</ed:col>
</ed:row>