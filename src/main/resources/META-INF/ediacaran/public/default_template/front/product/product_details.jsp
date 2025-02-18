<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<ec:include uri="/includes/head.jsp"/>
<style type="text/css">
.box-body {
	min-height: 120px !important;
}
</style>
<title>${module.name}</title>
</head>

<body>

	<ec:include uri="/includes/header.jsp"/>
	
	<section class="inner-headline">
		<ed:container>
			<ed:row>
				<ed:col size="4">
					<div class="inner-heading">
						<h2>${module.name}</h2>
					</div>
				</ed:col>
				<ed:col size="8">
					<ec:breadcrumb title="${module.name}">
						<ec:breadcrumb-path 
							icon="home" 
							text="" 
							lnk="${plugins.ediacaran.marketplace.web_path}" />
						<ec:breadcrumb-path 
							text="#{header.breadcrumb.parent2}" 
							lnk="${plugins.ediacaran.marketplace.web_path}/"
							bundle="${messages}"
							/>
						<ec:breadcrumb-path 
							text="#{header.breadcrumb.parent}" 
							lnk="${plugins.ediacaran.marketplace.web_path}/search"
							bundle="${messages}"
							/>
					</ec:breadcrumb>
				</ed:col>
			</ed:row>
		</ed:container>
	</section>

	<section class="content">
		<ed:container>
		<ed:row>
			<ed:col size="8">
				<h3>${module.name}</h3>
				<hr>
				<p>${module.description}</p>
			</ed:col>
			<ed:col size="2">
				<ed:row>
					<ed:col size="12">
						<ec:image align="center" 
							src="${plugins.ediacaran.marketplace.image_prefix_address}${empty module.thumb? '/images/module_thumb.png' : module.publicThumb}"/>
					</ed:col>
				</ed:row>
				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.type.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						${module.type.getName(locale)}
					</ed:col>
				</ed:row>

				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.supplier.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						${module.supplier.name}
					</ed:col>
				</ed:row>

				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.version.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						${module.version.versionName}
					</ed:col>
				</ed:row>

				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.supported_versions.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						<ec:list>
						<c:forEach items="${module.supportedVersionsList}" var="ver">
							<ec:list-item>${ver.versionName}</ec:list-item>
						</c:forEach>
						</ec:list>
					</ed:col>
				</ed:row>

				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.tags.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						${module.tagsString}
					</ed:col>
				</ed:row>

				<ed:row style="form">
					<ed:col size="12">
						<b><fmt:message key="details.cost.label" bundle="${messages}"/>:</b>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col size="12">
						${module.getCostString(locale)}/${module.measurementUnit.getName(locale)}
					</ed:col>
				</ed:row>

				<ed:row>
					<ed:col size="12">
						<ec:button 
							label="#{details.download.label}" 
							enabled="${!empty module.moduleInstaller}" 
							align="center"
							bundle="${messages}">
							<ec:event type="click">
								location.href = '${plugins.ediacaran.marketplace.web_path}/module/download/${module.protectedID}'; 
							</ec:event>
						</ec:button>
					</ed:col>
				</ed:row>				
			</ed:col>
		</ed:row>
		</ed:container>
	</section>

	<ec:include uri="/includes/footer.jsp"/>
 
</body>
</html>