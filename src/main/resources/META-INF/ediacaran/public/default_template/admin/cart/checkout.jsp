<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<script type="text/javascript">
$.AppContext.onload(function(){			

	var $accordion = $.AppContext.utils.getById('cart_steps');
	var $tabs = $accordion.getItens();
	
	for(let $tab of $tabs){
		$tab.setEnabled(false);
	}
	
	$tabs[0].select();
		
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
				<ec:breadcrumb-path text="#{cart_review.back.title}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/?resetCart=false" bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ed:row>
			<ed:col size="9">
				<ed:row>
					<ed:col>
						<ec:accordion id="cart_steps">
							<ec:accordion-item  id="cart_client" title="#{tabs.client.title}" bundle="${messages}">
								<!-- client-tab -->
								<jsp:include page="client_tab.jsp"/>
								<!-- /client-tab -->
							</ec:accordion-item>
							<c:if test="${vars.supportShipping}">
								<ec:accordion-item id="cart_address" title="#{tabs.address.title}" bundle="${messages}">
									<!-- address-tab -->
									<jsp:include page="address_tab.jsp"/>
									<!-- /address-tab -->
								</ec:accordion-item>
								<ec:accordion-item id="cart_shipping" title="#{tabs.shipping.title}" bundle="${messages}">
									<!-- address-tab -->
									<jsp:include page="shipping_tab.jsp"/>
									<!-- /address-tab -->
								</ec:accordion-item>
							</c:if>
							<ec:accordion-item id="cart_payment" title="#{tabs.payment.title}" bundle="${messages}">
								<!-- payment-tab -->
								<jsp:include page="payment_tab.jsp"/>
								<!-- /payment-tab -->
							</ec:accordion-item>
						</ec:accordion>			
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col id="cart_result">
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="3" id="cart_widgets">
				<jsp:include page="/default_template/front/cart/widgets.jsp"/>
			</ed:col>
		</ed:row>
	</ec:box-body>
</ec:box>