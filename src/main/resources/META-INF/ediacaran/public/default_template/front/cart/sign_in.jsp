<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.sign_in" var="sys_messages"/>
<form id="signin_form"  
    method="POST" action="${pageContext.request.contextPath}/cart/sign-in"
    dest-content="#signin_form_result" accept-charset="UTF-8">
		
		<h3>
			<fmt:message key="cart_review.sign_in" bundle="${sys_messages}" /> 
			<b><fmt:message key="cart_review.sign_in_message" bundle="${sys_messages}" /></b>
		</h3>
		<div class="controls controls-row">
			<div class="control-group col-sm-12 col-md-12 col-lg-12">
				<input class="col-xs-12 form-control input-df" type="text" name="username" 
					placeholder="<fmt:message key="cart_review.sign_in.form.username.placeholder" bundle="${sys_messages}" />">
			</div>
		</div>
		<div class="controls controls-row">
			<div class="control-group col-sm-12 col-md-12 col-lg-12">
				<input class="col-xs-12 form-control input-df" type="password" name="password" 
				placeholder="<fmt:message key="cart_review.sign_in.form.password.placeholder" bundle="${sys_messages}" />">
			</div>
		</div>
		<div class="controls controls-row">
			<div class="control-group col-sm-12 col-md-12 col-lg-12">
				<button type="submit" class="btn btn-df btn-success col-xs-12"><fmt:message key="cart_review.sign_in.form.submit" bundle="${sys_messages}" /></button>
			</div>
		</div>
		<div id="signin_form_result" class="result-check"></div>
		<p>
			<small><a href="${pageContext.request.contextPath}/forgot-password"><fmt:message key="cart_review.sign_in.forgot_password" bundle="${sys_messages}" /></a></small>
		</p>
		<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/language/${locale}/sign-in.js" charset="utf-8"></script>
		<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/sign-in.js" charset="utf-8"></script>
</form>
