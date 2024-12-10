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
		<ec:box-header>
			<b>Client information</b>
		</ec:box-header>
		<ec:box-body>
			<span id="client_data_view">
				<ec:include uri="${vars.client_data_view}" resolved="true" />
			</span>
			<ed:row>
				<ed:col id="client_update_result"></ed:col>
			</ed:row>
		</ec:box-body>
		<ec:box-footer>
			<ec:button label="Save" align="right" actionType="submit"/>
		</ec:box-footer>
	</ec:box>
</ec:form>
