package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.SalesPluginConstants;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;

public class ProductUtil {

	public static String getPublicID(Product product) {
		return product.getProtectedID() + "-" + StringUtil.normalize(product.getName(), "-");		
	}
	
	public static String getPublicThumbPath(ProductImage e) {

		if(e == null || e.getId() == null) {
			return null;
		}
		
		Product product = new Product();
		product.setId(e.getProduct());
		
		String productPath = getPublicThumbPath(product);
		return productPath + "/images/" + e.getId().toLowerCase();
	}
	
	public static String getThumbPath(ProductImage e) {

		if(e == null || e.getId() == null) {
			return null;
		}
		
		Product product = new Product();
		product.setId(e.getProduct());
		
		String productPath = getThumbPath(product);
		return productPath + "/images/" + e.getId().toLowerCase();
	}

	public static String getPublicThumbPath(Product e) {
		
		if(e == null || e.getId() <= 0) {
			return null;
		}
		
		return "/" + getThumbPath("p" + Integer.toString(e.getId(), Character.MAX_RADIX).toLowerCase());
	}
	
	public static String getThumbPath(Product e) {
		
		if(e == null || e.getId() <= 0) {
			return null;
		}
		
		return getThumbPath("p" + Integer.toString(e.getId(), Character.MAX_RADIX).toLowerCase());
	}
	
	private static String getThumbPath(String id) {
		List<String> partsList = new ArrayList<>();
		
		int maxlen = id.length() / 3;
		maxlen = maxlen == 0? id.length() : maxlen;
		
		char[] chars = id.toCharArray();
		int i = 0;
		
		while(i<chars.length) {
			
			int from = i;
			int to = i + maxlen;
			
			if(to > chars.length) {
				to = chars.length - 1;
			}
			
			char[] tmp = Arrays.copyOfRange(chars, from, to);
			partsList.add(new String(tmp));
			i += maxlen;
			
		}
		
		String path = String.join("/", partsList);
		
		return SalesPluginConstants.PRODUCTS_DRIVER_NAME + SalesPluginConstants.IMAGES_DRIVER_PATH + "/" + path;
	}
	
}
