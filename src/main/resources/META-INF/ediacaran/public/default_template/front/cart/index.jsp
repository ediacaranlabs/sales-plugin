<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="system.templates.front.default_template.cart.index" var="sys_messages"/>
<fmt:message key="cart_review.banner.title" bundle="${sys_messages}" var="title" scope="request"/>
<fmt:message key="cart_review.header.author" bundle="${sys_messages}" var="author" scope="request"/>
<c:set var="page" value="/templates/front/default_template/cart/index-content.jsp" scope="request"/>
<c:import url="/templates/front/default_template/base.jsp"/>