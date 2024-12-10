<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<input type="hidden" value="${vars.client.protectedID}" name="protectedID">
<ed:row>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="email"
			label="#{form.email}"
			value="${vars.client.email}"
			placeholder="#{form.email.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:EMAIL'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.validation.email.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.validation.email.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().EMAIL</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.validation.email.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="firstName"
			label="#{form.first_name}"
			value="${vars.client.firstName}"
			placeholder="#{form.first_name.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:FIRST_NAME'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.validation.first_name.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.validation.first_name.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.validation.first_name.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="lastName"
			label="#{form.last_name}"
			value="${vars.client.lastName}"
			placeholder="#{form.last_name.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:LAST_NAME'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.last_name.validation.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.last_name.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.last_name.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="data.document"
			label="#{form.document}"
			value="${empty vars.client.addData['document']? '' : '***********'}"
			placeholder="#{form.document.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:DOCUMENT'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.document.validation.not_empty}" 
					bundle="${messages}"/>
				<c:if test="${empty vars.client.addData['document']}">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.document.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^([0-9]{11,11}|[0-9]{14,14})$/</ec:field-validator-param>
				</ec:field-validator-rule>
				</c:if>
				<c:if test="${!empty vars.client.addData['document']}">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.document.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^(\*{11,11})$/</ec:field-validator-param>
				</ec:field-validator-rule>
				</c:if>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="organization"
			label="#{form.organization}"
			value="${vars.client.organization}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:ORGANIZATION'])}"
			placeholder="#{form.organization.placeholder}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.organization.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.organization.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col classStyle="form-group has-feedback">
		<ec:select
			id="user_country"
			name="country.isoAlpha3"
			label="#{form.country}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:COUNTRY'])}"
			bundle="${messages}">
			<ec:option 
				label="#{form.country.placeholder}" 
				value=""
				bundle="${messages}"/>
			<c:forEach items="${vars.countries}" var="country">
				<ec:option 
					label="${country.name}"
					value="${country.isoAlpha3}"
					selected="${vars.client.country.isoAlpha3 == country.isoAlpha3}" 
					bundle="${messages}"/>
			</c:forEach>
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.country.validation.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.country.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
			<ec:event type="change">
				var $form = $event.source.getFirstParent(function($e){
					return $e.getTagName() == 'form';
				});
				
				if($form == null){
					return;
				}
				
				var $countryField = $form.getField('country.isoAlpha3');
				var $country = $countryField.getValue();
				
				if($country){
					$form.submit(
						false, 
						"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit", 
						"client_data_view"
					);
				}
			</ec:event>
		</ec:select>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="address"
			label="#{form.address}"
			value="${vars.client.address}"
			placeholder="#{form.address.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:ADDRESS'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.address.validation.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.address.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.address.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="complement"
			label="#{form.complement}"
			value="${vars.client.complement}"
			placeholder="#{form.complement.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:COMPLEMENT'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.complement.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.complement.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="city"
			label="#{form.city}"
			value="${vars.client.city}"
			placeholder="#{form.city.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:CITY'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.city.validation.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.city.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.city.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="region"
			label="#{form.region}"
			value="${vars.client.region}"
			placeholder="#{form.region.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:REGION'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.region.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.region.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="zip"
			label="#{form.zip}"
			value="${vars.client.zip}"
			placeholder="#{form.zip.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:ZIP'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{form.zip.validation.not_empty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.zip.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^[0-9]{3,}$/</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.zip.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">60</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<ec:textfield 
			name="phone"
			label="#{form.phone}"
			value="${vars.client.phone}"
			placeholder="#{form.phone.placeholder}"
			readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:PHONE'])}"
			bundle="${messages}">
			<ec:field-validator form="client_form">
				<ec:field-validator-rule 
					name="regexp"
					message="#{form.phone.validation.regexp}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^[0-9]{3,}$/</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{form.phone.validation.string_length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">16</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
	<ed:col classStyle="form-group has-feedback">
		<c:if test="${vars.completeForm}">
			<ec:radio 
				name="activated"
				label="#{form.activated}"
				value="true"
				selected="${vars.client.activated}"
				readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:ACTIVATED'])}"
				inline="true"
				bundle="${messages}">
				<ec:field-validator form="client_form">
					<ec:field-validator-rule 
						name="stringLength" 
						message="#{form.activated.validation.string_length}" 
						bundle="${messages}">
							<ec:field-validator-param name="min">1</ec:field-validator-param>
					</ec:field-validator-rule>
				</ec:field-validator>		 
			 </ec:radio>
			<ec:radio 
				name="activated"
				label="#{form.deactivated}"
				value="false"
				selected="${!vars.client.activated}"
				readonly="${!pageContext.request.userPrincipal.isGranted(['SALES:CLIENT:FIELDS:ACTIVATED'])}"
				inline="true"
				bundle="${messages}">
				<ec:field-validator form="client_form">
					<ec:field-validator-rule 
						name="stringLength" 
						message="#{form.deactivated.validation.string_length}" 
						bundle="${messages}">
							<ec:field-validator-param name="min">1</ec:field-validator-param>
					</ec:field-validator-rule>
				</ec:field-validator>		 
		 	</ec:radio>
		</c:if>
	 </ed:col>	
</ed:row>