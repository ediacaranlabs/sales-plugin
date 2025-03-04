<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<span formgroup="options" formgrouptype="index">
	<ec:accordion>
		<ec:accordion-item title="${option.description}">
			<input type="hidden" name="protectedID" value="${option.protectedID}">
			<ed:row>
				<ed:col size="6" classStyle="form-group has-feedback">
					<ec:textfield 
						name="value" 
						value="${option.value}"
						placeholder="#{form.option.value.placeholder}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:VALUE')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.option.value.validation.notEmpty}" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.option.value.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.option.value.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">128</ec:field-validator-param>
							</ec:field-validator-rule>
						</ec:field-validator>
					</ec:textfield>
				</ed:col>
				<ed:col size="6" classStyle="form-group has-feedback">
					<ec:textfield 
						name="description"
						value="${option.description}"
						placeholder="#{form.option.description.placeholder}"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:DESCRIPTION')}"
						bundle="${messages}">
						<ec:field-validator>
							<ec:field-validator-rule 
								name="notEmpty" 
								message="#{form.option.description.validation.notEmpty}" 
								bundle="${messages}"/>
							<ec:field-validator-rule 
								name="regexp"
								message="#{form.option.description.validation.regexp}"
								bundle="${messages}">
								<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().ADDRESS_FORMAT</ec:field-validator-param>
							</ec:field-validator-rule>
							<ec:field-validator-rule 
								name="stringLength" 
								message="#{form.option.description.validation.stringLength}" 
								bundle="${messages}">
									<ec:field-validator-param name="min">1</ec:field-validator-param>
									<ec:field-validator-param name="max">256</ec:field-validator-param>
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
								
								var $descriptionFieldPath = $group.getPath();
								var $descriptionField = $form.getField($descriptionFieldPath + ".description");
								var $item = $accordion.getItens()[0];
								$item.setTitle($descriptionField.getValue());
							}
						</ec:event>
						
					</ec:textfield>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col classStyle="form-group has-feedback">
					<ec:checkbox 
						name="deleted" 
						label="#{form.option.deleted.label}" 
						align="right" 
						value="true"
						readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:DELETED')}"
						bundle="${messages}"/>
				</ed:col>
			</ed:row>
		</ec:accordion-item>
	</ec:accordion>
</span>
