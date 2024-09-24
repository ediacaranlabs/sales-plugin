package br.com.uoutec.community.ediacaran.junit.sales.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import br.com.uoutec.community.ediacaran.sales.entity.Discount;
import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;

public class ProductRequestMock extends ProductRequest{

	private static final long serialVersionUID = -7808516937058901825L;

	@Generated("SparkTools")
	private ProductRequestMock(Builder builder) {
		this.id = builder.id;
		this.serial = builder.serial;
		this.product = builder.product;
		this.productID = builder.productID;
		this.units = builder.units;
		this.periodType = builder.periodType;
		this.cost = builder.cost;
		this.additionalCost = builder.additionalCost;
		this.discounts = builder.discounts;
		this.discount = builder.discount;
		this.tax = builder.tax;
		this.currency = builder.currency;
		this.addData = builder.addData;
		this.shortDescription = builder.shortDescription;
		this.description = builder.description;
		this.availability = builder.availability;
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private String id;
		private String serial;
		private Product product;
		private String productID;
		private int units;
		private PeriodType periodType;
		private BigDecimal cost;
		private BigDecimal additionalCost;
		private List<Discount> discounts = Collections.emptyList();
		private BigDecimal discount;
		private BigDecimal tax;
		private String currency;
		private Map<String, String> addData = Collections.emptyMap();
		private String shortDescription;
		private String description;
		private boolean availability;

		private Builder() {
		}

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withSerial(String serial) {
			this.serial = serial;
			return this;
		}

		public Builder withProduct(Product product) {
			this.product = product;
			return this;
		}

		public Builder withProductID(String productID) {
			this.productID = productID;
			return this;
		}

		public Builder withUnits(int units) {
			this.units = units;
			return this;
		}

		public Builder withPeriodType(PeriodType periodType) {
			this.periodType = periodType;
			return this;
		}

		public Builder withCost(BigDecimal cost) {
			this.cost = cost;
			return this;
		}

		public Builder withAdditionalCost(BigDecimal additionalCost) {
			this.additionalCost = additionalCost;
			return this;
		}

		public Builder withDiscounts(List<Discount> discounts) {
			this.discounts = discounts;
			return this;
		}

		public Builder withDiscount(BigDecimal discount) {
			this.discount = discount;
			return this;
		}

		public Builder withTax(BigDecimal tax) {
			this.tax = tax;
			return this;
		}

		public Builder withCurrency(String currency) {
			this.currency = currency;
			return this;
		}

		public Builder withAddData(Map<String, String> addData) {
			this.addData = addData;
			return this;
		}

		public Builder withShortDescription(String shortDescription) {
			this.shortDescription = shortDescription;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withAvailability(boolean availability) {
			this.availability = availability;
			return this;
		}

		public ProductRequestMock build() {
			return new ProductRequestMock(this);
		}
	}

}
