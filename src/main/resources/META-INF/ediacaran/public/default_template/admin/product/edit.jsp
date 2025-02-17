<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>
<section class="inner-headline">
	<ed:row>
		<ed:col size="4">
			<div class="inner-heading">
				<h2><fmt:message key="header.title" bundle="${messages}"/></h2>
			</div>
		</ed:col>
		<ed:col size="8">
			<ec:breadcrumb title="#{header.title}" bundle="${messages}">
				<ec:breadcrumb-path icon="home" text="" lnk="#" />
				<ec:breadcrumb-path 
					text="#{header.breadcrumb.parent}" 
					lnk="#!${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products" 
					bundle="${messages}"/>
			</ec:breadcrumb>
		</ed:col>
	</ed:row>
</section>

<ec:box>
	<ec:box-body>
		<ec:form method="POST" id="product_form" enctype="multipart/form-data"
			update="result_product_form" 
			action="${plugins.ediacaran.sales.web_path}${plugins.ediacaran.front.admin_context}/products/save/${vars.entity.productType.toLowerCase()}" >
			<ed:row>
				<ed:col>
					<span formgroup="product">
						<ed:row style="form">
							<ed:col size="3" classStyle="form-group has-feedback">
								<ec:imagefield name="thumbnail"
									src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
									button="#{resource_form.thumbnail.button}" 
									bundle="${messages}" width="200" height="200" border="squad"/>
							</ed:col>
							<ed:col size="9">
								<ec:tabs>
									<ec:tabs-item active="true" title="Details">
										<jsp:include page="/default_template/admin/product/product_tab.jsp"/>
									</ec:tabs-item>
									<ec:tabs-item title="Image Gallery">
										<jsp:include page="/default_template/admin/product/image_gallery_tab.jsp"/>
									</ec:tabs-item>
								</ec:tabs>
							</ed:col>
						</ed:row>
					</span>
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col id="result_product_form">
				</ed:col>
			</ed:row>
			<ed:row>
				<ed:col size="12">
					<ec:button 
						label="#{resource_form.save.label}" 
						align="right"
						actionType="submit"
						bundle="${messages}"/>
				</ed:col>
			</ed:row>
		</ec:form>
	</ec:box-body>
</ec:box>				    
