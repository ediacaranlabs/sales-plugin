<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>

<span formgroup="shippingAddress" formgrouptype="index">
	<ec:accordion>
		<ec:accordion-item title="">
			<jsp:include page="address.jsp"/>
		</ec:accordion-item>
	</ec:accordion>					
</span>