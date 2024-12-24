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
				<h2><fmt:message key="title" bundle="${messages}" /> </h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path text="#{parent.title}" lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients" bundle="${messages}"/>
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
				<ec:tabs-item title="#{tabs.addresses.title}" active="true" bundle="${messages}">
					<ed:row>
						<ed:col id="selected_address">
							<jsp:include page="address_selected.jsp"/>
						</ed:col>
					</ed:row>
				</ec:tabs-item>
				<ec:tabs-item title="#{tabs.address_list.title}" bundle="${messages}">
					<ed:row>
						<ed:col id="addressList">
							<c:forEach items="${vars.addresses}" var="address">
								<c:set var="address" value="${address}" scope="request"/>
								<jsp:include page="address_group.jsp"/>
							</c:forEach>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:button id="addshippingAddressButton" label="#{tabs.address_list.add_address.label}" align="right" bundle="${messages}" actionType="button">
								<ec:event type="click">
									$.AppContext.utils.appendContentByID("${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/address", "addressList");
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
			<ec:button label="#{save.label}" align="right" bundle="${messages}" actionType="submit"/>
		</ec:box-footer>
	</ec:box>
</ec:form>
