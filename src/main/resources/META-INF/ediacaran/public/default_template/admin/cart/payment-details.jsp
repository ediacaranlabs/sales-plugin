<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<%--<ec:setBundle var="messages" locale="${locale}"/>--%>

<c:if test="${vars.payment_gateway_list.size() > 1}">
<ed:row>
		<ed:col size="12">
			<c:set var="firstPaymentGateway" value="${vars.payment_gateway_list.get(0)}"/>
			<c:forEach var="paymentGateway" items="${vars.payment_gateway_list}">
				<ec:radio 
					inline="true"
					name="payment.paymentType" 
					value="${paymentGateway.id}" 
					selected="${firstPaymentGateway.id == paymentGateway.id}" 
					label="${paymentGateway.name}">
					<ec:event type="change">
						var $form = $.AppContext.utils.getById('payment_form');
						var $paymentType = $form.getField('payment.paymentType');
						var $paymentTypeValue = $paymentType.getValue();
						
						$form.submit(
							false, 
							"${plugins.ediacaran.sales.web_path}/cart/payment-type/" + $paymentTypeValue, 
							"payment_form_area"
						);
						
					</ec:event>
				</ec:radio>
			</c:forEach>
		</ed:col>
</ed:row>
</c:if>
<c:if test="${vars.payment_gateway_list.size() == 1}">
	<input type="hidden" name="payment.paymentType" value="${vars.payment_gateway_list.get(0).id}">
</c:if>
<ed:row>
	<ed:col size="12" id="payment_form_area">
		<c:if test="${vars.payment_gateway_list.size() > 0}">
		<script type="text/javascript">
			$.AppContext.onload(function(){			
				$.AppContext.utils
					.updateContentByID(
							"#!${plugins.ediacaran.sales.web_path}/cart/payment-type/${vars.payment_gateway_list.get(0).id}", 
							"payment_form_area"
					);
			});	
		</script>
			<jsp:include page="${plugins.ediacaran.sales.web_path}/cart/payment-type/${vars.payment_gateway_list.get(0).id}"/>
		</c:if>
	</ed:col>
</ed:row>