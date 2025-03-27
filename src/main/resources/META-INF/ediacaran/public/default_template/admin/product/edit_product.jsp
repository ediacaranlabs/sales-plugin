<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<ed:row>
	<ed:col>
		<span formgroup="product">
			<ed:row style="form">
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:imagefield name="thumbnail"
						src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
						button="#{resource_form.thumbnail.button}" 
						bundle="${messages}" width="200" height="200" border="squad"/>
				</ed:col>
				<ed:col size="9">
					<ec:tabs>
						<ec:tabs-item active="true" title="Details">
							<jsp:include page="/default_template/admin/product/product_tab.jsp"/>
						</ec:tabs-item>
						<ec:tabs-item title="Image Gallery">
							<jsp:include page="/default_template/admin/product/image_gallery_tab.jsp"/>
						</ec:tabs-item>
						<ec:tabs-item title="Properties">
							<c:forEach items="${vars.entity.attributes}" var="propertyEntry">
								<c:set var="property" value="${propertyEntry.value}"/>
								<c:set var="propertyMetadata" value="${vars.attributesMetadata[property.productAttributeId]}"/>
								<span formgroup="attributes" formgrouptype="index">
									<ec:table-row>
										 <ec:table-col classStyle="form-group has-feedback">
											<c:choose>
												<c:when test="${propertyMetadata.type == 'TEXT'}">
													<c:if test="${propertyMetadata.rows > 0}">
														<ec:textarea rows="${propertyMetadata.rows}" label="${propertyMetadata.name}" name="${propertyMetadata.code}">${property.value}</ec:textarea>
													</c:if>
													<c:if test="${propertyMetadata.rows <= 0}">
														<ec:textfield label="${propertyMetadata.name}" name="${propertyMetadata.code}" value="${property.value}"/>
													</c:if>
												</c:when>
												<c:when test="${propertyMetadata.type == 'SELECT'}">
													<ec:select name="${propertyMetadata.code}" label="${propertyMetadata.name}">
														<c:set var="opt_selected" value="${property.value}"/>
														<c:forEach items="${propertyMetadata.options}" var="opts">
															<ec:option label="${opts.description}" selected="${opts.value == opt_selected}" value="${opts.value}"/>
														</c:forEach>
													</ec:select>
												</c:when>
												<c:when test="${propertyMetadata.type == 'MULTISELECT'}">
													<ec:select name="${propertyMetadata.code}" label="${propertyMetadata.name}" multiple="true" rows="5">
														<c:forEach items="${propertyMetadata.options}" var="opts">
															<ec:option label="${opts.description}" 
																selected="${property.containsValue(opts.value)}" value="${opts.value}"/>
														</c:forEach>
													</ec:select>
												</c:when>
												<c:when test="${propertyMetadata.type == 'SELECT_LIST'}">
													<ec:label>${propertyMetadata.name}</ec:label><br>
													<c:set var="opt_selected" value="${property.value}"/>
													<c:forEach items="${propertyMetadata.options}" var="opts">
														<ec:radio inline="true" name="${propertyMetadata.code}" label="${opts.description}" value="${opts.value}" selected="${opts.value == opt_selected}"/><br>
													</c:forEach>
												</c:when>
												<c:when test="${propertyMetadata.type == 'MULTISELECT_LIST'}">
													<ec:label>${propertyMetadata.name}</ec:label><br>
													<c:forEach items="${propertyMetadata.options}" var="opts">
														<ec:checkbox inline="true" name="${propertyMetadata.code}" label="${opts.description}" value="${opts.value}" 
															selected="${property.containsValue(opts.value)}"/><br>
													</c:forEach>
												</c:when>
											</c:choose>
											<ec:field-validator form="product_metadata_form" field="${propertyMetadata.code}">
											
												<c:if test="${!propertyMetadata.allowEmpty}">
												<ec:field-validator-rule name="notEmpty" message="The ${propertyMetadata.name} is required"/>
												</c:if>
							
												<c:if test="${(propertyMetadata.type == 'MULTISELECT' || propertyMetadata.type == 'MULTISELECT_LIST') && (propertyMetadata.minLength > 0 || propertyMetadata.maxLength > 0)}">
												<ec:field-validator-rule name="choice"  message="Please choose ${propertyMetadata.minLength} - ${propertyMetadata.maxLength}!">
													<c:if test="${propertyMetadata.minLength > 0}">
													<ec:field-validator-param name="min">${propertyMetadata.minLength}</ec:field-validator-param>
													</c:if>
													<c:if test="${propertyMetadata.maxLength > 0}">
													<ec:field-validator-param name="max">${propertyMetadata.maxLength}</ec:field-validator-param>
													</c:if>
												</ec:field-validator-rule>
												</c:if>
																	
												<c:if test="${!(propertyMetadata.type == 'MULTISELECT' || propertyMetadata.type == 'MULTISELECT_LIST') && (propertyMetadata.minLength > 0 || propertyMetadata.maxLength > 0)}">
												<ec:field-validator-rule name="stringLength"  message="The ${propertyMetadata.name} is short or large!">
													<c:if test="${propertyMetadata.min > 0}">
													<ec:field-validator-param name="min">${propertyMetadata.minLength}</ec:field-validator-param>
													</c:if>
													<c:if test="${propertyMetadata.max > 0}">
													<ec:field-validator-param name="max">${propertyMetadata.maxLength}</ec:field-validator-param>
													</c:if>
												</ec:field-validator-rule>
												</c:if>
							
												<c:if test="${!empty propertyMetadata.regex}">
												<ec:field-validator-rule name="regexp"  message="Invalid format!">
													<ec:field-validator-param name="regexp" raw="true">/${propertyMetadata.regex}/</ec:field-validator-param>
												</ec:field-validator-rule>
												</c:if>
												
											</ec:field-validator>
										</ec:table-col>
										<ec:table-col>
											<small>${propertyMetadata.description}</small>
										</ec:table-col>
									</ec:table-row>
								</span>
							</c:forEach>
						</ec:tabs-item>
						<c:forEach items="${vars.tabs}" var="tab">
						<!-- tab (${tab.id}) -->
						<ec:tabs-item title="${tab.title}">
							<ec:sectionView section="${tab.content}"/>
						</ec:tabs-item>
						<!-- /tab (${tab.id}) -->
						</c:forEach>
					</ec:tabs>
				</ed:col>
			</ed:row>
		</span>
	</ed:col>
</ed:row>
