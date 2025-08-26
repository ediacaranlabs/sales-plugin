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
				<ec:breadcrumb-path text="#{origin_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders"  bundle="${messages}"/>
				<ec:breadcrumb-path text="#${vars.shipping.order}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${vars.shipping.order}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="shippingForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/shippings/confirm" method="POST" update="shippingFormResult">
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
				<b><fmt:message key="canceled_in" bundle="${messages}"/>:</b> ${vars.shipping.toStringCancelDate(locale)}<br>
				<b><fmt:message key="received_in" bundle="${messages}"/>:</b> ${vars.shipping.toStringReceivedDate(locale)}<br>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.shipping.order}<br>
				<b><fmt:message key="closed" bundle="${messages}"/>:</b>
				<c:if test="${vars.shipping.closed}">
					<fmt:message key="closed" bundle="${messages}"/><br>
				</c:if> 
				<c:if test="${!vars.shipping.closed}">
					<fmt:message key="not_closed" bundle="${messages}"/><br>
				</c:if> 
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
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.shipping.products}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><ec:center>${product.units}</ec:center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
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
							<ed:col size="12">
								<ec:textfield label="#{tabs.details.form.shipping_method.label}" value="${vars.selectedShippingMethod.name}" enabled="false" bundle="${messages}"></ec:textfield>
							</ed:col>
						</ed:row>
						<ed:row style="form">
							<ed:col>
								<c:if test="${!empty vars.selectedShippingMethod.getOwnerView(vars.shipping)}">
									<ec:include uri="${vars.selectedShippingMethod.getOwnerView(vars.shipping)}" resolved="true" />
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
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${vars.shipping.order}');			
			</ec:event>
		</ec:button>
		<ec:button label="#{confirm.label}" actionType="submit" style="light" align="right" bundle="${messages}"/>
	</ec:box-footer>
</ec:box>
</ec:form>
