<%@ taglib uri="http://java.sun.com/jsp/jstl/core"                  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"             prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<c:if test="${!empty exception}">
	<ec:alert id="exceptionMessage" style="danger">
		${exception.message}
	</ec:alert>
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				$.AppContext.sales.updateProducts();
			}
		);
	</script>
</c:if>
<c:if test="${empty exception}">
	<c:set var="paymentResource" value="${checkout.paymentResource}"/>
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				var $paymentResource = "${paymentResource}";
				var $orderResource   = "#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/orders/edit/${checkout.order.id}";
				if ($paymentResource.trim().length === 0) {
					$.AppContext.utils.updateContent($orderResource);    
				}
				else{
					$.AppContext.openLink($paymentResource);
				}
				
			}
		);
	</script>
</c:if>