package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;

@Entity
@Table(name="rw_product_package")
@EntityListeners(ProductPackageEntityListener.class)
public class ProductPackageEntity {

	@Id
	@Column(name="cod_product_package", length=32)
	private String id;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_shipping", referencedColumnName="cod_shipping")
	private ShippingEntity shipping;
	
	@Column(name="vlr_weight")
	private Float weight;
	
	@Column(name="vlr_height")
	private Float height;
	
	@Column(name="vlr_width")
	private Float width;
	
	@Column(name="vlr_depth")
	private Float depth;
	
	@OneToMany(mappedBy = "productPackage")
	private List<ProductRequestEntity> products;

	public ProductPackageEntity(ProductPackage e) {
		this.depth = e.getDepth();
		this.height = e.getHeight();
		this.id = e.getId();
		this.weight = e.getWeight();
		this.width = e.getWidth();
		
		if(e.getProducts() != null) {
			this.products = new ArrayList<>();
			for(ProductRequest p: e.getProducts()) {
				this.products.add(new ProductRequestEntity(this, p));
			}
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getDepth() {
		return depth;
	}

	public void setDepth(Float depth) {
		this.depth = depth;
	}

	public List<ProductRequestEntity> getProducts() {
		return products;
	}

	public void setProducts(List<ProductRequestEntity> products) {
		this.products = products;
	}
	
	public ProductPackage toEntity(){
		return this.toEntity(null);
	}
	
	public ProductPackage toEntity(ProductPackage e){
		
		try{
			
			if(e == null) {
				e =  new ProductPackage();
			}
			
			e.setDepth(this.depth == null? 0f : this.depth.floatValue());
			e.setHeight(this.height == null? 0f : this.height.floatValue());
			e.setId(this.id);
			
			e.setWeight(this.weight == null? 0f : this.height.floatValue());
			e.setWidth(this.width == null? 0f : this.width.floatValue());

			if(this.products != null) {
				List<ProductRequest> list = new ArrayList<>();
				for(ProductRequestEntity p: this.products) {
					list.add(p.toEntity());
				}
				e.setProducts(list);
			}
			
			return e;
		}
		catch(Throwable ex){
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPackageEntity other = (ProductPackageEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
