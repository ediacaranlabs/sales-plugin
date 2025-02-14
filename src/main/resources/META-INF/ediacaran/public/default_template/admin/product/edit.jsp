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
					lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/modules" 
					bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:form method="POST" id="resource_form" enctype="multipart/form-data"
			update="result_resource_form" 
			action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/save" >
			<input type="hidden" name="protectedID" value="${vars.entity.protectedID}">
			<ed:row style="form">
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:imagefield name="thumbnail"
						src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
						button="#{resource_form.thumbnail.button}" 
						bundle="${messages}" width="300" height="400" border="squad">
					</ec:imagefield>
				</ed:col>
				<ed:col size="9">
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
		       			<ed:col classStyle="form-group has-feedback">
							<ec:textfield 
								name="tagsString" 
								label="#{resource_form.tags.label}" 
								align="center" 
								value="${vars.entity.tagsString}"
								readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:TAGS')}"
								bundle="${messages}">
								<ec:field-validator>
									<ec:field-validator-rule 
										name="stringLength" 
										message="#{resource_form.tags.length}" 
										bundle="${messages}">
											<ec:field-validator-param name="max">600</ec:field-validator-param>
									</ec:field-validator-rule>
									<ec:field-validator-rule 
										name="regexp" 
										message="#{resource_form.tags.regex}"
										bundle="${messages}">
										<ec:field-validator-param name="regexp" raw="true">/^([^,.]+(\,[^,.]+)*)$/</ec:field-validator-param>
									</ec:field-validator-rule>
								</ec:field-validator>								
							</ec:textfield>
		       			</ed:col>
		       		</ed:row>
					<ed:row style="form">
						<ed:col size="4" classStyle="form-group has-feedback">
							<ec:field-group>
								<ec:prepend-field>
					    			<ec:select 
										readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:CURRENCY')}"
					    				name="currency">
					    				<ec:option value="USD">USD</ec:option>
										<ec:field-validator>
											<ec:field-validator-rule 
												name="notEmpty" 
												message="#{resource_form.currency.notEmpty}" 
												bundle="${messages}"/>
										</ec:field-validator>
					    			</ec:select>
								</ec:prepend-field>
								<ec:textfield 
									name="cost" 
									readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:COST')}"
									value="${vars.entity.cost}">
									<ec:field-validator>
										<ec:field-validator-rule 
											name="notEmpty" 
											message="#{resource_form.cost.notEmpty}" 
											bundle="${messages}"/>
										<ec:field-validator-rule 
											name="regexp" 
											message="#{resource_form.cost.regex}"
											bundle="${messages}">
											<ec:field-validator-param name="regexp" raw="true">/[0-9]{1,10}\.[0-9]{2,2}/</ec:field-validator-param>
										</ec:field-validator-rule>
									</ec:field-validator>								
								</ec:textfield>
							</ec:field-group>
						</ed:col>
						<ed:col size="8" classStyle="form-group has-feedback">
		    				<c:forEach items="${vars.periodList}" var="period" >
		    					<ec:radio name="periodType" 
		    						label="${period.getName(locale)}" 
									readonly="${!pageContext.request.userPrincipal.isGrantedPermission('SALES:PRODUCT:FIELDS:PERIOD')}"
		    						value="${period}" 
		    						selected="${vars.entity.periodType == period}" inline="true" />
		    				</c:forEach>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col size="12" id="result_resource_form">
						</ed:col>
					</ed:row>
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
