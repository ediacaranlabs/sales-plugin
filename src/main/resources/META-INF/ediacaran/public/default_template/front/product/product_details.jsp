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

<meta property="og:url" content="${plugins.ediacaran.system.website}${plugins.ediacaran.sales.web_path}/products${entity.publicID}">
<meta property="fb:app_id">
<meta property="og:title" content="${entity.name}">
<meta property="og:description" content="${entity.shortDescription}">
<meta name="description" content="${entity.shortDescription}">
<meta property="keywords" content="${entity.tagsString}">
<meta property="og:type" content="article">
<meta property="og:image:width" content="960">
<meta property="og:image:height" content="960">
<meta property="og:image:type" content="image/png">
<meta property="og:image" content="${plugins.ediacaran.sales.image_prefix_address}${empty entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : entity.publicThumb}">
<meta property="og:image:secure_url" content="${plugins.ediacaran.sales.image_prefix_address}${empty entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : entity.publicThumb}">
<%--<meta property="og:locale" content="pt_BR">--%>
<meta name="twitter:image" content="${plugins.ediacaran.sales.image_prefix_address}${empty entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : entity.publicThumb}">
<meta name="twitter:card" content="summary_large_image">

<ec:include uri="/includes/head.jsp"/>
<style type="text/css">
.box-body {
	min-height: 120px !important;
}

#produc_images img{
	width: 100px;
	height: auto;
	cursor: pointer;
}
</style>
<title>${entity.name}</title>
</head>

<body>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<ed:row style="form">
				<ed:col size="12">
					<div class="inner-heading">
						<ec:left><h4>${entity.name}</h4></ec:left>
						<ec:right>
							<ec:breadcrumb title="${entity.name}">
								<ec:breadcrumb-path 
									icon="home" 
									text="" 
									lnk="${plugins.ediacaran.sales.web_path}" />
								<ec:breadcrumb-path 
									text="#{header.breadcrumb.parent}" 
									lnk="${plugins.ediacaran.sales.web_path}/products"
									bundle="${messages}"
									/>
							</ec:breadcrumb>
						</ec:right>
					</div>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section class="content">
		<ed:container>
		<ed:row>
			<ed:col size="9">
				<ed:row>
					<ed:col size="5">
						<ec:image id="principal_img"
							style="fluid"
							src="${plugins.ediacaran.sales.image_prefix_address}${empty entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : entity.publicThumb}"/>
					</ed:col>
					<ed:col size="7">
						<h3>${entity.name}</h3>
						<p>${entity.shortDescription}<p>
						<c:forEach items="${infos}" var="inf">
							<!-- info (${inf.id}) -->
							<ec:sectionView section="${tab.content}"/>
							<!-- /info (${inf.id}) -->
						</c:forEach>
						<h5>${entity.getCostString(locale)}</h5>
						<c:if test="${images.size() > 0}">
							<ec:carousel id="produc_images">
								<c:forEach items="${images}" var="image">
									<ec:carousel-item>
										<a href="#" id="product_img_${image.protectedID}">
											<ec:image align="center" src="${plugins.ediacaran.sales.image_prefix_address}${image.publicThumb}"/>
										</a>
										<ec:event componentName="product_img_${image.protectedID}" type="click">
											var $img = $.AppContext.utils.getById('principal_img');
											$img.setAttribute('src', '${plugins.ediacaran.sales.image_prefix_address}${image.publicThumb}');
										</ec:event>
									</ec:carousel-item>
								</c:forEach>
							</ec:carousel>
						</c:if>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						<ed:row>
							<ed:col>
								<h3><fmt:message key="tabs.details" bundle="${messages}"/></h3>
								${entity.description}
							</ed:col>
						</ed:row>
						<ed:row>
							<ed:col>
								<h3><fmt:message key="tabs.characteristics" bundle="${messages}"/></h3>
								<ec:table style="striped">
									<c:forEach var="prop" items="${entity.getProductAttributes(true)}">
										<ec:table-row>
											<ec:table-col>
												${prop.name}
											</ec:table-col>
											<ec:table-col>
												${prop.value}
											</ec:table-col>
										</ec:table-row>
									</c:forEach>
								</ec:table>
							</ed:col>
						</ed:row>
						
						<c:forEach items="${tabs}" var="tab">
						<!-- tab (${tab.id}) -->
						<ed:row>
							<ed:col>
								<h3>${tab.title}</h3>
								<ec:sectionView section="${tab.content}"/>
							</ed:col>
						</ed:row>
						<!-- /tab (${tab.id}) -->
						</c:forEach>
						
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:button 
							label="#{form.add_cart.label}" 
							align="right"
							style="default"
							bundle="${messages}">
							<ec:event type="click">
								location.href = '${plugins.ediacaran.sales.web_path}/cart/add/${entity.protectedID}'; 
							</ec:event>
						</ec:button>
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="3">
				<jsp:include page="widgets.jsp"/>
			</ed:col>
		</ed:row>
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>