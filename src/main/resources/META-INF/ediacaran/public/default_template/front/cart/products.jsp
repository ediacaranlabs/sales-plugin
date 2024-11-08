<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>

<c:if test="${!empty productException}">
	<ec:alert id="exceptionMessage" style="danger">${productException.message}</ec:alert>
</c:if>
<style>

@media (max-width: 992px) {
	#product-line {
		display: none;
	}
}

@media (min-width: 992px) {
	#product-line-min {
		display: none;
	}
}


</style>
<ed:row>
	<ed:col size="9">
		<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
		
			<ec:box>
				<ec:box-header>
					<h3>${productRequest.name}</h3>
				</ec:box-header>
				<ec:box-body>
				
					<ed:row id="product-line-min">
						<ed:col>
							<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
						</ed:col>
						<ed:col>
							<ed:row>
								<ed:col size="12">
									${productRequest.description}
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col id="cart_item_value_${step.index}">
									<ec:center>
										${productRequest.product.currency}<br>
										<fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
									</ec:center>
								</ed:col>
								<ed:col id="update-item-cart-form-${productRequest.serial}">
									<ec:form id="update-item-cart-form-${productRequest.serial}">
										<ec:select name="units"> <!-- style="width: 60px" -->
											<ec:option value="1" selected="${productRequest.units == 1}">1 ${empty productRequest.product.periodType || productRequest.product.periodType == 'UNDEFINED'? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
											<c:if test="${productRequest.maxExtra > 1}">
												<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
													<ec:option value="${units}" selected="${productRequest.units == units}">${units} ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
												</c:forEach>
											</c:if>
											<ec:event type="change">
												var $form  = $.AppContext.utils.getById('cart-item-form-${productRequest.serial}');
												var $units = $form.getField('units');
												var $qty   = $units.getValue();
												$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
											</ec:event>
										</ec:select>
									</ec:form>
								</ed:col>
								<ed:col id="cart_item_description_${step.index}">
									<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
										<input type="hidden" name="product" value="${productRequest.serial}">
										<ec:button label="" icon="trash" align="center" actionType="submit" style="danger"/>
									</ec:form>
								</ed:col>
							</ed:row>
						</ed:col>
					</ed:row>
					
					
					<ed:row id="product-line">
						<ed:col size="2">
							<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
						</ed:col>
						<ed:col size="4" id="cart_item_description_${step.index}">
							<ec:center>${productRequest.description}</ec:center>
							<p>
						</ed:col>
						<ed:col id="cart_item_value_${step.index}">
							<ec:center>
								${productRequest.product.currency}<br>
								<fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
							</ec:center>
						</ed:col>
						<ed:col id="update-item-cart-form-${productRequest.serial}">
							<ec:form id="update-item-cart-form-${productRequest.serial}">
								<ec:select name="units"> <!-- style="width: 60px" -->
									<ec:option value="1" selected="${productRequest.units == 1}">1 ${empty productRequest.product.periodType || productRequest.product.periodType == 'UNDEFINED'? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
									<c:if test="${productRequest.maxExtra > 1}">
										<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
											<ec:option value="${units}" selected="${productRequest.units == units}">${units} ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
										</c:forEach>
									</c:if>
									<ec:event type="change">
										var $form  = $.AppContext.utils.getById('cart-item-form-${productRequest.serial}');
										var $units = $form.getField('units');
										var $qty   = $units.getValue();
										$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
									</ec:event>
								</ec:select>
							</ec:form>
						</ed:col>
						<ed:col id="cart_item_description_${step.index}">
							<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<ec:button label="" icon="trash" align="center" actionType="submit" style="danger"/>
							</ec:form>
						</ed:col>
					</ed:row>
				
				</ec:box-body>
			</ec:box>
			
			<%--		
			<ec:box>
				<ec:box-header>
					<h3>${productRequest.name}</h3>
				</ec:box-header>
				<ec:box-body>
				
					<ed:row>
						<ed:col size="2">
							<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
						</ed:col>
						<ed:col size="6" id="cart_item_description_${step.index}">
							<ec:center>${productRequest.description}</ec:center>
							<p>
						</ed:col>
						<ed:col id="cart_item_value_${step.index}">
							<ec:center>
								${productRequest.product.currency}<br>
								<fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
							</ec:center>
						</ed:col>
						<ed:col id="update-item-cart-form-${productRequest.serial}">
							<ec:form id="update-item-cart-form-${productRequest.serial}">
								<ec:select name="units"> <!-- style="width: 60px" -->
									<ec:option value="1" selected="${productRequest.units == 1}">1 ${empty productRequest.product.periodType || productRequest.product.periodType == 'UNDEFINED'? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
									<c:if test="${productRequest.maxExtra > 1}">
										<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
											<ec:option value="${units}" selected="${productRequest.units == units}">${units} ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
										</c:forEach>
									</c:if>
									<ec:event type="change">
										var $form  = $.AppContext.utils.getById('cart-item-form-${productRequest.serial}');
										var $units = $form.getField('units');
										var $qty   = $units.getValue();
										$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
									</ec:event>
								</ec:select>
							</ec:form>
						</ed:col>
						<ed:col id="cart_item_description_${step.index}">
							<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<ec:button label="" icon="trash" align="center" actionType="submit" style="danger"/>
							</ec:form>
						</ed:col>
					</ed:row>
				
				</ec:box-body>
			</ec:box>
			--%>
			
		</c:forEach>

<%--
		<ec:table>
			<ec:table-header>
				<ec:table-col>#</ec:table-col>
				<ec:table-col><ec:center><fmt:message key="cart_review.table.product" bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="cart_review.table.price" bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="cart_review.table.quantity"	bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="cart_review.table.action" bundle="${messages}"/></ec:center></ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
					<ec:table-row classStyle="${productRequest.availability? 'available-item' : 'unavailable-item'}">
						<ec:table-col id="cart_item_index_${step.index}">${step.index + 1}</ec:table-col>
						<ec:table-col id="cart_item_description_${step.index}"><ec:center>${productRequest.description}</ec:center></ec:table-col>
						<ec:table-col id="cart_item_value_${step.index}">
							<ec:center>
								${productRequest.product.currency}<br>
								<fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
							</ec:center>
						</ec:table-col>
						<ec:table-col>
							<ec:form id="update-item-cart-form-${productRequest.serial}">
								<ec:select name="units"> <!-- style="width: 60px" -->
									<ec:option value="1" selected="${productRequest.units == 1}">1 ${empty productRequest.product.periodType || productRequest.product.periodType == 'UNDEFINED'? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
									<c:if test="${productRequest.maxExtra > 1}">
										<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
											<ec:option value="${units}" selected="${productRequest.units == units}">${units} ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.getName(locale))}</ec:option>
										</c:forEach>
									</c:if>
									<ec:event type="change">
										var $form  = $.AppContext.utils.getById('cart-item-form-${productRequest.serial}');
										var $units = $form.getField('units');
										var $qty   = $units.getValue();
										$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
									</ec:event>
								</ec:select>
							</ec:form>
						</ec:table-col>
						<ec:table-col>
							<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<ec:button label="" icon="trash" align="center" actionType="submit" style="danger"/>
							</ec:form>
						</ec:table-col>
					</ec:table-row>
				</c:forEach>
			</ec:table-body>
		</ec:table>
--%>		
	</ed:col>
	<ed:col size="3">
		<c:forEach items="${Controller.widgets}" var="widget">
			<ed:row style="cart_widget_title">
				<ed:col>
					<h3>${widget.title}</h3>
					<hr>
				</ed:col>
			</ed:row>
			<ed:row style="cart_widget">
				<ed:col>
					<ec:include uri="${widget.resource}"/>
				</ed:col>
			</ed:row>
		</c:forEach>
			<ed:row style="cart_widget_title">
				<ed:col>
					<h3><fmt:message key="cart_review.table.summary" bundle="${messages}" /></h3>
					<hr>
				</ed:col>
			</ed:row>
			<ed:row style="cart_widget">
				<ed:col>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.sub_total" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_subtotal">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.subtotal}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.discount" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_discounts">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalDiscount}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.tax" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_taxes">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalTax}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.total" bundle="${messages}"/>:
						</ed:col>
						<ed:col>
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.total}"/>
						</ed:col>
					</ed:row>
				</ed:col>
			</ed:row>
	</ed:col>
</ed:row>