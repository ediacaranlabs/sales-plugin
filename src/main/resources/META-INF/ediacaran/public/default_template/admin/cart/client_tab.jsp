<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>
<ed:row>
	<ed:col>
		<ec:tabs id="client_tabs">
			<ec:tabs-item id="selected_client_tabs" title="Client" active="true">
				<ec:form id="form_user" method="POST" action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/select/client" update="cart_result">
				<span id="client_data_view" formgroup="client">
					<ec:include uri="${vars.client_data_view}" resolved="true" />
				</span>
				</ec:form>
			</ec:tabs-item>
			<ec:tabs-item id="search_client_tabs" title="Search">
			
				<ec:data-table id="user_form_search" 
					action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/clients/search">
					<ed:row>
						<ed:col size="3">
			    			<ec:textfield
			    				label="Name" 
			    				name="fullName" 
			    				bundle="${messages}"/>
						</ed:col>
						<ed:col size="3">
			    			<ec:textfield 
			    				label="Email"
			    				name="email" 
			    				bundle="${messages}"/>
						</ed:col>
						<ed:col size="3">
			    			<ec:select label="Country" name="country">
			    				<ec:option value=""></ec:option>
			    				<c:forEach  items="${vars.countries}" var="country" >
				    				<ec:option value="${country.isoAlpha3}">${country.name}</ec:option>
			    				</c:forEach>
			    			</ec:select>
						</ed:col>
						<ed:col size="3">
			    			<ec:textfield 
			    				label="City"
			    				name="city" 
			    				bundle="${messages}"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:button icon="search" label="Search"  actionType="submit" align="right"/>
						</ed:col>
					</ed:row>
					<ec:data-result var="response">
					<ec:table style="striped">
						<ec:table-header>
							<ec:table-col>
								<b>First name</b>
							</ec:table-col>
							<ec:table-col>
								<b>Last name</b>
							</ec:table-col>
							<ec:table-col>
								<b>Country</b>
							</ec:table-col>
							<ec:table-col>
							</ec:table-col>
						</ec:table-header>
						<ec:table-body>
						<ec:forEach items="!{response.itens}" var="item">
						<ec:table-row>
							<ec:table-col>
								!{item.firstName}
							</ec:table-col>
							<ec:table-col>
								!{item.lastName}
							</ec:table-col>
							<ec:table-col>
								!{item.country.name}
							</ec:table-col>
							<ec:table-col>
								<ec:button id="button_product_!{item.protectedID}" label="Select" icon="check" align="right" actionType="button" >
								<ec:event type="click">
									
									$.AppContext.utils.updateContentByID(
											'#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/cart/clients/!{item.protectedID}',
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
		<ec:button label="Next" icon2="chevron-right" actionType="submit"  align="right" form="form_user"/>
		<ec:button icon="chevron-left" label="Back" actionType="button" align="right" >
			<ec:event type="click">
				var $accordion = $.AppContext.utils.getById('cart_steps');
				var $accordionItens = $accordion.getItens();
				$accordionItens[0].select();
			</ec:event>
		</ec:button>
		<ec:button label="New" icon="plus" actionType="button" align="right" >
			<ec:event type="click">
				$.AppContext.utils.updateContentByID(
					'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit?type=simplified', 
					'client_data_view'
				);									
			</ec:event>
		</ec:button>
		<ec:button label="Search" icon="search" actionType="button" align="right" >
			<ec:event type="click">
				var $tabs = $.AppContext.utils.getById('client_tabs');
				var $tab = $tabs.getItem("search_client_tabs");
				$tab.select();
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>