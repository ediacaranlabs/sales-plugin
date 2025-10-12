<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>

<ed:row>
	<ed:col id="product_content">
		<jsp:include page="products.jsp"/>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<div id="result-checkout" class="result-check"></div>
		<ec:button id="checkout_page_button" align="right" style="default" actionType="button" label="#{checkout.label}" bundle="${messages}">
			<ec:event type="click">
				location.href = '${plugins.ediacaran.sales.web_path}/cart/checkout';
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>