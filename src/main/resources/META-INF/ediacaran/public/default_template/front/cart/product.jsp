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
				<ec:center><ec:image src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/></ec:center>
			</ed:col>
			<ed:col>
				<ed:row>
					<ed:col size="12">
						${productRequest.description}
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:form id="update-item-cart-form-min-${productRequest.serial}">
							<ec:select name="units"> <!-- style="width: 60px" -->
								<ec:option value="1" selected="${productRequest.units == 1}">1${empty productRequest.product.periodType || productRequest.product.periodType == 'UNITS'? '' : '/'.concat(productRequest.product.periodType.getName(locale))}</ec:option>
								<c:if test="${productRequest.maxExtra > 1}">
									<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
										<ec:option value="${units}" selected="${productRequest.units == units}">${units}${empty productRequest.product.periodType || productRequest.product.periodType == 'UNITS'? '' : '/'.concat(productRequest.product.periodType.getName(locale))}</ec:option>
									</c:forEach>
								</c:if>
								<ec:event type="change">
									var $form  = $.AppContext.utils.getById('update-item-cart-form-min-${productRequest.serial}');
									var $units = $form.getField('units');
									var $qty   = $units.getValue();
									$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
								</ec:event>
							</ec:select>
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
				<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
			</ed:col>
			<ed:col size="4" id="cart_item_description_${step.index}">
				<ec:center>${productRequest.description}</ec:center>
				<p>
			</ed:col>
			<ed:col size="3">
				<ec:form id="update-item-cart-form-${productRequest.serial}">
					<ec:select name="units"> <!-- style="width: 60px" -->
						<ec:option value="1" selected="${productRequest.units == 1}">1${empty productRequest.product.periodType || productRequest.product.periodType == 'UNITS'? '' : '/'.concat(productRequest.product.periodType.getName(locale))}</ec:option>
						<c:if test="${productRequest.maxExtra > 1}">
							<c:forEach var="units" begin="2" end="${productRequest.maxExtra + 1}">
								<ec:option value="${units}" selected="${productRequest.units == units}">${units}${empty productRequest.product.periodType || productRequest.product.periodType == 'UNITS'? '' : '/'.concat(productRequest.product.periodType.getName(locale))}</ec:option>
							</c:forEach>
						</c:if>
						<ec:event type="change">
							var $form  = $.AppContext.utils.getById('update-item-cart-form-${productRequest.serial}');
							var $units = $form.getField('units');
							var $qty   = $units.getValue();
							$.AppContext.utils.updateContentByID('${plugins.ediacaran.sales.web_path}/cart/units/${productRequest.serial}/' + $qty, "product_content");
						</ec:event>
					</ec:select>
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
