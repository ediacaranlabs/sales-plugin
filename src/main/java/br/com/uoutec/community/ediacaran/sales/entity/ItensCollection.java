package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.com.uoutec.application.bugfix.JDK8046171;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;

public class ItensCollection implements Serializable{

	private static final long serialVersionUID = -1646012927852286756L;
	
	private ConcurrentMap<String, ProductRequest> itens;

	public ItensCollection(){
		this.itens = new ConcurrentHashMap<String, ProductRequest>();
	}
	
	public ProductRequest get(String id){
		return this.itens.get(id);
	}

	public void remove(ProductRequest item){
		
		if(item.getSerial() == null){
			throw new NullPointerException();
		}
		
		ProductRequest product = this.itens.get(item.getSerial());
		
		if(product != null){
			this.itens.remove(product.getSerial());
		}
		else{
			throw new IllegalArgumentException();
		}
		
	}

	public void add(ProductRequest item) throws MaxItensException, ProductTypeRegistryException{
		
		if(item.getSerial() == null){
			throw new NullPointerException();
		}
		
		ProductRequest product = this.itens.get(item.getSerial());
		
		if(product != null){
			product.setUnits(product.getUnits() + 1);
			
			if(item.getUnits() > item.getMaxExtra() + 1){
				throw new MaxItensException();
			}
			
			this.itens.put(product.getSerial(), product);
		}
		else{
			
			if(this.itens.size() + 1 > CartRegistry.CART_MAX_ITENS){
				throw new MaxItensException();
			}
			
			if(item.getUnits() > item.getMaxExtra() + 1){
				throw new MaxItensException();
			}
			
			this.itens.put(item.getSerial(), item);
		}
	}

	public void setQty(ProductRequest item, int quantity) throws MaxItensException, ProductTypeRegistryException{
		
		if(item.getSerial() == null){
			throw new NullPointerException();
		}

		if(quantity < 0){
			throw new IllegalArgumentException("quantity");
		}
		
		ProductRequest product = this.itens.get(item.getSerial());
		
		if(product != null){
			
			product.setUnits(quantity);
			
			if(quantity == 0){
				this.itens.remove(item.getSerial());
			}
			else{
				
				if(item.getUnits() > item.getMaxExtra() + 1){
					throw new MaxItensException();
				}
				
				this.itens.put(item.getSerial(), product);
			}
		}
		else{
			throw new IllegalArgumentException("item");
		}
		
	}
	
	public Map<Integer, List<ProductRequest>> getItensGroupByProductType(){
		return null;
	}
	
	public boolean isNoitems(){
		return this.itens.isEmpty();
	}
	
	public void clear(){
		this.itens.clear();
	}
	
	public Collection<ProductRequest> getItens() {
		return JDK8046171.values(itens);
	}

	public int getSize(){
		return this.itens.size();
	}
	
	public int getTotalItens(){
		int itens = 0;
		for(ProductRequest p: this.itens.values()){
			itens += p.getUnits();
		}
		return itens;
	}

}
