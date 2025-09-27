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
	<ec:box-body>
		<ec:data-table id="productCategorySearchForm" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/category/search">
			<ed:row>
				<ed:col size="8">
	    			<ec:textfield name="name" label="#{search.form.name.label}" bundle="${messages}"/>
				</ed:col>
				<ed:col size="4">
					<ec:select label="Category" name="parent" bundle="${messages}">
						<ec:option value=""></ec:option>
						<c:forEach items="${vars.categories}" var="category">
							<ec:option label="${category.name}">${category.protectedID}</ec:option>
						</c:forEach>
					</ec:select>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button icon="search" label="#{search.form.search.label}" actionType="submit" align="right" bundle="${messages}"/>
					<ec:button icon="pencil" label="New Category" align="right" actionType="button">
						<ec:event type="click">
							$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/category/edit');
						</ec:event>
					</ec:button>
				</ed:col>
			</ed:row>
			<ec:data-result var="response">
				<ec:table>
					<ec:table-header>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.image" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.name" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.description" bundle="${messages}"/></ec:center></ec:table-col>
						<ec:table-col><ec:center><fmt:message key="result.tab.title.action" bundle="${messages}"/></ec:center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<ec:forEach items="!{response.itens}" var="item">
						<ec:table-row>
							<ec:table-col>
								<ec:if test="!{item.thumbnail == null}">
									<ec:image 
										classStyle="product_image_thumb"
										src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png')}"
										style="thumbnail"
										align="center"
									/>
								</ec:if>
								<ec:if test="!{item.thumbnail != null}">
									<ec:image
										classStyle="product_image_thumb" 
										src="${plugins.ediacaran.sales.image_prefix_address}!{item.thumbnail}"
										style="thumbnail"
										align="center"
									/>
								</ec:if>
							</ec:table-col>
							<ec:table-col><ec:center>!{item.name}</ec:center></ec:table-col>
							<ec:table-col><ec:center>!{item.description}</ec:center></ec:table-col>
							<ec:table-col>
								<ec:center>
								<ec:button icon="pencil" style="info" actionType="button">
									<ec:event type="click">
										$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/category/edit/!{item.id}');
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