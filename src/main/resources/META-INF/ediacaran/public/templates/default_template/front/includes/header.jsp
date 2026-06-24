<%@page trimDirectiveWhitespaces="true" %>

<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"              prefix="fn"%>
<ec:import-object id="menubar/front/default" var="defaultMenu"/>
<style>
/*
.inner-headline.top-header{
	padding-top: 40px;
	padding-bottom: 40px;
}
*/
header .fa:not([class*="fa-inverse"]){
	color: #ffffff;
}

.navbar .navbar-toggler i {
	color: #ffffff;
}
.navbar .container{
	padding: 10px 10px;
}
/*
.top-header .logo {
	flex: 0 0 25%;
	max-width: 25%;
}

.top-header .form {
	flex: 0 0 67%;
	max-width: 67%;
}

.top-header .cart {
	flex: 0 0 8%;
	max-width: 8%;
}
*/

.search-section.logo-area {
    position: relative;
    width: 100%;
    padding-right: 15px;
    padding-left: 15px;	
	flex-basis: unset;
}

.search-section.form-area {
    position: relative;
    width: 100%;
    padding-right: 15px;
    padding-left: 15px;
	flex-basis: unset;
}

.search-section.cart-area {
	display: none;
}

@media screen and (min-width: 768px){
	
	.search-section.logo-area {
		flex: 0 0 25%;
		max-width: 25%;
	}

	.search-section.form-area {
		flex: 0 0 75%;
		max-width: 75%;
	}

	.search-section.cart-area {
		display: none;
	}
	
}

@media screen and (min-width: 992px){

	.search-section.logo-area {
		flex: 0 0 17%;
		max-width: 17%;
		flex-basis: unset;
	}

	.search-section.form-area {
		flex: 0 0 83%;
		max-width: 83%;
		flex-basis: unset;
	}

	.search-section.cart-area {
	}

	
}

@media screen and (min-width: 1200px){

	.menu-section.cart-area {
		display: none;
	}

	.search-section.logo-area {
		flex: 0 0 17%;
		max-width: 17%;
	}

	.search-section.form-area {
		flex: 0 0 75%;
		max-width: 75%;
	}

	.search-section.cart-area {
		flex: 0 0 8%;
		max-width: 8%;
		display: block;
	}

}



</style>
    <!-- start header -->
   <header>
		<ec:menu-bar id="top_menu" 
				expand="xl" 
				style="light">
			<ed:container>
			<ec:menu-toggler menuID="top_menu_body">
				<ec:icon icon="bars"/>
			</ec:menu-toggler>
			<ec:right classStyle="menu-section cart-area"><a href="${plugins.ediacaran.sales.web_path}/cart"><ec:icon icon="shopping-cart" style="default" /></a></ec:right>			
			<ec:menu-bar-brand>
				<c:if test="${empty plugins.ediacaran.front.image_logo}">
					<h1>${plugins.ediacaran.front.text_logo}</h1>
				</c:if>
				<c:if test="${!empty plugins.ediacaran.front.image_logo}">
					<ec:image src="${plugins.ediacaran.front.image_logo}"/>
				</c:if>
				<%--<ec:image src="/img/logo.png"/>--%>
			</ec:menu-bar-brand>
			<ec:menu-body collapse="true">
				<ec:menu-itens align="right">
					<c:forEach items="${defaultMenu.itens}" var="menu">
						<c:choose>
	
							<c:when test="${not empty menu.body}">
								<ec:menu>
									<ec:menu-label>
										<c:if test="${not empty menu.icon}">
											<ec:icon icon="${menu.icon}" size="1"/>
										</c:if>
										${menu.fullName}
										<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
									</ec:menu-label>
									<ec:menu-itens resource="${menu.body}"/>
								</ec:menu>
							</c:when>
	
							<c:when test="${fn:length(menu.itens) != 0}">
								<ec:menu>
									<ec:menu-label>
										<c:if test="${not empty menu.icon}">
											<ec:icon icon="${menu.icon}" size="1"/>
										</c:if>
										${menu.fullName}
										<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
									</ec:menu-label>
									<ec:menu-itens id="menu_itens_xx">
										<c:forEach items="${menu.itens}" var="item">
											<ec:menu-item href="${item.resource}">
												<c:if test="${not empty item.icon}">
													<ec:icon icon="${item.icon}" size="1"/>
												</c:if>
												${item.fullName}
												<ec:badge id="${item.id}_badge" style="${empty item.badgeStyle? 'info' : item.badgeStyle}" type="navbar">${item.badge}</ec:badge>
											</ec:menu-item>
										</c:forEach>
									</ec:menu-itens>
								</ec:menu>
							</c:when>
	
							<c:otherwise>

								<ec:menu-item href="${menu.resource}">
									<c:if test="${not empty menu.icon}">
										<ec:icon icon="${menu.icon}" size="1"/>
									</c:if>
									${menu.fullName}
									<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
								</ec:menu-item>
							</c:otherwise>
							
						</c:choose>
					</c:forEach>
				</ec:menu-itens>
			</ec:menu-body>
			</ed:container>
		</ec:menu-bar>
		<section class="inner-headline top-header">
			<ed:container>
				<form action="${plugins.ediacaran.sales.web_path}/products/" method="post">
					<ed:row>
						<ed:col classStyle="search-section logo-area">
							<c:if test="${empty plugins.ediacaran.front.image_logo}">
								<h3>${plugins.ediacaran.front.text_logo}</h3>
							</c:if>
							<c:if test="${!empty plugins.ediacaran.front.image_logo}">
								<ec:image src="${plugins.ediacaran.front.image_logo}"/>
							</c:if>
						</ed:col>
						<ed:col classStyle="search-section form-area">
							<ec:center>
									<input type="hidden" name="resultPerPage" value="9">
									<ec:field-group>
										<ec:textfield 
											name="name" 
											value="${productSearch.name}"
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
						<ed:col classStyle="search-section cart-area">
							<ec:right><a href="${plugins.ediacaran.sales.web_path}/cart"><ec:icon icon="shopping-cart" style="default" /></a></ec:right>
						</ed:col>
					</ed:row>
				</form>
			</ed:container>
		</section>
	</header>

    <!-- end header -->