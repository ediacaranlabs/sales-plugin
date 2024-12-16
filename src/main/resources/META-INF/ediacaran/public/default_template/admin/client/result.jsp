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
	<script type="text/javascript">
	$.AppContext.onload(function(){			
	
		var $form = $.AppContext.utils.getById('client_form');
		
		var $protectedID = $form.getField('client.protectedID');
		$protectedID.setValue('${vars.client.protectedID}');

		var $billingAddress = $form.getField('billingAddress.protectedID');
		
		if($billingAddress){
			$billingAddress.setValue('${vars.billingAddress.protectedID}');
		}

		<c:forEach items="${vars.shippingAddress}" var="shippingAddress" varStatus="shippingAddressStep">
		var $shippingAddress = $form.getField('shippingAddress[${shippingAddressStep.index}].protectedID');
		$shippingAddress.setValue('${shippingAddress.protectedID}');
		</c:forEach>
		
	});
	</script>
	<ec:alert style="success">
		<fmt:message key="result.success" bundle="${sys_messages}" />
	</ec:alert>
</c:if>