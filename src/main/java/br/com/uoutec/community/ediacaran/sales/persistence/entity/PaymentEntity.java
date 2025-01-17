package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.brandao.brutos.bean.BeanInstance;
import org.brandao.brutos.bean.BeanProperty;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Entity
@Table(name="rw_payment")
@EntityListeners(PaymentEntityListener.class)
public class PaymentEntity implements Serializable{

	private static final long serialVersionUID = -1070328474697632265L;

	private static final Set<String> excludeFields;
	
	static{
		excludeFields = new HashSet<String>();
		BeanInstance i = new BeanInstance(null, Payment.class);
		
		List<BeanProperty> list = i.getProperties();
		
		for(BeanProperty p: list){
			excludeFields.add(p.getName());
		}
	}
	
	@Id
	@Column(name="cod_payment", length=32)
	private String id;

	@Column(name="set_payment_type", length=32)
	private String paymentType;

	@Column(name="cod_order", length=32)
	private String order;
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;
	
	@Column(name="vlr_tax", scale=3, precision=12)
	private BigDecimal tax;

	@Column(name="vlr_total", scale=3, precision=12)
	private BigDecimal total;
	
	@Column(name="dat_received_from")
	private LocalDateTime receivedFrom;
	
	@Column(name="set_status")
	private PaymentStatus status;
	
	@Column(name="dsc_currency", length=3)
	private String currency;

	@Column(name="dsc_type_name", length=128)
	private String typeName;
	
	@Lob
	@Column(name="dsc_ext_data")
	private String extendData;

	public PaymentEntity(){
	}
	
	public PaymentEntity(Payment e){
		this.typeName           = e.getClass().getName();
		this.order              = e.getOrderId();
		this.currency           = e.getCurrency();
		this.tax                = e.getTax();
		this.id                 = e.getId();
		this.receivedFrom       = e.getReceivedFrom();
		this.paymentType        = e.getPaymentType();
		this.value              = e.getValue();
		this.discount           = e.getDiscount();
		this.total              = e.getTotal();
		this.status             = e.getStatus();
		
		Map<String,String> actualData = e.getAddData();
		Map<String,String> data = DataUtil.encode(e, excludeFields);

		if(actualData == null){
			actualData = data;
		}
		else{
			actualData.putAll(data);
		}
		
		this.extendData = DataUtil.encode(actualData);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getExtendData() {
		return extendData;
	}

	public void setExtendData(String extendData) {
		this.extendData = extendData;
	}
	
	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public LocalDateTime getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(LocalDateTime receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public Payment toEntity() {
		return this.toEntity(null);
	}
	
	public Payment toEntity(Payment e) {
		
		try{
			if(e == null){
				if(this.paymentType == null){
					e = new Payment();
				}
				else{
					EntityInheritanceManager entityInheritanceUtil = 
							EntityContextPlugin.getEntity(EntityInheritanceManager.class);
						
					e = entityInheritanceUtil.getInstance(Payment.class, this.paymentType);
					
					if(e == null){
						e = new Payment();
					}
				}
			}
			
			if(this.extendData != null){
				Map<String,String> data = DataUtil.decode(this.extendData);
				DataUtil.decode(data, e);
				e.setAddData(data);
			}
			
			e.setOrderId(this.order);
			e.setReceivedFrom(this.receivedFrom);
			e.setTax(this.tax);
			e.setCurrency(this.currency);
			e.setId(this.id);
			e.setPaymentType(this.paymentType);
			e.setValue(this.value);
			e.setDiscount(this.discount);
			e.setTotal(this.total);
			e.setStatus(this.status);;
			
			return e;
		}
		catch(Throwable ex){
			throw new RuntimeException(ex);
		}
	}
	
}
