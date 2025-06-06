<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<style>
.product_image_thumb {
	width: 128px;
	aspect-ratio: 1/1;
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
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:data-table id="productSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata/search">
			<ed:row>
				<ed:col>
	    			<ec:textfield name="name" label="#{search.form.name.label}" bundle="${messages}"/>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button icon="search" label="#{search.form.search.label}" form="productSearchForm"  actionType="submit" align="right" bundle="${messages}"/>
					<ec:button label="#{search.form.new.label}"  actionType="button" align="right" bundle="${messages}">
						<ec:event type="click">
							$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata/edit');
						</ec:event>
					</ec:button>
				</ed:col>
			</ed:row>
			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.name" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.description" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.actions" bundle="${messages}"/></ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.itens}" var="item">
						<ec:table-row>
							<ec:table-col><ec:center>!{item.name}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.description}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button icon="pencil" style="info" actionType="button">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/product-metadata/edit/!{item.protectedID}');
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