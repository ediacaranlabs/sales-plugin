<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle var="messages"/>

<c:if test="${!empty productException}">
	<ec:alert id="cart_error_message" style="danger">${productException.message}</ec:alert>
</c:if>

<ed:row>
	<ed:col size="9">
		<ec:table>
			<ec:table-header>
				<ec:table-col>#</ec:table-col>
				<ec:table-col><fmt:message key="cart_review.table.product" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="cart_review.table.price" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="cart_review.table.quantity"	bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="cart_review.table.action" bundle="${messages}"/></ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
					<ec:table-row classStyle="${productRequest.availability? 'available-item' : 'unavailable-item'}">
						<ec:table-col id="cart_item_index_${step.index}">${step.index + 1}</ec:table-col>
						<ec:table-col id="cart_item_description_${step.index}">${productRequest.description}</ec:table-col>
						<ec:table-col id="cart_item_value_${step.index}">
							${productRequest.product.currency}<br>
							<fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
						</ec:table-col>
						<ec:table-col>
							<ec:form id="cart_item_change_form_${step.index}">
								<ec:select id="cart_item_units_${step.index}" name="units_${step.index}"> <!-- style="width: 60px" -->
									<ec:option value="1" selected="${productRequest.units == 1}">1 ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.friendlyName)}</ec:option>
									<c:if test="${productRequest.product.productType.maxExtra > 1}">
										<c:forEach var="units" begin="2" end="${productRequest.product.productType.maxExtra + 1}">
											<ec:option value="${units}" selected="${productRequest.units == units}">${units} ${empty productRequest.product.periodType? '' : '/ '.concat(productRequest.product.periodType.friendlyName)}</ec:option>
										</c:forEach>
									</c:if>
									<ec:event type="change">
										var $form  = $.AppContext.utils.getById('cart_item_change_form_${step.index}');
										var $units = $form.getField('units_${step.index}');
										var $qty   = $units.getValue();
										$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
									</ec:event>
								</ec:select>
							</ec:form>
						</ec:table-col>
						<ec:table-col>
							<ec:form id="cart_item_remove_form_${step.index}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<ec:button label="#{cart_review.table.action}" bundle="${messages}" actionType="submit" style="danger"/>
							</ec:form>
						</ec:table-col>
					</ec:table-row>
				</c:forEach>
			</ec:table-body>
		</ec:table>
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
						<ed:col size="12">
							<fmt:message key="cart_review.table.summary" bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="6">
							<fmt:message key="cart_review.checkout.sub_total" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_subtotal" size="6">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.subtotal}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="6">
							<fmt:message key="cart_review.checkout.discount" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_discounts" size="6">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalDiscount}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="6">
							<fmt:message key="cart_review.checkout.tax" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_taxes" size="6">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalTax}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="6">
							<fmt:message key="cart_review.checkout.total" bundle="${messages}"/>:
						</ed:col>
						<ed:col size="6">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.total}"/>
						</ed:col>
					</ed:row>
				</ed:col>
			</ed:row>
	</ed:col>
</ed:row>