<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>

<ec:form id="cart_shipping_form" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/shipping/select" update="cart_result">
	<ed:row>
		<ed:col id="cart_shipping_details"  classStyle="form-group has-feedback">
			<jsp:include page="/default_template/front/panel/cart/shipping.jsp"/>
		</ed:col>
	</ed:row>
	<ed:row>
		<ed:col>
			<ec:button label="#{next.label}" icon2="chevron-right" actionType="button" bundle="${messages}"  align="right">
				<ec:event type="click">
					$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/widgets', 'cart_widgets');
					$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/payment-details', 'cart_payment_details');
				
					var $accordion = $.AppContext.utils.getById('cart_steps');
					var $item = $accordion.getItem('cart_shipping');
					var $nextItem = $item.getNext();
					
					$nextItem.select();
				</ec:event>
			</ec:button>
			<ec:button icon="chevron-left" label="#{back.label}" actionType="button" bundle="${messages}" align="right" >
				<ec:event type="click">
					$.AppContext.utils.updateContentByID(
						'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/address/select', 
						'client_address_form',
						false,
						function($resp){
							var $accordion = $.AppContext.utils.getById('cart_steps');
							var $item = $accordion.getItem('cart_shipping');
							var $previousItem = $item.getPrevious();
							
							$previousItem.select();
						}
					);
				</ec:event>
			</ec:button>
		</ed:col>
	</ed:row>
</ec:form>