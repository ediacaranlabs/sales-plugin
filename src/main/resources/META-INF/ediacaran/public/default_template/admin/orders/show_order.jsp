<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.admin.default_template.service_manager.orders.show_order" var="sys_messages"/>
<section class="content-header">
	<h1>
		<fmt:message key="show_order.title" bundle="${sys_messages}"/> <small>#${vars.order.id.id}</small>
	</h1>
	<ol class="breadcrumb">
		<li><a href="#"><i class="fa fa-cog"></i> <fmt:message key="show_order.origin_menu" bundle="${sys_messages}"/></a></li>
		<li>
			<a href="#!/service-manager/orders">
			<fmt:message key="show_order.origin_sub_menu" bundle="${sys_messages}"/>
			</a>
		</li>
		<li class="active">
			<fmt:message key="show_order.title" bundle="${sys_messages}"/> #${vars.order.id.id}
		</li>
	</ol>
</section>
<section class="invoice">
	<!-- title row -->
	<div class="row">
		<div class="col-xs-12">
			<h2 class="page-header">
				<i class="fa fa-globe"></i> UoUTec <small class="pull-right"><fmt:message key="show_order.date" bundle="${sys_messages}"/>:
					<fmt:formatDate dateStyle="MEDIUM" type="DATE" value="${vars.order.date}"/></small>
			</h2>
		</div>
		<!-- /.col -->
	</div>
	<!-- info row -->
	<div class="row invoice-info">
		<div class="col-sm-4 invoice-col">
			<b><fmt:message key="show_order.invoice_id" bundle="${sys_messages}"/> #${vars.order.invoice.id}</b><br>
			<br> 
			<b><fmt:message key="show_order.order_id" bundle="${sys_messages}"/>:</b> #${vars.order.id.id}<br>
			<b><fmt:message key="show_order.payment_due" bundle="${sys_messages}"/>:</b> <fmt:formatDate dateStyle="MEDIUM" type="DATE" value="${vars.order.date}"/><br>
			<b><fmt:message key="show_order.account" bundle="${sys_messages}"/>:</b> ${vars.order.id.systemUserId}<br>
			<b><fmt:message key="show_order.status" bundle="${sys_messages}"/>:</b> ${vars.order.status.friendlyName}
		</div>
		<!-- /.col -->
	</div>
	<!-- /.row -->
	<!-- Table row -->
	<div class="row">
		<div class="col-xs-12 table-responsive">
			<table class="table table-striped">
				<thead>
					<tr>
						<th><fmt:message key="show_order.table_product.quantity" bundle="${sys_messages}"/></th>
						<th><fmt:message key="show_order.table_product.product" bundle="${sys_messages}"/></th>
						<th><fmt:message key="show_order.table_product.serial" bundle="${sys_messages}"/></th>
						<th><fmt:message key="show_order.table_product.description" bundle="${sys_messages}"/></th>
						<th><fmt:message key="show_order.table_product.subtotal" bundle="${sys_messages}"/></th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${vars.order.itens}" var="product">
					<tr>
						<td>${product.units}</td>
						<td>${product.product.name}</td>
						<td>${product.serial}</td>
						<td>${product.product.description}</td>
						<td>${product.currency} <fmt:formatNumber pattern="###,###,##0.00"  value="${product.subtotal}"/></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- /.col -->
	</div>
	<!-- /.row -->

	<div class="row">
		<div class="col-xs-6">
		    <!-- payment method page -->
		    <c:if test="${!empty vars['payment_view']}">
				<jsp:include page="${vars['payment_view']}"/>
			</c:if>
		</div>
		<!-- /.col -->
		<div class="col-xs-6">
			<p class="lead">
				<fmt:message key="show_order.payment_due" bundle="${sys_messages}"/>
				<fmt:formatDate dateStyle="MEDIUM" type="DATE" value="${vars.order.date}"/>
			</p>

			<div class="table-responsive">
				<table class="table">
					<tbody>
						<tr>
							<th style="width: 50%"><fmt:message key="show_order.table_product.subtotal" bundle="${sys_messages}"/>:</th>
							<td>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.value}"/></td>
						</tr>
						<tr>
							<th><fmt:message key="show_order.table_product.discount" bundle="${sys_messages}"/>:</th>
							<td>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.discount}"/></td>
						</tr>
						<tr>
							<th><fmt:message key="show_order.table_product.tax" bundle="${sys_messages}"/></th>
							<td>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.tax}"/></td>
						</tr>
						<tr>
							<th><fmt:message key="show_order.table_product.shipping" bundle="${sys_messages}"/>:</th>
							<td>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.shipping.value}"/></td>
						</tr>
						<tr>
							<th><fmt:message key="show_order.table_product.total" bundle="${sys_messages}"/>:</th>
							<td>${vars.order.payment.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${vars.order.payment.total}"/></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<!-- /.col -->
	</div>
	<!-- /.row -->

    <%--
	<!-- this row will not appear when printing -->
	<div class="row no-print">
		<div class="col-xs-12">
			<a href="invoice-print.html" target="_blank" class="btn btn-default"><i
				class="fa fa-print"></i> Print</a>
			<button type="button" class="btn btn-success pull-right">
				<i class="fa fa-credit-card"></i> Submit Payment
			</button>
			<button type="button" class="btn btn-primary pull-right"
				style="margin-right: 5px;">
				<i class="fa fa-download"></i> Generate PDF
			</button>
		</div>
	</div>
	 --%>
</section>