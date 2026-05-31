package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

public class ProductRequestUtil {

	public static Map<String, ProductRequest> toMap(Collection<ProductRequest> values){
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		return transientItens;
	}

	public static Map<String, ProductRequest> getNegativeUnits(Collection<ProductRequest> values){
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			if(pr.getUnits() < 0) {
				transientItens.put(pr.getSerial(), pr);
			}
		}
		
		return transientItens;
	}
	
	public static void addUnits(Map<String, ProductRequest> values, Collection<ProductRequest> itens){
		
		for(ProductRequest pr: itens) {
			ProductRequest tpr = values.get(pr.getSerial());
			if(tpr == null) {
				continue;
			}
			
			tpr.setUnits(tpr.getUnits() + pr.getUnits());
		}
		
	}

	public static void subUnits(Map<String, ProductRequest> values, Collection<ProductRequest> itens){
		
		for(ProductRequest pr: itens) {
			ProductRequest tpr = values.get(pr.getSerial());
			if(tpr == null) {
				continue;
			}
			
			tpr.setUnits(tpr.getUnits() - pr.getUnits());
		}
		
	}
	
	public static void resetUnits(Map<String, ProductRequest> values){
		for(ProductRequest pr: values.values()) {
			pr.setUnits(0);
		}
	}

	public static void removeEmptyUnits(Map<String, ProductRequest> values){
		
		Set<String> keys = new HashSet<>();
		
		values.entrySet().stream().forEach((e)->{
			if(e.getValue().getUnits() == 0) {
				keys.add(e.getKey());
			}
		});
		
		keys.stream().forEach((e)->{
			values.remove(e);
		});
		
	}
	
	public static void setUnits(Map<String, ProductRequest> values, Map<String, Integer> itens) throws ItemNotFoundOrderRegistryException{
		
		for(Entry<String,Integer> e: itens.entrySet()) {
			
			ProductRequest tpr = values.get(e.getKey());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(e.getKey());
			}
			
			tpr.setUnits(e.getValue().intValue());
		}
		

	}

	public static Collection<ProductRequest> createCollectionRequest(Collection<ProductRequest> values, Map<String, Integer> itens) throws ItemNotFoundOrderRegistryException{
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(values);
		ProductRequestUtil.resetUnits(map);
		ProductRequestUtil.setUnits(map, itens);
		ProductRequestUtil.removeEmptyUnits(map);
		return map.values();
	}
	
}
