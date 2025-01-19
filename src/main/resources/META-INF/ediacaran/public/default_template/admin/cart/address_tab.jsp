<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:form id="address_user" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/address/select" update="cart_result">
	<span id="client_address_form">
	</span>
	<ed:row>
		<ed:col>
			<ec:button label="#{next_button.title}" icon2="chevron-right" actionType="submit" bundle="${messages}"  align="right" />
			<ec:button icon="chevron-left" label="#{back_button.title}" actionType="button" bundle="${messages}" align="right" >
				<ec:event type="click">
					var $accordion = $.AppContext.utils.getById('cart_steps');
					var $item = $accordion.getItem('cart_address');
					var $previousItem = $item.getPrevious();
					
					$previousItem.select();
				</ec:event>
			</ec:button>
		</ed:col>
	</ed:row>
</ec:form>
