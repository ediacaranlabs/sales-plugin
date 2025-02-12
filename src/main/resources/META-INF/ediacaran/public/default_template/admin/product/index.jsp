<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

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
	<ec:box-header><fmt:message key="title" bundle="${messages}"/></ec:box-header>
	<ec:box-body>
		<ec:data-table id="productSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/products/search">
			<ed:row>
				<ed:col size="5">
	    			<ec:textfield name="#{tabs.search.form.name.label}" label="Name" bundle="${messages}"/>
				</ed:col>
				<ed:col size="3">
					<ec:select label="#{tabs.search.form.product_type.label}" name="productType" bundle="${messages}">
						<c:forEach items="${vars.productTypes}" var="productType">
						<ec:option value="${productType.code}">${productType.name}</ec:option>
						</c:forEach>
					</ec:select>
				</ed:col>
				<ed:col size="4">
		    			<ec:textfield label="#{tabs.search.form.min_cost.label}" name="minCost" bundle="${messages}"/>
		    			<ec:textfield label="#{tabs.search.form.max_cost.label}" name="maxCost" bundle="${messages}"/>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button icon="search" label="#{tabs.search.form.search.label}" form="productSearchForm"  actionType="submit" align="right" bundle="${messages}"/>
				</ed:col>
			</ed:row>
			<hr>
			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center>Image</ec:center></ec:table-col>
						<ec:table-col><ec:center>Name</ec:center></ec:table-col>
						<ec:table-col><ec:center>Value</ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.data}" var="item">
						<ec:table-row>
							<ec:table-col><ec:center><small>!{item.protectedID}</small></ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.name}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.date}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.status}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.total}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button id="!{item.id}_button" icon="search" style="info" actionType="button">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/!{item.id}');
									</ec:event>
								</ec:button>
								</ec:center>
							</ec:table-col>
						</ec:table-row>
						</ec:forEach>
					</ec:table-body>
				</ec:table>
			
			
				<ed:row>
				<ec:forEach items="!{response.itens}" var="item">
					<ed:col size="6">
					<ec:box>
						<ec:box-body>
							<ec:form id="product_!{item.protectedID}" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/add/!{item.protectedID}" update="cart_result">
								<ed:row>
									<ed:col size="12" id="product_content_!{item.protectedID}">
										<script type="text/javascript">
											$.AppContext.utils.updateContentByID(
													'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/products/!{item.protectedID}', 
													'product_content_!{item.protectedID}'
											);
										</script>
									</ed:col>
								</ed:row>
								<ed:row>
									<ed:col size="12">
										<ec:button label="#{tabs.search.form.add.label}" icon="plus" align="right" actionType="submit" bundle="${messages}"/>
									</ed:col>
								</ed:row>
							</ec:form>
						</ec:box-body>
					</ec:box>
					</ed:col>
				</ec:forEach>
				</ed:row>
			</ec:data-result>
		</ec:data-table>
	</ec:box-body>
</ec:box>