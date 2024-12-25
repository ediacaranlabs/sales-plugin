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
	
		$.AppContext.utils.updateContentByID(
				'${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/select-address/${vars.client.protectedID}/simplified', 
				'selected_address'
			);									
		
		var $form = $.AppContext.utils.getById('client_form');
		
		var $protectedID = $form.getField('client.protectedID');
		$protectedID.setValue('${vars.client.protectedID}');

		/*
		var $billingAddress = $form.getField('billingAddress.protectedID');
		
		if($billingAddress){
			$billingAddress.setValue('${vars.billingAddress.protectedID}');
		}
		*/
		
		<c:forEach items="${vars.addresses}" var="address" varStatus="addressStep">
		var $address = $form.getField('addresses[${addressStep.index}].protectedID');
		$address.setValue('${address.protectedID}');
		</c:forEach>
		
		var $addressList = $.AppContext.utils.getById('addressList');
		var $addrsArray = $addressList.search(function($e){
			return $e.getAttribute("formgroup") == 'addresses';
		});
		
		for (let $addr of $addrsArray){
			let $path = $addr.getAttribute("group-path");
			let $deleted = $form.getField($path + ".deleted");
			
			if($deleted.getValue()){
				$addr.remove();
			}
		}
		
	});
	</script>
	<ec:alert style="success">
		<fmt:message key="result.success" bundle="${sys_messages}" />
	</ec:alert>
</c:if>