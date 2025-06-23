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
		//get product form
		var $form = $.AppContext.utils.getById('product_form');
		
		// set product id
		var $protectedID = $form.getField('product.protectedID');
		$protectedID.setValue('${vars.entity.protectedID}');
		
		
		// set images id
		<c:forEach items="${vars.images}" var="image" varStatus="imagesStep">
		var $image = $form.getField('product.images[${imagesStep.index}].protectedID');
		$image.setValue('${image.protectedID}');
		</c:forEach>
		
		//remove deleted images
		
		//get all images
		var $imagesElement = $.AppContext.utils.getById('imagesArea');
		var $images = $imagesElement.search(function($e){
			return $e.getAttribute("formgroup") == 'images';
		});
		
		//remove images marked as deleted
		for (let $img of $images){
			let $path = $img.getAttribute("group-path");
			let $deleted = $form.getField($path + ".deleted");
			
			if($deleted.getValue()){
				$img.remove();
			}
			
		}
		
	});
	</script>
</c:if>
