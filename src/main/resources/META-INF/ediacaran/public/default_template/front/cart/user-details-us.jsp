<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.user_details" var="sys_messages"/>

<!-- row -->
<div class="row">
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- first-name -->
			<label class="control-label" for="customer.firstName"><fmt:message
				key="cart_user_details.form.firstname" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.firstName}" class="form-control input-df" type="text" name="customer.firstName" 
				placeholder="">
		<!-- /first-name -->
		</div>
	</div>
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- last-name -->
			<label class="control-label" for="customer.lastName"><fmt:message
				key="cart_user_details.form.lastname" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.lastName}" class="form-control input-df" type="text" name="customer.lastName" 
		    	placeholder="" value="">
		<!-- /last-name -->
	    </div>
	</div>
</div>
<!-- /row -->

<!-- row -->
<div class="row">
	<div class="col-sm-12 col-md-12 col-lg-12">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- address -->
			<label class="control-label" for="customer.address"><fmt:message
					key="cart_user_details.form.address" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.address}" class="col-xs-12 form-control input-df" type="text" name="customer.address" 
			    placeholder="">
		<!-- /address -->
		</div>
	</div>
</div>
<!-- /row -->

<!-- row -->
<div class="row">
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- city -->
			<label class="control-label" for="customer.city"><fmt:message
					key="cart_user_details.form.city" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.city}" class="col-xs-12 form-control input-df" type="text" name="customer.city" placeholder="">
		<!-- /city -->
		</div>
	</div>
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- region -->
			<label class="control-label" for="customer.region"><fmt:message
					key="cart_user_details.form.state" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.region}" class="col-xs-12 form-control input-df" type="text" name="customer.region"
				placeholder="">
		<!-- /region -->
	    </div>
	</div>
</div>
<!-- /row -->

<!-- row -->
<div class="row">
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!--  country -->
			<label class="control-label" for="customer.country"><fmt:message
					key="cart_user_details.form.country" bundle="${sys_messages}" /></label>
			<input type="hidden" name="customer.country" value="${empty vars.customer.country? 'XXX' : vars.customer.country.isoAlpha3}">
			<select disabled name="customer.country"  
				class="col-xs-12 form-control input-df">
				<option value="">
					<fmt:message key="cart_user_details.form.country.placeholder" bundle="${sys_messages}" />
				</option>
				<c:forEach items="${vars.countries}" var="country">
					<option ${vars.customer.country.isoAlpha3 == country.isoAlpha3? 'selected ' : ''}value="${country.isoAlpha3}">${country.name}</option>
				</c:forEach>
				<option ${empty vars.customer.country? 'selected ' : ''}value="XXX">
					<fmt:message key="cart_user_details.form.country.default" bundle="${sys_messages}" />
				</option>
			</select>
		<!--  /country -->
		</div>
	</div>
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- zip -->
			<label class="control-label" for="customer.zip"><fmt:message
					key="cart_user_details.form.zip" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.zip}" class="col-xs-12 form-control input-df" type="text" name="customer.zip" 
		    placeholder="">
		<!-- /zip -->
	    </div>
	</div>
</div>
<!-- /row -->

<!-- row -->
<div class="row">
	<div class="col-sm-6 col-md-6 col-lg-6">
		<div class="form-group col-sm-12 col-md-12 col-lg-12">
		<!-- phone -->
			<label class="control-label" for="customer.phone"><fmt:message
					key="cart_user_details.form.phone" bundle="${sys_messages}" /></label>
			<input value="${vars.customer.phone}" class="col-xs-12 form-control input-df" type="text" name="customer.phone" 
			    placeholder="">
		<!-- /phone -->
		</div>
	</div>
</div>
<!-- /row -->
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/language/${locale}/user-details-us.js" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/templates/front/default_template/cart/js/user-details.js" charset="utf-8"></script>