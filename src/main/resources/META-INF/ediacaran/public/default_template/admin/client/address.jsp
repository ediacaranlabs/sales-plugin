<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<input type="hidden" value="${address.protectedID}" name="protectedID">
<ed:row>
	<ed:col size="6" classStyle="form-group has-feedback">
		<ec:textfield 
			name="firstName"
			value="${address.firstName}"
			label="#{form.first_name}"
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
	<ed:col size="6" classStyle="form-group has-feedback">
		<ec:textfield 
			name="lastName"
			value="${address.lastName}"
			label="#{form.last_name}"
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
</ed:row>				
<ed:row>
	<ed:col size="2" classStyle="form-group has-feedback">
		<ec:textfield 
			name="zip"
			value="${address.zip}"
			label="#{form.zip}"
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:textfield>
	</ed:col>
	<ed:col size="10" classStyle="form-group has-feedback">
		<ec:textfield 
			name="address"
			value="${address.address}"
			label="#{form.address}"
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:textfield>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col size="3" classStyle="form-group has-feedback">
		<ec:textfield 
			name="complement"
			value="${address.complement}"
			label="#{form.complement}"
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:textfield>
	</ed:col>
	<ed:col size="3" classStyle="form-group has-feedback">
		<ec:select
			name="country.isoAlpha3"
			value="${address.country.isoAlpha3}"
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
					selected="${address.country.isoAlpha3 == country.isoAlpha3}" 
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:select>
	</ed:col>
	<ed:col size="3" classStyle="form-group has-feedback">
		<ec:textfield 
			name="region"
			value="${address.region}"
			label="#{form.region}"
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:textfield>
	</ed:col>
	<ed:col size="3" classStyle="form-group has-feedback">
		<ec:textfield 
			name="city"
			value="${address.city}"
			label="#{form.city}"
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
			<ec:event type="keyup">
				var $source = $event.source;
				var $form = $source.getForm();
				
				var $group = $source.getFormGroup();
				
				var $accordion = $group.getFirstChild();
				
				if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
					var $item = $accordion.getItens()[0];
					var $path = $group.getPath();
					var $address = $form.getField($path + ".address");
					var $complement = $form.getField($path + ".complement");
					var $city = $form.getField($path + ".city");
					var $region = $form.getField($path + ".region");
					var $country = $form.getField($path + ".country.isoAlpha3");
					var $zip = $form.getField($path + ".zip");
					$item.setTitle($address.getValue() + " " + $complement.getValue() + " " + $city.getValue() + " " + $region.getValue() + " " + $country.getValue() + " - " + $zip.getValue());
				}
			</ec:event>
		</ec:textfield>
	</ed:col>
</ed:row>