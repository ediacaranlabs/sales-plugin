<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<ec:include uri="/includes/head.jsp"/>
<style type="text/css">
.box-body {
	min-height: 120px !important;
}

#detachedFilters{
	border-right: 1px solid #dcdcdc;
	height: 100%;
	padding-left: 1.5rem;
	padding-right: 1.5rem;
	background-color: #fcfcfc;
}

#pageBody{
	min-height: 600px;
}

/*
.sidebar-group .sidebar {
	width: 200px;
}

@media screen and (min-width: 1200px) {
    .sidebar-group .sidebar-content {
        margin-left: 200px;
    }
}
*/
.filter_group .card-body {
	max-height: 300px;
	overflow-y: auto;
}

.group_title{
	color: #acacac;
	text-transform: uppercase;
	font-weight: bold;
}

.filter {
	padding-bottom: 15px;
}

.filter_title {
	color: #acacac;
	text-transform: uppercase;
}
.filter_option .form-check {
	padding-left: 1.8rem;
	padding-bottom: 0.3rem;
}

</style>
<title><fmt:message key="header.title" bundle="${messages}"/></title>
</head>

<body>
	<script type="text/javascript">
		$.AppContext.onload(function(){
			var $search_form = $.AppContext.utils.getById('search_form');
			$search_form.submit();
		});
	</script>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<ed:row>
				<ed:col size="4">
					<div class="inner-heading">
						<h2><fmt:message key="header.title" bundle="${messages}"/></h2>
					</div>
				</ed:col>
				<ed:col size="8">
					<ec:breadcrumb title="#{header.title}" bundle="${messages}">
						<ec:breadcrumb-path 
							icon="home" 
							text="" 
							lnk="${plugins.ediacaran.sales.web_path}"/>
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<ec:data-table id="search_form" action="${plugins.ediacaran.sales.web_path}/products/search">
		<ec:sidebar-group id="pageBody" show="true">
			<ec:sidebar>
				<ec:body id="detachedFilters">
				</ec:body>
			</ec:sidebar>
			<ec:sidebar-content>
				<ed:container>
					<ec:body id="content-body">
						<ec:box>
							<ec:box-body>
			    				<ed:row>
			    					<ed:col>
										<!-- start search form  -->
					    				<ed:row id="filter_toggler">
					    					<ed:col>
								 				<ec:menu-toggler menuID="pageBody">
													<ec:icon icon="bars" size="1" />
												</ec:menu-toggler>
					    					</ed:col>
					    				</ed:row>
										<ed:row>
											<ed:col size="12">
												<ec:field-group>
													<ec:textfield 
														name="name" 
														placeholder="#{search.placeholder}" 
														value="${text}"
														bundle="${messages}"/>
													<ec:append-field>
														<ec:button 
															actionType="submit" 
															icon="search" 
															label="#{search.button}"
															bundle="${messages}"/>
													</ec:append-field>
												</ec:field-group>
											</ed:col>
										</ed:row>
					    				<span style="display:none" id="filters">
					    				</span>
										<!-- end search form  -->
										<!-- start search result  -->
										<ec:data-result var="response" from="search_form">
											<ed:row>
												<script type="text/javascript">
													var $tmpObjStr = '!{JSON.stringify(response.filters)}';
													
													if($tmpObjStr.length == 0){
														$tmpObjStr = '[]';
													}
													
													var $tmpObj = JSON.parse($tmpObjStr);
													var $filter_toggler = $.AppContext.utils.getById('filter_toggler');
													var $pageBody = $.AppContext.utils.getById('pageBody');

													if($tmpObj.length > 0){
														var $tmp = $.AppContext.utils.applyTemplate("filterTemplate", JSON.parse($tmpObjStr));
														$.AppContext.utils.content.update("detachedFilters", $tmp);
														$filter_toggler.setVisible(true);
														$pageBody.addClass('show');
													}
													else{
														$pageBody.removeClass('show');
														$filter_toggler.setVisible(false);
													}
													
												</script>
												<ec:forEach items="!{response.itens}" var="item">
													<ed:col size="3">
														<ec:box>
															<ec:box-body>
																<ed:row style="form">
																	<ed:col size="12">
																		<a href="${plugins.ediacaran.sales.web_path}/products!{item.publicID}">
																		<ec:if test="!{item.thumbnail == null}">
																			<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid"/>
																		</ec:if>
																		<ec:if test="!{item.thumbnail != null}">
																			<ec:image src="${plugins.ediacaran.sales.image_prefix_address}!{item.thumbnail}" style="fluid"/>
																		</ec:if>
																		</a>
																	</ed:col>
																</ed:row>
																<ed:row>
																	<ed:col size="12">
																		<h5>!{item.name}</h5><br>
																		<b>!{item.cost}</b><br>
																		!{item.shortDescription}<br>
																	</ed:col>
																</ed:row>
																<ed:row>
																	<ed:col size="12">
																		<ec:button 
																			label="#{result.add_cart.label}" 
																			align="right"
																			bundle="${messages}">
																			<ec:event type="click">
																				location.href = '${plugins.ediacaran.sales.web_path}/cart/add/!{item.protectedID}'; 
																			</ec:event>
																		</ec:button>
																	</ed:col>
																</ed:row>
															</ec:box-body>
														</ec:box>
													</ed:col>
												</ec:forEach>
											</ed:row>
										</ec:data-result>
										<!-- end search result  -->
			    					</ed:col>
			    				</ed:row>
							</ec:box-body>
						</ec:box>
					</ec:body>
				</ed:container>
			</ec:sidebar-content>
		</ec:sidebar-group>
	</ec:data-table>

	<ec:template var="filtersGroup" id="filterTemplate">
		<b><fmt:message key="search.filters.title" bundle="${messages}"/></b>
		<hr>
		<ec:forEach items="!{filtersGroup}" var="group">
			<span formgroup="filters" formgrouptype="index" class="filter_group">
				<input type="hidden" name="protectedID" value="!{group.protectedID}">
				<%--<span class="group_title">!{group.title}</span><br>--%>
				<ec:forEach items="!{group.filters}" var="filter">
					<span formgroup="filters" formgrouptype="index" class="filter">
						<input type="hidden" name="protectedID" value="!{filter.protectedID}">
						<span class="filter_title">!{filter.title}</span>
						
						<ec:forEach items="!{filter.options}" var="option">
							<span class="filter_option">
								<ec:checkbox label="!{option.description}" name="values" selected="!{option.selected}" value="!{option.value}">
									<ec:event type="click">
										var $form = $.AppContext.utils.getById('search_form');
										$form.submit();
									</ec:event>
								</ec:checkbox>
							</span>
						</ec:forEach>
					</span>
					<br>
				</ec:forEach>
			</span>		
		</ec:forEach>	
	</ec:template>
	
	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>