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


@media screen and (min-width: 1200px) {
    .sidebar-group .sidebar-content {
        margin-left: 350px;
    }
}

.sidebar-group .sidebar {
    width: 350px;
}

.sidebar-group:not(.show) .sidebar {
    margin-left: -350px;
}

.navbar-toggler .fa:not([class*="fa-inverse"]) {
    color: #000000;
}

#content-body{
	padding-left: 1em;
	padding-right: 1em;
	background-color: #fcfcfc;
}

#detachedFilters{
	height: 100%;
	padding-left: 1.5rem;
	padding-right: 1.5rem;
	background-color: #fcfcfc;
}

#pageBody{
	min-height: 600px;
}

#filter_toggler button{
	padding: 0px 0px;
}

#category_filter a {
	text-decoration: none;
	cursor: pointer;
}

#category_filter .filter_clear {
	display: none;
	color: red;
}

#category_filter .filter_clear.show{
	display: block;
}

#category_filter .filter_option a{
	color: inherit;
}

#category_filter .filter_title a{
	color: inherit;
}

#category_filter .selected {
	color: red;
}

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
	font-weight: bold;
}

.filter_option .form-check {
	padding-left: 1.8rem;
	padding-bottom: 0.3rem;
}

.filter_option .category-check {
	padding-left: 0.3rem;
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
			<ed:row style="form">
				<ed:col size="12">
					<div class="inner-heading">
						<ec:left><h4><fmt:message key="header.title" bundle="${messages}"/></h4></ec:left>
						<ec:right>
							<ec:breadcrumb title="#{header.title}" bundle="${messages}">
								<ec:breadcrumb-path 
									icon="home" 
									text="" 
									lnk="${plugins.ediacaran.sales.web_path}"/>
							</ec:breadcrumb>
						</ec:right>
					</div>
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
					<ec:body id="content-body">
	    				<ed:row>
	    					<ed:col>
								<!-- start search form  -->
								<ed:row>
									<ed:col size="12">
										<input type="hidden" name="name" value="${productSearch.name}">
										<input type="hidden" name="category" value="${productSearch.category.protectedID}">
										<input type="hidden" name="resultPerPage" value="9">
						 				<ec:menu-toggler menuID="pageBody">
											<ec:icon icon="bars" size="1" />
										</ec:menu-toggler>
										
									</ed:col>
								</ed:row>
			    				<span style="display:none" id="filters">
			    				</span>
								<!-- end search form  -->
								<!-- start search result  -->
								<ec:data-result var="response" from="search_form">
									<ed:row>
										<script type="text/javascript">
											var $tmpResponse = '!{JSON.stringify(response)}';
											var response = JSON.parse($tmpResponse);
											
											var $filter_toggler = $.AppContext.utils.getById('filter_toggler');
											var $pageBody = $.AppContext.utils.getById('pageBody');

											if(response.filters.length > 0 || response.categories.length > 0){

												/* get selected category */
												
												let $form = $.AppContext.utils.getById('search_form');
												let $categoryField = $form.getField('category');
												response.selectedCategory = $categoryField.getValue();
												
												/* apply filters template */
												let $tmp = $.AppContext.utils.applyTemplate("filterTemplate", response);
												$.AppContext.utils.content.update("detachedFilters", $tmp);
												
												/* show filters */
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
													<span class="product">
														<ed:row style="form">
															<ed:col size="12">
																<a class="image" href="${plugins.ediacaran.sales.web_path}/products!{item.publicID}">
																<ec:if test="!{item.thumbnail == null}">
																	<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid" align="center"/>
																</ec:if>
																<ec:if test="!{item.thumbnail != null}">
																	<ec:image src="${plugins.ediacaran.sales.image_prefix_address}!{item.thumbnail}" style="fluid" align="center"/>
																</ec:if>
																</a>
															</ed:col>
														</ed:row>
														<ed:row>
															<ed:col size="12">
																<span class="title"><a href="${plugins.ediacaran.sales.web_path}/products!{item.publicID}">!{item.name}</a></span><br>
																<ec:if test="!{item.hasDiscount}">
																	<span class="discount">!{item.productValueWithoutDiscount.symbol}!{item.productValueWithoutDiscount.wholeNumberString}!{item.productValueWithoutDiscount.fractionalPartString}</span><br>
																</ec:if>
																!{item.productValue.symbol}<span class="price">!{item.productValue.wholeNumberString}</span>!{item.productValue.fractionalPartString}<br>
															</ed:col>
														</ed:row>
														<ed:row>
															<ed:col size="12">
																<ec:button 
																	label="#{result.add_cart.label}" 
																	align="right"
																	size="small"
																	style="default"
																	bundle="${messages}">
																	<ec:event type="click">
																		location.href = '${plugins.ediacaran.sales.web_path}/cart/add/!{item.protectedID}'; 
																	</ec:event>
																</ec:button>
															</ed:col>
														</ed:row>
														
													</span>
											</ed:col>
										</ec:forEach>
									</ed:row>
								</ec:data-result>
								<!-- end search result  -->
	    					</ed:col>
	    				</ed:row>
					
					
					
					</ec:body>
			</ec:sidebar-content>
		</ec:sidebar-group>
	</ec:data-table>

	<ec:template var="response" id="filterTemplate">
		<ec:if test="!{response.categories.length > 0}">
			<b>Categories</b>
			<hr>
			<ed:row>
				<ed:col id="category_filter">
					<ec:forEach items="!{response.categories}" var="category">
						<span class="filter">
							<span class="filter_title !{response.selectedCategory == category.protectedID? 'selected' : '' }">
								<a id="cat_!{category.protectedID}" href="#">!{category.title}</a><br>
								<ec:event componentName="cat_!{category.protectedID}" type="click">
										let $form = $.AppContext.utils.getById('search_form');
										let $categoryField = $form.getField('category');
										
										$categoryField.setValue('!{category.protectedID}');
										$form.submit();
								</ec:event>
							</span>
							
							<ec:forEach items="!{category.subcategories}" var="subCategory">
								<span class="filter_option !{response.selectedCategory == subCategory.protectedID? 'selected' : '' }">
									<span class="category-check">
									
										<a id="cat_!{subCategory.protectedID}" class="" href="#">!{subCategory.title}</a><br>
										<ec:event componentName="cat_!{subCategory.protectedID}" type="click">
											let $form = $.AppContext.utils.getById('search_form');
											let $categoryField = $form.getField('category');
											
											$categoryField.setValue('!{subCategory.protectedID}');
											$form.submit();
										</ec:event>
									
									</span>
								</span>
								
							</ec:forEach>
						</span>
					</ec:forEach>
					
					<span class="filter_clear !{response.selectedCategory != ''? 'show' : '' }">
						<a id="cat_clear" class="filter_option" href="#">Remove filter</a><br>
						<ec:event componentName="cat_clear" type="click">
								let $form = $.AppContext.utils.getById('search_form');
								let $categoryField = $form.getField('category');
								let $categoryValue = $categoryField.getValue();
								
								$categoryField.setValue('');
								$form.submit();
						</ec:event>
					</span>
					
				</ed:col>
			</ed:row>
		</ec:if>
		
		<ec:if test="!{response.selectedCategory != ''}">
			<b><fmt:message key="search.filters.title" bundle="${messages}"/></b>
			<hr>
			
			<ec:forEach items="!{response.filters}" var="group">
				<span formgroup="filters" formgrouptype="index" class="filter_group">
					<input type="hidden" name="protectedID" value="!{group.protectedID}">
					<ec:forEach items="!{group.filters}" var="filter">
						<span formgroup="filters" formgrouptype="index" class="filter">
							<input type="hidden" name="protectedID" value="!{filter.protectedID}">
							<span class="filter_title">!{filter.title} (!{group.title})</span>
							
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
		</ec:if>
		
	</ec:template>
	
	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>