<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<script type="text/javascript">
$.AppContext.onload(function(){			

	var $accordion = $.AppContext.utils.getById('cart_steps');
	$accordion.select("cart_products");
	$accordion.setEnabled("cart_products", false);
	$accordion.setEnabled("cart_client", false);
	$accordion.setEnabled("cart_address", false);
	$accordion.setEnabled("cart_payment", false);
		
});
</script>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<style>

#productSearchForm_result {
	min-height: 200px;
	display: block;
}
</style>
<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2><fmt:message key="cart_review.title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{cart_review.title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ed:row>
			<ed:col size="12" id="cart_result">
			</ed:col>
		</ed:row>
		<ed:row>
			<!-- products-table -->
			<ed:col size="9">
				
				<ec:accordion id="cart_steps">
					<ec:accordion-item id="cart_products" title="Products">
						<!-- products-tab -->
						<jsp:include page="products_tab.jsp"/>
						<!-- /products-tab -->
					</ec:accordion-item>
					<ec:accordion-item  id="cart_client" title="Client">
						<!-- client-tab -->
						<jsp:include page="client_tab.jsp"/>
						<!-- /client-tab -->
					</ec:accordion-item>
					<ec:accordion-item id="cart_address" title="Address">
						<!-- address-tab -->
						<jsp:include page="address_tab.jsp"/>
						<!-- /address-tab -->
					</ec:accordion-item>
					<ec:accordion-item id="cart_payment" title="Payment method">
						<!-- payment-tab -->
						<jsp:include page="payment_tab.jsp"/>
						<!-- /payment-tab -->
					</ec:accordion-item>
				</ec:accordion>
	
			</ed:col>
			<!-- /products-table -->
			<ed:col size="3" id="cart_widgets">
				<jsp:include page="widgets.jsp"/>
			</ed:col>
		</ed:row>
	</ec:box-body>
</ec:box>
