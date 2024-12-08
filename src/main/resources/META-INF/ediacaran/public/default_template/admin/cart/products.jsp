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

<c:if test="${Controller.cart.itens.size() == 0}">
	<ec:center id="empty_cart"><ec:icon icon="shopping-cart" size="3"/> <h3>Empty cart</h3></ec:center>
	<script type="text/javascript">
	$.AppContext.onload(function(){			
		var $nextButton = $.AppContext.utils.getById('products_next_button');
		$nextButton.setEnabled(false);
	});
	</script>
</c:if>

<c:if test="${Controller.cart.itens.size() != 0}">
	<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
		<ed:row>
			<ed:col size="12" id="product_content_cart_${productRequest.serial}">
				<script type="text/javascript">
					$.AppContext.utils.updateContentByID(
							'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/product-cart/${productRequest.serial}', 
							'product_content_cart_${productRequest.serial}'
					);
				</script>
			</ed:col>
		</ed:row>
	</c:forEach>
	<script type="text/javascript">
	$.AppContext.onload(function(){			
		var $nextButton = $.AppContext.utils.getById('products_next_button');
		$nextButton.setEnabled(true);
	});
	</script>
</c:if>