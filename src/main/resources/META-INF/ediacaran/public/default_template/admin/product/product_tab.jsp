<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textfield 
			name="name" 
			value="${vars.entity.name}" 
			label="#{resource_form.name.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:NAME')}"
			bundle="${messages}">
			<ec:field-validator>
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{resource_form.name.notEmpty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{resource_form.name.regex}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{resource_form.name.length}" 
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
			label="Product type"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:PRODUCT_METADATA')}"
			bundle="${messages}">
			<c:forEach items="${vars.productMetadataList}" var="metadata">
				<ec:option value="${metadata.protectedID}" selected="${metadata.id == vars.entity.metadata}">${metadata.name}</ec:option>
			</c:forEach>
			<ec:field-validator field="description">
				<ec:field-validator-rule 
					name="notEmpty" 
					message="#{resource_form.description.notEmpty}" 
					bundle="${messages}"/>
				<ec:field-validator-rule 
					name="regexp"
					message="#{resource_form.description.regex}"
					bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule 
					name="stringLength" 
					message="#{resource_form.description.length}" 
					bundle="${messages}">
						<ec:field-validator-param name="min">3</ec:field-validator-param>
						<ec:field-validator-param name="max">256</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:select>
	</ed:col>
</ed:row>

<ed:row style="form">
	<ed:col size="12" classStyle="form-group has-feedback">
		<ec:textarea 
			rows="2" 
			name="shortDescription" 
			label="Short description"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:SHORT_DESCRIPTION')}"
			bundle="${messages}">${vars.entity.shortDescription}</ec:textarea>
		<ec:field-validator field="description">
			<ec:field-validator-rule 
				name="notEmpty" 
				message="#{resource_form.description.notEmpty}" 
				bundle="${messages}"/>
			<ec:field-validator-rule 
				name="regexp"
				message="#{resource_form.description.regex}"
				bundle="${messages}">
				<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
			</ec:field-validator-rule>
			<ec:field-validator-rule 
				name="stringLength" 
				message="#{resource_form.description.length}" 
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
			rows="10" 
			name="description" 
			label="#{resource_form.description.label}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:DESCRIPTION')}"
			bundle="${messages}">${vars.entity.description}</ec:textarea>
		<ec:field-validator field="description">
			<ec:field-validator-rule 
				name="notEmpty" 
				message="#{resource_form.description.notEmpty}" 
				bundle="${messages}"/>
			<ec:field-validator-rule 
				name="regexp"
				message="#{resource_form.description.regex}"
				bundle="${messages}">
				<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
			</ec:field-validator-rule>
			<ec:field-validator-rule 
				name="stringLength" 
				message="#{resource_form.description.length}" 
				bundle="${messages}">
					<ec:field-validator-param name="min">3</ec:field-validator-param>
					<ec:field-validator-param name="max">2048</ec:field-validator-param>
			</ec:field-validator-rule>
		</ec:field-validator>
	</ed:col>
</ed:row>
<ed:row style="form">
	<ed:col size="4">
		<ec:label>Cost</ec:label>
		<ec:field-group>
			<ec:prepend-field>
				<ec:select
					readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:CURRENCY')}"
					name="currency">
					<ec:option value="USD">USD</ec:option>
					<ec:field-validator>
						<ec:field-validator-rule name="notEmpty"
							message="#{resource_form.currency.notEmpty}" bundle="${messages}" />
					</ec:field-validator>
				</ec:select>
			</ec:prepend-field>
			<ec:textfield name="cost"
				readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:COST')}"
				value="${vars.entity.cost}">
				<ec:field-validator>
					<ec:field-validator-rule name="notEmpty"
						message="#{resource_form.cost.notEmpty}" bundle="${messages}" />
					<ec:field-validator-rule name="regexp"
						message="#{resource_form.cost.regex}" bundle="${messages}">
						<ec:field-validator-param name="regexp" raw="true">/[0-9]{1,10}\.[0-9]{2,2}/</ec:field-validator-param>
					</ec:field-validator-rule>
				</ec:field-validator>
			</ec:textfield>
			<ec:append-field>
				<ec:append-field>
					<ec:select name="measurementUnit"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:MEASUREMENT_UNIT')}">
						<ec:option></ec:option>
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
	<ed:col size="8" classStyle="form-group has-feedback">
		<ec:textfield name="tagsString" label="#{resource_form.tags.label}"
			align="center" value="${vars.entity.tagsString}"
			readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:TAGS')}"
			bundle="${messages}">
			<ec:field-validator>
				<ec:field-validator-rule name="stringLength"
					message="#{resource_form.tags.length}" bundle="${messages}">
					<ec:field-validator-param name="max">600</ec:field-validator-param>
				</ec:field-validator-rule>
				<ec:field-validator-rule name="regexp"
					message="#{resource_form.tags.regex}" bundle="${messages}">
					<ec:field-validator-param name="regexp" raw="true">/^([^,.]+(\,[^,.]+)*)$/</ec:field-validator-param>
				</ec:field-validator-rule>
			</ec:field-validator>
		</ec:textfield>
	</ed:col>
</ed:row>
