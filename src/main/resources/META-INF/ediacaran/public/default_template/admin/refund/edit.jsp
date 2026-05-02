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
				<b><fmt:message key="refund_code" bundle="${messages}"/>:</b> #${vars.refudn.id}<br>
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
						<%--<ec:table-col><center><fmt:message key="product_table.description" bundle="${messages}"/></center></ec:table-col>--%>
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
									<span formgroup="products" formgrouptype="index">
										<input type="hidden" name="serial" value="${product.serial}">
										<ec:textfield maxlength="2" name="units" value="${product.units}" enabled="${empty vars.refund.id}">
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
								<%--<ec:table-col><center>${product.product.shortDescription}</center></ec:table-col>--%>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
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
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.shipping.order}');			
			</ec:event>
		</ec:button>
		<ec:button actionType="submit" label="#{save.label}" align="right"  style="success"
			bundle="${messages}" enabled="${empty vars.refund.id}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/refunds/save" />
	</ec:box-footer>
</ec:box>
</ec:form>
