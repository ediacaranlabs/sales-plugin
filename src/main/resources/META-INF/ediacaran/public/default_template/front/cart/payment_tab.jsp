<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>

<ec:form id="cart_checkout" method="POST" action="${plugins.ediacaran.sales.web_path}/cart/checkout" update="cart_result">
	<span id="cart_payment_details" formgroup="payment">
		<jsp:include page="payment-details.jsp"/>
	</span>
	<ed:row>
		<ed:col>
			<ec:button label="#{checkout.label}" icon="check-circle" actionType="submit" align="right" style="success" bundle="${messages}"/>
			<ec:button icon="chevron-left" label="#{back.label}" actionType="button" align="right" bundle="${messages}">
				<ec:event type="click">
					var $accordion = $.AppContext.utils.getById('cart_steps');
					var $item = $accordion.getItem('cart_payment');
					var $previousItem = $item.getPrevious();
					
					$previousItem.select();
				</ec:event>
			</ec:button>
		</ed:col>
	</ed:row>
</ec:form>
