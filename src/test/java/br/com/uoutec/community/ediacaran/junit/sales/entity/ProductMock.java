package br.com.uoutec.community.ediacaran.junit.sales.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

import br.com.uoutec.community.ediacaran.sales.entity.Discount;
import br.com.uoutec.community.ediacaran.sales.entity.PeriodType;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;

public class ProductMock extends Product{

	private static final long serialVersionUID = -6172034849966870684L;

	@Generated("SparkTools")
	private ProductMock(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.periodType = builder.periodType;
		this.productType = builder.productType;
		this.additionalCost = builder.additionalCost;
		this.cost = builder.cost;
		this.currency = builder.currency;
		this.discounts = builder.discounts;
		this.discount = builder.discount;
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private int id;
		private String name;
		private String description;
		private PeriodType periodType;
		private ProductType productType;
		private BigDecimal additionalCost;
		private BigDecimal cost;
		private String currency;
		private List<Discount> discounts = Collections.emptyList();
		private BigDecimal discount;

		private Builder() {
		}

		public Builder withId(int id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withPeriodType(PeriodType periodType) {
			this.periodType = periodType;
			return this;
		}

		public Builder withProductType(ProductType productType) {
			this.productType = productType;
			return this;
		}

		public Builder withAdditionalCost(BigDecimal additionalCost) {
			this.additionalCost = additionalCost;
			return this;
		}

		public Builder withCost(BigDecimal cost) {
			this.cost = cost;
			return this;
		}

		public Builder withCurrency(String currency) {
			this.currency = currency;
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

		public ProductMock build() {
			return new ProductMock(this);
		}
	}

}
