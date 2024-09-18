<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"       prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"      prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.index" var="sys_messages"/>
<link href="${pageContext.request.contextPath}/templates/front/default_template/cart/css/cart.css" rel="stylesheet">
<div class="banner">
	<div class="container">
		<div class="row">
			<div class="col-lg-5 col-sm-6">
				<h2>
					<fmt:message key="cart_review.banner.title"
						bundle="${sys_messages}" />
				</h2>
				<fmt:message key="cart_review.banner.description"
					bundle="${sys_messages}" />
			</div>
		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="col-sm-12 col-md-12 col-lg-12 segment-name">
			<h3>
				<fmt:message key="cart_review.title" bundle="${sys_messages}" />
			</h3>
			<hr>
		</div>
	</div>
	<%--
	<div class="row">
		<div class="col-sm-12 col-md-12 col-lg-12 text-center segment-name">
			<h5>
				<fmt:message key="cart_review.subtitle" bundle="${sys_messages}" />
			</h5>
			<!-- <hr> -->
		</div>
	</div>
	--%>
	<!-- tabela produtos -->
	<div class="row">
		<div id="product_content" class="col-sm-12 col-md-12 col-lg-12">
			<jsp:include page="products.jsp"/>
		</div>
	</div>
	<!-- /tabela produtos -->

	<!-- identificação -->
	<c:if test="${empty sessionScope.system_user}">
		<div id="id_area" class="form-box-wrap-bottom">
			<div class="row">
				<!-- login -->
				<div class="col-sm-4 col-md-4 col-lg-4">
					<div class="form-box-wrap form-box-wrap-bottom">
						<jsp:include page="sign_in.jsp"/>
					</div>
				</div>
				<!-- /login -->
		
				<!-- cadastro -->
				<div class="col-sm-8 col-md-8 col-lg-8">
					<div class="col-xs-12 form-box-wrap form-box-wrap-bottom">
						<jsp:include page="sign_up.jsp"/>
					</div>
				</div>
				<!-- /cadastro -->
			</div>
		</div>
	</c:if>
	<!-- /identificação -->

	<!-- payment area -->
	<c:if test="${!empty sessionScope.system_user}">
		<form method="post" id="payment_form" action="${pageContext.request.contextPath}/cart/checkout"
			role="form" class="form-horizontal" data-toggle="validator" dest-content="#result-checkout">
		<div id="payment_area">
			<div class="row">
				
				<!-- payment info -->
				<div class="col-sm-6 col-md-6 col-lg-6">
					<div class="row">
						<div class="col-xs-12 segment-name">
							<h3>
								<fmt:message key="cart_review.user.title" bundle="${sys_messages}" />
								<hr>
							</h3>
						</div>
					</div>
					<div class="row">
						<div id="cart_user_form" class="col-xs-12">
							<jsp:include page="${vars['user_view']}"/> 
						</div>
					</div>
				</div>
				<!-- /payment info -->
				 
				<!-- payment data -->
				<div class="col-sm-6 col-md-6 col-lg-6">
					<div class="row">
						<div class="col-xs-12 segment-name">
							<h3>
								<fmt:message key="cart_review.payment.title" bundle="${sys_messages}" />
								<hr>
							</h3>
						</div>
					</div>
					<div class="row">
						<div id="cart_payment_details" class="col-xs-12">
							<jsp:include page="payment-details.jsp"/> 
						</div>
					</div>					
					<div class="row">
						<div class="col-xs-12">
							<div id="result-checkout" class="result-check"></div>
							<button id="button_payment_form" type="submit" class="pull-right btn btn-success"><fmt:message key="cart_review.checkout.submit" bundle="${sys_messages}" /></button>
						</div>
					</div>
				</div>
				<!-- /payment data -->
				
			</div>
		</div>
		</form>
	</c:if>
	<!-- /payment area -->

</div>
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/language/${locale}/cart.js" charset="utf-8" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/cart.js"                    charset="utf-8" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/templates/shared/default_template/resources/js/util.js"              charset="utf-8" type="text/javascript"></script>