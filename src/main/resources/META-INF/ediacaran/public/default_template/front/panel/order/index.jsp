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
		<ec:data-table id="orderSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/search">
			<ed:row>
				<ed:col size="4">
		    		<ec:field-group>
		    			<ec:datefield name="startDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('orderSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$endDate.setProperty('min', $startDate.getValue());
		    				</ec:event>
		    			</ec:datefield>
		    			<ec:datefield name="endDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('orderSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$startDate.setProperty('max', $endDate.getValue());
		    				</ec:event>
		    			</ec:datefield>
		    		</ec:field-group>
				</ed:col>
				<ed:col size="8">
				</ed:col>
			</ed:row>
		</ec:data-table>
	
		<ec:table>
			<ec:table-header>
				<ec:table-col><ec:center><fmt:message key="show-orders.table.data" bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="show-orders.table.status" bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="show-orders.table.total" bundle="${messages}"/></ec:center></ec:table-col>
				<ec:table-col><ec:center><fmt:message key="show-orders.table.action" bundle="${messages}"/></ec:center></ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach var="order" items="${orders}">
					<ec:table-col><ec:center>${order.toStringDate(locale)}</ec:center></ec:table-col>
					<ec:table-col><ec:center>${order.status.getName(locale)}</ec:center></ec:table-col>
					<ec:table-col><ec:center>${order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${order.payment.total}"/></ec:center></ec:table-col>
					<ec:table-col>
						<ec:center>
						<ec:button label="" icon="search" style="info">
							<ec:event type="click">
								$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${order.id}');
							</ec:event>
						</ec:button>
						</ec:center>
					</ec:table-col>
				</c:forEach>
			</ec:table-body>
		</ec:table>
	</ec:box-body>
</ec:box>