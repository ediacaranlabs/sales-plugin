<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<style>

#product_content {
	min-height: 200px;
	display: block;
}

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
		<c:if test="${!empty vars.user && !vars.user.complete}">
			<ec:alert style="danger">
				<a href="${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}#!${plugins.ediacaran.front.perfil_page}?redirectTo=${plugins.ediacaran.sales.web_path}/cart">
					<fmt:message key="cart_review.retriction_msg" bundle="${messages}" />
				</a>
			</ec:alert>
		</c:if>
		<ed:row>
			<ed:col size="12" id="cart_result">
			</ed:col>
		</ed:row>
		<ed:row>
			<!-- products-table -->
			<ed:col size="10">
				<ec:tabs>
					<ec:tabs-item title="Cart" active="true">
						<ed:row>
							<ed:col size="12">
									<h3>Products</h3>
									<hr>
							</ed:col>
						</ed:row>
						<span id="product_content">
							<jsp:include page="${plugins.ediacaran.sales.template}/admin/cart/products.jsp"/>
						</span>
						
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
						
					</ec:tabs-item>
					<ec:tabs-item title="Products/Services">
						<ec:data-table id="productSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/search">
							<ed:row>
								<ed:col size="4">
					    			<ec:textfield name="name" label="Name" placeholder="name" bundle="${messages}"/>
								</ed:col>
								<ed:col size="3">
									<ec:select label="Product type" name="productType">
										<c:forEach items="${vars.productTypes}" var="productType">
										<ec:option value="${productType.code}">${productType.name}</ec:option>
										</c:forEach>
									</ec:select>
								</ed:col>
								<ed:col size="3">
						    			<ec:textfield label="Mínimum (Cost)" name="minCost" placeholder="min cost" bundle="${messages}"/>
						    			<ec:textfield label="Maximum (Cost)" name="maxCost" placeholder="max cost" bundle="${messages}"/>
								</ed:col>
								<ed:col size="2">
									<ec:button icon="search" label="Search" form="productSearchForm" style="info" actionType="submit" align="right"/>				
								</ed:col>
							</ed:row>
							<hr>
							
							<ec:data-result var="response">
								<ec:forEach items="!{response.itens}" var="item">
									<ec:box>
										<ec:box-body>
											<ec:form id="product_!{item.protectedID}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/add/!{item.protectedID}" update="cart_result">
												<ed:row>
													<ed:col size="10" id="product_content_!{item.protectedID}">
														<script type="text/javascript">
															$.AppContext.utils.updateContentByID(
																	'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/product-form/!{item.protectedID}', 
																	'product_content_!{item.protectedID}'
															);
														</script>
													</ed:col>
													<ed:col size="2">
														<ec:button label="Add" align="right" actionType="submit" form="product_!{item.protectedID}"/>
													</ed:col>
												</ed:row>
											</ec:form>
										</ec:box-body>
									</ec:box>
								</ec:forEach>
							</ec:data-result>
						</ec:data-table>
					</ec:tabs-item>
				</ec:tabs>
			</ed:col>
			<!-- /products-table -->
			<ed:col size="2" id="cart_widgets">
				<jsp:include page="widgets.jsp"/>
			</ed:col>
		</ed:row>
	</ec:box-body>
	<ec:box-footer>
		<ec:button align="right" actionType="submit" label="#{cart_review.checkout.submit}" bundle="${messages}"/>
	</ec:box-footer>
</ec:box>
