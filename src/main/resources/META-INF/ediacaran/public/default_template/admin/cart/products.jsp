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
	<ed:col id="products-list" size="9">
		<c:forEach var="productRequest" varStatus="step" items="${Controller.cart.itens}">
			<c:set var="productRequest" scope="request" value="${productRequest}" />
			<ec:include uri="${Controller.getProductCartView(productRequest.product.productType)}" resolved="true" />
		</c:forEach>
	</ed:col>
	<ed:col size="3">
		<c:forEach items="${Controller.widgets}" var="widget">
			<ed:row style="cart_widget_title">
				<ed:col>
					<h3>${widget.title}</h3>
					<hr>
				</ed:col>
			</ed:row>
			<ed:row style="cart_widget">
				<ed:col>
					<ec:include uri="${widget.resource}"/>
				</ed:col>
			</ed:row>
		</c:forEach>
			<ed:row style="cart_widget_title">
				<ed:col>
					<h3><fmt:message key="cart_review.table.summary" bundle="${messages}" /></h3>
					<hr>
				</ed:col>
			</ed:row>
			<ed:row style="cart_widget">
				<ed:col>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.sub_total" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_subtotal">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.subtotal}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.discount" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_discounts">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalDiscount}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.tax" bundle="${messages}"/>:
						</ed:col>
						<ed:col id="cart_taxes">
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalTax}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<fmt:message key="cart_review.checkout.total" bundle="${messages}"/>:
						</ed:col>
						<ed:col>
							<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.total}"/>
						</ed:col>
					</ed:row>
				</ed:col>
			</ed:row>
	</ed:col>
</ed:row>