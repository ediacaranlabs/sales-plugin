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
		<ec:form id="form_user" method="POST" action="${plugins.ediacaran.sales.web_path}/cart/select/client" update="cart_result">
		<span id="client_data_view" formgroup="client">
			<ec:include uri="${vars.client_data_view}" resolved="true" />
		</span>
		</ec:form>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button label="#{next.label}" icon2="chevron-right" actionType="submit"  align="right" form="form_user" bundle="${messages}"/>
	</ed:col>
</ed:row>