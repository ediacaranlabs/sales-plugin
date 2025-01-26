<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setBundle var="messages" locale="${locale}"/>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<ec:include uri="/includes/head.jsp"/>
<link href="${plugins.ediacaran.sales.web_path}/default_template/front/cart/css/cart.css" rel="stylesheet">
<script src="${plugins.ediacaran.sales.web_path}/default_template/front/cart/js/cart.js" charset="utf-8" type="text/javascript"></script>
<script type="text/javascript">
	$.AppContext.sales.context = '${plugins.ediacaran.sales.web_path}';
</script>
<title><fmt:message key="cart_review.banner.title" bundle="${messages}"/></title>
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
</head>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<ed:row>
				<ed:col size="4">
					<div class="inner-heading">
						<ed:row>
							<h2><fmt:message key="cart_review.title" bundle="${messages}"/></h2>
						</ed:row>
					</div>
				</ed:col>
				<ed:col size="8">
					<ec:breadcrumb title="#{cart_review.title}" bundle="${messages}">
						<ec:breadcrumb-path icon="home" text="" lnk="/" />
						<ec:breadcrumb-path text="#{cart_review.back.title}" lnk="${plugins.ediacaran.sales.web_path}/cart" bundle="${messages}"/>
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section>
		<ed:container>
			<ed:row>
				<ed:col size="9">
					<ed:row>
						<ed:col>
							<ec:accordion id="cart_steps">
								<c:if test="${!vars.completedRegister}">
									<ec:accordion-item  id="cart_client" title="#{tabs.client.title}" bundle="${messages}">
										<!-- client-tab -->
										<jsp:include page="client_tab.jsp"/>
										<!-- /client-tab -->
									</ec:accordion-item>
								</c:if>						
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
					<jsp:include page="widgets.jsp"/>
				</ed:col>
			</ed:row>		
		</ed:container>
	</section>


	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>	