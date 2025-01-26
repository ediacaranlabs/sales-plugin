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
				<ed:col size="12" id="cart_result">
				</ed:col>
			</ed:row>
			<ed:row>
				<!-- products-table -->
				<ed:col size="9" id="cart">
					<!-- products-tab -->
					<jsp:include page="products_tab.jsp"/>
					<!-- /products-tab -->
				</ed:col>
				<!-- /products-table -->
				<ed:col size="3" id="cart_widgets">
					<jsp:include page="widgets.jsp"/>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>