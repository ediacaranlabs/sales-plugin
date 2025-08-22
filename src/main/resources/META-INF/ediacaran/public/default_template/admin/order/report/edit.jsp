<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>
<style>
.owner-message {
}
.cause {
	width: 35em;
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
				<ec:breadcrumb-path text="#{origin_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/report"  bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="orderReportForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/report/save" method="POST" update="orderReportFormResult">
	<input type="hidden" name="order" value="${vars.orderReport.order.id}">
	<input type="hidden" name="id" value="${vars.orderReport.id}">
</ec:form>

<ec:form id="sendMessageOrderReport" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/sendMessage" method="POST" update="orderReportFormResult">
	<input type="hidden" name="orderReport" value="${vars.orderReport.id}">
</ec:form>

<ec:box>
	<ec:box-header><b><fmt:message key="order_report_id" bundle="${messages}"/></b> #${vars.orderReport.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row>
			<ed:col>
				<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.orderReport.toStringDate(locale)}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col size="4">
				<b><fmt:message key="report_code" bundle="${messages}"/>:</b> #${vars.orderReport.id}<br>
				<b><fmt:message key="created_in" bundle="${messages}"/>:</b> ${vars.orderReport.toStringDate(locale)}<br>
				<b><fmt:message key="status" bundle="${messages}"/>:</b> ${vars.orderReport.status.getName(locale)}<br>
				<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.orderReport.order.id}<br>
			</ed:col>
			<ed:col size="4">
			</ed:col>
			<ed:col size="4">
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<ec:table>
					<ec:table-header>
						<ec:table-col><center><fmt:message key="product_table.id" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.product" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="product_table.cause" bundle="${messages}"/></center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.orderReport.products}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col classStyle="cause form-group has-feedback" >
									<c:if test="${!empty vars.orderReport.id}">
										<ec:center>${product.cause.getName(locale)}</ec:center>
									</c:if>
									<c:if test="${empty vars.orderReport.id}">
										<span formgroup="products" formgrouptype="index">
											<input type="hidden" form="orderReportForm" name="serial" value="${product.serial}">
											<ec:select name="cause" form="orderReportForm">
												<ec:option value=""><fmt:message key="product_table.form.cause.empty_value" bundle="${messages}"/></ec:option>
												<c:forEach items="${vars.causeList}" var="cause">
													<ec:option value="${cause}">${cause.getName(locale)}</ec:option>
												</c:forEach>
												<%--
												<ec:field-validator>
													<ec:field-validator-rule name="notEmpty" message="#{product_table.form.cause.validation.notEmpty}" bundle="${messages}"/>
												</ec:field-validator>
												--%>
											</ec:select>
										</span>
									</c:if>
								</ec:table-col>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
			</ed:col>
			<ed:col size="4">
				<ec:box>
					<ec:box-header>Messages</ec:box-header>
					<ec:box-body>
						<ec:data-table id="orderSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/messages">
							<input type="hidden" name="id" value="${vars.orderReport.id}">
							<ec:data-result var="response">
								<ec:forEach items="!{response.itens}" var="item">
									<ed:row style="form" classStyle="!{item.client? 'owner-message' : ''}">
										<ed:col><b>!{item.date}</b><br><small>!{item.userName}</small></ed:col>
									</ed:row>
									<ed:row style="form" classStyle="!{item.client? 'owner-message' : ''}">
										<ed:col size="1"></ed:col>
										<ed:col>!{item.message}</ed:col>
									</ed:row>
									<hr>
								</ec:forEach>
								<ed:row>
									<ed:col classStyle="qty form-group has-feedback">
										<ec:textarea maxlength="128" id="messageField" rows="2" name="message" form="sendMessageOrderReport"></ec:textarea>
										<ec:field-validator form="sendMessageOrderReport" field="messageField">
											<ec:field-validator-rule name="notEmpty" message="#{product_table.form.message.validation.notEmpty}" bundle="${messages}"/>
											<%--
											<ec:field-validator-rule name="between" message="#{product_table.form.message.validation.between}" bundle="${messages}">
													<ec:field-validator-param name="min">0</ec:field-validator-param>
													<ec:field-validator-param name="max">128</ec:field-validator-param>
											</ec:field-validator-rule>
											--%>
										</ec:field-validator>
									</ed:col>
								</ed:row>
								<%--
								<ed:row>
									<ed:col>
										<ec:button label="#{product_table.form.button_message.label}" form="sendMessageOrderReport" bundle="${messages}" actionType="submit" style="info" align="right"/>
									</ed:col>
								</ed:row>
								 --%>
							</ec:data-result>
						</ec:data-table>
					</ec:box-body>
				</ec:box>
			</ed:col>
		</ed:row>
		<script type="text/javascript">
			$.AppContext.onload(function(){
				var $fr = $.AppContext.utils.getById('orderSearchForm');
				$fr.submit();
			});
		</script>
		<ed:row>
			<ed:col id="orderReportFormResult">
			</ed:col>
		</ed:row>
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{show_order.label}" actionType="button" style="light" 
			align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/show/${vars.orderReport.order.id}');			
			</ec:event>
		</ec:button>
		<c:if test="${empty vars.orderReport.id}">
			<ec:button actionType="submit" label="#{save.label}" align="right" style="success"
				bundle="${messages}" enabled="${empty vars.orderReport.id}" form="orderReportForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/report/save" />
		</c:if>
		<c:if test="${!empty vars.orderReport.id}">
			<ec:button label="#{product_table.form.button_message.label}" form="sendMessageOrderReport" style="success" bundle="${messages}" actionType="submit" align="right"/>
		</c:if>		
	</ec:box-footer>
</ec:box>
