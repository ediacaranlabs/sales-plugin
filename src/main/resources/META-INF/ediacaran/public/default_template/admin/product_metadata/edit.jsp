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
			<ed:row>
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:imagefield name="thumbnail"
						src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
						button="Select" 
						bundle="${messages}" width="200" height="200" border="squad"/>
				</ed:col>
				<ed:col size="9">
					<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
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
											<ec:field-validator-param name="min">3</ec:field-validator-param>
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
									<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
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
			<ed:row>
				<ed:col id="attrArea">
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button label="Add attribute" align="right" actionType="button" bundle="${messages}">
						<ec:event type="click">
							var $attr = $.AppContext.utils.applyTemplate("metadata_attribute", {});
							$.AppContext.utils.content.append("attrArea",$attr);
						</ec:event>
					</ec:button>
				</ed:col>
			</ed:row>
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
<ec:tabs>
	<ec:tabs-item title="Attribute">
	</ec:tabs-item>
</ec:tabs>
</ec:template>
<%-- 
<ec:tabs>
	<ec:tabs-item title="Attribute">
		<input type="hidden" name="protectedID" value="!{entity.protectedID}">
		<ed:row>
			<ed:col size="3">
				<ec:textfield 
					name="code" 
					label="Code"
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
								<ec:field-validator-param name="min">2</ec:field-validator-param>
								<ec:field-validator-param name="max">32</ec:field-validator-param>
						</ec:field-validator-rule>
					</ec:field-validator>
				</ec:textfield>
			</ed:col>
			<ed:col size="9">
				<ec:textfield 
					name="name" 
					label="Name"
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
								<ec:field-validator-param name="min">2</ec:field-validator-param>
								<ec:field-validator-param name="max">128</ec:field-validator-param>
						</ec:field-validator-rule>
					</ec:field-validator>
				</ec:textfield>
			</ed:col>
		</ed:row>
		
		<ed:row>
			<ed:col size="4">
				<ec:checkbox name="allowEmpty" label="Allow empty"/>
			</ed:col>
			<ed:col size="4">
				<ec:textfield 
					name="rows" 
					label="Rows"
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
			<ed:col size="4">
				<ec:textfield 
					name="regex" 
					label="Regex"
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
		</ed:row>
		
		<ed:row>
			<ed:col size="3">
				<ec:textfield 
					name="minLength" 
					label="Min length"
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
			<ed:col size="3">
				<ec:textfield 
					name="maxLength" 
					label="Max length"
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
			<ed:col size="3">
				<ec:textfield 
					name="min" 
					label="Min"
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
			<ed:col size="3">
				<ec:textfield 
					name="max" 
					label="Max"
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
		
	</ec:tabs-item>
</ec:tabs>
--%>