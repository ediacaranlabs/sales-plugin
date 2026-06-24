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
		<ec:data-table id="orderSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/search">
			<ed:row>
				<ed:col size="2">
	    			<ec:textfield name="id" placeholder="#{form.id.placeholder}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="4">
		    		<ec:field-group>
		    			<ec:dateField name="startDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('orderSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$endDate.setProperty('min', $startDate.getValue());
		    				</ec:event>
		    			</ec:dateField>
		    			<ec:dateField name="endDate">
		    				<ec:event type="change">
								var $form = $.AppContext.utils.getById('orderSearchForm');
								var $startDate = $form.getField('startDate');
								var $endDate = $form.getField('endDate');
								$startDate.setProperty('max', $endDate.getValue());
		    				</ec:event>
		    			</ec:dateField>
		    		</ec:field-group>
				</ed:col>
				<ed:col size="3">
					<ec:select name="status">
						<ec:option value=""><fmt:message key="form.status.placeholder" bundle="${messages}"/></ec:option>
						<c:forEach items="${vars.statusList}" var="status">
						<ec:option value="${status}">${status.getName(locale)}</ec:option>
						</c:forEach>
					</ec:select>
				</ed:col>
				<ed:col size="3">
		    		<ec:field-group>
		    			<ec:textfield name="minTotal" placeholder="#{form.mintotal.placeholder}" bundle="${messages}"/>
		    			<ec:textfield name="maxTotal" placeholder="#{form.maxtotal.placeholder}" bundle="${messages}"/>
		    		</ec:field-group>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col size="12">
					<ec:button label="Pesquisar" style="info" actionType="submit" align="right"/>
				</ed:col>
			</ed:row>

			<ec:data-result var="response">
			
				<ec:if test="!{window.innerWidth < 992}">
					<ec:forEach items="!{response.data}" var="item">
						<ec:box classStyle="carousel-group">
							<ec:box-header>
								<h3>!{item.id}</h3>
							</ec:box-header>
							<ec:box-body>
								<ed:row style="form">
									<ed:col>
										<b><fmt:message key="table.data" bundle="${messages}"/></b>: !{item.date}
									</ed:col>
								</ed:row>
								<ed:row style="form">
									<ed:col>
										<b><fmt:message key="table.status" bundle="${messages}"/></b>: !{item.status}
									</ed:col>
								</ed:row>
								<ed:row style="form">
									<ed:col>
										<b><fmt:message key="table.total" bundle="${messages}"/></b>: !{item.total}
									</ed:col>
								</ed:row>
								<ed:row>
									<ed:col>
										<ec:button id="!{item.id}_button" label="Ver" style="info" align="right" actionType="button">
											<ec:event type="click">
												$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/!{item.id}');
											</ec:event>
										</ec:button>
									</ed:col>
								</ed:row>
							</ec:box-body>
						</ec:box>
					</ec:forEach>
				</ec:if>
				
				<ec:if test="!{window.innerWidth >= 992}">
				
					<ec:table>
						<ec:table-header>
							<ec:table-col><ec:center><fmt:message key="table.id" bundle="${messages}"/></ec:center></ec:table-col>
							<ec:table-col><ec:center><fmt:message key="table.data" bundle="${messages}"/></ec:center></ec:table-col>
							<ec:table-col><ec:center><fmt:message key="table.status" bundle="${messages}"/></ec:center></ec:table-col>
							<ec:table-col><ec:center><fmt:message key="table.total" bundle="${messages}"/></ec:center></ec:table-col>
							<ec:table-col><ec:center><fmt:message key="table.action" bundle="${messages}"/></ec:center></ec:table-col>
						</ec:table-header>
						<ec:table-body>
							<ec:forEach items="!{response.data}" var="item">
							<ec:table-row>
								<ec:table-col><ec:center><small>!{item.id}</small></ec:center></ec:table-col>
								<ec:table-col><ec:center>!{item.date}</ec:center></ec:table-col>
								<ec:table-col><ec:center>!{item.status}</ec:center></ec:table-col>
								<ec:table-col><ec:center>!{item.total}</ec:center></ec:table-col>
								<ec:table-col>
									<ec:center>
									<ec:button id="!{item.id}_button" icon="search" style="info" actionType="button">
										<ec:event type="click">
											$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/!{item.id}');
										</ec:event>
									</ec:button>
									</ec:center>
								</ec:table-col>
							</ec:table-row>
							</ec:forEach>
						</ec:table-body>
					</ec:table>
				
				
				</ec:if>
				
				
			</ec:data-result>
		</ec:data-table>
		
	</ec:box-body>
</ec:box>