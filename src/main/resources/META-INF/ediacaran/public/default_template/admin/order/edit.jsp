<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>

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
				<ec:breadcrumb-path text="#{origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders"  bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><b><fmt:message key="title" bundle="${messages}"/></b> #${vars.order.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.order.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<%--<b><fmt:message key="invoice_id" bundle="${messages}"/>:</b> #${vars.order.invoice.id}<br>--%>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.order.id}<br>
				<b><fmt:message key="payment_due" bundle="${messages}"/>:</b> ${vars.order.toStringDate(locale)}<br>
				<b><fmt:message key="account" bundle="${messages}"/>:</b> ${vars.order.client}<br>
				<b><fmt:message key="status" bundle="${messages}"/>:</b> ${vars.order.status.getName(locale)}
			</ed:col>
			<ed:col>
				<b><fmt:message key="billing_address.title" bundle="${messages}"/></b><p>
				${vars.order.billingAddress.firstName} ${vars.order.billingAddress.lastName}<br>
				${vars.order.billingAddress.addressLine1}<br>
				${vars.order.billingAddress.addressLine2}<br>
				${vars.order.billingAddress.zip} ${vars.order.billingAddress.city} ${vars.order.billingAddress.region} ${vars.order.billingAddress.country.name}
			</ed:col>
			<ed:col>
				<b><fmt:message key="shipping_address.title" bundle="${messages}"/></b><p>
				${vars.order.shippingAddress.firstName} ${vars.order.shippingAddress.lastName}<br>
				${vars.order.shippingAddress.addressLine1}<br>
				${vars.order.shippingAddress.addressLine2}<br>
				${vars.order.shippingAddress.zip} ${vars.order.shippingAddress.city} ${vars.order.shippingAddress.region} ${vars.order.shippingAddress.country.name}
			</ed:col>
		</ed:row>
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
						<c:forEach items="${vars.order.itens}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center>${product.product.shortDescription}</center></ec:table-col>
								<ec:table-col><center>${product.units}</center></ec:table-col>
								<ec:table-col><center>${product.displaySubtotal}</center></ec:table-col>
								<ec:table-col><center>${product.displayDiscount}</center></ec:table-col>
								<ec:table-col><center>${product.displayTax}</center></ec:table-col>
								<ec:table-col><center>${product.displayTotal}</center></ec:table-col>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
			</ed:col>
		</ed:row>

		<ed:row>
			<ed:col size="2">
				<p>
					<fmt:message key="payment_due" bundle="${messages}"/>
					${vars.order.toStringDate(locale)}
				</p>
			</ed:col>
			<ed:col size="10">
				<ec:tabs>
					<ec:tabs-item title="#{tabs.totals.title}" bundle="${messages}" active="true">
						<ec:description-list>
							<ec:description title="#{table_product.subtotal}" truncate="false" bundle="${messages}">
								${vars.order.payment.displayTotal}
							</ec:description>
							<%--
							<ec:description title="#{table_product.discount}" truncate="false" bundle="${messages}">
								${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.discount}"/>
							</ec:description>
							--%>
							<c:forEach items="${vars.order.taxes}" var="tax">
							<ec:description title="${tax.name}" truncate="false">
								<c:if test="${tax.discount}">-</c:if>
								<c:if test="${tax.type == 'UNIT'}">${tax.displayValue}</c:if>
								<c:if test="${tax.type == 'PERCENTAGE'}"><fmt:formatNumber pattern="###,###,##0.00" value="${tax.value}"/> %</c:if>
							</ec:description>
							</c:forEach>
							<%--
							<ec:description title="#{table_product.tax}" truncate="false" bundle="${messages}">
								${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.tax}"/>
							</ec:description>
							--%>
							<ec:description title="#{table_product.total}" truncate="false" bundle="${messages}">
								${vars.order.payment.displayTotal}
							</ec:description>
						</ec:description-list>
					</ec:tabs-item>
					<ec:tabs-item title="#{widgets.payment}" bundle="${messages}">
						<span id="payment_type_tab">
					    <c:if test="${!empty vars['payment_view']}">
							<script type="text/javascript">
								$.AppContext.onload(function(){			
									$.AppContext.utils
										.updateContentByID(
												"#!${vars['payment_view']}", 
												"payment_type_tab"
										);
								});	
							</script>
					    </c:if>
						</span>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.invoices.title}" bundle="${messages}">
						<ec:table>
							<ec:table-header>
								<ec:table-col><center><small><fmt:message key="table_invoice.id" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.cancel_date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.subtotal" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.tax" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.discount" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.total" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.actions" bundle="${messages}"/></small></center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.invoices}" var="invoice">
								<ec:table-row style="${invoice.cancelDate != null? 'danger' : ''}">
									<ec:table-col><center><small>${invoice.id}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.toStringDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.toStringCancelDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displaySubtotal}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayTax}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayDiscount}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayTotal}</small></center></ec:table-col>
									<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices/show/${invoice.id}">
										<fmt:message key="table_invoice.actions.details" bundle="${messages}"/>
									</a></small></center></ec:table-col>
								</ec:table-row>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.shipping.title}" bundle="${messages}">
					
						<ec:table>
							<ec:table-header>
								<ec:table-col><center><small><fmt:message key="table_shipping.id" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.cancel_date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.type" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><small><fmt:message key="table_shipping.dest" bundle="${messages}"/></small></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.actions" bundle="${messages}"/></small></center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.shippings}" var="shipping">
								<ec:table-row style="${shipping.cancelDate != null? 'danger' : ''}">
									<ec:table-col><center><small>${shipping.id}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.toStringDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.toStringCancelDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.shippingType}</small></center></ec:table-col>
									<ec:table-col>
										<small>
											${shipping.dest.firstName} ${vars.shipping.dest.lastName}<br>
											${shipping.dest.addressLine1}<br>
											${shipping.dest.addressLine2}<br>
											${shipping.dest.zip} ${shipping.dest.city} ${shipping.dest.region} ${shipping.dest.country.name}
										</small>
									</ec:table-col>
									<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/edit/${shipping.id}">
										<fmt:message key="table_shipping.actions.details" bundle="${messages}"/>
									</a></small></center></ec:table-col>
								</ec:table-row>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<c:forEach items="${vars.widgets}" var="widget">
						<ec:tabs-item title="${widget.title}" >
							<span id="${widget.id}_tab">
								<script type="text/javascript">
									$.AppContext.onload(function(){			
										$.AppContext.utils
											.updateContentByID(
													"#!${widget.resource}", 
													"${widget.id}_tab"
											);
									});	
								</script>
							</span>
						</ec:tabs-item>
					</c:forEach>
				</ec:tabs>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col id="order_result">
			</ed:col>
		</ed:row>
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{search.label}" align="right" style="light" actionType="button" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders');
			</ec:event>
		</ec:button>
		<ec:button label="#{shipping.label}" align="right" style="light" enabled="${vars.order.completeInvoice != null && vars.order.completeShipping == null}" actionType="button" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/shippings/new/${vars.order.id}');
			</ec:event>
		</ec:button>
		<ec:button label="#{create_invoice.label}" align="right" style="light" enabled="${vars.order.payment.receivedFrom != null && vars.order.completeInvoice == null}" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices/new/${vars.order.id}');
			</ec:event>
		</ec:button>
		<ec:button label="#{submit_payment.label}" icon="credit-card" enabled="${vars.order.payment.receivedFrom == null}" style="success" align="right" actionType="button" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContentByID('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/payment/${vars.order.id}', "order_result");
			</ec:event>
		</ec:button>
	</ec:box-footer>
</ec:box>