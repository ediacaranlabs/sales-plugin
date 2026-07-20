<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:import url="/default_template/front/panel/client/address.jsp"/>
<script type="text/javascript">
$.AppContext.onload(function(){
	var $addressForm = $.AppContext.utils.getById('address_user');
	$addressForm.updateMetadata();
});
</script>