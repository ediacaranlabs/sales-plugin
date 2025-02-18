<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>
<ec:box>
	<ec:box-header>
		<h3>${productRequest.name}</h3>
	</ec:box-header>
	<ec:box-body>
	
		<ed:row id="product-line-min">
			<ed:col id="product-image">
				<ec:center><ec:image 
				 	style="fluid"
					src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.productRequest.product.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.productRequest.product.publicThumb}"/></ec:center>
			</ed:col>
			<ed:col>
				<ed:row>
					<ed:col size="12">
						${productRequest.description}
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:form id="update-item-cart-form-min-${productRequest.serial}" classStyle="form-group has-feedback">
							<ec:field-group>
								<ec:textfield name="units" value="${productRequest.units}">
									<ec:event type="change">
										var $form  = $.AppContext.utils.getById('update-item-cart-form-min-${productRequest.serial}');
										var $units = $form.getField('units');
										var $qty   = $units.getValue();
										
										var $intQTY = parseInt($qty);
														
										if($intQTY > 0){
											$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/widgets', 'cart_widgets');
											$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
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
							<%--
							<ec:select name="units"> <!-- style="width: 60px" -->
								<ec:option value="1" selected="${productRequest.units == 1}">1${empty productRequest.product.measurementUnit || productRequest.product.measurementUnit == 'UNITS'? '' : '/'.concat(productRequest.product.measurementUnit.getName(locale))}</ec:option>
								<c:if test="${productRequest.maxExtra > 1}">
									<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
										<ec:option value="${units}" selected="${productRequest.units == units}">${units}${empty productRequest.product.measurementUnit || productRequest.product.measurementUnit == 'UNITS'? '' : '/'.concat(productRequest.product.measurementUnit.getName(locale))}</ec:option>
									</c:forEach>
								</c:if>
								<ec:event type="change">
									var $form  = $.AppContext.utils.getById('update-item-cart-form-min-${productRequest.serial}');
									var $units = $form.getField('units');
									var $qty   = $units.getValue();
									$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
								</ec:event>
							</ec:select>
							 --%>
						</ec:form>
					</ed:col>
					<ed:col id="cart_item_value_${step.index}">
						<ec:center>
							${productRequest.product.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
						</ec:center>
					</ed:col>
					<ed:col id="cart_item_description_${step.index}">
						<ec:center>
							<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
								<input type="hidden" name="product" value="${productRequest.serial}">
								<ec:button label="" icon="trash" actionType="submit" style="danger"/>
							</ec:form>
						</ec:center>
					</ed:col>
				</ed:row>
			</ed:col>
		</ed:row>
		
		
		<ed:row id="product-line">
			<ed:col size="2">
				<ec:center>
				<ec:image 
			 		style="fluid"
					src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.productRequest.product.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.productRequest.product.publicThumb}"/>
				</ec:center>
			</ed:col>
			<ed:col size="4" id="cart_item_description_${step.index}">
				<ec:center>${productRequest.description}</ec:center>
				<p>
			</ed:col>
			<ed:col size="3">
				<ec:form id="update-item-cart-form-${productRequest.serial}" classStyle="form-group has-feedback">
					<ec:field-group>
						<ec:textfield name="units" value="${productRequest.units}">
							<ec:event type="change">
								var $form  = $.AppContext.utils.getById('update-item-cart-form-${productRequest.serial}');
								var $units = $form.getField('units');
								var $qty   = $units.getValue();
								
								var $intQTY = parseInt($qty);
												
								if($intQTY > 0){
										$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/widgets', 'cart_widgets');
									$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
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
					<%--
					<ec:select name="units"> <!-- style="width: 60px" -->
						<ec:option value="1" selected="${productRequest.units == 1}">1${empty productRequest.product.measurementUnit || productRequest.product.measurementUnit == 'UNITS'? '' : '/'.concat(productRequest.product.measurementUnit.getName(locale))}</ec:option>
						<c:if test="${productRequest.maxExtra > 1}">
							<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
								<ec:option value="${units}" selected="${productRequest.units == units}">${units}${empty productRequest.product.measurementUnit || productRequest.product.measurementUnit == 'UNITS'? '' : '/'.concat(productRequest.product.measurementUnit.getName(locale))}</ec:option>
							</c:forEach>
						</c:if>
						<ec:event type="change">
							var $form  = $.AppContext.utils.getById('update-item-cart-form-${productRequest.serial}');
							var $units = $form.getField('units');
							var $qty   = $units.getValue();
							$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
						</ec:event>
					</ec:select>
					--%>
				</ec:form>
			</ed:col>
			<ed:col id="cart_item_value_${step.index}"  size="2">
				<ec:center>
						${productRequest.product.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${productRequest.subtotal}"/>
				</ec:center>
			</ed:col>
			<ed:col id="cart_item_description_${step.index}"  size="1">
				<ec:form id="remove-item-cart-form-${productRequest.serial}" method="POST" action="${pageContext.request.contextPath}/cart/remove">
					<input type="hidden" name="product" value="${productRequest.serial}">
					<ec:button label="" icon="trash" align="center" actionType="submit" style="danger"/>
				</ec:form>
			</ed:col>
		</ed:row>
	
	</ec:box-body>
</ec:box>
