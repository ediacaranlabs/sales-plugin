<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${!empty exception}">
	<div class="alert alert-danger alert-dismissable col-xs-12">
		<button type="button" class="close" data-dismiss="alert"
			aria-hidden="true">&times;</button>
		${exception.message}
	</div>				
</c:if>
<c:if test="${empty exception}">
	<script type="text/javascript">
		$.AppContext.onload(
			function (){
				$.AppContext.openLink("${pageContext.request.contextPath}/cart#payment_region");
			}
		);
	</script>
</c:if>