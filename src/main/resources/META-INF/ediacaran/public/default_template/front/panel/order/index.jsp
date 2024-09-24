<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>

<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2><fmt:message key="show-orders.title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{show-orders.title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><fmt:message key="show-orders.table.title" bundle="${messages}"/></ec:box-header>
	<ec:box-body>
		<ec:table>
			<ec:table-header>
				<ec:table-col><fmt:message key="show-orders.table.data" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show-orders.table.status" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show-orders.table.total" bundle="${messages}"/></ec:table-col>
				<ec:table-col><fmt:message key="show-orders.table.action" bundle="${messages}"/></ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach var="order" items="${orders}">
					<ec:table-col>${order.toStringDate(lcoale)}</ec:table-col>
					<ec:table-col>${order.status.friendlyName}</ec:table-col>
					<ec:table-col>${order.payment.currency} <fmt:formatNumber pattern="###,###,###.00" value="${order.payment.total}"/></ec:table-col>
					<ec:table-col>
						<a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${order.id.id}">
							<fmt:message key="show-orders.table.button_show" bundle="${messages}"/>
						</a>
					</ec:table-col>
				</c:forEach>
			</ec:table-body>
		</ec:table>
	</ec:box-body>
</ec:box>