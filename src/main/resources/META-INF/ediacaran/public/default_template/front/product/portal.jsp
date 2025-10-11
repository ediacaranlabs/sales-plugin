<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<ec:include uri="/includes/head.jsp"/>
<style type="text/css">

.btn-default {
    background-color: #e9ecef;;
    border-color: #ced4da;
}

.btn-default .fa:not([class*="fa-inverse"]) {
    color: #000000;
}

.inner-headline {
	padding-top: 1em;
	padding-bottom: 1em;
}

.category img {
	max-height: 100px;
	aspect-ratio: 1/1;
}

.category a {
	color: #000000;
}

.category a:hover {
	color: #000000;
	text-decoration: none;
}

.product {
	border: 1px solid #f5f5f5;
	display: inline-grid;
	border-radius: 4px;
	padding: 1em 1em;
}

.product  a{
	text-decoration: none;
}

.product  a:hover {
	color: inherit;
	text-decoration: none;
}

.product .image img {
	max-height: 220px;
	aspect-ratio: 1/1;
}

.product .title {
	font-size: 16px;
    line-height: 24px;
}

.product .price {
	font-size: 28px;
}
    
.navbar-brand{
	display: none;
} 
    
#top_menu{
	padding: 0px 0px;
	background-color: #252525;
}

#top_menu a{
	color: #FFFFFF;
}

.icondefault.fa:not([class*="fa-inverse"]) {
	color: #ffffff;
}

.line-cart{
	padding-top: 1em;
}

.form {
  height: 50px;
  display: flex;
  align-items: center;
}

section.body{
	padding-top: 1em;
}

</style>
<title>Product Plugin - Ediacaran application</title>
</head>

<body>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<form action="${plugins.ediacaran.sales.web_path}/products/search" method="post">
				<ed:row classStyle="form">
					<ed:col size="3">
						<c:if test="${empty plugins.ediacaran.front.image_logo}">
							<h1>${plugins.ediacaran.front.text_logo}</h1>
						</c:if>
						<c:if test="${!empty plugins.ediacaran.front.image_logo}">
							<ec:image src="${plugins.ediacaran.front.image_logo}"/>
						</c:if>
					</ed:col>
					<ed:col size="8">
						<ec:center>
								<input type="hidden" name="resultPerPage" value="9">
								<ec:field-group>
									<ec:textfield 
										name="name" 
										value="${text}"
										bundle="${messages}"/>
									<ec:append-field>
										<ec:button 
											actionType="submit" 
											icon="search"
											style="default" 
											bundle="${messages}"/>
									</ec:append-field>
								</ec:field-group>					
						</ec:center>
					</ed:col>
					<ed:col size="1">
						<ec:center><a href="${plugins.ediacaran.sales.web_path}/cart"><ec:icon icon="shopping-cart" style="default" /></a></ec:center>
					</ed:col>
				</ed:row>
			</form>
		</ed:container>
	</section>

	<c:if test="${!empty vars.categories}">
	<section class="body">
		<ed:container>
			<h3>Categorias</h3>
			<ec:carousel>
				<c:forEach items="${vars.categories}" var="category">
					<ec:carousel-item>
						<span class="category">
							<a href="${plugins.ediacaran.sales.image_prefix_address}/products/category/${category.protectedID}">
								<ec:image align="center" style="circle" src="${plugins.ediacaran.sales.image_prefix_address}${empty category.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : category.publicThumb}"/>
								<ec:center>${category.getFullName(locale)}</ec:center>
							</a>
						</span>
					</ec:carousel-item>
				</c:forEach>
			</ec:carousel>
		</ed:container>		
	</section>
	</c:if>
	
	<c:if test="${!empty vars.offers}">
	<section class="body">
		<ed:container>
			<h3>Offers</h3>
			
			<ed:row>
				<c:forEach items="${vars.offers}" var="item">
					<ed:col size="3">
						<span class="product">
							<ed:row style="form">
								<ed:col size="12">
									<a class="image" href="${plugins.ediacaran.sales.web_path}/products${item.publicID}">
									<c:if test="${item.publicThumb == null}">
										<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid" align="center"/>
									</c:if>
									<c:if test="${item.publicThumb != null}">
										<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${item.publicThumb}" style="fluid" align="center"/>
									</c:if>
									</a>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<span class="title"><a href="${plugins.ediacaran.sales.web_path}/products${item.publicID}">${item.name}</a></span><br>
									${item.productCurrency.symbol}<span class="price">${item.productCurrency.wholeNumberString}</span>${item.productCurrency.fractionalPartString}<br>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<ec:button 
										label="Add cart" 
										align="right"
										size="small"
										style="default"
										bundle="${messages}">
										<ec:event type="click">
											location.href = '${plugins.ediacaran.sales.web_path}/cart/add/${item.protectedID}'; 
										</ec:event>
									</ec:button>
								</ed:col>
							</ed:row>
							
						</span>
					</ed:col>
				</c:forEach>
			</ed:row>					
			
		</ed:container>		
	</section>
	</c:if>
	
	<c:if test="${!empty vars.products}">
	<section class="body">
		<ed:container>
			<h3>Products</h3>
			
			<ed:row>
				<c:forEach items="${vars.products}" var="item">
					<ed:col size="3">
						<span class="product">
							<ed:row style="form">
								<ed:col size="12">
									<a class="image" href="${plugins.ediacaran.sales.web_path}/products${item.publicID}">
									<c:if test="${item.publicThumb == null}">
										<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid" align="center"/>
									</c:if>
									<c:if test="${item.publicThumb != null}">
										<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${item.publicThumb}" style="fluid" align="center"/>
									</c:if>
									</a>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<span class="title"><a href="${plugins.ediacaran.sales.web_path}/products${item.publicID}">${item.name}</a></span><br>
									${item.productCurrency.symbol}<span class="price">${item.productCurrency.wholeNumberString}</span>${item.productCurrency.fractionalPartString}<br>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<ec:button 
										label="Add cart" 
										align="right"
										size="small"
										style="default"
										bundle="${messages}">
										<ec:event type="click">
											location.href = '${plugins.ediacaran.sales.web_path}/cart/add/${item.protectedID}'; 
										</ec:event>
									</ec:button>
								</ed:col>
							</ed:row>
							
						</span>
					</ed:col>
				</c:forEach>
			</ed:row>				
		</ed:container>		
	</section>
	</c:if>

	
	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>