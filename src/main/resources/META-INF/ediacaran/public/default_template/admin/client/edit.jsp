<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>

<ec:setBundle var="messages" locale="${locale}"/>

<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2>Client</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Client" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="Clients" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:form id="client_form" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/save" update="client_update_result">
	<ec:box>
		<ec:box-body>
			<span id="client_data_view" formgroup="client">
				<ec:include uri="${vars.client_data_view}" resolved="true" />
			</span>
			<ec:tabs>
				<ec:tabs-item title="Billing address" active="true">
					<ed:row>
						<ed:col>
							<span formgroup="client">
							<ec:checkbox label="use default" name="useDefaultBillingAddress" value="true" selected="${empty vars.client.protectedID || vars.client.useDefaultBillingAddress}">
								<ec:event type="change">
									var $form = $event.source.getFirstParent(function($e){
										return $e.getTagName() == 'form';
									});
									
									if($form == null){
										return;
									}
									
									var $defaultBillingAddress = $form.getField('client.useDefaultBillingAddress');
									var $checked = $defaultBillingAddress.getValue() === 'true';
									
									if($checked){
										$.AppContext.utils.content.update("billingAddress", "");
									}
									else{
										$.AppContext.utils.loadResourceContent("billingAddress", "${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/address");
									}
									
								</ec:event>
							</ec:checkbox>
							</span>
						</ed:col>
					</ed:row>
						<ed:row>
							<ed:col>
								<span id="billingAddress" formgroup="billingAddress">
									<c:if test="${!vars.client.useDefaultBillingAddress}">
										<c:set var="address" value="${vars.billing_address}" scope="request"/>
											<jsp:include page="address.jsp"/>
									</c:if>
								</span>	
							</ed:col>
						</ed:row>
				</ec:tabs-item>
				<ec:tabs-item title="Shipping address">
				
					<ed:row>
						<ed:col>
							<span formgroup="client">
							<ec:checkbox label="use default" name="useDefaultShippingAddress" value="true" selected="${empty vars.client.protectedID || vars.client.useDefaultShippingAddress}">
								<ec:event type="change">
									var $form = $event.source.getFirstParent(function($e){
										return $e.getTagName() == 'form';
									});
									
									if($form == null){
										return;
									}
								
									var $defaultShippingAddress = $form.getField('client.useDefaultShippingAddress');
									var $checked = $defaultShippingAddress.getValue() === 'true';
									var $addshippingAddressButton = $.AppContext.utils.getById('addshippingAddressButton');
									
									if($checked){
										//$.AppContext.utils.content.update("shippingAddressList", "");
										$addshippingAddressButton.setEnabled(false);						
									}
									else{
										$addshippingAddressButton.setEnabled(true);						
									}
									
								</ec:event>
							</ec:checkbox>
							</span>
						</ed:col>
					</ed:row>
						<ed:row>
							<ed:col id="shippingAddressList">
								<c:forEach items="${vars.shipping_addresses}" var="shippingAddress">
									<c:set var="address" value="${shippingAddress}" scope="request"/>
									<jsp:include page="address_group.jsp"/>
								</c:forEach>
							</ed:col>
						</ed:row>
					<ed:row>
						<ed:col>
							<ec:button id="addshippingAddressButton" label="Add Address" align="right" actionType="button" enabled="${!vars.client.useDefaultShippingAddress}">
								<ec:event type="click">
									$.AppContext.utils.appendContentByID("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/address/group", "shippingAddressList");
								</ec:event>
							</ec:button>
						</ed:col>
					</ed:row>
			
				</ec:tabs-item>				
			</ec:tabs>			
			<ed:row>
				<ed:col id="client_update_result"></ed:col>
			</ed:row>
		</ec:box-body>
		<ec:box-footer>
			<ec:button label="Save" align="right" actionType="submit"/>
		</ec:box-footer>
	</ec:box>
</ec:form>
