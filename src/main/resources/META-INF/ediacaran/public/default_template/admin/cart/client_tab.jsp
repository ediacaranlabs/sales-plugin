<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="messages" locale="${locale}"/>
<ed:row>
	<ed:col>
		<ec:tabs id="client_tabs">
			<ec:tabs-item id="selected_client_tabs" title="#{tabs.client.title}" active="true" bundle="${messages}">
				<ec:form id="form_user" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/select/client" update="cart_result">
				<span id="client_data_view" formgroup="client">
					<ec:include uri="${vars.client_data_view}" resolved="true" />
				</span>
				</ec:form>
			</ec:tabs-item>
			<ec:tabs-item id="search_client_tabs" title="#{tabs.search.title}" bundle="${messages}">
			
				<ec:data-table id="user_form_search" 
					action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client/search">
					<ed:row>
						<ed:col size="3">
			    			<ec:textfield
			    				label="#{tabs.search.form.name.label}" 
			    				name="fullName" 
			    				bundle="${messages}"/>
						</ed:col>
						<ed:col size="3">
			    			<ec:textfield 
			    				label="#{tabs.search.form.email.label}"
			    				name="email" 
			    				bundle="${messages}"/>
						</ed:col>
						<ed:col size="3">
			    			<ec:select label="#{tabs.search.form.country.label}" name="country" bundle="${messages}">
			    				<ec:option value=""></ec:option>
			    				<c:forEach  items="${vars.countries}" var="country" >
				    				<ec:option value="${country.isoAlpha3}">${country.name}</ec:option>
			    				</c:forEach>
			    			</ec:select>
						</ed:col>
						<ed:col size="3">
			    			<ec:textfield 
			    				label="#{tabs.search.form.city.label}"
			    				name="city" 
			    				bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:button icon="search" label="#{tabs.search.form.search.label}"  actionType="submit" align="right" bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ec:data-result var="response">
					<ec:table style="striped">
						<ec:table-header>
							<ec:table-col>
								<ec:center><b><fmt:message key="tabs.search.result.firt_name.label" bundle="${messages}"/></b></ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center><b><fmt:message key="tabs.search.result.last_name.label" bundle="${messages}"/></b></ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center><b><fmt:message key="tabs.search.result.country.label" bundle="${messages}"/></b></ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center><fmt:message key="tabs.search.result.actions.label" bundle="${messages}"/></b></ec:center>
							</ec:table-col>
						</ec:table-header>
						<ec:table-body>
						<ec:forEach items="!{response.itens}" var="item">
						<ec:table-row>
							<ec:table-col>
								<ec:center>!{item.firstName}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center>!{item.lastName}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:center>!{item.country.name}</ec:center>
							</ec:table-col>
							<ec:table-col>
								<ec:button id="button_product_!{item.protectedID}" label="#{tabs.search.result.select.label}" icon="check" align="right" actionType="button" bundle="${messages}">
								<ec:event type="click">
									
									$.AppContext.utils.updateContentByID(
											'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client/!{item.protectedID}',
											'client_data_view'
									);
									
									var $tabs = $.AppContext.utils.getById('client_tabs');
									var $tab = $tabs.getItem("selected_client_tabs");
									$tab.select();

								</ec:event>
								</ec:button>
							</ec:table-col>
						</ec:table-row>
						</ec:forEach>
						</ec:table-body>
					</ec:table>
					</ec:data-result>
				</ec:data-table>
			</ec:tabs-item>
		</ec:tabs>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button label="#{next.label}" icon2="chevron-right" actionType="button"  align="right" form="form_user" bundle="${messages}">
			<ec:event type="click">
				var $userForm = $.AppContext.utils.getById('form_user');
				$userForm.submit(
					true, 
					"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/select/client", 
					"cart_result", function($resp){
					
						var $accordion = $.AppContext.utils.getById('cart_steps');
						var $item = $accordion.getItem('cart_client');
						var $nextItem = $item.getNext();
					
						if($nextItem.getAttribute("id") == "cart_address"){
						
							$.AppContext.utils.updateContentByID(
								"${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/address/select", 
								'client_address_form'
							);									
						
						}
						else
						if($nextItem.getAttribute("id") == "cart_payment"){
						
							$.AppContext.utils.updateContentByID(
								'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/widgets', 
								'cart_widgets'
							);
							$.AppContext.utils.updateContentByID(
								'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/payment-details', 
								'cart_payment_details'
							);
							
						}
					
						$nextItem.select();
					
					}
				);
			</ec:event>
		</ec:button>
		<ec:button label="#{new.label}" icon="plus" actionType="button" align="right" bundle="${messages}">
			<ec:event type="click">
				$.AppContext.utils.updateContentByID(
					/*'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit?type=simplified',*/ 
					'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/client', 
					'client_data_view'
				);									
			</ec:event>
		</ec:button>
		<ec:button label="#{search.label}" icon="search" actionType="button" align="right" bundle="${messages}">
			<ec:event type="click">
				var $tabs = $.AppContext.utils.getById('client_tabs');
				var $tab = $tabs.getItem("search_client_tabs");
				$tab.select();
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>