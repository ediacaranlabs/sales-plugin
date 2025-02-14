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
	<ec:box-body>
		<ec:data-table id="productSearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/search">
			<ed:row>
				<ed:col size="5">
	    			<ec:textfield name="name" label="#{search.form.name.label}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="3">
					<ec:select label="#{search.form.product_type.label}" name="productType" bundle="${messages}">
						<ec:option value=""></ec:option>
						<c:forEach items="${vars.productTypes}" var="productType">
						<ec:option value="${productType.code}">${productType.name}</ec:option>
						</c:forEach>
					</ec:select>
				</ed:col>
				<ed:col size="4">
		    			<ec:textfield label="#{search.form.min_cost.label}" name="minCost" bundle="${messages}"/>
		    			<ec:textfield label="#{search.form.max_cost.label}" name="maxCost" bundle="${messages}"/>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button icon="search" label="#{search.form.search.label}" form="productSearchForm"  actionType="submit" align="right" bundle="${messages}"/>
					<ec:dropdown label="#{search.form.new.label}" style="primary" align="right" bundle="${messages}">
						<c:forEach items="${vars.productTypes}" var="productType">
							<ec:dropdown-item src="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/edit/${fn:toLowerCase(productType.code)}">${productType.name}</ec:dropdown-item>
						</c:forEach>
					</ec:dropdown>
				</ed:col>
			</ed:row>
			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.image" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.name" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.value" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center>tags</ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.action" bundle="${messages}"/></ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.itens}" var="item">
						<ec:table-row>
							<ec:table-col><ec:center>
								<ec:image 
									src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
									style="thumbnail"
									align="center"
								/>
							</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.name}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.cost}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.tags}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button icon="pencil" style="info" actionType="button">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/edit/!{item.productType.toLowerCase()}/!{item.protectedID}');
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