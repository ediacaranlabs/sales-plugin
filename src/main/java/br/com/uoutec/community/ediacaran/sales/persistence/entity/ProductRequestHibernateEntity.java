package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.Discount;
import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;

@Entity
@Table(name="rw_product_request")
public class ProductRequestHibernateEntity implements Serializable{

	private static final long serialVersionUID = -6395849000853228077L;

	@Id
	@Column(name="cod_product_request", length=38)
	private String id;
	
	@Column(name="dsc_serial", length=30, unique=true)
	private String serial;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product", referencedColumnName="cod_product")
	private ProductHibernateEntity product;

	@Column(name="dsc_productid", length=30)
	private String productID;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_order", referencedColumnName="cod_order")
	private OrderHibernateEntity order;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_period_type", length=32)
	private PeriodType periodType;
	
	@Column(name="vlr_units", length=11)
	private Integer units;

	@Column(name="dsc_description", length=11)
	private String description;
	
	@Column(name="dsc_short_description", length=11)
	private String shortDescription;
	
	@Column(name="vlr_cost", scale=3, precision=12)
	private BigDecimal cost;
	
	@Column(name="vlr_add_cost", scale=2, precision=12)
	private BigDecimal additionalCost;
	
	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;

	@Column(name="dsc_currency", length=3)
	private String currency;

	@OneToMany(mappedBy="productRequest", fetch=FetchType.LAZY)
	private List<ProductRequestDiscountHibernateEntity> discounts;
	
	@Lob
	@Column(name="dsc_data")
	private String addData;
	
	public ProductRequestHibernateEntity(){
	}
	
	public ProductRequestHibernateEntity(OrderHibernateEntity order, ProductRequest e){
		this.id               = e.getId();
		this.serial           = e.getSerial();
		this.order            = order;
		this.addData          = DataUtil.encode(e.getAddData());
		this.cost             = e.getCost();
		this.additionalCost   = e.getAdditionalCost();
		this.currency         = e.getCurrency();
		this.product          = e.getProduct() == null? null : new ProductHibernateEntity(e.getProduct());
		this.units            = e.getUnits();
		this.periodType       = e.getPeriodType();
		this.shortDescription = e.getShortDescription();
		this.description      = e.getDescription();
		this.productID        = e.getProductID();
		this.discount         = e.getDiscount();
		
		List<Discount> discounts = e.getDiscounts();
		
		if(discounts != null){
			this.discounts = new ArrayList<ProductRequestDiscountHibernateEntity>();
			for(Discount discount: discounts){
				this.discounts.add(new ProductRequestDiscountHibernateEntity(this, discount));
			}
		}
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public ProductHibernateEntity getProduct() {
		return product;
	}

	public void setProduct(ProductHibernateEntity product) {
		this.product = product;
	}

	public OrderHibernateEntity getOrder() {
		return order;
	}

	public void setOrder(OrderHibernateEntity order) {
		this.order = order;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getAddData() {
		return addData;
	}

	public void setAddData(String addData) {
		this.addData = addData;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public BigDecimal getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(BigDecimal additionalCost) {
		this.additionalCost = additionalCost;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public List<ProductRequestDiscountHibernateEntity> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<ProductRequestDiscountHibernateEntity> discounts) {
		this.discounts = discounts;
	}

	public ProductRequest toEntity(){
		ProductRequest e = new ProductRequest();
		e.setSerial(this.serial);
		e.setCost(this.cost);
		e.setCurrency(this.currency);
		e.setId(this.id);
		e.setProduct(this.product == null? null : this.product.toEntity());
		e.setAddData(DataUtil.decode(this.addData));
		e.setUnits(this.units);
		e.setAdditionalCost(this.additionalCost);
		e.setPeriodType(this.periodType);
		e.setShortDescription(this.shortDescription);
		e.setDescription(this.description);
		e.setProductID(this.productID);
		e.setDiscount(this.discount);
		
		if(this.discounts != null){
			List<Discount> discounts = new ArrayList<Discount>();
			e.setDiscounts(discounts);
			
			for(ProductRequestDiscountHibernateEntity prd: this.discounts){
				discounts.add(prd.toEntity());
			}
			
		}
		
		return e;
	}
}
