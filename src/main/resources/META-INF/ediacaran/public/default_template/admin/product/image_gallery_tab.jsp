<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@page trimDirectiveWhitespaces="true" %>
<%--<ec:setBundle var="messages" locale="${locale}"/>--%>
<ec:setTemplatePackage name="admin"/>
<style>
</style>
<ed:row>
	<ed:col id="imagesArea">
	</ed:col>
</ed:row>
<ed:row>
	<ed:col>
		<ec:button label="Add image" align="right" actionType="button">
			<ec:event type="click">
				var $image = $.AppContext.utils.applyTemplate("imageTemplate", {});
				$.AppContext.utils.content.append("imagesArea",$image);
			</ec:event>
		</ec:button>
	</ed:col>
</ed:row>

<!--  Image Template -->
<ec:template id="imageTemplate" var="obj">
	<span formgroup="image">
		<ec:accordion>
			<ec:accordion-item title="Image" active="true">
				<ed:row>
					<ed:col size="3">
						<ec:imagefield name="image"
							src="${plugins.ediacaran.sales.image_prefix_address}${plugins.ediacaran.sales.template.concat('/front/cart/imgs/product.png')}"
							button="Select" 
							width="128" height="128" border="squad">
						</ec:imagefield>
					</ed:col>
					<ed:col size="9">
						<ec:textarea id="image_textarea" name="description" rows="1"></ec:textarea>
					</ed:col>
				</ed:row>
				<ed:row>
					<ed:col>
						<ec:checkbox label="Remove" align="right" name="deleted" value="true"/>
					</ed:col>
				</ed:row>
			</ec:accordion-item>
		</ec:accordion>
	</span>
</ec:template>