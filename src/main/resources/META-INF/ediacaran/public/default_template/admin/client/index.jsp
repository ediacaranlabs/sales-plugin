<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" 					prefix="fmt"%> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" 					prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" 				prefix="fn" %>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer" 	prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setTemplatePackage name="admin"/>

<ec:setBundle var="messages" locale="${locale}"/>

<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2>Clients</h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="Clients" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:data-table id="client_search_form" 
			action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/search">
			<ed:row>
				<ed:col size="3">
	    			<ec:textfield 
	    				label="Name"
	    				name="fullName" 
	    				bundle="${messages}"/>
				</ed:col>
				<ed:col size="3">
	    			<ec:textfield 
	    				label="Email"
	    				name="email" 
	    				bundle="${messages}"/>
				</ed:col>
				<ed:col size="3">
	    			<ec:select label="Country" name="country">
	    				<ec:option value="">Select a Country</ec:option>
	    				<c:forEach  items="${vars.countries}" var="country" >
		    				<ec:option value="${country.isoAlpha3}">${country.name}</ec:option>
	    				</c:forEach>
	    			</ec:select>
				</ed:col>
				<ed:col size="3">
	    			<ec:textfield 
	    				label="City"
	    				name="city" 
	    				bundle="${messages}"/>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col>
					<ec:button icon="search" label="Search"  actionType="submit" align="right"/>
				</ed:col>
			</ed:row>
			<ec:data-result var="response">
			<ec:table style="striped">
				<ec:table-header>
					<ec:table-col>
						<b>First name</b>
					</ec:table-col>
					<ec:table-col>
						<b>Last name</b>
					</ec:table-col>
					<ec:table-col>
						<b>email</b>
					</ec:table-col>
					<ec:table-col>
						<b>Country</b>
					</ec:table-col>
					<ec:table-col>
					</ec:table-col>
				</ec:table-header>
				<ec:table-body>
				<ec:forEach items="!{response.itens}" var="item">
				<ec:table-row>
					<ec:table-col>
						!{item.firstName}
					</ec:table-col>
					<ec:table-col>
						!{item.lastName}
					</ec:table-col>
					<ec:table-col>
						!{item.email}
					</ec:table-col>
					<ec:table-col>
						!{item.country.name}
					</ec:table-col>
					<ec:table-col>
						<ec:button label="Edit" icon="pencil" align="right" actionType="button" >
						<ec:event type="click">
							$.AppContext.utils.updateContent('#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/clients/edit/!{item.protectedID}');
						</ec:event>
						</ec:button>
					</ec:table-col>
				</ec:table-row>
				</ec:forEach>
				</ec:table-body>
			</ec:table>
			</ec:data-result>
		</ec:data-table>

	</ec:box-body>
</ec:box>
