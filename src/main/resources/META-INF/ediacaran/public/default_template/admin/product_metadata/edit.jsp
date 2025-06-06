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
			<span formgroup="product_metadata">
				<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
				<ed:row>
					<ed:col>
						<ec:tabs>
							<ec:tabs-item title="#{tabs.information.title}" active="true" bundle="${messages}">
							
								<ed:row>
									<ed:col size="3" classStyle="form-group has-feedback">
										<ec:imagefield name="thumbnail"
											src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
											button="#{form.thumbnail.button}"
											width="200" 
											height="200" 
											border="squad" 
											bundle="${messages}"/>
									</ed:col>
									<ed:col size="9">
										<ed:row style="form">
											<ed:col size="12" classStyle="form-group has-feedback">
												<ec:textfield 
													name="name" 
													value="${vars.entity.name}" 
													label="#{form.name.label}"
													readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:FIELDS:NAME')}"
													bundle="${messages}">
													<ec:field-validator>
														<ec:field-validator-rule 
															name="notEmpty" 
															message="#{form.name.validation.notEmpty}" 
															bundle="${messages}"/>
														<%--
														<ec:field-validator-rule 
															name="regexp"
															message="#{form.name.validation.regex}"
															bundle="${messages}">
															<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().NAME_FORMAT</ec:field-validator-param>
														</ec:field-validator-rule>
														--%>
														<ec:field-validator-rule 
															name="stringLength" 
															message="#{form.name.validation.stringLength}" 
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
													id="product_def_description"
													rows="5" 
													name="description" 
													label="#{form.description.label}"
													readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT_METADATA:FIELDS:DESCRIPTION')}"
													bundle="${messages}">${vars.entity.description}</ec:textarea>
												<ec:field-validator field="product_def_description">
													<ec:field-validator-rule 
														name="notEmpty" 
														message="#{form.description.validation.notEmpty}" 
														bundle="${messages}"/>
													<%--
													<ec:field-validator-rule 
														name="regexp"
														message="#{form.description.validation.regex}"
														bundle="${messages}">
														<ec:field-validator-param name="regexp" raw="true">$.AppContext.regexUtil.patterns().WORD_NUM</ec:field-validator-param>
													</ec:field-validator-rule>
													--%>
													<ec:field-validator-rule 
														name="stringLength" 
														message="#{form.description.validation.stringLength}" 
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
							<ec:tabs-item title="#{tabs.attribute.title}" bundle="${messages}">
								<ed:row>
									<ed:col id="attrArea">
										<c:forEach items="${vars.entity.attributeList}" var="attribute">
											<c:set var="attribute" value="${attribute}" scope="request"/>
											<jsp:include page="attribute_server.jsp"/>
										</c:forEach>
									</ed:col>
								</ed:row>
								<ed:row>
									<ed:col>
										<ec:button label="#{from.add_attribute.label}" align="right" actionType="button" bundle="${messages}">
											<ec:event type="click">
												var $attr = $.AppContext.utils.applyTemplate("metadata_attribute", {
													optsAreaID: 'optsArea_' + new Date().getTime() + "_" + Math.floor(Math.random() * 1000)
												});
												$.AppContext.utils.content.append("attrArea",$attr);
												
												let $source = $event.source;
												let $form = $source.getForm();
												
												$form.updateFieldIndex();
												$form.updateFieldNames();
											</ec:event>
										</ec:button>
									</ed:col>
								</ed:row>
							</ec:tabs-item>
						</ec:tabs>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col id="result_product_form">
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
			</span>
		</ec:form>
	</ec:box-body>
</ec:box>
				    
<ec:template id="metadata_attribute" var="attribute">
	<c:remove var="attribute" scope="request"/>
	<jsp:include page="attribute_client.jsp"/>
</ec:template>

<ec:template id="option_attribute" var="option">
	<c:remove var="option" scope="request"/>
	<jsp:include page="option_client.jsp"/>
</ec:template>

<script type="text/javascript">
$.AppContext.onload(function(){
	let $form = $.AppContext.utils.getById('product_metadata_form');
	$form.updateFieldIndex();
	$form.updateFieldNames();
})
</script>