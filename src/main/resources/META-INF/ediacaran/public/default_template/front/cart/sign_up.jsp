<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.sign_up" var="sys_messages"/>
<form id="signup_form" 
    method="POST" action="${pageContext.request.contextPath}/cart/sign-up"
    dest-content="#signup_form_result" accept-charset="UTF-8">
	<h3>
		<fmt:message key="cart_review.sign_up" bundle="${sys_messages}" /> 
		<b><fmt:message key="cart_review.sign_up_message" bundle="${sys_messages}" /></b>
	</h3>
	<div class="controls controls-row">
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="firstName" 
				placeholder="<fmt:message key="cart_review.sign_up.form.firstname.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="lastName" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.lastname.placeholder" bundle="${sys_messages}" />"
				value="">
		</div>
		<div class="control-group controls-col col-sm-6 col-md-6 col-lg-6">
			<input class="col-xs-12 form-control input-df" type="password" name="password" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.password.placeholder" bundle="${sys_messages}" />">
		</div>
	</div>
	<div class="controls controls-row">
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="email" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.email.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="c_email" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.confirm_email.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-6 col-md-6 col-lg-6">
			<input class="col-xs-12 form-control input-df" type="text" name="address" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.address.placeholder" bundle="${sys_messages}" />">
		</div>
	</div>
	<div class="controls controls-row">
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="city" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.city.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-3 col-md-3 col-lg-3">
			<input class="col-xs-12 form-control input-df" type="text" name="region"
				placeholder="<fmt:message key="cart_review.sign_up.form.state.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-6 col-md-6 col-lg-6">
			<select name="country"  
				class="col-xs-12 form-control input-df">
				<option value="">
					<fmt:message key="cart_review.sign_up.form.country.placeholder" bundle="${sys_messages}" />
				</option>
				<c:forEach items="${vars.countries}" var="country">
					<option value="${country.isoAlpha3}">${country.name}</option>
				</c:forEach>
				<option value="XXX">
					<fmt:message key="cart_review.sign_up.form.country.default" bundle="${sys_messages}" />
				</option>
			</select>
	
	<%-- 	
			<input class="col-xs-12 form-control input-df" type="text" name="country"
				placeholder="<fmt:message key="cart_review.sign_up.form.country.placeholder" bundle="${sys_messages}" />">
	--%>
		</div>
	</div>
	<div class="controls controls-row">
		<div class="control-group controls-col col-sm-2 col-md-2 col-lg-2">
			<input class="col-xs-12 form-control input-df" type="text" name="zip" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.cep.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-4 col-md-4 col-lg-4">
			<input class="col-xs-12 form-control input-df" type="text" name="phone" 
			    placeholder="<fmt:message key="cart_review.sign_up.form.phone.placeholder" bundle="${sys_messages}" />">
		</div>
		<div class="control-group controls-col col-sm-6 col-md-6 col-lg-6">
			<button type="submit" class="btn btn-df btn-success col-xs-12"><fmt:message key="cart_review.sign_up.form.submit" bundle="${sys_messages}" /></button>
		</div>
	</div>
	<div id="signup_form_result" class="result-check"></div>
	<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/language/${locale}/sign-up.js" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/sign-up.js" charset="utf-8"></script>
</form>