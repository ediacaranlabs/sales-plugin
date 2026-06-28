<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<ec:box>
	<ec:box-header>
		<h3>Completed orders</h3>
	</ec:box-header>
	<ec:box-body>
		<ec:table>
			<ec:table-header>
				<ec:table-col>
					<ec:center>Product</ec:center>
				</ec:table-col>
				<ec:table-col>
					<ec:center>Date</ec:center>
				</ec:table-col>
				<ec:table-col>
					<ec:center>Actions</ec:center>
				</ec:table-col>
			</ec:table-header>
			<ec:table-body>
				<c:forEach items="${vars.orders}" var="order">
					<c:forEach items="${order.itens}" var="product">
						<ec:table-row>
							<ec:table-col>
								<ec:center>${product.product.name}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center>${order.toStringDate(locale)}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/show/${order.id}">
										Details
								</a></small></center>						
							</ec:table-col>
						</ec:table-row>
					</c:forEach>
				</c:forEach>
			</ec:table-body>
		</ec:table>
	</ec:box-body>
</ec:box>