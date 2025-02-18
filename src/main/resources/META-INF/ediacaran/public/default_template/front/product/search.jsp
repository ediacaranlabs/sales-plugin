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
						<ec:breadcrumb-path 
							text="#{header.breadcrumb.parent}" 
							lnk="${plugins.ediacaran.sales.web_path}/"
							bundle="${messages}" />
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section class="content">
		<ed:container>
		<ed:row>
			<ed:col size="12">
				<ec:data-table id="search_form" action="${plugins.ediacaran.sales.web_path}/products/search">
					<!-- start search form  -->
					<ed:row>
						<ed:col size="12">
							<ec:field-group>
								<ec:textfield 
									name="text" 
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
					<!-- end search form  -->
					<!-- start search result  -->
					<ec:data-result var="response">
						<ed:row>
							<ec:forEach items="!{response.itens}" var="item">
								<ed:col size="3">
									<ec:box>
										<ec:box-body>
											<ed:row style="form">
												<ed:col size="12">
													<ec:if test="!{item.thumbnail == null}">
														<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid"/>
													</ec:if>
													<ec:if test="!{item.thumbnail != null}">
														<ec:image src="${plugins.ediacaran.sales.image_prefix_address}!{item.thumbnail}" style="fluid"/>
													</ec:if>
												</ed:col>
											</ed:row>
											<ed:row>
												<ed:col size="12">
													!{item.name}<br>
													<b>!{item.cost}</b><br>
													!{item.description}<br>
												</ed:col>
											</ed:row>
											<ed:row>
												<ed:col size="12">
													<ec:button 
														label="Add cart" 
														align="right"
														bundle="${messages}">
														<ec:event type="click">
															location.href = '${plugins.ediacaran.sales.web_path}/products/!{item.protectedID}'; 
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
				</ec:data-table>
			</ed:col>
		</ed:row>
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>