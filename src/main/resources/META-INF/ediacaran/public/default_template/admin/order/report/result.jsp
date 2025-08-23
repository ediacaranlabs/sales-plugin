<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<ec:setTemplatePackage name="admin"/>
<ec:setBundle var="sys_messages" locale="${locale}"/>
<c:if test="${!empty exception}">
	<ec:alert style="danger">
		<div id="exceptionMessage">${exception.message}</div>
	</ec:alert>
</c:if>
<c:if test="${empty exception}">
	<ec:alert style="success">
		<fmt:message key="result.success" bundle="${sys_messages}" />
	</ec:alert>
	<script type="text/javascript">
	$.AppContext.onload(function(){
		$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${vars.orderReport.order.id}');
	});
	</script>
</c:if>
