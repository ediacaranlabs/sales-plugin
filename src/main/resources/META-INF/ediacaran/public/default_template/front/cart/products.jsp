<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.products" var="sys_messages"/>
<c:if test="${!empty productException}">
	<div id="cart_error_message" class="alert alert-danger alert-dismissable col-xs-12">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		${productException.message}
	</div>				
</c:if>
<div class="row product-table">
	<div class="col-sm-9 col-md-9 col-lg-9">
		<!-- lista de produtos -->
		<table class="table table-responsive">
			<thead>
				<tr class="header">
					<th>#</th>
					<th><fmt:message key="cart_review.table.product"
							bundle="${sys_messages}" /></th>
					<th><fmt:message key="cart_review.table.price"
							bundle="${sys_messages}" /></th>
					<th><fmt:message key="cart_review.table.quantity"
							bundle="${sys_messages}" /></th>
					<th><fmt:message key="cart_review.table.action"
							bundle="${sys_messages}" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="productRequest" varStatus="step" items="${sessionScope.cart.itens}">
					<tr class="${productRequest.availability? 'available-item' : 'unavailable-item'}">
						<td id="cart_item_index_${step.index}">${step.index + 1}</td>
						<td id="cart_item_description_${step.index}">${productRequest.description}</td>
						<td id="cart_item_value_${step.index}">${productRequest.product.currency}<br> <fmt:formatNumber
								pattern="###,###,##0.00" value="${productRequest.subtotal}" /></td>
						<td class="form-inline form-group">
							<select id="cart_item_units_${step.index}" name="units_${step.index}" style="width: 60px"
								onchange="javascript:updateProductQty('/cart/units/${productRequest.serial}', 'units_${step.index}')"
								class="form-control">
									<option value="1"
										${productRequest.units == 1? ' selected' : ''}>1</option>
									<c:if test="${productRequest.product.productType.maxExtra > 1}">
										<c:forEach var="units" begin="2"
											end="${productRequest.product.productType.maxExtra + 1}">
											<option value="${units}"
												${productRequest.units == units? ' selected' : ''}>${units}</option>
										</c:forEach>
									</c:if>
							</select>
							<label  id="cart_item_units_label_${step.index}" for="units_${step.index}">${empty productRequest.product.periodType? '' : productRequest.product.periodType.friendlyName}</label>
						</td>
						<td>
							<form id="cart_item_remove_form_${step.index}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<button	type="submit" class="btn btn-danger btn-xs"><fmt:message key="cart_review.table.action" bundle="${sys_messages}" /></button>					
							</form>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- /lista de produtos -->
	</div>
	
	<div class="col-sm-3 col-md-3 col-lg-3">
	
		<!-- cupons -->
		<c:if test="${Controller.enabledCouponSupport}">
		<div class="col-xs-12 form-box-wrap-bottom" style="border: 2px solid #ececec; border-radius: 10px; padding-bottom: 10px">
			<jsp:include page="${Controller.couponPublicResource}"/>
		</div>
		</c:if>
		<!-- /cupons -->
		
		<!-- envio -->
		<c:if test="${Controller.enabledShippingSupport}">
		<div class="col-xs-12 form-box-wrap-bottom" style="border: 2px solid #ececec; border-radius: 10px;">
			<jsp:include page="${Controller.shippingPublicResource}"/>
		</div>
		</c:if>
		<!-- /envio -->
		
		<!-- sumário -->
		<div class="col-xs-12 form-box-wrap-bottom" style="border: 2px solid #ececec; border-radius: 10px;">
			<table class="table">
				<thead>
				<tr>
					<th colspan="2" class="segment-name">
						<fmt:message key="cart_review.table.summary" bundle="${sys_messages}" />
					</th>
				</tr>
				</thead>
				<tbody>
					<tr>
						<th style="width: 50%">
							<fmt:message key="cart_review.checkout.sub_total" bundle="${sys_messages}"/>:
						</th>
						<td id="cart_subtotal">
							<fmt:formatNumber pattern="###,###,##0.00" value="${sessionScope.cart.subtotal}"/>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="cart_review.checkout.discount" bundle="${sys_messages}"/>:
						</th>
						<td id="cart_discounts">
							<fmt:formatNumber pattern="###,###,##0.00" value="${sessionScope.cart.totalDiscount}"/>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="cart_review.checkout.tax" bundle="${sys_messages}"/>:
						</th>
						<td id="cart_taxes">
							<fmt:formatNumber pattern="###,###,##0.00" value="${sessionScope.cart.totalTax}"/>
						</td>
					</tr>
					<tr>
						<th>
							<fmt:message key="cart_review.checkout.total" bundle="${sys_messages}"/>:
						</th>
						<td id="cart_total">
							<fmt:formatNumber pattern="###,###,##0.00" value="${sessionScope.cart.total}"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- /sumário -->
	</div>
</div>