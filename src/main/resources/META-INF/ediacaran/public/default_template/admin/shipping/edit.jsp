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
				<h2>Shipping</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Shipping" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="Shippings" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.shipping.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.shipping.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="invoiceForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/save" method="POST" update="shippingFormResult">
	<input type="hidden" name="order" value="${vars.shipping.order}">
<ec:box>
	<ec:box-header><b>Shipping</b> #${vars.shipping.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3>Date: ${vars.shipping.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col size="4">
				<b>Shipping:</b> #${vars.shipping.id}<br>
				<b>Created in:</b> ${vars.shipping.toStringDate(locale)}<br>
				<b>Order:</b> #${vars.shipping.order}<br>
			</ed:col>
			<ed:col size="4">
				<b>Origin address</b><p>
				${vars.shipping.origin.firstName} ${vars.shipping.origin.lastName}<br>
				${vars.shipping.origin.addressLine1}<br>
				${vars.shipping.origin.addressLine2}<br>
				${vars.shipping.origin.zip} ${vars.shipping.origin.city} ${vars.shipping.origin.region} ${vars.shipping.origin.country.name}
			</ed:col>
			<ed:col size="4">
				<b>Destination address</b><p>
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
						<ec:table-col><center>ID</center></ec:table-col>
						<ec:table-col><center>Qty</center></ec:table-col>
						<ec:table-col><center>Product</center></ec:table-col>
						<ec:table-col><center>description</center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.shipping.products}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col classStyle="qty form-group has-feedback" >
									<span formgroup="products" formgrouptype="index">
										<input type="hidden" name="serial" value="${product.serial}">
										<ec:textfield maxlength="2" name="units" value="${product.units}" enabled="${empty vars.shipping.id}">
											<ec:field-validator>
												<ec:field-validator-rule name="notEmpty" message="Must be informed"/>
												<ec:field-validator-rule name="between" message="Must be between 1 to ${product.units}">
														<ec:field-validator-param name="min">1</ec:field-validator-param>
														<ec:field-validator-param name="max">${product.units}</ec:field-validator-param>
												</ec:field-validator-rule>
											</ec:field-validator>
										</ec:textfield>
									</span>
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
			<ed:col size="5">
			</ed:col>
			<ed:col size="7">
				<ec:tabs>
					<ec:tabs-item active="true" title="Details">
						<ed:row>
							<ed:col classStyle="form-group has-feedback">
								<ec:select label="Shipping method" name="shippingType" enabled="${empty vars.shipping.id}">
									<ec:field-validator>
										<ec:field-validator-rule name="notEmpty" message="Must be informed"/>
									</ec:field-validator>
									<ec:option value=""></ec:option>
									<c:forEach items="${vars.shippingMethods}" var="shippingMethod">
										<ec:option value="${shippingMethod.id}" selected="${shippingMethod.id == selectedShippingMethod.id}">${shippingMethod.name}</ec:option>
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
						<ed:row>
							<ed:col id="shippingTypeArea">
								<c:if test="${!empty selectedShippingMethod.getView(vars.shipping)}">
									<ec:include uri="${selectedShippingMethod.getView(vars.shipping)}" />
								</c:if>
							</ed:col>
						</ed:row>
					</ec:tabs-item>
					<ec:tabs-item title="Cancelation">
						<ec:textarea id="justification_field" label="Justification" name="justification" rows="5" enabled="${!empty vars.shipping.id && empty vars.shipping.cancelDate}">${vars.shipping.cancelJustification}</ec:textarea>
						<c:if test="${!empty vars.shipping.id && empty vars.shipping.cancelDate}">
							<ec:field-validator field="justification_field">
								<ec:field-validator-rule name="notEmpty" message="Must be informed"/>
								<ec:field-validator-rule name="stringLength" message="0 to 255">
										<ec:field-validator-param name="min">0</ec:field-validator-param>
										<ec:field-validator-param name="max">255</ec:field-validator-param>
								</ec:field-validator-rule>
							</ec:field-validator>
						</c:if>
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
		<ec:button label="Show order" actionType="button" align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.shipping.order}');			
			</ec:event>
		</ec:button>
		<ec:button actionType="submit" label="Save" align="right" bundle="${messages}" enabled="${empty vars.shipping.id}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/save" />
		<ec:button actionType="submit" label="Cancel" align="right" bundle="${messages}" enabled="${!empty vars.shipping.id && empty vars.shipping.cancelDate}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/cancel"/>
	</ec:box-footer>
</ec:box>
</ec:form>
