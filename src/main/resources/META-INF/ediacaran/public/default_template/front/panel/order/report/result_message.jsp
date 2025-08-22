<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<ec:setTemplatePackage name="admin"/>
<script type="text/javascript">
	$.AppContext.onload(function(){
		var $fr = $.AppContext.utils.getById('orderSearchForm');
		$fr.submit();
	});
</script>
