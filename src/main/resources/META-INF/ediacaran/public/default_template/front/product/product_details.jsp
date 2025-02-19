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
<title>${entity.name}</title>
</head>

<body>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<ed:row>
				<ed:col size="4">
					<div class="inner-heading">
						<h2>${entity.name}</h2>
					</div>
				</ed:col>
				<ed:col size="8">
					<ec:breadcrumb title="${entity.name}">
						<ec:breadcrumb-path 
							icon="home" 
							text="" 
							lnk="${plugins.ediacaran.sales.web_path}" />
						<ec:breadcrumb-path 
							text="#{header.breadcrumb.parent}" 
							lnk="${plugins.ediacaran.sales.web_path}/search"
							bundle="${messages}"
							/>
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section class="content">
		<ed:container>
		<ed:row>
			<ed:col size="10">
				<ed:row>
					<ed:col size="3">
						<ec:image
							style="fluid"
							src="${plugins.ediacaran.sales.image_prefix_address}${empty entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : entity.publicThumb}"/>
					</ed:col>
					<ed:col size="9">
						<h3>${entity.name}</h3>
						<p>${entity.shortDescription}<p>
						<h5>${entity.getCostString(locale)}</h5>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						<ec:tabs>
							<ec:tabs-item title="Details" active="true">
								${entity.description}
							</ec:tabs-item>
						</ec:tabs>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:button 
							label="Add cart" 
							align="right"
							bundle="${messages}">
							<ec:event type="click">
								location.href = '${plugins.ediacaran.sales.web_path}/cart/add/!{entity.protectedID}'; 
							</ec:event>
						</ec:button>
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="2">
			</ed:col>
		</ed:row>
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>