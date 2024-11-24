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
				<h2>Invoice</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.invoice.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${vars.invoice.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><b><fmt:message key="invoice_id" bundle="${messages}"/></b> #${vars.invoice.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.invoice.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<b><fmt:message key="invoice_id" bundle="${messages}"/>:</b> #${vars.invoice.id}<br>
				<b><fmt:message key="created_in" bundle="${messages}"/>:</b> ${vars.invoice.toStringDate(locale)}<br>
				<b><fmt:message key="account" bundle="${messages}"/>:</b> ${vars.invoice.owner}<br>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.invoice.order}<br>
			</ed:col>
		</ed:row>
		<script type="text/javascript">
			var invoiceEntity = {};
			invoiceEntity.itens = {};
			invoiceEntity.order = '${vars.invoice.order}';
		</script>
		<ec:form id="invoiceForm">
			<ed:row>
				<ed:col>
					<ec:table>
						<ec:table-header>
							<ec:table-col><center><fmt:message key="table_product.serial" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.product" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.description" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.quantity" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.subtotal" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.discount" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.tax" bundle="${messages}"/></center></ec:table-col>
							<ec:table-col><center><fmt:message key="table_product.total" bundle="${messages}"/></center></ec:table-col>
						</ec:table-header>
						<ec:table-body>
							<c:forEach items="${vars.invoice.itens}" var="product">
								<script type="text/javascript">
									invoiceEntity.itens['${product.serial}'] = '${product.units}';
								</script>
								<ec:table-row>
									<ec:table-col><center>${product.serial}</center></ec:table-col>
									<ec:table-col><center>${product.product.name}</center></ec:table-col>
									<ec:table-col><center>${product.product.description}</center></ec:table-col>
									<ec:table-col classStyle="qty form-group has-feedback" >
										<ec:textfield maxlength="2" name="${product.serial}" value="${product.units}">
											<ec:event type="keyup">
												var $form = $.AppContext.utils.getById('invoiceForm');
												var $field = $form.getField('${product.serial}');
												
												invoiceEntity.itens['${product.serial}'] = $field.getValue();
												
												var $i = parseFloat(invoiceEntity.itens['${product.serial}']);
												var $max = parseFloat('${product.units}');
												
												if(isNaN($i) || isNaN($max) || $i < 0 || $i > $max){
													return;
												}
												
												$.AppContext.utils.postJson(
													'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices/recalc_invoice',
													invoiceEntity,
													function ($e){
													
														if(!$e.itens){
															return;
														}
														
														for (let $j = 0; $j < $e.itens.length; $j++) {
															var $i = $e.itens[$j];
														  
														  	if($i.serial == '${product.serial}'){

																$.AppContext.utils.getById('${product.serial}_subtotal').setValue($i.currency + '<br>' + (Math.round($i.subtotal * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('${product.serial}_discount').setValue($i.currency + '<br>' + (Math.round($i.discounts * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('${product.serial}_tax').setValue($i.currency + '<br>' + (Math.round($i.taxes * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('${product.serial}_total').setValue($i.currency + '<br>' + (Math.round($i.total * 100) / 100).toFixed(2) );
																
																$.AppContext.utils.getById('subtotal').setValue($i.currency + ' ' + (Math.round($e.subtotal * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('discounts').setValue($i.currency + ' ' + (Math.round($e.discounts * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('taxes').setValue($i.currency + ' ' + (Math.round($e.taxes * 100) / 100).toFixed(2) );
																$.AppContext.utils.getById('total').setValue($i.currency + ' ' + (Math.round($e.total * 100) / 100).toFixed(2) );
															}
														  
														}
														
													}
												);
												
											</ec:event>
											<ec:field-validator>
												<ec:field-validator-rule name="notEmpty" message="Must be informed"/>
												<ec:field-validator-rule name="between" message="Must be between 0 to ${product.units}">
														<ec:field-validator-param name="min">0</ec:field-validator-param>
														<ec:field-validator-param name="max">${product.units}</ec:field-validator-param>
												</ec:field-validator-rule>
											</ec:field-validator>
										</ec:textfield>
									</ec:table-col>
									<ec:table-col><center id="${product.serial}_subtotal">${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.subtotal}"/></center></ec:table-col>
									<ec:table-col><center id="${product.serial}_discount">${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.discount}"/></center></ec:table-col>
									<ec:table-col><center id="${product.serial}_tax">${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.tax}"/></center></ec:table-col>
									<ec:table-col><center id="${product.serial}_total">${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.total}"/></center></ec:table-col>
								</ec:table-row>
							</c:forEach>
						</ec:table-body>
					</ec:table>
				</ed:col>
			</ed:row>
		</ec:form>
		<ed:row>
			<ed:col size="5">
			</ed:col>
			<ed:col size="7">
				<p>
					<fmt:message key="created_in" bundle="${messages}"/>
					${vars.invoice.toStringDate(locale)}
				</p>
				
				<ec:description-list>
					<ec:description title="#{table_product.subtotal}" truncate="false" bundle="${messages}">
						<span id="subtotal">${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.subtotal}"/></span>
					</ec:description>
					<ec:description title="#{table_product.discount}" truncate="false" bundle="${messages}">
						<span id="discounts">${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.discount}"/></span>
					</ec:description>
					<ec:description title="#{table_product.tax}" truncate="false" bundle="${messages}">
						<span id="taxes">${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.tax}"/></span>
					</ec:description>
					<ec:description title="#{table_product.total}" truncate="false" bundle="${messages}">
						<span id="total">${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.total}"/></span>
					</ec:description>
				</ec:description-list>
			</ed:col>
		</ed:row>
	
	</ec:box-body>
</ec:box>