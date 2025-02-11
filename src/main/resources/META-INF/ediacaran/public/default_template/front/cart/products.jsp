<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>

<c:if test="${!empty productException}">
	<ec:alert id="exceptionMessage" style="danger">${productException.message}</ec:alert>
</c:if>
<style>

#delete-icon {
	cursor: pointer;
}

#products-list select {

}

#product-image {
  margin: auto;
}

@media (max-width: 992px) {
	#product-line {
		display: none;
	}
	
	#product-image {
		flex-grow: unset;
		margin-left: 2rem;
        margin-right: 2rem;
	}
}

@media (min-width: 992px) {
	#product-line-min {
		display: none;
	}
}


</style>
<ed:row>
	<ed:col id="products-list">
		<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
			<c:set var="productRequest" scope="request" value="${productRequest}" />
			<c:set var="productView" scope="request" value="${Controller.getProductCartView(productRequest.product.productType)}" />
			<c:set var="productViewVars" scope="request" value="${productView.vars}" />
			<c:if test="${!empty productView.view}">
				<ec:include uri="${productView.view}" resolved="${productView.resolvedView}" />
			</c:if>
		</c:forEach>
	</ed:col>
</ed:row>