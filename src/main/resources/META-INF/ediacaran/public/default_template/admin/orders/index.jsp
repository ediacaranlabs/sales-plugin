<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.admin.default_template.service_manager.orders.index" var="sys_messages"/>
<section class="content-header">
	<h1>	
		<fmt:message key="show-orders.title" bundle="${sys_messages}"/><small><fmt:message key="show-orders.sub_title" bundle="${sys_messages}"/></small>
	</h1>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-cog"></i> <fmt:message key="show-orders.origin_menu" bundle="${sys_messages}"/></a></li>
		<li class="active"> <fmt:message key="show-orders.origin_sub_menu" bundle="${sys_messages}"/></li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title"><fmt:message key="show-orders.table.title" bundle="${sys_messages}"/></h3>
				</div>
				<!-- /.box-header -->
				<div class="box-body table-responsive no-padding">
					<table class="table table-hover">
						<tbody>
							<tr>
								<th><fmt:message key="show-orders.table.data" bundle="${sys_messages}"/></th>
								<th><fmt:message key="show-orders.table.status" bundle="${sys_messages}"/></th>
								<%--<th><fmt:message key="show-orders.table.payment_type" bundle="${sys_messages}"/></th>--%>
								<th><fmt:message key="show-orders.table.total" bundle="${sys_messages}"/></th>
								<th><fmt:message key="show-orders.table.action" bundle="${sys_messages}"/></th>
							</tr>
							<c:forEach var="order" items="${orders}">
							<tr>
								<td><fmt:formatDate dateStyle="MEDIUM" type="DATE" value="${order.date}"/></td>
								<td>${order.status.friendlyName}</td>
								<%--<td>${order.payment.paymentType.name}</td>--%>
								<td>${order.payment.currency} <fmt:formatNumber pattern="###,###,###.00" value="${order.payment.total}"/></td>
								<td>
								<a href="#!/service-manager/orders/show-order/${order.id.id}"
									class="btn btn-df btn-success"><fmt:message key="show-orders.table.button_show" bundle="${sys_messages}"/></a>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /.box -->
		</div>
	</div>
</section>