<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<span id="beforeProduct"></span>
<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textfield 
			name="name" 
			value="${vars.entity.name}" 
			label="#{product.form.name.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:NAME')}"
			bundle="${messages}">
			<ec:field-validator>
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{product.form.name.notEmpty}" 
					bundle="${messages}"/>
				<%--
				<ec:field-validator-rule 
					name="regexp"
					message="#{product.form.name.regex}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				--%>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{product.form.name.length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">256</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
</ed:row>
<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:select 
			name="productMetadata" 
			label="#{product.form.product_metadata.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:PRODUCT_METADATA')}"
			bundle="${messages}">
			<ec:option value=""></ec:option>
			<c:forEach items="${vars.productMetadataList}" var="metadata">
				<ec:option value="${metadata.protectedID}" selected="${metadata.id == vars.entity.metadata}">${metadata.name}</ec:option>
			</c:forEach>
			<ec:field-validator>
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{product.form.product_metadata.notEmpty}" 
					bundle="${messages}"/>
			</ec:field-validator>
			<ec:event type="change">
				let $source = $event.source;
				let $form = $source.getForm();
				$form.submit(
					false, 
					"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/show/${vars.entity.productType.toLowerCase()}/attribute_tab", 
					"attribute_tab"
				); 
			</ec:event>
		</ec:select>
	</ed:col>
</ed:row>

<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textarea 
			id="product_form_shortDescription"
			rows="2" 
			name="shortDescription" 
			label="#{product.form.short_description.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:SHORT_DESCRIPTION')}"
			bundle="${messages}">${vars.entity.shortDescription}</ec:textarea>
		<ec:field-validator form="product_form" field="product_form_shortDescription">
			<ec:field-validator-rule 
				name="notEmpty" 
				message="#{product.form.short_description.notEmpty}" 
				bundle="${messages}"/>
			<%--
			<ec:field-validator-rule 
				name="regexp"
				message="#{product.form.short_description.regex}"
				bundle="${messages}">
				<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
			</ec:field-validator-rule>
			--%>
			<ec:field-validator-rule 
				name="stringLength" 
				message="#{product.form.short_description.length}" 
				bundle="${messages}">
					<ec:field-validator-param name="min">3</ec:field-validator-param>
					<ec:field-validator-param name="max">256</ec:field-validator-param>
			</ec:field-validator-rule>
		</ec:field-validator>
	</ed:col>
</ed:row>
<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textarea
			id="product_form_description" 
			rows="10" 
			name="description" 
			label="#{product.form.description.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:DESCRIPTION')}"
			bundle="${messages}">${vars.entity.description}</ec:textarea>
		<ec:field-validator form="product_form" field="product_form_description">
			<ec:field-validator-rule 
				name="notEmpty" 
				message="#{product.form.description.notEmpty}" 
				bundle="${messages}"/>
			<%--
			<ec:field-validator-rule 
				name="regexp"
				message="#{product.form.description.regex}"
				bundle="${messages}">
				<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
			</ec:field-validator-rule>
			--%>
			<ec:field-validator-rule 
				name="stringLength" 
				message="#{product.form.description.length}" 
				bundle="${messages}">
					<ec:field-validator-param name="min">3</ec:field-validator-param>
					<ec:field-validator-param name="max">2048</ec:field-validator-param>
			</ec:field-validator-rule>
		</ec:field-validator>
	</ed:col>
</ed:row>
<ed:row style="form">
	<ed:col size="6" classStyle="form-group has-feedback">
		<ec:select
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:DISPLAY')}" 
			label="#{product.form.display.label}" 
			name="display" 
			bundle="${messages}">
			<ec:option value=""><fmt:message key="product.form.display.options.disabled" bundle="${messages}" /></ec:option>
			<ec:option value="true" selected="${vars.entity.getDisplay() != null && vars.entity.display}"><fmt:message key="product.form.display.options.show" bundle="${messages}" /></ec:option>
			<ec:option value="false" selected="${vars.entity.getDisplay() != null && !vars.entity.display}"><fmt:message key="product.form.display.options.hide" bundle="${messages}" /></ec:option>
			<ec:field-validator>
				<ec:field-validator-rule name="notEmpty"
					message="#{product.form.display.notEmpty}" bundle="${messages}" />
			</ec:field-validator>
		</ec:select>
	</ed:col>
	<ed:col size="6" classStyle="form-group has-feedback">
		<ec:label><fmt:message key="product.form.cost.label" bundle="${messages}"/></ec:label>
		<ec:field-group>
			<ec:prepend-field>
				<ec:select
					readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:CURRENCY')}"
					name="currency">
					<c:forEach items="${vars.currencyList}" var="currency">
						<ec:option value="${currency.currencyCode}" selected="${vars.entity.currency == currency.currencyCode}">${currency.symbol}</ec:option>
					</c:forEach>
					<ec:field-validator>
						<ec:field-validator-rule name="notEmpty"
							message="#{product.form.currency.notEmpty}" bundle="${messages}" />
					</ec:field-validator>
				</ec:select>
			</ec:prepend-field>
			<ec:textfield name="cost"
				readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:COST')}"
				value="${vars.entity.cost}">
				<ec:field-validator>
					<ec:field-validator-rule name="notEmpty"
						message="#{product.form.cost.notEmpty}" bundle="${messages}" />
					<ec:field-validator-rule name="regexp"
						message="#{product.form.cost.regex}" bundle="${messages}">
						<ec:field-validator-param name="regexp" raw="true">/[0-9]{1,10}(\.[0-9]{2,2})?/</ec:field-validator-param>
					</ec:field-validator-rule>
				</ec:field-validator>
			</ec:textfield>
			<ec:append-field>
				<ec:append-field>
					<ec:select name="measurementUnit"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:MEASUREMENT_UNIT')}">
						<c:forEach items="${vars.measurementUnit}" var="measurementUnit">
							<ec:option
								selected="${vars.entity.measurementUnit == measurementUnit}"
								value="${measurementUnit}">${measurementUnit.getName(locale)}</ec:option>
						</c:forEach>
					</ec:select>
				</ec:append-field>
			</ec:append-field>
		</ec:field-group>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textfield name="tagsString" label="#{product.form.tags.label}"
			align="center" value="${vars.entity.tagsString}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:TAGS')}"
			bundle="${messages}">
			<ec:field-validator>
				<ec:field-validator-rule name="stringLength"
					message="#{product.form.tags.length}" bundle="${messages}">
					<ec:field-validator-param name="max">600</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule name="regexp"
					message="#{product.form.tags.regex}" bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^([^,.]+(\,[^,.]+)*)$/</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
</ed:row>
<span id="afterProduct"></span>
