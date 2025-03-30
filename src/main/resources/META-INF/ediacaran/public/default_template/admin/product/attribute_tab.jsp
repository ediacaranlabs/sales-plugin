<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<c:forEach items="${vars.attributesMetadata}" var="propertyEntry">
	<c:set var="propertyMetadata" value="${propertyEntry.value}"/>
	<c:set var="property" value="${vars.entity.attributes[propertyMetadata.code]}"/>
	<span formgroup="attributes">
		<ed:row>
			 <ed:col classStyle="form-group has-feedback">
				<c:choose>
					<c:when test="${propertyMetadata.type == 'TEXT'}">
						<c:if test="${propertyMetadata.rows > 0}">
							<ec:textarea id="attribute_${propertyMetadata.code}" rows="${propertyMetadata.rows}" label="${propertyMetadata.name}" name="${propertyMetadata.code}">${property.value}</ec:textarea>
						</c:if>
						<c:if test="${propertyMetadata.rows <= 0}">
							<ec:textfield id="attribute_${propertyMetadata.code}" label="${propertyMetadata.name}" name="${propertyMetadata.code}" value="${property.value}"/>
						</c:if>
					</c:when>
					<c:when test="${propertyMetadata.type == 'SELECT'}">
						<ec:select id="attribute_${propertyMetadata.code}" name="${propertyMetadata.code}" label="${propertyMetadata.name}">
							<c:set var="opt_selected" value="${property.value}"/>
							<c:forEach items="${propertyMetadata.options}" var="opts">
								<ec:option label="${opts.description}" selected="${opts.value == opt_selected}" value="${opts.value}"/>
							</c:forEach>
						</ec:select>
					</c:when>
					<c:when test="${propertyMetadata.type == 'MULTISELECT'}">
						<ec:select id="attribute_${propertyMetadata.code}" name="${propertyMetadata.code}" label="${propertyMetadata.name}" multiple="true" rows="5">
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
							<ec:radio id="attribute_${propertyMetadata.code}" inline="true" name="${propertyMetadata.code}" label="${opts.description}" value="${opts.value}" selected="${opts.value == opt_selected}"/><br>
						</c:forEach>
					</c:when>
					<c:when test="${propertyMetadata.type == 'MULTISELECT_LIST'}">
						<ec:label>${propertyMetadata.name}</ec:label><br>
						<c:forEach items="${propertyMetadata.options}" var="opts">
							<ec:checkbox id="attribute_${propertyMetadata.code}" inline="true" name="${propertyMetadata.code}" label="${opts.description}" value="${opts.value}" 
								selected="${property.containsValue(opts.value)}"/><br>
						</c:forEach>
					</c:when>
				</c:choose>
				<ec:field-validator form="product_form" field="attribute_${propertyMetadata.code}">
				
					<c:if test="${!propertyMetadata.allowEmpty}">
					<ec:field-validator-rule name="notEmpty" bundle="${messages}" message="#{form.fields.notEmpty}"/>
					</c:if>

					<c:if test="${(propertyMetadata.type == 'MULTISELECT' || propertyMetadata.type == 'MULTISELECT_LIST') && (propertyMetadata.minLength > 0 || propertyMetadata.maxLength > 0)}">
					<ec:field-validator-rule name="choice" bundle="${messages}" message="#{form.fields.choice}">
						<c:if test="${propertyMetadata.minLength > 0}">
						<ec:field-validator-param name="min">${propertyMetadata.minLength}</ec:field-validator-param>
						</c:if>
						<c:if test="${propertyMetadata.maxLength > 0}">
						<ec:field-validator-param name="max">${propertyMetadata.maxLength}</ec:field-validator-param>
						</c:if>
					</ec:field-validator-rule>
					</c:if>
										
					<c:if test="${!(propertyMetadata.type == 'MULTISELECT' || propertyMetadata.type == 'MULTISELECT_LIST')}">
					
						<c:if test="${(propertyMetadata.valueType == 'DECIMAL' || propertyMetadata.valueType == 'INTEGER') && (propertyMetadata.min > 0 || propertyMetadata.max > 0)}">
							<%--
							<ec:field-validator-rule name="integer"  message="The ${propertyMetadata.name} is invalid!">
								<c:if test="${propertyMetadata.min > 0}">
								<ec:field-validator-param name="min">${propertyMetadata.min}</ec:field-validator-param>
								</c:if>
								<c:if test="${propertyMetadata.max > 0}">
								<ec:field-validator-param name="max">${propertyMetadata.max}</ec:field-validator-param>
								</c:if>
							</ec:field-validator-rule>
							--%>
							<c:if test="${propertyMetadata.valueType == 'INTEGER' && empty propertyMetadata.regex}">
								<ec:field-validator-rule name="regexp" bundle="${messages}" message="#{form.fields.regexp}">
									<ec:field-validator-param name="regexp" raw="true">/^\d+$/</ec:field-validator-param>
								</ec:field-validator-rule>
							</c:if>
							
							<c:if test="${propertyMetadata.valueType == 'DECIMAL' && empty propertyMetadata.regex}">
								<ec:field-validator-rule name="regexp" bundle="${messages}" message="#{form.fields.regexp}">
									<ec:field-validator-param name="regexp" raw="true">/^\d+\.\d+$/</ec:field-validator-param>
								</ec:field-validator-rule>
							</c:if>
							
						</c:if>

						<c:if test="${(propertyMetadata.valueType == 'TEXT') && (propertyMetadata.minLength > 0 || propertyMetadata.maxLength > 0)}">
							<ec:field-validator-rule name="stringLength" bundle="${messages}" message="#{form.fields.stringLength}">
								<c:if test="${propertyMetadata.min > 0}">
								<ec:field-validator-param name="min">${propertyMetadata.minLength}</ec:field-validator-param>
								</c:if>
								<c:if test="${propertyMetadata.max > 0}">
								<ec:field-validator-param name="max">${propertyMetadata.maxLength}</ec:field-validator-param>
								</c:if>
							</ec:field-validator-rule>
						</c:if>
						
					</c:if>

					<c:if test="${!empty propertyMetadata.regex}">
					<ec:field-validator-rule name="regexp" bundle="${messages}" message="#{form.fields.regexp}">
						<ec:field-validator-param name="regexp" raw="true">/${propertyMetadata.regex}/</ec:field-validator-param>
					</ec:field-validator-rule>
					</c:if>
					
				</ec:field-validator>
			</ed:col>
		</ed:row>
	</span>
</c:forEach>
