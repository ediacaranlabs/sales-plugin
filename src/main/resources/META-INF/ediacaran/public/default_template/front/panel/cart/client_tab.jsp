<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>
<ed:row>
	<ed:col>
		<ec:form id="form_user" method="POST" action="${plugins.ediacaran.sales.web_path}/cart/select/client" update="cart_result">
		<span id="client_data_view" formgroup="client">
			<ec:include uri="${vars.client_data_view}" resolved="true" />
		</span>
		</ec:form>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button label="#{next.label}" icon2="chevron-right" actionType="button"  align="right" form="form_user" bundle="${messages}">
			<ec:event type="click">
				var $userForm = $.AppContext.utils.getById('form_user');
				$userForm.submit(
					true, 
					"${plugins.ediacaran.sales.web_path}/cart/select/client", 
					"cart_result", function($resp){
					
						$.AppContext.utils.updateContentByID(
							"${plugins.ediacaran.sales.web_path}/cart/address/select", 
							'client_address_form'
						);									
					
						$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/widgets', 'cart_widgets');
						$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/payment-details', 'cart_payment_details');
					
						var $accordion = $.AppContext.utils.getById('cart_steps');
						var $item = $accordion.getItem('cart_client');
						var $nextItem = $item.getNext();
						
						$nextItem.select();
					
					}
				);
			</ec:event>
		</ec:button>
		<ec:button label="#{reset.label}" actionType="button"  align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/client', 'client_data_view');
			</ec:event>
		</ec:button>	
	</ed:col>
</ed:row>