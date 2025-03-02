<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<span formgroup="attributes" formgrouptype="index">
	<ec:accordion>
		<ec:accordion-item title="${attribute.name}">
		
			<input type="hidden" name="protectedID" value="${attribute.protectedID}">
			<ed:row>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="code" 
						value="${attribute.code}"
						label="Code"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:CODE')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="empty" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[a-z0-9]+{_[a-z0-9]+}*/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">32</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="9" classStyle="form-group has-feedback">
					<ec:textfield 
						name="name" 
						value="${attribute.name}"
						label="Name"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:NAME')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="empty" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
								var $item = $accordion.getItens()[0];
								$item.setTitle($nameField.getValue());
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
						<ec:option value="">Select a type</ec:option>
						<c:forEach items="${vars.types}" var="type">
							<ec:option value="${type}" selected="${attribute.type == type}">${type.getName(locale)}</ec:option>
						</c:forEach>
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="empty" 
								bundle="${messages}"/>
						</ec:field-validator>
					</ec:select>
				</ed:col>
				<ed:col size="6" classStyle="form-group has-feedback">
					<ec:select 
						name="valueType" 
						label="Value type"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:VALUE_TYPE')}"
						bundle="${messages}">
						<ec:option value="">Select a value type</ec:option>
						<c:forEach items="${vars.valueTypes}" var="valueType">
							<ec:option value="${valueType}" selected="${attribute.valueType == valueType}">${valueType.getName(locale)}</ec:option>
						</c:forEach>
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="empty" 
								bundle="${messages}"/>
						</ec:field-validator>
					</ec:select>
				</ed:col>
			</ed:row>
			
			<ed:row>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:textfield 
						name="minLength" 
						value="${attribute.minLength}"
						label="Min length"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MIN_LEN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						value="${attribute.maxLength}"
						label="Max length"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MAX_LEN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						value="${attribute.min}"
						label="Min"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MIN')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+{\.[0-9]+}{0,1}/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						value="${attribute.max}"
						label="Max"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:MAX')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+{\.[0-9]+}{0,1}/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						value="${attribute.rows}"
						label="Rows"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:ROWS')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						label="Regex"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:REGEX')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						value="${attribute.order}"
						label="Order"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:ORDER')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="regexp"
								message="regex"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">/[0-9]+/</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="length" 
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
						label="Delete" 
						align="right"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:FIELDS:DELETED')}"
						bundle="${messages}"/>
					<ec:checkbox 
						name="allowEmpty"
						selected="${attribute.allowEmpty}" 
						label="Allow empty&nbsp;" 
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
			<c:if test="${empty entity}">
				<c:set var="optionsArea" value="!{attribute.optsAreaID}"/>
			</c:if>
			<c:if test="${!empty entity}">
				<c:set var="optionsArea" value="${attribute.protectedID}"/>
			</c:if>
			<ed:row>
				<ed:col id="${optionsArea}">
					<c:forEach items="${attribute.options}" var="option">
						<c:set var="option" value="${option}" scope="request"/>
						<jsp:include page="option.jsp"/>
					</c:forEach>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button label="Add option" align="right" actionType="button" bundle="${messages}">
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
