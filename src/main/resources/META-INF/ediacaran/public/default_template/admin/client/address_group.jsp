<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" prefix="ed"%>

<span formgroup="shippingAddress" formgrouptype="index">
	<ec:accordion>
		<ec:accordion-item title="${address.address} ${address.complement} ${address.city} ${address.region} ${address.country.isoAlpha3} - ${address.zip}">
			<jsp:include page="address.jsp"/>
			<ed:row>
				<ed:col>
					<ec:checkbox label="Remove" value="true" name="deleted" align="right"/>
				</ed:col>
			</ed:row>
		</ec:accordion-item>
	</ec:accordion>					
</span>