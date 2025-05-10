<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"                    prefix="fmt"%> 
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>

<ec:setBundle var="messages" locale="${locale}"/>

<ed:row>
	<ed:col size="6">
		<ec:image 
			classStyle="product_image_thumb"
			src="${product.publicThumb == null? plugins.ediacaran.sales.image_prefix_address.concat(plugins.ediacaran.sales.template).concat('/front/cart/imgs/product.png') : plugins.ediacaran.sales.image_prefix_address.concat(product.publicThumb)}"
			style="thumbnail"
			align="center"
		/>
	</ed:col>
	<ed:col size="6">
		<ed:row>
			<ed:col>
				<h3>${product.name}</h3>
			</ed:col>
		</ed:row>
		<ed:row>
			<ed:col>
				<b>${product.displayValue}</b><br>
				${product.shortDescription}
			</ed:col>
		</ed:row>
	
	</ed:col>
</ed:row>