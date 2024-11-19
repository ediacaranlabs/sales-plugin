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
		<ec:data-table id="invoiceSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices/search">
			<ed:row>
				<ed:col size="2">
	    			<ec:textfield name="id" placeholder="id"/>
				</ed:col>
				<ed:col size="2">
	    			<ec:textfield name="order" placeholder="order"/>
				</ed:col>
				<ed:col size="4">
		    		<ec:field-group>
		    			<ec:datefield name="startDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('invoiceSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$endDate.setProperty('min', $startDate.getValue());
		    				</ec:event>
		    			</ec:datefield>
		    			<ec:datefield name="endDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('invoiceSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$startDate.setProperty('max', $endDate.getValue());
		    				</ec:event>
		    			</ec:datefield>
		    		</ec:field-group>
				</ed:col>
				<ed:col size="3">
		    		<ec:field-group>
		    			<ec:textfield name="minTotal" placeholder="min total"/>
		    			<ec:textfield name="maxTotal" placeholder="max total"/>
		    		</ec:field-group>
				</ed:col>
				<ed:col size="1">
					<ec:button icon="search" style="info" actionType="submit" align="right"/>
				</ed:col>
			</ed:row>

			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center>#ID</ec:center></ec:table-col>
						<ec:table-col><ec:center>#Order</ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="show-orders.table.data" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center>Subtotal</ec:center></ec:table-col>
						<ec:table-col><ec:center>Taxes</ec:center></ec:table-col>
						<ec:table-col><ec:center>Discounts</ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="show-orders.table.total" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="show-orders.table.action" bundle="${messages}"/></ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.data}" var="item">
							<ec:table-col><ec:center><small>!{item.id}</small></ec:center></ec:table-col>
							<ec:table-col><ec:center><small>!{item.order}</small></ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.date}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.subTotal}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.taxes}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.discounts}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.total}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button icon="search" style="info">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices/show/!{item.id}');
									</ec:event>
								</ec:button>
								</ec:center>
							</ec:table-col>
						</ec:forEach>
					</ec:table-body>
				</ec:table>
			</ec:data-result>
		</ec:data-table>
		
	</ec:box-body>
</ec:box>