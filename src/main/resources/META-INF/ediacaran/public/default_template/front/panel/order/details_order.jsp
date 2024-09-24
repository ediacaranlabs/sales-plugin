<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setTemplatePackage name="admin"/>
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
				<ec:breadcrumb-path text="#{show_order.origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ed:row>
	<ed:col>
		<h2><fmt:message key="show_order.date" bundle="${messages}"/>: ${vars.order.toStringDate(lcoale)}</h2>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<b><fmt:message key="show_order.invoice_id" bundle="${messages}"/> #${vars.order.invoice.id}</b><br>
		<br> 
		<b><fmt:message key="show_order.order_id" bundle="${messages}"/>:</b> #${vars.order.id.id}<br>
		<b><fmt:message key="show_order.payment_due" bundle="${messages}"/>:</b> ${vars.order.toStringDate(lcoale)}<br>
		<b><fmt:message key="show_order.account" bundle="${messages}"/>:</b> ${vars.order.id.systemUserId}<br>
		<b><fmt:message key="show_order.status" bundle="${messages}"/>:</b> ${vars.order.status.friendlyName}
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:table>
			<ec:table-header>
				<ec:table-col><fmt:message key="show_order.table_product.quantity" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show_order.table_product.product" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show_order.table_product.serial" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show_order.table_product.description" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show_order.table_product.subtotal" bundle="${messages}"/></ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach items="${vars.order.itens}" var="product">
					<ec:table-row>
						<ec:table-col>${product.units}</ec:table-col>
						<ec:table-col>${product.product.name}</ec:table-col>
						<ec:table-col>${product.serial}</ec:table-col>
						<ec:table-col>${product.product.description}</ec:table-col>
						<ec:table-col>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.subtotal}"/></ec:table-col>
					</ec:table-row>
				</c:forEach>
			</ec:table-body>
		</ec:table>
	</ed:col>
</ed:row>

<ed:row>
	<ed:col size="6">
		    <!-- payment method page -->
		    <c:if test="${!empty vars['payment_view']}">
				<jsp:include page="${vars['payment_view']}"/>
			</c:if>
		    <!-- /payment method page -->
	</ed:col>
	<ed:col size="6">
		<p>
			<fmt:message key="show_order.payment_due" bundle="${messages}"/>
			${vars.order.toStringDate(lcoale)}
		</p>
		
		<ec:description-list>
			<ec:description title="#{show_order.table_product.subtotal}" bundle="${messages}">
				${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.value}"/>
			</ec:description>
			<ec:description title="#{show_order.table_product.discount}" bundle="${messages}">
				${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.discount}"/>
			</ec:description>
			<ec:description title="#{show_order.table_product.tax}" bundle="${messages}">
				${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.tax}"/>
			</ec:description>
			<ec:description title="#{show_order.table_product.shipping}" bundle="${messages}">
				${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.shipping.value}"/>
			</ec:description>
			<ec:description title="#{show_order.table_product.total}" bundle="${messages}">
				${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.total}"/>
			</ec:description>
		</ec:description-list>
	</ed:col>
</ed:row>
