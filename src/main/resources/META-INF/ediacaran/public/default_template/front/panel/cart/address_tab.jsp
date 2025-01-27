<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:form id="address_user" method="POST" action="${plugins.ediacaran.sales.web_path}/cart/address/select" update="cart_result">
	<span id="client_address_form">
		<jsp:include page="select_address.jsp"/>
	</span>
	<ed:row>
		<ed:col>
		
			<ec:button label="#{next_button.title}" icon2="chevron-right" actionType="button" bundle="${messages}"  align="right" >
				<ec:event type="click">
					var $userForm = $.AppContext.utils.getById('address_user');
					$userForm.submit(
						true, 
						"${plugins.ediacaran.sales.web_path}/cart/address/select", 
						"cart_result", function($resp){
						
							$.AppContext.utils.updateContentByID(
								"${plugins.ediacaran.sales.web_path}/cart/shipping/select", 
								'cart_shipping_details'
							);									
						
							var $accordion = $.AppContext.utils.getById('cart_steps');
							var $item = $accordion.getItem('cart_address');
							var $nextItem = $item.getNext();
							
							$nextItem.select();
						
						}
					);
				</ec:event>
			</ec:button>
			<c:if test="${!vars.completedRegister}">
			<ec:button icon="chevron-left" label="#{back_button.title}" actionType="button" bundle="${messages}" align="right" >
				<ec:event type="click">
					var $userForm = $.AppContext.utils.getById('form_user');
					$userForm.submit(
						true, 
						"${plugins.ediacaran.sales.web_path}/cart/client", 
						"client_data_view", function($resp){
						
							var $accordion = $.AppContext.utils.getById('cart_steps');
							var $item = $accordion.getItem('cart_address');
							var $previousItem = $item.getPrevious();
							
							$previousItem.select();
						
						}
					);
				</ec:event>
				<%--
				<ec:event type="click">
					var $accordion = $.AppContext.utils.getById('cart_steps');
					var $item = $accordion.getItem('cart_address');
					var $previousItem = $item.getPrevious();
					
					$previousItem.select();
				</ec:event>
				--%>
			</ec:button>
			</c:if>		
		</ed:col>
	</ed:row>
</ec:form>
