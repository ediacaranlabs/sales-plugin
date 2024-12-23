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
		<span formgroup="billingAddress">
			<ec:select name="protectedID">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.isoAlpha3} - ${vars.client.zip} (Default Address)</ec:option>
				<c:forEach items="${vars.addresses}" var="address">
					<ec:option value="${address.protectedID}" selected="${vars.client.billingAddress.id == address.id}">${address.addressLine1} ${address.addressLine2} ${address.city} ${address.region} ${address.country.isoAlpha3} - ${address.zip}</ec:option>
				</c:forEach>
			</ec:select>		
		</span>			
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
	<h5>Shipping address</h5>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<span formgroup="shippingAddress">
			<ec:select name="protectedID">
				<ec:option value="">${vars.client.addressLine1} ${vars.client.addressLine2} ${vars.client.city} ${vars.client.region} ${vars.client.country.isoAlpha3} - ${vars.client.zip} (Default Address)</ec:option>
				<c:forEach items="${vars.addresses}" var="address">
					<ec:option value="${address.protectedID}" selected="${vars.client.shippingAddress.id == address.id}">${address.addressLine1} ${address.addressLine2} ${address.city} ${address.region} ${address.country.isoAlpha3} - ${address.zip}</ec:option>
				</c:forEach>
			</ec:select>		
		</span>			
	</ed:col>
</ed:row>