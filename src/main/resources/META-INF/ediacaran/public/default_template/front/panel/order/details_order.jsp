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
				<h2><fmt:message key="show_order.title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{show_order.title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{show_order.origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders"  bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><b><fmt:message key="show_order.title" bundle="${messages}"/></b> #${vars.order.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="show_order.date" bundle="${messages}"/>: ${vars.order.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<%--<b><fmt:message key="show_order.invoice_id" bundle="${messages}"/>:</b> #${vars.order.invoice.id}<br>--%>
				<b><fmt:message key="show_order.order_id" bundle="${messages}"/>:</b> #${vars.order.id}<br>
				<b><fmt:message key="show_order.payment_due" bundle="${messages}"/>:</b> ${vars.order.toStringDate(locale)}<br>
				<b><fmt:message key="show_order.account" bundle="${messages}"/>:</b> ${vars.order.owner}<br>
				<b><fmt:message key="show_order.status" bundle="${messages}"/>:</b> ${vars.order.status.getName(locale)}
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<ec:table>
					<ec:table-header>
						<ec:table-col><center><fmt:message key="show_order.table_product.serial" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.product" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.description" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.quantity" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.subtotal" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center>Discounts</center></ec:table-col>
						<ec:table-col><center>Taxes</center></ec:table-col>
						<ec:table-col><center>Total</center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.order.itens}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center>${product.product.description}</center></ec:table-col>
								<ec:table-col><center>${product.units}</center></ec:table-col>
								<ec:table-col><center>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.subtotal}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.discount}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.tax}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.total}"/></center></ec:table-col>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
			</ed:col>
		</ed:row>

		<ed:row>
			<ed:col size="5">
				<p>
					<fmt:message key="show_order.payment_due" bundle="${messages}"/>
					${vars.order.toStringDate(locale)}
				</p>
				
				<ec:description-list>
					<ec:description title="#{show_order.table_product.subtotal}" truncate="false" bundle="${messages}">
						${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.value}"/>
					</ec:description>
					<%--
					<ec:description title="#{show_order.table_product.discount}" truncate="false" bundle="${messages}">
						${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.discount}"/>
					</ec:description>
					--%>
					<c:forEach items="${vars.order.taxes}" var="tax">
					<ec:description title="${tax.name}" truncate="false">
						<c:if test="${tax.type == 'UNIT'}">${vars.order.payment.currency}</c:if>
						<c:if test="${tax.discount}">-</c:if>
						 <fmt:formatNumber pattern="###,###,##0.00" value="${tax.value}"/>
						<c:if test="${tax.type == 'PERCENTAGE'}"> %</c:if>
					</ec:description>
					</c:forEach>
					<%--
					<ec:description title="#{show_order.table_product.tax}" truncate="false" bundle="${messages}">
						${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.tax}"/>
					</ec:description>
					--%>
					<ec:description title="#{show_order.table_product.total}" truncate="false" bundle="${messages}">
						${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.total}"/>
					</ec:description>
				</ec:description-list>
			</ed:col>
			<ed:col size="7">
				<ec:tabs>
					<ec:tabs-item title="#{show_order.invoice_id}" bundle="${messages}" active="true" >
						<ec:table>
							<ec:table-header>
								<ec:table-col>#ID</ec:table-col>
								<ec:table-col>Date</ec:table-col>
								<ec:table-col>Subtotal</ec:table-col>
								<ec:table-col>Taxes</ec:table-col>
								<ec:table-col>Discounts</ec:table-col>
								<ec:table-col>Total</ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.order.invoice}" var="invoice">
								<ec:table-row>
									<ec:table-col><small>${invoice.id}</small></ec:table-col>
									<ec:table-col>${vars.order.toStringDate(locale)}</ec:table-col>
									<ec:table-col>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${invoice.subtotal}"/></ec:table-col>
									<ec:table-col>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${invoice.tax}"/></ec:table-col>
									<ec:table-col>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${invoice.discount}"/></ec:table-col>
									<ec:table-col>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${invoice.total}"/></ec:table-col>
								</ec:table-row>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<ec:tabs-item title="#{show_order.widgets.payment}" bundle="${messages}">
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
	
	</ec:box-body>
</ec:box>