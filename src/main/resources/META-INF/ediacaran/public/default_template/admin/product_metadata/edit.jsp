<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>
<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2>Product metadata</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Product metadata" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path 
					text="Products metadata" 
					lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata" 
					bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:form method="POST" id="product_metadata_form" enctype="multipart/form-data"
			update="result_product_form" 
			action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata/save" >
			<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
			<ec:tabs>
				<ec:tabs-item title="Information" active="true">
				
					<ed:row>
						<ed:col size="3" classStyle="form-group has-feedback">
							<ec:imagefield name="thumbnail"
								src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
								button="Select" 
								bundle="${messages}" width="200" height="200" border="squad"/>
						</ed:col>
						<ed:col size="9">
							<ed:row style="form">
								<ed:col size="12" classStyle="form-group has-feedback">
									<ec:textfield 
										name="name" 
										value="${vars.entity.name}" 
										label="Name"
										readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:FIELDS:NAME')}"
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
									</ec:textfield>
								</ed:col>
							</ed:row>
							<ed:row style="form">
								<ed:col size="12" classStyle="form-group has-feedback">
									<ec:textarea 
										rows="10" 
										name="description" 
										label="Description"
										readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:FIELDS:DESCRIPTION')}"
										bundle="${messages}">${vars.entity.description}</ec:textarea>
									<ec:field-validator field="description">
										<ec:field-validator-rule 
											name="notEmpty" 
											message="not empty" 
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
												<ec:field-validator-param name="min">3</ec:field-validator-param>
												<ec:field-validator-param name="max">256</ec:field-validator-param>
										</ec:field-validator-rule>
									</ec:field-validator>
								</ed:col>
							</ed:row>
						</ed:col>
					</ed:row>
				
				</ec:tabs-item>
				<ec:tabs-item title="Attributes">
					<ed:row>
						<ed:col id="attrArea">
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:button label="Add attribute" align="right" actionType="button" bundle="${messages}">
								<ec:event type="click">
									var $attr = $.AppContext.utils.applyTemplate("metadata_attribute", {
										optsAreaID: 'optsArea_' + new Date().getTime() + "_" + Math.floor(Math.random() * 1000)
									});
									$.AppContext.utils.content.append("attrArea",$attr);
								</ec:event>
							</ec:button>
						</ed:col>
					</ed:row>
				</ec:tabs-item>
			</ec:tabs>
			<ed:row>
				<ed:col id="result_product_form">
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col size="12">
					<ec:button 
						label="#{resource_form.save.label}" 
						align="right"
						actionType="submit"
						bundle="${messages}"/>
				</ed:col>
			</ed:row>
		</ec:form>
	</ec:box-body>
</ec:box>
				    
<ec:template id="metadata_attribute" var="entity">
	<span formgroup="attributes" formgrouptype="index">
		<ec:accordion>
			<ec:accordion-item title="">
			
				<input type="hidden" name="protectedID">
				<ed:row>
					<ed:col size="3" classStyle="form-group has-feedback">
						<ec:textfield 
							name="code" 
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
								<ec:option value="${type}">${type.getName(locale)}</ec:option>
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
								<ec:option value="${valueType}">${valueType.getName(locale)}</ec:option>
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
				<ed:row>
					<ed:col id="!{entity.optsAreaID}">
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:button label="Add option" align="right" actionType="button" bundle="${messages}">
							<ec:event type="click">
								var $option = $.AppContext.utils.applyTemplate("option_attribute", {});
								$.AppContext.utils.content.append("!{entity.optsAreaID}", $option);
							</ec:event>
						</ec:button>
					</ed:col>
				</ed:row>
				
			</ec:accordion-item>
		</ec:accordion>
	</span>
</ec:template>

<ec:template id="option_attribute" var="entity">
	<span formgroup="options" formgrouptype="index">
		<ec:accordion>
			<ec:accordion-item title="">
				<input type="hidden" name="protectedID">
				<ed:row>
					<ed:col size="6" classStyle="form-group has-feedback">
						<ec:textfield 
							name="value" 
							placeholder="Value"
							readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:VALUE')}"
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
						</ec:textfield>
					</ed:col>
					<ed:col size="6" classStyle="form-group has-feedback">
						<ec:textfield 
							name="description"
							placeholder="Description"
							readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:DESCRIPTION')}"
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
							label="Delete" 
							align="right" 
							value="true"
							readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:ATTRIBUTE:OPTION:FIELDS:DELETED')}"
							bundle="${messages}"/>
					</ed:col>
				</ed:row>
			</ec:accordion-item>
		</ec:accordion>
	</span>
</ec:template>