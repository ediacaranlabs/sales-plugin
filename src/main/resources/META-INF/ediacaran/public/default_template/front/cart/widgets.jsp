<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>

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
		<h3><fmt:message key="cart_review.table.summary" bundle="${messages}"/></h3>
		<hr>
	</ed:col>
</ed:row>
<ed:row style="cart_widget">
	<ed:col>
		<ed:row style="form">
			<ed:col>
				<ed:row style="form">
					<ed:col><fmt:message key="cart_review.checkout.sub_total" bundle="${messages}"/></ed:col>
					<ed:col><ec:right><fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.subtotal}"/></ec:right></ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col><fmt:message key="cart_review.checkout.discount" bundle="${messages}"/></ed:col>
					<ed:col><ec:right><fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalDiscount}"/></ec:right></ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col><fmt:message key="cart_review.checkout.tax" bundle="${messages}"/></ed:col>
					<ed:col><ec:right><fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalTax}"/></ec:right></ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						<hr>
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col><h5><fmt:message key="cart_review.checkout.total" bundle="${messages}"/></h5></ed:col>
					<ed:col><h5><ec:right><fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.total}"/></ec:right></h5></ed:col>
				</ed:row>
			</ed:col>
		</ed:row>
		<%--<c:if test="${Controller.cart.totalItens > 0 && (empty vars.show_widget_itens || vars.show_widget_itens)}">--%>
		<c:if test="${Controller.cart.totalItens > 0}">
		<ed:row style="form">
			<ed:col>
				<c:if test="${Controller.cart.totalItens <= 1}">
					<fmt:message key="cart_review.table.item" bundle="${messages}"/>
				</c:if> 
				<c:if test="${Controller.cart.totalItens > 1}">
					<fmt:message key="cart_review.table.itens" bundle="${messages}"/>
				</c:if> 
				<fmt:message key="cart_review.table.itens.suffix" bundle="${messages}"/>
				<hr>
			</ed:col>
		</ed:row>
		<c:forEach items="${Controller.cart.itens}" var="item">
		<ed:row style="form">
			<ed:col>
				<ec:center><ec:image 
					style="fluid"
					src="${plugins.ediacaran.sales.image_prefix_address}${empty item.product.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : item.product.publicThumb}"/></ec:center>
			</ed:col>
			<ed:col>
				<ed:row style="form">
					<ed:col>
						${item.name}
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						Qty: ${item.units}
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						${item.symbol} <fmt:formatNumber pattern="###,###,##0.00" value="${item.total}"/>
					</ed:col>
				</ed:row>
			</ed:col>
		</ed:row>
		</c:forEach>
		</c:if>
	</ed:col>
</ed:row>
<%--			
<ed:row style="cart_widget_title">
	<ed:col>
		<h3><fmt:message key="cart_review.table.summary" bundle="${messages}" /></h3>
		<hr>
	</ed:col>
</ed:row>
<ed:row style="cart_widget">
	<ed:col>
		<ed:row style="form">
			<ed:col>
				<fmt:message key="cart_review.checkout.sub_total" bundle="${messages}"/>:
			</ed:col>
			<ed:col id="cart_subtotal">
				<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.subtotal}"/>
			</ed:col>
		</ed:row>
		<ed:row style="form">
			<ed:col>
				<fmt:message key="cart_review.checkout.discount" bundle="${messages}"/>:
			</ed:col>
			<ed:col id="cart_discounts">
				<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalDiscount}"/>
			</ed:col>
		</ed:row>
		<ed:row style="form">
			<ed:col>
				<fmt:message key="cart_review.checkout.tax" bundle="${messages}"/>:
			</ed:col>
			<ed:col id="cart_taxes">
				<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.totalTax}"/>
			</ed:col>
		</ed:row>
		<ed:row style="form">
			<ed:col>
				<fmt:message key="cart_review.checkout.total" bundle="${messages}"/>:
			</ed:col>
			<ed:col>
				<fmt:formatNumber pattern="###,###,##0.00" value="${Controller.cart.total}"/>
			</ed:col>
		</ed:row>
	</ed:col>
</ed:row>
--%>