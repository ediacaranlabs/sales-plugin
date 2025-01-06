package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;

@Entity
@Table(name="rw_product_request")
@EntityListeners(ProductRequestEntityListener.class)
public class ProductRequestEntity implements Serializable{

	private static final long serialVersionUID = -6395849000853228077L;

	@Id
	@Column(name="cod_product_request", length=38)
	private String id;
	
	@Column(name="dsc_serial", length=30)
	private String serial;
	
	@Column(name="dsc_name", length=128)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_product", referencedColumnName="cod_product")
	private ProductEntity product;

	@Column(name="dsc_productid", length=30)
	private String productID;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_order", referencedColumnName="cod_order")
	private OrderEntity order;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_invoice", referencedColumnName="cod_invoice")
	private InvoiceEntity invoice;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cod_shipping", referencedColumnName="cod_shipping")
	private ShippingEntity shipping;
	
	@Enumerated(EnumType.STRING)
	@Column(name="set_period_type", length=32)
	private PeriodType periodType;
	
	@Column(name="vlr_units", length=11)
	private Integer units;

	@Lob
	@Column(name="dsc_description")
	private String description;
	
	@Lob
	@Column(name="dsc_short_description")
	private String shortDescription;
	
	@Column(name="vlr_cost", scale=3, precision=12)
	private BigDecimal cost;
	
	@Column(name="dsc_currency", length=3)
	private String currency;

	@OneToMany(mappedBy="productRequest", fetch=FetchType.LAZY)
	private List<ProductRequestTaxEntity> taxes;
	
	@Lob
	@Column(name="dsc_data")
	private String addData;
	
	public ProductRequestEntity(){
	}
	
	public ProductRequestEntity(OrderEntity order, ProductRequest e){
		this(order, null, null, e);
	}
	
	public ProductRequestEntity(InvoiceEntity invoice, ProductRequest e){
		this(null, invoice, null, e);
	}
	public ProductRequestEntity(ShippingEntity shipping, ProductRequest e){
		this(null, null, shipping, e);
	}

	public ProductRequestEntity(OrderEntity order, InvoiceEntity invoice, ShippingEntity shipping, ProductRequest e){
		this.id               = e.getId();
		this.name             = e.getName();
		this.serial           = e.getSerial();
		this.order            = order;
		this.invoice          = invoice;
		this.shipping         = shipping;
		this.addData          = DataUtil.encode(e.getAddData());
		this.cost             = e.getCost();
		this.currency         = e.getCurrency();
		this.product          = e.getProduct() == null? null : new ProductEntity(e.getProduct());
		this.units            = e.getUnits();
		this.periodType       = e.getPeriodType();
		this.shortDescription = e.getShortDescription();
		this.description      = e.getDescription();
		this.productID        = e.getProductID();
		
		List<Tax> taxes = e.getTaxes();
		
		if(taxes != null){
			this.taxes = new ArrayList<ProductRequestTaxEntity>();
			for(Tax tax: taxes){
				this.taxes.add(new ProductRequestTaxEntity(this, tax));
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

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
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

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public List<ProductRequestTaxEntity> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<ProductRequestTaxEntity> taxes) {
		this.taxes = taxes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InvoiceEntity getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
	}

	public ProductRequest toEntity(){
		ProductRequest e = new ProductRequest();
		e.setSerial(this.serial);
		e.setName(this.name);
		e.setCost(this.cost);
		e.setCurrency(this.currency);
		e.setId(this.id);
		e.setProduct(this.product == null? null : this.product.toEntity());
		e.setAddData(DataUtil.decode(this.addData));
		e.setUnits(this.units);
		e.setPeriodType(this.periodType);
		e.setShortDescription(this.shortDescription);
		e.setDescription(this.description);
		e.setProductID(this.productID);
		
		if(this.taxes != null){
			List<Tax> taxes = new ArrayList<Tax>();
			e.setTaxes(taxes);
			
			for(ProductRequestTaxEntity prd: this.taxes){
				taxes.add(prd.toEntity());
			}
			
		}
		
		return e;
	}
}
