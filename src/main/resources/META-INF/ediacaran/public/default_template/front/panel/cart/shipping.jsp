<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>

<c:forEach items="${vars.shippingOptions}" var="shippingOption" varStatus="shippingOptionSteps">
	<ec:radio name="shipping_method"  label="${shippingOption.toString(locale)}" value="${shippingOption.id}" selected="${shippingOptionSteps.index == 0}"/>
</c:forEach>
