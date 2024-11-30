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
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				$.AppContext.openLink("${link}");
			}
		);
			
	</script>
</c:if>