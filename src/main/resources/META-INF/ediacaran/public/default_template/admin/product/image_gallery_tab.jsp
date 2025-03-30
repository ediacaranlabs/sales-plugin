<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<ec:setBundle var="messages" locale="${locale}"/>
<ec:setTemplatePackage name="admin"/>
<ed:row id="imagesArea">
	<c:forEach items="${vars.images}" var="image" >
		<ed:col size="3" extAttrs="formgroup='images' formgrouptype='index'">
			<input type="hidden" name="protectedID" value="${image.protectedID}">
			<ec:box>
				<ec:box-body>
					<ed:row>
						<ed:col size="12">
							<ec:image 
								src="${plugins.ediacaran.sales.image_prefix_address}${empty image.image? plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png') : image.publicThumb}"
								style="fluid"/>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:textarea name="#{product.form.description.label}" rows="3" bundle="${messages}">${image.description}</ec:textarea>
						</ed:col>
					</ed:row>
					<ed:row>
						<ed:col>
							<ec:checkbox label="#{product.form.remove.label}" align="right" name="deleted" value="true" bundle="${messages}"/>
						</ed:col>
					</ed:row>
				</ec:box-body>
			</ec:box>
		</ed:col>
	</c:forEach>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button label="#{product.form.add_button.label}" align="right" actionType="button" bundle="${messages}">
			<ec:event type="click">
				var $image = $.AppContext.utils.applyTemplate("imageTemplate", {});
				$.AppContext.utils.content.append("imagesArea",$image);
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>

<!--  Image Template -->
<ec:template id="imageTemplate" var="obj">
	<ed:col size="3" extAttrs="formgroup='images' formgrouptype='index'">
		<input type="hidden" name="protectedID" value="${image.protectedID}">
		<ec:box>
			<ec:box-body>
				<ed:row>
					<ed:col size="12">
						<ec:imagefield name="image"
							src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png')}"
							button="#{product.form.image.button}" 
							width="128" height="128" border="squad" bundle="${messages}">
						</ec:imagefield>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:textarea name="#{product.form.description.label}" rows="3" bundle="${messages}"></ec:textarea>
					</ed:col>
				</ed:row>
			</ec:box-body>
		</ec:box>
	</ed:col>
</ec:template>
