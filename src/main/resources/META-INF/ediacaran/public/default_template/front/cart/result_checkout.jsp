<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${!empty exception}">
	<div class="alert alert-danger alert-dismissable span12">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		${exception.message}
	</div>				
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				updateProducts();
			}
		);
	</script>
</c:if>
<c:if test="${empty exception}">
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				$.AppContext.openLink("${fn:startsWith(link, '/')? pageContext.request.contextPath.concat(link) : link}");
			}
		);
			
	</script>
</c:if>