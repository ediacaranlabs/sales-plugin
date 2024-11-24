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
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.invoice.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.invoice.order}"/>
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
					<fmt:message key="created_in" bundle="${messages}"/>
					${vars.invoice.toStringDate(locale)}
				</p>
				
				<ec:description-list>
					<ec:description title="#{table_product.subtotal}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.subtotal}"/>
					</ec:description>
					<ec:description title="#{table_product.discount}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.discount}"/>
					</ec:description>
					<ec:description title="#{table_product.tax}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.tax}"/>
					</ec:description>
					<ec:description title="#{table_product.total}" truncate="false" bundle="${messages}">
						${vars.invoice.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.invoice.total}"/>
					</ec:description>
				</ec:description-list>
			</ed:col>
		</ed:row>
	
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="Back" align="right">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.invoice.order}');			
			</ec:event>
		</ec:button>
	</ec:box-footer>
</ec:box>