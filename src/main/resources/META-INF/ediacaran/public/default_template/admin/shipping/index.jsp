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
				<h2><fmt:message key="title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-header><fmt:message key="table.title" bundle="${messages}"/></ec:box-header>
	<ec:box-body>
		<ec:data-table id="invoiceSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices/search">
			<ed:row>
				<ed:col size="1">
	    			<ec:textfield name="id" label="#{form.id.label}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="2">
	    			<ec:textfield name="order" label="#{form.order.label}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="3">
	    			<ec:textfield name="ownerName" label="#{form.client.label}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="1">
					<ec:label><fmt:message key="form.canceled.label" bundle="${messages}"/></ec:label>
	    			<ec:radio label="#{form.canceled.yes}" value="true" name="canceled" bundle="${messages}"/>
	    			<ec:radio label="#{form.canceled.no}" value="false" name="canceled" selected="true" bundle="${messages}"/>
				</ed:col>
				<ed:col size="2">
	    			<ec:datefield label="#{form.from.label}" name="startDate" bundle="${messages}">
	    				<ec:event type="change">
							var $form = $.AppContext.utils.getById('invoiceSearchForm');
							var $startDate = $form.getField('startDate');
							var $endDate = $form.getField('endDate');
							$endDate.setProperty('min', $startDate.getValue());
	    				</ec:event>
	    			</ec:datefield>
	    			<ec:datefield label="#{form.to.label}" name="endDate" bundle="${messages}">
	    				<ec:event type="change">
							var $form = $.AppContext.utils.getById('invoiceSearchForm');
							var $startDate = $form.getField('startDate');
							var $endDate = $form.getField('endDate');
							$startDate.setProperty('max', $endDate.getValue());
	    				</ec:event>
	    			</ec:datefield>
				</ed:col>
				<ed:col size="2">
	    			<ec:textfield label="#{form.mintotal.label}" name="minTotal" bundle="${messages}"/>
	    			<ec:textfield label="#{form.maxtotal.label}" name="maxTotal" bundle="${messages}"/>
				</ed:col>
				<ed:col size="1">
					<ec:button icon="search" style="info" actionType="submit" align="right"/>
				</ed:col>
			</ed:row>

			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center><fmt:message key="table.id" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.order" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.data" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.subtotal" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.taxes" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.discounts" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.total" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="table.action" bundle="${messages}"/></ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.data}" var="item">
						<ec:table-row>
							<ec:table-col><ec:center><small>!{item.id}</small></ec:center></ec:table-col>
							<ec:table-col><ec:center><small>!{item.order}</small></ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.date}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.subTotal}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.taxes}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.discounts}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.total}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button id="!{item.id}_button" icon="search" style="info" actionType="button">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/invoices/show/!{item.id}');
									</ec:event>
								</ec:button>
								</ec:center>
							</ec:table-col>
						</ec:table-row>
						</ec:forEach>
					</ec:table-body>
				</ec:table>
			</ec:data-result>
		</ec:data-table>
		
	</ec:box-body>
</ec:box>