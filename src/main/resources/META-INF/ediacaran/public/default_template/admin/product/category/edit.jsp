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
				<h2><fmt:message key="header.title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{header.title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path 
					text="#{header.breadcrumb.parent}" 
					lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/category" 
					bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:form method="POST" id="product_category_form" enctype="multipart/form-data"
			update="result_product_category_form" 
			action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/category/save/${vars.entity.protectedID}" >
			<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
			<ed:row>
				<ed:col>
					<span formgroup="productCategory">
						<ed:row style="form">
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
											label="#{form.name.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:CATEGORY:FIELDS:NAME')}"
											bundle="${messages}">
											<ec:field-validator>
												<ec:field-validator-rule 
													name="notEmpty" 
													message="#{form.name.notEmpty}" 
													bundle="${messages}"/>
												<ec:field-validator-rule 
													name="stringLength" 
													message="#{form.name.length}" 
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
										<ec:select 
											name="parent1" 
											label="#{form.parent1.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:CATEGORY:FIELDS:PARENT1')}"
											bundle="${messages}">
											<ec:option value=""></ec:option>
											<c:forEach items="${vars.categories1}" var="category">
												<ec:option value="${category.protectedID}" selected="${category.id == vars.entity.parent1.id}">${category.name}</ec:option>
											</c:forEach>
											<ec:event type="change">
												let $source = $event.source;
												let $form = $source.getForm();
											</ec:event>
										</ec:select>
									</ed:col>
								</ed:row>

								<ed:row style="form">
									<ed:col size="12" classStyle="form-group has-feedback">
										<ec:select 
											name="parent2" 
											label="#{form.parent2.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:CATEGORY:FIELDS:PARENT2')}"
											bundle="${messages}">
											<ec:option value=""></ec:option>
											<c:forEach items="${vars.categories2}" var="category">
												<ec:option value="${category.protectedID}" selected="${category.id == vars.entity.parent2.id}">${category.name}</ec:option>
											</c:forEach>
											<ec:event type="change">
												let $source = $event.source;
												let $form = $source.getForm();
											</ec:event>
										</ec:select>
									</ed:col>
								</ed:row>

								<ed:row style="form">
									<ed:col size="12" classStyle="form-group has-feedback">
										<ec:textfield 
											name="resourceBundle" 
											value="${vars.entity.resourceBundle}" 
											label="#{form.resourceBundle.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:CATEGORY:FIELDS:RESOURCEBUNDLE')}"
											bundle="${messages}">
											<ec:field-validator>
												<ec:field-validator-rule 
													name="stringLength" 
													message="#{form.resourceBundle.length}" 
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
										<ec:textfield 
											name="template" 
											value="${vars.entity.template}" 
											label="#{form.template.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:CATEGORY:FIELDS:TEMPLATE')}"
											bundle="${messages}">
											<ec:field-validator>
												<ec:field-validator-rule 
													name="stringLength" 
													message="#{form.template.length}" 
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
											id="product_category_form_description"
											rows="2" 
											name="description" 
											label="#{form.description.label}"
											readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:DESCRIPTION')}"
											bundle="${messages}">${vars.entity.description}</ec:textarea>
										<ec:field-validator form="product_category_form" field="product_category_form_description">
											<ec:field-validator-rule 
												name="notEmpty" 
												message="#{form.description.notEmpty}" 
												bundle="${messages}"/>
											<ec:field-validator-rule 
												name="stringLength" 
												message="#{form.description.length}" 
												bundle="${messages}">
													<ec:field-validator-param name="min">3</ec:field-validator-param>
													<ec:field-validator-param name="max">2000</ec:field-validator-param>
											</ec:field-validator-rule>
										</ec:field-validator>
									</ed:col>
								</ed:row>
							
							</ed:col>
						</ed:row>
					</span>
				</ed:col>
			</ed:row>
			
			<ed:row>
				<ed:col id="result_product_category_form">
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col size="12">
					<ec:button 
						label="#{form.save.label}" 
						align="right"
						actionType="submit"
						bundle="${messages}"/>
				</ed:col>
			</ed:row>
		</ec:form>
	</ec:box-body>
</ec:box>				    
<script type="text/javascript">
$.AppContext.onload(function(){
	let $form = $.AppContext.utils.getById('product_category_form');
	$form.updateFieldIndex();
	$form.updateFieldNames();
})
</script>