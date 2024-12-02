<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:box>
	<%--
	<ec:box-header>
		<h3>${product.name}</h3>
	</ec:box-header>
	--%>
	<ec:box-body>
	
		<ed:row>
			<%--
			<ed:col size="2">
				<ec:image align="center" src="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.sales.template}/front/cart/imgs/product.png"/>
			</ed:col>
			--%>
			<ed:col size="2">
				${product.name}
			</ed:col>
			<ed:col size="8">
				<ec:center>${product.description}</ec:center>
				<p>
			</ed:col>
			<ed:col size="2">
				<ec:center>
						${product.currency} <fmt:formatNumber pattern="###,###,##0.00" value="${product.cost}"/>
				</ec:center>
			</ed:col>
		</ed:row>
	
	</ec:box-body>
</ec:box>
