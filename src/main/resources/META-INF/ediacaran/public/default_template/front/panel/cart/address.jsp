<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<ec:include uri="/default_template/front/panel/client/address.jsp" resolved="true" />
<script type="text/javascript">
$.AppContext.onload(function(){
	var $addressForm = $.AppContext.utils.getById('address_user');
	$addressForm.updateMetadata();
});
</script>
