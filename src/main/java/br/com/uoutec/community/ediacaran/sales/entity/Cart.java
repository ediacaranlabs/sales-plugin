package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

@SessionScoped
public class Cart implements Serializable{

	private static final long serialVersionUID = 7487563886270371708L;

	@NotNull(groups = IdValidation.class)
	@Pattern(regexp = CommonValidation.UUID, groups = IdValidation.class)
	private String id;
	
	@NotNull(groups = DataValidation.class)
	private SystemUserID owner;
	
	@NotNull(groups = DataValidation.class)
	private ItensCollection itens;

	@NotNull(groups = DataValidation.class)
	private List<Discount> discounts;
	
	@NotNull(groups = DataValidation.class)
	private BigDecimal discount;
	
	@NotNull(groups = DataValidation.class)
	private BigDecimal tax;
	
	public Cart(){
		this.id       = UUID.randomUUID().toString();
		this.itens    = new ItensCollection();
		this.discount = BigDecimal.ZERO;
		this.tax      = BigDecimal.ZERO;
	}
	
	public SystemUserID getOwner() {
		return owner;
	}

	public void setOwner(SystemUserID owner) {
		
		if(this.owner != null) {
			throw new IllegalStateException();
		}
		
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public ProductRequest get(String id){
		return this.itens.get(id);
	}

	public ItensCollection getItensCollection(){
		return this.itens;
	}
	
	public void setItens(ItensCollection itens) {
		this.itens = itens;
	}

	public boolean isNoitems(){
		return this.itens.isNoitems();
	}
	
	public void clear(){
		this.id = UUID.randomUUID().toString();
		this.itens.clear();
	}
	
	public Collection<ProductRequest> getItens() {
		return itens.getItens();
	}

	public int getSize(){
		return this.itens.getSize();
	}
	
	public BigDecimal getTax(){
		return BigDecimal.ZERO;
	}

	public BigDecimal getTotalTax(){
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getSubtotal(){
		BigDecimal value = BigDecimal.ZERO;
		
		if(this.itens == null || this.itens.getItens().isEmpty()){
			return value;
		}
		
		for(ProductRequest pr: this.itens.getItens()){
			value = value.add(pr.getSubtotal());
		}
		
		return value;
	}
	
	public BigDecimal getTotalDiscount(){
		BigDecimal value = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens.getItens()){
			if(pr.getDiscount() != null){
				value = value.add(pr.getDiscount());
			}
		}
		
		return value;
	}
	
	public BigDecimal getTotal(){
		BigDecimal value = BigDecimal.ZERO;
		
		for(ProductRequest pr: this.itens.getItens()){
			value = value.add(pr.getTotal());
		}
		
		if(this.discount != null && this.discount.compareTo(BigDecimal.ZERO) > 0){ 
			if(this.discount.compareTo(value) <= 0){
				value = value.subtract(this.discount);
			}
			else{
				value = BigDecimal.ZERO;
			}
		}
		
		if(this.tax != null && this.tax.compareTo(BigDecimal.ZERO) > 0){
			value = value.add(this.tax);
		}
		
		return value;
	}

	public int getTotalItens(){
		return this.itens.getTotalItens();
	}

}
