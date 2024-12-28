<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>
<ed:row>
	<ed:col>
		<ec:tabs id="products_tabs">
			<ec:tabs-item id="itens_products_tabs" title="Itens" active="true">
				<span id="product_content">
					<jsp:include page="${plugins.ediacaran.sales.template}/admin/cart/products.jsp"/>
				</span>
			</ec:tabs-item>
			<ec:tabs-item id="search_products_tabs" title="Search">
			
				<ec:data-table id="productSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/products/search">
					<ed:row>
						<ed:col size="5">
			    			<ec:textfield name="name" label="Name" placeholder="name" bundle="${messages}"/>
						</ed:col>
						<ed:col size="3">
							<ec:select label="Product type" name="productType">
								<c:forEach items="${vars.productTypes}" var="productType">
								<ec:option value="${productType.code}">${productType.name}</ec:option>
								</c:forEach>
							</ec:select>
						</ed:col>
						<ed:col size="4">
				    			<ec:textfield label="Mínimum (Cost)" name="minCost" placeholder="min cost" bundle="${messages}"/>
				    			<ec:textfield label="Maximum (Cost)" name="maxCost" placeholder="max cost" bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:button icon="search" label="Search" form="productSearchForm"  actionType="submit" align="right"/>
						</ed:col>
					</ed:row>
					<hr>
					<ec:data-result var="response">
						<ed:row>
						<ec:forEach items="!{response.itens}" var="item">
							<ed:col size="6">
							<ec:box>
								<ec:box-body>
									<ec:form id="product_!{item.protectedID}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/add/!{item.protectedID}" update="cart_result">
										<ed:row>
											<ed:col size="12" id="product_content_!{item.protectedID}">
												<script type="text/javascript">
													$.AppContext.utils.updateContentByID(
															'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/products/!{item.protectedID}', 
															'product_content_!{item.protectedID}'
													);
												</script>
											</ed:col>
										</ed:row>
										<ed:row>
											<ed:col size="12">
												<ec:button label="Add" icon="plus" align="right" actionType="submit" />
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
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button id="products_next_button" label="Next" icon2="chevron-right" actionType="button" align="right" >
			<ec:event type="click">
				var $accordion = $.AppContext.utils.getById('cart_steps');
				var $accordionItens = $accordion.getItens();
				$accordionItens[1].select();
			</ec:event>
		</ec:button>
		<ec:button label="Search" icon="search" actionType="button" align="right" >
			<ec:event type="click">
				var $tabs = $.AppContext.utils.getById('products_tabs');
				$tabs.select("search_products_tabs");
			</ec:event>
		</ec:button>
		<ec:button label="Itens" icon="list" actionType="button" align="right" >
			<ec:event type="click">
				var $tabs = $.AppContext.utils.getById('products_tabs');
				$tabs.select("itens_products_tabs");
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>