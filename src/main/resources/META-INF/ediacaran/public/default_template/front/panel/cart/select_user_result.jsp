<%@ taglib uri="http://java.sun.com/jsp/jstl/core"                  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"             prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<c:if test="${!empty exception}">
	<ec:alert id="exceptionMessage" style="danger">
		${exception.message}
	</ec:alert>
</c:if>
<%--
<c:if test="${empty exception}">
	<script type="text/javascript">

		$.AppContext.utils.updateContentByID(
			"${vars.select_address_uri}", 
			'client_address_form'
		);									

		var $accordion = $.AppContext.utils.getById('cart_steps');
		var $item = $accordion.getItem('cart_client');
		var $nextItem = $item.getNext();
		
		$nextItem.select();
		
	</script>
</c:if>
--%>