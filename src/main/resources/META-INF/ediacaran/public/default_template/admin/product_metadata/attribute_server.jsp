<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<span formgroup="attributes" formgrouptype="index">
	<ec:accordion>
		<ec:accordion-item title="${attribute.code} - ${attribute.name}">
		
			<input type="hidden" name="protectedID" value="${attribute.protectedID}">
			<ed:row>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="code" 
						value="${attribute.code}"
						label="#{form.attribute.code.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:CODE')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.attribute.code.validation.notEmpty}" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.code.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[a-z0-9]+(_[a-z0-9]+)*/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.code.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">32</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
						<ec:event type="keyup">
							var $source = $event.source;
							var $form = $source.getForm();
							
							var $group = $source.getFormGroup();
							
							var $accordion = $group.getFirstChild();
							
							if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
							
								$form.updateFieldIndex();
								$form.updateFieldNames();
								
								var $nameFieldPath = $group.getPath();
								var $nameField = $form.getField($nameFieldPath + ".name");
								var $codeField = $form.getField($nameFieldPath + ".code");
								var $item = $accordion.getItens()[0];
								$item.setTitle($codeField.getValue() + " - " + $nameField.getValue());
							}
						</ec:event>
					</ec:textfield>
				</ed:col>
				<ed:col size="9" classStyle="form-group has-feedback">
					<ec:textfield 
						name="name" 
						value="${attribute.name}"
						label="#{form.attribute.name.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:NAME')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.attribute.name.validation.notEmpty}" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.name.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.name.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">128</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
						<ec:event type="keyup">
							var $source = $event.source;
							var $form = $source.getForm();
							
							var $group = $source.getFormGroup();
							
							var $accordion = $group.getFirstChild();
							
							if($accordion instanceof $.AppContext.types.components.accordion.Accordion){
							
								$form.updateFieldIndex();
								$form.updateFieldNames();
								
								var $nameFieldPath = $group.getPath();
								var $nameField = $form.getField($nameFieldPath + ".name");
								var $codeField = $form.getField($nameFieldPath + ".code");
								var $item = $accordion.getItens()[0];
								$item.setTitle($codeField.getValue() + " - " + $nameField.getValue());
							}
						</ec:event>
					</ec:textfield>
				</ed:col>
			</ed:row>
			
			<ed:row>
				<ed:col size="6" classStyle="form-group has-feedback">
					<ec:select 
						name="type" 
						label="Type"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:TYPE')}"
						bundle="${messages}">
						<ec:option value=""><fmt:message key="form.attribute.type.emptyOption" bundle="${messages}"/></ec:option>
						<c:forEach items="${vars.types}" var="type">
							<ec:option value="${type}" selected="${attribute.type == type}">${type.getName(locale)}</ec:option>
						</c:forEach>
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.attribute.type.validation.notEmpty}" 
								bundle="${messages}"/>
						</ec:field-validator>
					</ec:select>
				</ed:col>
				<ed:col size="6" classStyle="form-group has-feedback">
					<ec:select 
						name="valueType" 
						label="#{form.attribute.value_type.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:VALUE_TYPE')}"
						bundle="${messages}">
						<ec:option value=""><fmt:message key="form.attribute.value_type.emptyOption" bundle="${messages}"/></ec:option>
						<c:forEach items="${vars.valueTypes}" var="valueType">
							<ec:option value="${valueType}" selected="${attribute.valueType == valueType}">${valueType.getName(locale)}</ec:option>
						</c:forEach>
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.attribute.value_type.validation.notEmpty}" 
								bundle="${messages}"/>
						</ec:field-validator>
					</ec:select>
				</ed:col>
			</ed:row>
			
			<ed:row>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="minLength" 
						value="${attribute.minLength == 0? '' : attribute.minLength}"
						label="#{form.attribute.minLength.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MIN_LEN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.minLength.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.minLength.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">2</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="maxLength" 
						value="${attribute.maxLength == 0? '' : attribute.maxLength}"
						label="#{form.attribute.maxLength.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MAX_LEN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.maxLength.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.maxLength.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">2</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="min" 
						value="${attribute.min == 0? '' : attribute.min}"
						label="#{form.attribute.min.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MIN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.min.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+(\.[0-9]+){0,1}/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.min.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">12</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="max" 
						value="${attribute.max == 0? '' : attribute.max}"
						label="#{form.attribute.max.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MAX')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.max.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+(\.[0-9]+){0,1}/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.max.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">12</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
			</ed:row>		
		
			<ed:row>
				<ed:col size="4" classStyle="form-group has-feedback">
					<ec:textfield 
						name="rows" 
						value="${attribute.rows == 0? '' : attribute.rows}"
						label="#{form.attribute.rows.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:ROWS')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.rows.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.rows.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">2</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="4" classStyle="form-group has-feedback">
					<ec:textfield 
						name="regex" 
						value="${attribute.regex}"
						label="#{form.attribute.regex.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:REGEX')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.regex.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">128</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="4" classStyle="form-group has-feedback">
					<ec:textfield 
						name="order" 
						value="${attribute.order == 0? '' : attribute.order}"
						label="#{form.attribute.order.label}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:ORDER')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.attribute.order.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.attribute.order.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">2</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
			</ed:row>
		
			<ed:row>
				<ed:col classStyle="form-group has-feedback">
					<ec:checkbox 
						name="deleted" 
						label="#{form.attribute.deleted.label}" 
						align="right"
						value="true"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:DELETED')}"
						bundle="${messages}">
						<ec:event type="click">
							let $source = $event.source;
							let $form = $source.getForm();
							
							$form.updateFieldIndex();
							$form.updateFieldNames();
							
							let $group = $source.getFormGroup();

							let $path = $group.getAttribute("group-path");
							let $deleted = $form.getField($path + ".deleted");
							let $protectedID = $form.getField($path + ".protectedID");
							
							if($protectedID.getValue() == '' && $deleted.getValue()){
								$group.remove();
							}
						</ec:event>
					</ec:checkbox>
					<ec:checkbox 
						name="allowEmpty"
						selected="${attribute.allowEmpty}" 
						label="#{form.attribute.allowEmpty.label}" 
						value="true" 
						align="right"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:ALLOW_EMPTY')}"
						bundle="${messages}"/>
						
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					Options
				</ed:col>
			</ed:row>
			<c:if test="${empty attribute}">
				<c:set var="optionsArea" value="!{attribute.optsAreaID}"/>
			</c:if>
			<c:if test="${!empty attribute}">
				<c:set var="optionsArea" value="${attribute.protectedID}"/>
			</c:if>
			<ed:row>
				<ed:col id="${optionsArea}">
					<c:forEach items="${attribute.options}" var="option">
						<c:set var="option" value="${option}" scope="request"/>
						<jsp:include page="option_server.jsp"/>
					</c:forEach>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button label="#{form.attribute.add_option.label}" align="right" actionType="button" bundle="${messages}">
						<ec:event type="click">
							var $option = $.AppContext.utils.applyTemplate("option_attribute", {});
							$.AppContext.utils.content.append("${optionsArea}", $option);
						</ec:event>
					</ec:button>
				</ed:col>
			</ed:row>
			
		</ec:accordion-item>
	</ec:accordion>
</span>
