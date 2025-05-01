<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>

<ec:box>
	<ec:box-header>
		<h3>${productRequest.name}</h3>
	</ec:box-header>
	<ec:box-body>
		<c:if test="${!empty exception}">
			<ec:alert id="exceptionMessage" style="danger">
				${exception.message}
			</ec:alert>
		</c:if>
		
		<ed:row style="form">
			<ed:col size="3">
				<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
			</ed:col>
			<ed:col size="4" id="cart_item_description_${step.index}">
				<ed:row style="form">
					<ed:col>
						<ed:row>
							<ed:col>
								<b>${productRequest.name}</b>
								<p>${productRequest.shortDescription}</p>
							</ed:col>
						</ed:row>

						<ed:row>
							<ed:col>

								<ec:form id="update-item-cart-form-${productRequest.serial}">
									<ec:event type="submit">
										$event.handler.preventDefault();
									</ec:event>
									<ec:field-group>
										<ec:textfield name="units" value="${productRequest.units}" > <!-- style="width: 60px" -->
											<ec:event type="change">
												var $form  = $.AppContext.utils.getById('update-item-cart-form-${productRequest.serial}');
												var $units = $form.getField('units');
												var $qty   = $units.getValue();
					
												var $intQTY = parseInt($qty);
												
												if($intQTY > 0){
													$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/units/${productRequest.serial}/' + $qty, "product_content_cart_${productRequest.serial}");
													$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/widgets', 'cart_widgets');
													$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/payment-details', 'cart_payment_details');
												}
											</ec:event>
											<ec:field-validator>
												<ec:field-validator-rule name="notEmpty" message="#{form.units.validation.notEmpty}" bundle="${messages}"/>
												<ec:field-validator-rule name="between" message="#{form.between.validation.notEmpty}" bundle="${messages}">
														<ec:field-validator-param name="min">0</ec:field-validator-param>
														<ec:field-validator-param name="max">${empty productRequest.maxExtra || productRequest.maxExtra < 0? 1 : productRequest.maxExtra}</ec:field-validator-param>
												</ec:field-validator-rule>
											</ec:field-validator>
										</ec:textfield>
										<c:if test="${!empty productRequest.product.measurementUnit && productRequest.product.measurementUnit != 'UNITS'}">
											<ec:append-field>
												<ec:prepend-field-item>${empty productRequest.product.measurementUnit || productRequest.product.measurementUnit == 'UNITS'? '' : '/'.concat(productRequest.product.measurementUnit.getName(locale))}</ec:prepend-field-item>
											</ec:append-field>
										</c:if>
									</ec:field-group>
									
								</ec:form>



							</ed:col>
						</ed:row>
							
					</ed:col>
				</ed:row>
			</ed:col>
			<ed:col size="4">
				<ec:center>
						${productRequest.product.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
				</ec:center>
			</ed:col>
			<ed:col id="cart_item_value_${step.index}" size="1">
					<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/remove" update="cart_result">
						<input type="hidden" name="product" value="${productRequest.serial}">
						<ec:button label="" icon="trash" align="right" actionType="submit" style="danger"/>
					</ec:form>
			</ed:col>
		</ed:row>
		
	</ec:box-body>
</ec:box>
