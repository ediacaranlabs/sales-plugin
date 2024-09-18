<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.index" var="sys_messages"/>
<div class="row form-group">
		<c:if test="${sessionScope.cart.total.signum() == 0}">
		<div class="col-sm-12 col-md-12 col-lg-12">
			<fmt:message key="cart_review.payment.free_message" bundle="${sys_messages}" />
			<input name="payment.paymentType" type="hidden"	value="Free">
		</div>
		</c:if>
		<c:if test="${sessionScope.cart.total.signum() != 0}">
		<c:forEach items="${vars['payment_gateway_list']}" var="paymentGateway">
			<%--
		    <c:if test="${sessionScope.cart.payment.paymentType == paymentGateway.id}">
		    	<c:set var="selectedPaymentGateway" value="${paymentGateway}"/>
		    </c:if>
		    --%>
		    <c:if test="${paymentGateway.id != 'Free'}">
				<div class="col-xs-4">
					<label class="control-label" for="payment.paymentType">
						${paymentGateway.name}
					</label><br>
					<input name="payment.paymentType" type="radio"
						<%--${empty selectedPaymentGateway? '' : ' checked'}--%>
						class="checkbox-inline"
						onclick="javascript:updatePaymentForm()"
						value="${paymentGateway.id}">
				</div>
			</c:if>
		</c:forEach>
		</c:if>
	</div>
<div class="row">
	<div id="payment_form_area" class="col-sm-12 col-md-12 col-lg-12">
		<%--
	    <c:if test="${!empty selectedPaymentGateway}">
			<jsp:include page="${selectedPaymentGateway.getPaymentView(sessionScope.system_user, null)}"/>
		</c:if>
		--%>
	</div>
</div>
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/language/${locale}/payment-details.js" charset="utf-8" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/payment-details.js" charset="utf-8"></script>