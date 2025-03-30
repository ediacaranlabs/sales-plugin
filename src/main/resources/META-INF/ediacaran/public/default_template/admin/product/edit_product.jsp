<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>

<ed:row>
	<ed:col>
		<span formgroup="product">
			<ed:row style="form">
				<ed:col size="3" classStyle="form-group has-feedback">
					<ec:imagefield name="thumbnail"
						src="${plugins.ediacaran.sales.image_prefix_address}${empty vars.entity.thumb? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : vars.entity.publicThumb}"
						button="#{product.form.thumbnail.button}" 
						bundle="${messages}" width="200" height="200" border="squad"/>
				</ed:col>
				<ed:col size="9">
					<ec:tabs>
						<ec:tabs-item active="true" title="#{product.tabs.details}" bundle="${messages}">
							<jsp:include page="/default_template/admin/product/product_tab.jsp"/>
						</ec:tabs-item>
						<ec:tabs-item title="#{product.tabs.images}" bundle="${messages}">
							<jsp:include page="/default_template/admin/product/image_gallery_tab.jsp"/>
						</ec:tabs-item>
						<ec:tabs-item title="#{product.tabs.properties}" bundle="${messages}">
							<span id="attribute_tab">
								<jsp:include page="/default_template/admin/product/attribute_tab.jsp"/>
							</span>
						</ec:tabs-item>
						<c:forEach items="${vars.tabs}" var="tab">
						<!-- tab (${tab.id}) -->
						<ec:tabs-item title="${tab.title}">
							<ec:sectionView section="${tab.content}"/>
						</ec:tabs-item>
						<!-- /tab (${tab.id}) -->
						</c:forEach>
					</ec:tabs>
				</ed:col>
			</ed:row>
		</span>
	</ed:col>
</ed:row>
