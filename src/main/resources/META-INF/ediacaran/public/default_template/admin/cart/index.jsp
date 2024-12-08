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
	$accordion.setEnabled("cart_payment", false);
		
});
</script>

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
		<ed:row>
			<ed:col size="12" id="cart_result">
			</ed:col>
		</ed:row>
		<ed:row>
			<!-- products-table -->
			<ed:col size="10">
			
				<ec:accordion id="cart_steps">
					<ec:accordion-item id="cart_products" title="Products">
						<ec:tabs>
							<ec:tabs-item title="Itens" active="true">
								<span id="product_content">
									<jsp:include page="${plugins.ediacaran.sales.template}/admin/cart/products.jsp"/>
								</span>
							</ec:tabs-item>
							<ec:tabs-item title="Search">
							
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
										<ed:row>
										<ec:forEach items="!{response.itens}" var="item">
											<ed:col size="3">
											<ec:box>
												<ec:box-body>
													<ec:form id="product_!{item.protectedID}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/add/!{item.protectedID}" update="cart_result">
														<ed:row>
															<ed:col size="12" id="product_content_!{item.protectedID}">
																<script type="text/javascript">
																	$.AppContext.utils.updateContentByID(
																			'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/product-form/!{item.protectedID}', 
																			'product_content_!{item.protectedID}'
																	);
																</script>
															</ed:col>
														</ed:row>
														<ed:row>
															<ed:col size="12">
																<ec:button label="Add" align="right" actionType="submit"/>
															</ed:col>
														</ed:row>
													</ec:form>
												</ec:box-body>
											</ec:box>
											</ed:col>
										</ec:forEach>
										</ed:row>
									</ec:data-result>
								</ec:data-table>
							
							</ec:tabs-item>
						</ec:tabs>
						<hr>
						<ed:row>
							<ed:col>
								<ec:button label="Next" actionType="button" align="right">
									<ec:event type="click">
										var $accordion = $.AppContext.utils.getById('cart_steps');
										var $accordionItens = $accordion.getItens();
										$accordionItens[1].select();
									</ec:event>
								</ec:button>
							</ed:col>
						</ed:row>
					</ec:accordion-item>
					<ec:accordion-item  id="cart_client" title="Client">
						<ec:tabs>
							<ec:tabs-item title="Client" active="true">
								<span id="user_data_view">
									<ec:include uri="${vars.user_data_view}" resolved="true" />
								</span>
							</ec:tabs-item>
							<ec:tabs-item title="Search">
							
								<ec:data-table id="user_form_search" 
									action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/search-users">
									<ed:row>
										<ed:col size="4">
							    			<ec:textfield 
							    				name="firstName" 
							    				placeholder="first name"
							    				bundle="${messages}"/>
										</ed:col>
										<ed:col size="4">
							    			<ec:textfield 
							    				name="lastName" 
							    				placeholder="last name"
							    				bundle="${messages}"/>
										</ed:col>
										<ed:col size="4">
							    			<ec:textfield 
							    				name="email" 
							    				placeholder="email"
							    				bundle="${messages}"/>
										</ed:col>
									</ed:row>
									<ed:row>
										<ed:col size="3">
							    			<ec:select name="country">
							    				<ec:option value=""></ec:option>
							    				<c:forEach  items="${vars.countries}" var="country" >
								    				<ec:option value="${country.isoAlpha3}">${country.name}</ec:option>
							    				</c:forEach>
							    			</ec:select>
										</ed:col>
										<ed:col size="3">
							    			<ec:textfield 
							    				name="city" 
							    				placeholder="city"
							    				bundle="${messages}"/>
										</ed:col>
										<ed:col size="6">
											<ec:button icon="search" label="Search" style="info" actionType="submit" align="right"/>
										</ed:col>
									</ed:row>
									<ec:data-result var="response">
									<ec:table style="striped">
										<ec:table-header>
											<ec:table-col>
												<b><fmt:message key="First name" bundle="${messages}"/></b>
											</ec:table-col>
											<ec:table-col>
												<b><fmt:message key="Last name" bundle="${messages}"/></b>
											</ec:table-col>
											<ec:table-col>
												<b><fmt:message key="email" bundle="${messages}"/></b>
											</ec:table-col>
											<ec:table-col>
												<b><fmt:message key="country" bundle="${messages}"/></b>
											</ec:table-col>
											<ec:table-col>
											</ec:table-col>
										</ec:table-header>
										<ec:table-body>
										<ec:forEach items="!{response.itens}" var="item">
										<ec:table-row>
											<ec:table-col>
												!{item.firstName}
											</ec:table-col>
											<ec:table-col>
												!{item.lastName}
											</ec:table-col>
											<ec:table-col>
												!{item.email}
											</ec:table-col>
											<ec:table-col>
												!{item.country.name}
											</ec:table-col>
											<ec:table-col>
												<ec:form id="product_!{item.protectedID}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/user/!{item.protectedID}" update="user_data_view">
													<ec:button label="Select" align="right" actionType="submit"/>
												</ec:form>
											</ec:table-col>
										</ec:table-row>
										</ec:forEach>
										</ec:table-body>
									</ec:table>
									</ec:data-result>
								</ec:data-table>
							
							
							</ec:tabs-item>
						</ec:tabs>
						<hr>
						<ed:row>
							<ed:col>
								<ec:button label="Next" actionType="button" align="right">
									<ec:event type="click">
										var $accordion = $.AppContext.utils.getById('cart_steps');
										var $accordionItens = $accordion.getItens();
										$accordionItens[2].select();
									</ec:event>
								</ec:button>
								<ec:button label="Back" actionType="button" align="right">
									<ec:event type="click">
										var $accordion = $.AppContext.utils.getById('cart_steps');
										var $accordionItens = $accordion.getItens();
										$accordionItens[0].select();
									</ec:event>
								</ec:button>
								<ec:button label="New" actionType="button" align="right">
								</ec:button>
							</ed:col>
						</ed:row>
					</ec:accordion-item>
					<ec:accordion-item id="cart_payment" title="Payment method">
						<span id="cart_payment_details">
							<jsp:include page="payment-details.jsp"/>
						</span>
						<ed:row>
							<ed:col>
								<ec:button label="Checkout" actionType="button" align="right"/>
								<ec:button label="Back" actionType="button" align="right">
									<ec:event type="click">
										var $accordion = $.AppContext.utils.getById('cart_steps');
										var $accordionItens = $accordion.getItens();
										$accordionItens[1].select();
									</ec:event>
								</ec:button>
							</ed:col>
						</ed:row>
					</ec:accordion-item>
				</ec:accordion>
	
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
