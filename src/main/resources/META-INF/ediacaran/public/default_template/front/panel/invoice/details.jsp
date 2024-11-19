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
				<h2>Invoice</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Invoice" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{show_order.origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.invoice.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${vars.invoice.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><b>Invoice</b> #${vars.invoice.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="show_order.date" bundle="${messages}"/>: ${vars.invoice.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<%--<b><fmt:message key="show_order.invoice_id" bundle="${messages}"/>:</b> #${vars.order.invoice.id}<br>--%>
				<b><fmt:message key="show_order.order_id" bundle="${messages}"/>:</b> #${vars.invoice.id}<br>
				<b><fmt:message key="show_order.payment_due" bundle="${messages}"/>:</b> ${vars.invoice.toStringDate(locale)}<br>
				<b><fmt:message key="show_order.account" bundle="${messages}"/>:</b> ${vars.invoice.owner}<br>
				<b>Order:</b> #${vars.invoice.order}<br>
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
						<ec:table-col><center><fmt:message key="show_order.table_product.discount" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.tax" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="show_order.table_product.total" bundle="${messages}"/></center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.invoice.itens}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center>${product.product.description}</center></ec:table-col>
								<ec:table-col><center>${product.units}</center></ec:table-col>
								<ec:table-col><center>${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.subtotal}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.discount}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.tax}"/></center></ec:table-col>
								<ec:table-col><center>${product.currency} <br> <fmt:formatNumber pattern="###,###,##0.00"  value="${product.total}"/></center></ec:table-col>
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
					<fmt:message key="show_order.payment_due" bundle="${messages}"/>
					${vars.invoice.toStringDate(locale)}
				</p>
				
				<ec:description-list>
					<ec:description title="#{show_order.table_product.subtotal}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.subtotal}"/>
					</ec:description>
					<ec:description title="#{show_order.table_product.discount}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.discount}"/>
					</ec:description>
					<ec:description title="#{show_order.table_product.tax}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.tax}"/>
					</ec:description>
					<ec:description title="#{show_order.table_product.total}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.total}"/>
					</ec:description>
				</ec:description-list>
			</ed:col>
		</ed:row>
	
	</ec:box-body>
</ec:box>