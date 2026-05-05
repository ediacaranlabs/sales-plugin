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
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{origin_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/refunds"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.refund.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.refund.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="invoiceForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/refunds/save" method="POST" update="refundFormResult">
	<input type="hidden" name="order" value="${vars.refund.order}">
	<input type="hidden" name="id" value="${vars.refund.id}">
<ec:box>
	<ec:box-header><b><fmt:message key="refund_id" bundle="${messages}"/></b> #${vars.refund.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.refund.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col size="12">
				<b><fmt:message key="refund_code" bundle="${messages}"/>:</b> #${vars.refund.id}<br>
				<b><fmt:message key="created_in" bundle="${messages}"/>:</b> ${vars.refund.toStringDate(locale)}<br>
				<b><fmt:message key="refund_in" bundle="${messages}"/>:</b> ${vars.refund.toStringRefundDate(locale)}<br>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.refund.order}<br>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<ec:table>
					<ec:table-header>
						<ec:table-col><center><fmt:message key="product_table.id" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.quantity" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.product" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.subtotal" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.discount" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.tax" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.total" bundle="${messages}"/></center></ec:table-col>

					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.refund.products}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col classStyle="qty form-group has-feedback" >
									<c:if test="${!empty vars.refund.id}">
										<ec:center>${product.units}</ec:center>
									</c:if>
									<c:if test="${empty vars.refund.id}">
									<span formgroup="itens">
										<ec:textfield maxlength="2" name="${product.serial}" value="${product.units}" enabled="${empty vars.refund.id}">
											<ec:event type="keyup">
												var $form = $event.source.getForm();
												
												/*
												var $group = $event.source.getFormGroup();
												
												var $unitsField = $form.getField($group.getPath() + ".units" );
												var $serialField = $form.getField($group.getPath() + ".serial" );
												
												var $unitsValue = $unitsField.getValue();
												var $serialValue = $serialField.getValue();
												*/
												//refundEntity.itens[$serialValue] = $unitsValue;

												var $serialName = $event.source.getAttribute('name');
												var $serialOriginalName = $event.source.getAttribute('originalname');
												var $serialField = $form.getField($serialName);
												var $serialValue = $serialField.getValue();
												console.log($serialName + ":" + $serialValue);
												$.AppContext.utils.postJson(
													'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/refunds/recalc',
													$form.toObject(),
													function ($e){
													
														if(!$e.itens){
															return;
														}
														
														for (let $j = 0; $j < $e.itens.length; $j++) {
															var $i = $e.itens[$j];
														  
														  	if($i.serial ==  $serialOriginalName){
	
																$.AppContext.utils.getById($serialOriginalName + '_subtotal').setValue($i.subtotal);
																$.AppContext.utils.getById($serialOriginalName + '_discount').setValue($i.discounts);
																$.AppContext.utils.getById($serialOriginalName + '_tax').setValue($i.taxes);
																$.AppContext.utils.getById($serialOriginalName + '_total').setValue($i.total);
																
															}
														  
														}
														
														$.AppContext.utils.getById('subtotal').setValue($e.subtotal);
														$.AppContext.utils.getById('discounts').setValue($e.discounts);
														$.AppContext.utils.getById('taxes').setValue($e.taxes);
														$.AppContext.utils.getById('total').setValue($e.total);
														
														
													}
												);
												
											</ec:event>										
											<ec:field-validator>
												<ec:field-validator-rule name="notEmpty" message="#{product_table.form.units.validation.notEmpty}" bundle="${messages}"/>
												<ec:field-validator-rule name="between" message="#{product_table.form.units.validation.between}" bundle="${messages}">
														<ec:field-validator-param name="min">0</ec:field-validator-param>
														<ec:field-validator-param name="max">${product.units}</ec:field-validator-param>
												</ec:field-validator-rule>
											</ec:field-validator>
										</ec:textfield>
									</span>
									</c:if>
								</ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center id="${product.serial}_subtotal">${product.displaySubtotal}</center></ec:table-col>
								<ec:table-col><center id="${product.serial}_discount">${product.displayDiscount}</center></ec:table-col>
								<ec:table-col><center id="${product.serial}_tax">${product.displayTax}</center></ec:table-col>
								<ec:table-col><center id="${product.serial}_total">${product.displayTotal}</center></ec:table-col>
								<%--<ec:table-col><center>${product.product.shortDescription}</center></ec:table-col>--%>
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
				<p>
					<fmt:message key="created_in" bundle="${messages}"/>
					${vars.refund.toStringDate(locale)}
				</p>
				
				<ec:description-list>
					<ec:description title="#{table_product.subtotal}" truncate="false" bundle="${messages}">
						<span id="subtotal">${vars.refund.displaySubtotal}</span>
					</ec:description>
					<ec:description title="#{table_product.discount}" truncate="false" bundle="${messages}">
						<span id="discounts">${vars.refund.displayDiscount}</span>
					</ec:description>
					<ec:description title="#{table_product.tax}" truncate="false" bundle="${messages}">
						<span id="taxes">${vars.refund.displayTax}</span>
					</ec:description>
					<ec:description title="#{table_product.total}" truncate="false" bundle="${messages}">
						<span id="total">${vars.refund.displayTotal}</span>
					</ec:description>
				</ec:description-list>
			</ed:col>
		</ed:row>		
		<ed:row>
			<ed:col id="refundFormResult">
			</ed:col>
		</ed:row>
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{show_order.label}" actionType="button" style="light" 
			align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.refund.order}');			
			</ec:event>
		</ec:button>
		<ec:button actionType="submit" label="#{save.label}" align="right"  style="success"
			bundle="${messages}" enabled="${empty vars.refund.id}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/refunds/save" />
	</ec:box-footer>
</ec:box>
</ec:form>
