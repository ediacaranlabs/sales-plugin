<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:box>
	<ec:box-header>
		<h3><fmt:message key="title" bundle="${messages}"/></h3>
	</ec:box-header>
	<ec:box-body>
		<ec:table>
			<ec:table-header>
				<ec:table-col>
					<ec:center><fmt:message key="table.product" bundle="${messages}"/></ec:center>
				</ec:table-col>
				<ec:table-col>
					<ec:center><fmt:message key="table.date" bundle="${messages}"/></ec:center>
				</ec:table-col>
				<ec:table-col>
					<ec:center><fmt:message key="table.actions" bundle="${messages}"/></ec:center>
				</ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach items="${vars.orders}" var="order">
					<c:forEach items="${order.itens}" var="product">
						<ec:table-row>
							<ec:table-col>
								<ec:center>${product.product.name}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center>${order.toStringDate(locale)}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<center>
									<small>
										<a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${order.id}">
											<fmt:message key="table.actions.details" bundle="${messages}"/>
										</a>
									</small>
								</center>						
							</ec:table-col>
						</ec:table-row>
					</c:forEach>
				</c:forEach>
			</ec:table-body>
		</ec:table>
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{more}" actionType="button" align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/');			
			</ec:event>
		</ec:button>
	</ec:box-footer>
</ec:box>