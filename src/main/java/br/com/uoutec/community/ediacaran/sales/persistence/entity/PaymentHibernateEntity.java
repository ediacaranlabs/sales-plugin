package br.com.uoutec.community.ediacaran.sales.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.brandao.brutos.bean.BeanInstance;
import org.brandao.brutos.bean.BeanProperty;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

@Entity
@Table(name="rw_payment")
public class PaymentHibernateEntity implements Serializable{

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
	
	@Column(name="vlr_value", scale=3, precision=12)
	private BigDecimal value;

	@Column(name="vlr_discount", scale=3, precision=12)
	private BigDecimal discount;
	
	@Column(name="vlr_tax", scale=3, precision=12)
	private BigDecimal tax;
	
	@NotNull
	@Column(name="dsc_currency", length=3)
	private String currency;

	@Column(name="dsc_type_name", length=128)
	private String typeName;
	
	@Lob
	@Column(name="dsc_ext_data")
	private String extendData;

	public PaymentHibernateEntity(){
	}
	
	public PaymentHibernateEntity(Payment e){
		this.typeName           = e.getClass().getName();
		this.currency           = e.getCurrency();
		this.tax                = e.getTax();
		this.id                 = e.getId();
		this.paymentType        = e.getPaymentType();
		this.value              = e.getValue();
		this.discount           = e.getDiscount();
		
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

	public String getExtendData() {
		return extendData;
	}

	public void setExtendData(String extendData) {
		this.extendData = extendData;
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
			
			e.setTax(this.tax);
			e.setCurrency(this.currency);
			e.setId(this.id);
			e.setPaymentType(this.paymentType);
			e.setValue(this.value);
			e.setDiscount(this.discount);
			
			return e;
		}
		catch(Throwable ex){
			throw new RuntimeException(ex);
		}
	}
	
}
