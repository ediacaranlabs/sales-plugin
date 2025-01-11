<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>
<style>
.qty{
	width: 5em;
}
</style>
<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2><fmt:message key="title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Shipping" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{origin_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.shipping.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.shipping.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="invoiceForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/save" method="POST" update="shippingFormResult">
	<input type="hidden" name="order" value="${vars.shipping.order}">
	<input type="hidden" name="id" value="${vars.shipping.id}">
<ec:box>
	<ec:box-header><b><fmt:message key="shipping_id" bundle="${messages}"/></b> #${vars.shipping.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.shipping.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col size="4">
				<b><fmt:message key="shipping_code" bundle="${messages}"/>:</b> #${vars.shipping.id}<br>
				<b><fmt:message key="created_in" bundle="${messages}"/>:</b> ${vars.shipping.toStringDate(locale)}<br>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.shipping.order}<br>
			</ed:col>
			<ed:col size="4">
				<b><fmt:message key="origin_address.title" bundle="${messages}"/></b><p>
				${vars.shipping.origin.firstName} ${vars.shipping.origin.lastName}<br>
				${vars.shipping.origin.addressLine1}<br>
				${vars.shipping.origin.addressLine2}<br>
				${vars.shipping.origin.zip} ${vars.shipping.origin.city} ${vars.shipping.origin.region} ${vars.shipping.origin.country.name}
			</ed:col>
			<ed:col size="4">
				<b><fmt:message key="destination_address.title" bundle="${messages}"/></b><p>
				${vars.shipping.dest.firstName} ${vars.shipping.dest.lastName}<br>
				${vars.shipping.dest.addressLine1}<br>
				${vars.shipping.dest.addressLine2}<br>
				${vars.shipping.dest.zip} ${vars.shipping.dest.city} ${vars.shipping.dest.region} ${vars.shipping.dest.country.name}
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<ec:table>
					<ec:table-header>
						<ec:table-col><center><fmt:message key="product_table.id" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.quantity" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.product" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.description" bundle="${messages}"/></center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.shipping.products}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col classStyle="qty form-group has-feedback" >
									<c:if test="${!empty vars.shipping.id}">
										<ec:center>${product.units}</ec:center>
									</c:if>
									<c:if test="${empty vars.shipping.id}">
									<span formgroup="products" formgrouptype="index">
										<input type="hidden" name="serial" value="${product.serial}">
										<ec:textfield maxlength="2" name="units" value="${product.units}" enabled="${empty vars.shipping.id}">
											<ec:field-validator>
												<ec:field-validator-rule name="notEmpty" message="#{product_table.form.units.validation.notEmpty}" bundle="${messages}"/>
												<ec:field-validator-rule name="between" message="#{product_table.form.units.validation.between}" bundle="${messages}">
														<ec:field-validator-param name="min">1</ec:field-validator-param>
														<ec:field-validator-param name="max">${product.units}</ec:field-validator-param>
												</ec:field-validator-rule>
											</ec:field-validator>
										</ec:textfield>
									</span>
									</c:if>
								</ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center>${product.product.description}</center></ec:table-col>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col size="2">
			</ed:col>
			<ed:col size="10">
				<ec:tabs>
					<ec:tabs-item active="true" title="#{tabs.details.title}" bundle="${messages}">
						<ed:row style="form">
							<ed:col classStyle="form-group has-feedback">
								<ec:select label="#{tabs.details.form.shipping_method.label}" name="shippingType" enabled="${empty vars.shipping.id}" bundle="${messages}">
									<ec:field-validator>
										<ec:field-validator-rule name="notEmpty" message="#{tabs.details.form.shipping_method.validation.notEmpty}" bundle="${messages}"/>
									</ec:field-validator>
									<ec:option value=""></ec:option>
									<c:forEach items="${vars.shippingMethods}" var="shippingMethod">
										<ec:option value="${shippingMethod.id}" selected="${shippingMethod.id == vars.selectedShippingMethod.id}">${shippingMethod.name}</ec:option>
									</c:forEach>
									<ec:event type="change">
										var $source = $event.source;
										var $form = $source.getForm();
									
										var $shippingTypeField = $form.getField($source.getAttribute('name'));
										var $shippingType = $shippingTypeField.getValue();
										
										if($shippingType){
											$form.submit(
												false, 
												"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/shippingtype/select", 
												"shippingTypeArea"
											);
										}
									</ec:event>
								</ec:select>
							</ed:col>
						</ed:row>
						<ed:row style="form">
							<ed:col id="shippingTypeArea">
								<c:if test="${!empty vars.selectedShippingMethod.getView(vars.shipping)}">
									<ec:include uri="${vars.selectedShippingMethod.getView(vars.shipping)}" resolved="true" />
								</c:if>
							</ed:col>
						</ed:row>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.cancelation.title}" bundle="${messages}">
						<ed:row>
							<ed:col classStyle="form-group has-feedback">
								<ec:textarea id="justification_field" label="#{tabs.cancelation.form.justification}" 
									name="cancelJustification" rows="5" enabled="${!empty vars.shipping.id && empty vars.shipping.cancelDate}" bundle="${messages}">${vars.shipping.cancelJustification}</ec:textarea>
								<c:if test="${!empty vars.shipping.id && empty vars.shipping.cancelDate}">
									<ec:field-validator field="justification_field">
										<ec:field-validator-rule name="notEmpty" message="#{tabs.cancelation.form.justification.validation.notEmpty}" bundle="${messages}"/>
										<ec:field-validator-rule name="stringLength" message="#{tabs.cancelation.form.justification.validation.stringLength}" bundle="${messages}"	>
												<ec:field-validator-param name="min">6</ec:field-validator-param>
												<ec:field-validator-param name="max">255</ec:field-validator-param>
										</ec:field-validator-rule>
									</ec:field-validator>
								</c:if>
							</ed:col>
						</ed:row>
					</ec:tabs-item>
				</ec:tabs>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col id="shippingFormResult">
			</ed:col>
		</ed:row>
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{show_order.label}" actionType="button" style="light" 
			align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.shipping.order}');			
			</ec:event>
		</ec:button>
		<ec:button actionType="submit" label="#{cancel.label}" align="right" style="danger"
			bundle="${messages}" enabled="${!empty vars.shipping.id && empty vars.shipping.cancelDate}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/cancel"/>
		<ec:button actionType="submit" label="#{save.label}" align="right"  style="success"
			bundle="${messages}" enabled="${empty vars.shipping.id}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/save" />
	</ec:box-footer>
</ec:box>
</ec:form>
