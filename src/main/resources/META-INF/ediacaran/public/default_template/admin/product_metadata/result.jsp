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
		let $form = $.AppContext.utils.getById('product_metadata_form');
		
		// set product id
		let $protectedID = $form.getField('product_metadata.protectedID');
		$protectedID.setValue('${vars.entity.protectedID}');
		
		
		let $attr;
		let $opt;
		// set attribute id
		<c:forEach items="${vars.attributes}" var="attribute" varStatus="attributeStep">
		$attr = $form.getField('product_metadata.attributes[${attributeStep.index}].protectedID');
		$attr.setValue('${attribute.protectedID}');
		
			//set opt id
			<c:forEach items="${attribute.options}" var="option" varStatus="optionStep">
			$opt = $form.getField('product_metadata.attributes[${attributeStep.index}].options[${optionStep.index}].protectedID');
			$opt.setValue('${option.protectedID}');
			</c:forEach>
		
		</c:forEach>
		
		//remove attributes images
		
		//get all attr
		let $attrArea = $.AppContext.utils.getById('attrArea');
		let $attrs = $attrArea.search(function($e){
			return $e.getAttribute("formgroup") == 'attributes';
		});
		
		//remove attr marked as deleted
		for (let $att of $attrs){
			let $path = $att.getAttribute("group-path");
			let $deleted = $form.getField($path + ".deleted");
			
			if($deleted.getValue()){
				$att.remove();
			}
			else{
				//remove opts
				var $opts = $att.search(function($e){
					return $e.getAttribute("formgroup") == 'options';
				});
				
				for (let $opt of $opts){
					let $path = $opt.getAttribute("group-path");
					let $deleted = $form.getField($path + ".deleted");
					
					if($deleted.getValue()){
						$opt.remove();
					}
					
				}
				
			}
			
		}
		
	});
	</script>
</c:if>
