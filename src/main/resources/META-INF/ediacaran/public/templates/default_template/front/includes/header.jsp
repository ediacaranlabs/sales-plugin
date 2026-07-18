<%@page trimDirectiveWhitespaces="true" %>

<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"              prefix="fn"%>
<ec:import-object id="menubar/front/default" var="defaultMenu"/>
<style>

.inner-headline {
	display: none;
}

.inner-headline.top-header{
	/*padding-top: 1em;
	padding-bottom: 1em;*/
	display: block;
}

.inner-headline.panel-menu{
	display: block;
}

.top-header a {
	color: #ffffff;
}

header .container {
	padding-top: 1em;
}

header .fa:not([class*="fa-inverse"]){
	color: #ffffff;
}

.navbar .navbar-toggler i {
	color: #ffffff;
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
		flex: 0 0 67%;
		max-width: 67%;
	}

	.menu-section.login {
		flex: 0 0 11%;
		max-width: 11%;
		display: block;
	}

	.menu-section.cart {
		flex: 0 0 5%;
		max-width: 5%;
		display: block;
	}

}

</style>
    <!-- start header -->
   <header>
		<section class="inner-headline top-header">
			<ed:container>
				<form action="${plugins.ediacaran.sales.web_path}/products/" method="post">
					<ed:row style="form">
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
						<ed:col classStyle="menu-section login">
							<c:if test="${not empty pageContext.request.userPrincipal}">
								Olá, <b><a href="${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}">${pageContext.request.userPrincipal.name}</a></b>
							</c:if>						
							<c:if test="${empty pageContext.request.userPrincipal}">
								Olá, <p><a href="${plugins.ediacaran.front.web_path}${plugins.ediacaran.front.panel_context}"><b>faça seu login!</b></a></p>
							</c:if>						
						</ed:col>
						<ed:col classStyle="menu-section cart">
							<ec:right><a href="${plugins.ediacaran.sales.web_path}/cart"><ec:icon icon="shopping-cart" style="default" /></a></ec:right>
						</ed:col>
					</ed:row>
				</form>
			</ed:container>
		</section>
	</header>

    <!-- end header -->