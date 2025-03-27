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
		
		if($protectedID.getValue() != '' && $protectedID.getValue() != '${vars.entity.protectedID}'){
			alert("invalid product metadata: " + "${vars.entity.protectedID}" + " != " + $protectedID.getValue() );
		}
		
		$protectedID.setValue('${vars.entity.protectedID}');
		
		let $attr;
		let $opt;
		
		// set attribute id
		<c:forEach items="${vars.attributes}" var="attribute" varStatus="attributeStep">
		
		$attr = $form.getField('product_metadata.attributes[${attributeStep.index}].protectedID');
		
		if($attr.getValue() != '' && $attr.getValue() != '${attribute.protectedID}'){
			alert("invalid attribute ID: product_metadata.attributes[${attributeStep.index}]: " + "${attribute.protectedID}" + " != " + $attr.getValue());
		}
		
		$attr.setValue('${attribute.protectedID}');
		
			//set opt id
			<c:forEach items="${attribute.options}" var="option" varStatus="optionStep">
			
			$opt = $form.getField('product_metadata.attributes[${attributeStep.index}].options[${optionStep.index}].protectedID');
			
			if($opt.getValue() != '' && $opt.getValue() != '${option.protectedID}'){
				alert("invalid option ID: product_metadata.attributes[${attributeStep.index}].options[${optionStep.index}]: " + "${option.protectedID}" + " != " + $opt.getValue());
			}
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
			
			//console.log("check attribute to remove: " + $path);
			if($deleted.getValue()){
				//console.log("removed attribute: " + $path);
				$att.remove();
			}
			else{
				//remove opts
				var $opts = $att.search(function($e){
					return $e.getAttribute("formgroup") == 'options';
				});
				
				for (let $opt of $opts){
					$path = $opt.getAttribute("group-path");
					$deleted = $form.getField($path + ".deleted");
					
					//console.log("check option to remove: " + $path + ": " + $deleted.getValue());
					
					if($deleted.getValue()){
						console.log("removed option: " + $path);
						$opt.remove();
					}
					
				}
				
			}
			
		}
		
	});
	</script>
</c:if>
