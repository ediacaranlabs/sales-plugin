<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<style>
html {
  scroll-behavior: smooth;
}
</style>
<ec:setBundle var="messages" locale="${locale}"/>
<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2><fmt:message key="title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{origin_sub_menu}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders"  bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box classStyle="order-box">
	<ec:box-header classStyle="full-mode"><b><fmt:message key="title" bundle="${messages}"/></b> #${vars.order.id}</ec:box-header>
	<ec:box-body>
	
		<ed:row style="form">
			<ed:col size="4">
				<ed:row style="form">
					<ed:col>
						<h3><fmt:message key="date" bundle="${messages}"/>: ${vars.order.toStringDate(locale)}</h3>
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						<b><fmt:message key="order_id" bundle="${messages}"/>:</b> #${vars.order.id}<br>
						<b><fmt:message key="payment_due" bundle="${messages}"/>:</b> ${vars.order.toStringDate(locale)}<br>
						<b><fmt:message key="status" bundle="${messages}"/>:</b> ${vars.order.status.getName(locale)}
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="4" classStyle="full-mode">
				<ed:row style="form">
					<ed:col>
						<h3><fmt:message key="billing_address.title" bundle="${messages}"/></h3>
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						${vars.order.billingAddress.firstName} ${vars.order.billingAddress.lastName}<br>
						${vars.order.billingAddress.addressLine1}<br>
						${vars.order.billingAddress.addressLine2}<br>
						${vars.order.billingAddress.zip} ${vars.order.billingAddress.city} ${vars.order.billingAddress.region} ${vars.order.billingAddress.country.name}
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="4" classStyle="full-mode">
				<ed:row style="form">
					<ed:col>
						<h3><fmt:message key="shipping_address.title" bundle="${messages}"/></h3>
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col>
						${vars.order.shippingAddress.firstName} ${vars.order.shippingAddress.lastName}<br>
						${vars.order.shippingAddress.addressLine1}<br>
						${vars.order.shippingAddress.addressLine2}<br>
						${vars.order.shippingAddress.zip} ${vars.order.shippingAddress.city} ${vars.order.shippingAddress.region} ${vars.order.shippingAddress.country.name}
					</ed:col>
				</ed:row>
			</ed:col>
		</ed:row>
		
		<ed:row classStyle="simplified-mode">
			<ed:col>
				<ec:carousel>
					<c:forEach items="${vars.order.itensResponse}" var="product">
						<ec:carousel-item>

									<ed:row style="form">
										<ed:col>
											<c:if test="${product.product.publicThumb == null}">
												<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png" style="fluid" align="center"/>
											</c:if>
											<c:if test="${product.product.publicThumb != null}">
												<ec:image src="${plugins.ediacaran.sales.image_prefix_address}${product.product.publicThumb}" style="fluid" align="center"/>
											</c:if>
										</ed:col>
									</ed:row>
									<ed:row style="form">
										<ed:col>
											<b>${product.product.name}</b><br>
											<b><fmt:message key="table_product.quantity" bundle="${messages}"/></b>: ${product.units}<br>
											<b><fmt:message key="table_product.subtotal" bundle="${messages}"/></b>: ${product.displaySubtotal}<br>
											<b><fmt:message key="table_product.discount" bundle="${messages}"/></b>: ${product.displayDiscount}<br>
											<b><fmt:message key="table_product.tax" bundle="${messages}"/></b>: ${product.displayTax}<br>
											<b><fmt:message key="table_product.total" bundle="${messages}"/></b>: ${product.displayTotal}<br>
										</ed:col>
									</ed:row>
									
									<ed:row>
										<ed:col>
											<ec:tabs>
												<ec:tabs-item active="true" title="#{widgets.payment}" bundle="${messages}">
													<span id="payment_type_tab">
												    <c:if test="${!empty vars['payment_view']}">
												    	<ec:include uri="${vars['payment_view']}" resolved="true" />
												    </c:if>
													</span>
												</ec:tabs-item>
												
												<ec:tabs-item title="#{tabs.shipping.title}" bundle="${messages}">
													<ec:table>
														<ec:table-header>
															<ec:table-col><center><small><fmt:message key="table_shipping.date" bundle="${messages}"/></small></center></ec:table-col>
															<ec:table-col><small><fmt:message key="table_shipping.dest" bundle="${messages}"/></small></ec:table-col>
															<ec:table-col><center><small><fmt:message key="table_shipping.actions" bundle="${messages}"/></small></center></ec:table-col>
														</ec:table-header>
														<ec:table-body>
															<c:forEach items="${vars.shippings}" var="shipping">
															<ec:table-row style="${shipping.cancelDate != null? 'danger' : ''}">
																<ec:table-col><center><small>${shipping.toStringDate(locale)}</small></center></ec:table-col>
																<ec:table-col>
																	<small>
																		${shipping.dest.firstName} ${vars.shipping.dest.lastName}<br>
																		${shipping.dest.addressLine1}<br>
																		${shipping.dest.addressLine2}<br>
																		${shipping.dest.zip} ${shipping.dest.city} ${shipping.dest.region} ${shipping.dest.country.name}
																	</small>
																</ec:table-col>
																<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/shippings/show/${shipping.id}">
																	<fmt:message key="table_shipping.actions.details" bundle="${messages}"/>
																</a></small></center></ec:table-col>
															</ec:table-row>
															</c:forEach>
														</ec:table-body>
													</ec:table>												
												</ec:tabs-item>
												
												<ec:tabs-item title="#{tabs.order_report.title}" bundle="${messages}">
													<ec:table>
														<ec:table-header>
															<ec:table-col><center><small><fmt:message key="table_report.date" bundle="${messages}"/></small></center></ec:table-col>
															<ec:table-col><center><small><fmt:message key="table_report.status" bundle="${messages}"/></small></center></ec:table-col>
															<ec:table-col><center><small><fmt:message key="table_report.actions" bundle="${messages}"/></small></center></ec:table-col>
														</ec:table-header>
														<ec:table-body>
															<c:forEach items="${vars.orderReportList}" var="orderReport">
															<ec:table-row>
																<ec:table-col><center><small>${orderReport.toStringDate(locale)}</small></center></ec:table-col>
																<ec:table-col><center><small>${orderReport.status.getName(locale)}</small></center></ec:table-col>
																<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/show/${orderReport.id}">
																	<fmt:message key="table_report.actions.details" bundle="${messages}"/>
																</a></small></center></ec:table-col>
															</ec:table-row>
															</c:forEach>
														</ec:table-body>
													</ec:table>
												</ec:tabs-item>

											</ec:tabs>
										</ed:col>
									</ed:row>

						</ec:carousel-item>
					</c:forEach>
				</ec:carousel>
			</ed:col>
		</ed:row>
		
		<ed:row classStyle="full-mode">
			<ed:col>
				<ec:table>
					<ec:table-header>
						<ec:table-col><center><fmt:message key="table_product.serial" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.product" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.quantity" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.subtotal" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.discount" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.tax" bundle="${messages}"/></center></ec:table-col>
						<ec:table-col><center><fmt:message key="table_product.total" bundle="${messages}"/></center></ec:table-col>
					</ec:table-header>
					<ec:table-body>
						<c:forEach items="${vars.order.itens}" var="product">
							<ec:table-row>
								<ec:table-col><center>${product.serial}</center></ec:table-col>
								<ec:table-col><center>${product.product.name}</center></ec:table-col>
								<ec:table-col><center>${product.units}</center></ec:table-col>
								<ec:table-col><center>${product.displaySubtotal}</center></ec:table-col>
								<ec:table-col><center>${product.displayDiscount}</center></ec:table-col>
								<ec:table-col><center>${product.displayTax}</center></ec:table-col>
								<ec:table-col><center>${product.displayTotal}</center></ec:table-col>
							</ec:table-row>
						</c:forEach>
					</ec:table-body>
				</ec:table>
			</ed:col>
		</ed:row>

		<ed:row id="payment_due" classStyle="full-mode">
			<ed:col size="12">
				<p>
					<fmt:message key="payment_due" bundle="${messages}"/>
					${vars.order.toStringDate(locale)}
				</p>
			</ed:col>
		</ed:row>
		<ed:row classStyle="full-mode">
			<ed:col size="12">
				<ec:tabs>
					<ec:tabs-item title="#{widgets.payment}" bundle="${messages}" active="true">
						<span id="payment_type_tab">
					    <c:if test="${!empty vars['payment_view']}">
					    	<ec:include uri="${vars['payment_view']}" resolved="true" />
					    </c:if>
						</span>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.totals.title}" bundle="${messages}">
					
						<ec:description-list>
							<ec:description title="#{table_product.subtotal}" truncate="false" bundle="${messages}">
								${vars.order.displaySubtotal}
							</ec:description>

							<ec:description title="#{table_product.discount}" truncate="false" bundle="${messages}">
								${vars.order.displayDiscount}
							</ec:description>
							
							<ec:description title="#{table_product.tax}" truncate="false" bundle="${messages}">
								${vars.order.displayTax}
							</ec:description>
						</ec:description-list>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.invoices.title}" bundle="${messages}">
					
						<ec:table>
							<ec:table-header>
								<ec:table-col><center><small><fmt:message key="table_invoice.id" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.subtotal" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.tax" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.discount" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.total" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_invoice.actions" bundle="${messages}"/></small></center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.invoices}" var="invoice">
								<c:if test="${invoice.cancelDate == null}">
								<ec:table-row>
									<ec:table-col><center><small>${invoice.id}</small></center></ec:table-col>
									<ec:table-col><center><small>${vars.order.toStringDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displaySubtotal}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayDiscount}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayTax}</small></center></ec:table-col>
									<ec:table-col><center><small>${invoice.displayTotal}</small></center></ec:table-col>
									<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/invoices/show/${invoice.id}">
										<fmt:message key="table_invoice.actions.details" bundle="${messages}"/>
									</a></small></center></ec:table-col>
								</ec:table-row>
								</c:if>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.shipping.title}" bundle="${messages}">
					
						<ec:table>
							<ec:table-header>
								<ec:table-col><center><small><fmt:message key="table_shipping.id" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.cancel_date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.type" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><small><fmt:message key="table_shipping.dest" bundle="${messages}"/></small></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_shipping.actions" bundle="${messages}"/></small></center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.shippings}" var="shipping">
								<ec:table-row style="${shipping.cancelDate != null? 'danger' : ''}">
									<ec:table-col><center><small>${shipping.id}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.toStringDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.toStringCancelDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${shipping.shippingType}</small></center></ec:table-col>
									<ec:table-col>
										<small>
											${shipping.dest.firstName} ${vars.shipping.dest.lastName}<br>
											${shipping.dest.addressLine1}<br>
											${shipping.dest.addressLine2}<br>
											${shipping.dest.zip} ${shipping.dest.city} ${shipping.dest.region} ${shipping.dest.country.name}
										</small>
									</ec:table-col>
									<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/shippings/show/${shipping.id}">
										<fmt:message key="table_shipping.actions.details" bundle="${messages}"/>
									</a></small></center></ec:table-col>
								</ec:table-row>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<ec:tabs-item title="#{tabs.order_report.title}" bundle="${messages}">
					
						<ec:table>
							<ec:table-header>
								<ec:table-col><center><small><fmt:message key="table_report.id" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_report.date" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_report.status" bundle="${messages}"/></small></center></ec:table-col>
								<ec:table-col><center><small><fmt:message key="table_report.actions" bundle="${messages}"/></small></center></ec:table-col>
							</ec:table-header>
							<ec:table-body>
								<c:forEach items="${vars.orderReportList}" var="orderReport">
								<ec:table-row>
									<ec:table-col><center><small>${orderReport.id}</small></center></ec:table-col>
									<ec:table-col><center><small>${orderReport.toStringDate(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small>${orderReport.status.getName(locale)}</small></center></ec:table-col>
									<ec:table-col><center><small><a href="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/show/${orderReport.id}">
										<fmt:message key="table_report.actions.details" bundle="${messages}"/>
									</a></small></center></ec:table-col>
								</ec:table-row>
								</c:forEach>
							</ec:table-body>
						</ec:table>
					</ec:tabs-item>
					<%--
					<c:forEach items="${vars.widgets}" var="widget">
						<ec:tabs-item title="${widget.title}" >
							<span id="${widget.id}_tab">
								<script type="text/javascript">
									$.AppContext.onload(function(){			
										$.AppContext.utils
											.updateContentByID(
													"#!${widget.resource}", 
													"${widget.id}_tab"
											);
									});	
								</script>
							</span>
						</ec:tabs-item>
					</c:forEach>
					--%>
				</ec:tabs>
			</ed:col>
		</ed:row>
		
	</ec:box-body>
	<ec:box-footer>
		<ec:button label="#{actions.back}" align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders');			
			</ec:event>
		</ec:button>
		<c:if test="${vars.order.status.allowedCreateOrderReport}">
			<ec:button label="#{actions.support}" align="right" bundle="${messages}">
				<ec:event type="click">
					$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.panel_context}/orders/report/new/${vars.order.id}');			
				</ec:event>
			</ec:button>
		</c:if>
	</ec:box-footer>
</ec:box>
