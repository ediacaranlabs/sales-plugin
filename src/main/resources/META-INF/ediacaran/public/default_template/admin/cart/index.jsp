<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>

<script type="text/javascript">
	$.AppContext.sales.context = '${plugins.ediacaran.sales.web_path}';
</script>

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

		<ed:row>
			<ed:col size="6">
				
			</ed:col>
			<ed:col size="6">
			
			
				<ec:data-table id="orderSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/search">
					<ed:row>
						<ed:col size="12">
								<h3>Products Search</h3>
								<hr>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="4">
			    			<ec:textfield name="name" label="Name" placeholder="name" bundle="${messages}"/>
						</ed:col>
						<ed:col size="4">
			    			<ec:textfield name="description" label="Description" placeholder="description" bundle="${messages}"/>
						</ed:col>
						<ed:col size="4">
				    			<ec:textfield label="Mínimum (Cost)" name="minCost" placeholder="min cost" bundle="${messages}"/>
				    			<ec:textfield label="Maximum (Cost)" name="maxCost" placeholder="max cost" bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="12">
							<ec:button icon="search" label="Search" style="info" actionType="submit" align="right"/>
						</ed:col>
					</ed:row>
					
					<ec:data-result var="response">
						<ec:table>
							<ec:table-header>
								<ec:table-col><ec:center><fmt:message key="table.id" bundle="${messages}"/></ec:center></ec:table-col>
								<ec:table-col><ec:center>Owner</ec:center></ec:table-col>
								<ec:table-col><ec:center><fmt:message key="table.data" bundle="${messages}"/></ec:center></ec:table-col>
								<ec:table-col><ec:center><fmt:message key="table.status" bundle="${messages}"/></ec:center></ec:table-col>
								<ec:table-col><ec:center><fmt:message key="table.total" bundle="${messages}"/></ec:center></ec:table-col>
								<ec:table-col><ec:center><fmt:message key="table.action" bundle="${messages}"/></ec:center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<ec:forEach items="!{response.data}" var="item">
								<ec:table-row>
									<ec:table-col><ec:center><small>!{item.id}</small></ec:center></ec:table-col>
									<ec:table-col><ec:center>!{item.owner}</ec:center></ec:table-col>
									<ec:table-col><ec:center>!{item.date}</ec:center></ec:table-col>
									<ec:table-col><ec:center>!{item.status}</ec:center></ec:table-col>
									<ec:table-col><ec:center>!{item.total}</ec:center></ec:table-col>
									<ec:table-col>
										<ec:center>
										<ec:button id="!{item.id}_button" icon="search" style="info" actionType="button">
											<ec:event type="click">
												$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/!{item.id}');
											</ec:event>
										</ec:button>
										</ec:center>
									</ec:table-col>
								</ec:table-row>
								</ec:forEach>
							</ec:table-body>
						</ec:table>
					</ec:data-result>
				</ec:data-table>
			
			
			
				
			</ed:col>
		</ed:row>
		
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
				</ed:col>
			</ed:row>
		
		</ec:form>
		<!-- /payment-area -->	
	</ec:box-body>
	<ec:box-footer>
		<ec:button align="right" actionType="submit" label="#{cart_review.checkout.submit}" bundle="${messages}"/>
	</ec:box-footer>
</ec:box>
