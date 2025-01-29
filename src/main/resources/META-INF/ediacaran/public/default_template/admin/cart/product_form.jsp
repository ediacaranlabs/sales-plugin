<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>
<ed:row>
	<ed:col>
		<ec:center><b>${product.name}</b></ec:center>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:center>${product.description}</ec:center>
		<p>
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:center>
				${product.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${product.cost}"/>
		</ec:center>
	</ed:col>
</ed:row>
	