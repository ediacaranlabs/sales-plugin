<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>

<!DOCTYPE html>
<html>

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
</head>

<body>

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
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section>
		<ed:container>
			<ed:row>
				<ed:col size="12">
						<hr>
				</ed:col>
			</ed:row>
			
			<c:if test="${!empty vars.user && !vars.user.complete}">
				<ec:alert style="danger">
					<a href="${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}#!${plugins.ediacaran.front.perfil_page}?redirectTo=${plugins.ediacaran.sales.web_path}/cart">
						<fmt:message key="cart_review.retriction_msg" bundle="${messages}" />
					</a>
				</ec:alert>
			</c:if>
			
			<!-- products-table -->
			<ed:row>
				<ed:col id="product_content" size="12">
					<jsp:include page="products.jsp"/>
				</ed:col>
			</ed:row>
			<!-- /products-table -->

			<!-- payment-area -->
			<ec:form id="payment_form" method="post" action="${plugins.ediacaran.sales.web_path}/cart/checkout" update="result-checkout">
				<c:if test="${!empty pageContext.request.userPrincipal}">
				<ed:row>
					<ed:col size="12">
							<h3><fmt:message key="cart_review.payment.title" bundle="${messages}" /></h3>
							<hr>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col id="cart_payment_details" size="12">
						<jsp:include page="payment-details.jsp"/>
					</ed:col>
				</ed:row>
				</c:if>
				
				<ed:row>
					<ed:col size="12">
						<div id="result-checkout" class="result-check"></div>
						<ec:button align="right" actionType="submit" label="#{cart_review.checkout.submit}" bundle="${messages}"/>
					</ed:col>
				</ed:row>

			</ec:form>
			<!-- /payment-area -->
			
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>