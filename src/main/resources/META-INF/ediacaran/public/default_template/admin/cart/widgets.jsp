<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
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
			