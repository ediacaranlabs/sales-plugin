<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<ec:setBundle var="messages" locale="${locale}"/>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<ec:include uri="/includes/head.jsp"/>
<title>${plugins.ediacaran.sales.portal_title}</title>
</head>

<body>

	<ec:include uri="/includes/header.jsp"/>

	<section class="inner-headline">
		<ed:container>
			<ed:row style="form">
				<ed:col size="12">
					<div class="inner-heading">
					</div>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<c:if test="${!empty vars.categories}">
	<section class="body">
		<ed:container>
			<h3><fmt:message key="sections.categories" bundle="${messages}" /></h3>
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
			<h3><fmt:message key="sections.offers" bundle="${messages}" /></h3>
			
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
									<c:if test="${item.hasDiscount()}">
										<span class="discount">${item.getProductValue(false)}</span><br>
									</c:if>
									${item.productValue.symbol}<span class="price">${item.productValue.wholeNumberString}</span>${item.productValue.fractionalPartString}<br>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<ec:button 
										label="#{cart.add_button}" 
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
			<h3><fmt:message key="sections.products" bundle="${messages}" /></h3>
			
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
									<c:if test="${item.hasDiscount()}">
										<span class="discount">${item.getProductValue(false).symbol}${item.getProductValue(false).wholeNumberString}${item.getProductValue(false).fractionalPartString}</span><br>
									</c:if>
									${item.productValue.symbol}<span class="price">${item.productValue.wholeNumberString}</span>${item.productValue.fractionalPartString}<br>
								</ed:col>
							</ed:row>
							<ed:row>
								<ed:col size="12">
									<ec:button 
										label="#{cart.add_button}" 
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