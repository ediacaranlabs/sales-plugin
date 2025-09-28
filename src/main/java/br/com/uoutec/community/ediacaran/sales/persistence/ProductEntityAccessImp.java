package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductAttributeValueType;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

@RequestScoped
public class ProductEntityAccessImp 
	extends AbstractEntityAccess<Product, ProductEntity>
	implements ProductEntityAccess{

	public ProductEntityAccessImp() {
		super(null);
	}
	
	@Inject
	public ProductEntityAccessImp(EntityManager entityManager) {
		super(entityManager);
	}
	
	public void save(Product value) throws EntityAccessException {
		try{
			ProductEntity pEntity = new ProductEntity(value);
			entityManager.persist(pEntity);
			entityManager.flush();
			
			if(pEntity.getAttributes() != null) {
				for(ProductAttributeValueEntity e: pEntity.getAttributes()) {
					e.getId().setProductID(pEntity.getId());
					e.setProduct(pEntity);
					entityManager.persist(e);
				}
			}
			
			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void update(Product value) throws EntityAccessException {
		try{
			ProductEntity pEntity = new ProductEntity(value);
			ProductEntity actualEntity = entityManager.find(ProductEntity.class, value.getId());
			//Product actualValue = actualEntity.toEntity();
			
			List<ProductAttributeValueEntity> actualAttributesEntity = actualEntity.getAttributes();
			
			//TODO: javax.persistence.EntityNotFoundException: 
			// Unable to find br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntity with 
			// id br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityID@xxxxx
			List<ProductAttributeValueEntity> attributes = pEntity.getAttributes();
			pEntity.setAttributes(null);
			
			if(attributes != null) {
				
				if(actualAttributesEntity != null) {
					actualAttributesEntity = new ArrayList<>(actualAttributesEntity);
					for(ProductAttributeValueEntity e: actualAttributesEntity) {
						//TODO: cascade exception productIndex
						e.setProductIndex(null);
						entityManager.remove(e);
					}
				}
				
				//entityManager.flush();
				/*
				if(actualValue.getAttributes() != null) {
					for(ProductAttributeValue e: actualValue.getAttributes().values()) {
						ProductAttributeValueEntity x = new ProductAttributeValueEntity(e.getValue(), e, actualValue);
						x = entityManager.merge(x);
						entityManager.remove(x);
					}
				}
				*/
				
				for(ProductAttributeValueEntity e: attributes) {
					if(entityManager.find(ProductAttributeValueEntity.class, e.getId()) == null) {
						entityManager.persist(e);
					}
					else {
						e = entityManager.merge(e);
					}
				}

			}

			pEntity = (ProductEntity)entityManager.merge(pEntity);
			pEntity.setAttributes(attributes);
			entityManager.flush();

			pEntity.toEntity(value);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void delete(Product value) throws EntityAccessException {
		try{
			ProductEntity pEntity = entityManager.find(ProductEntity.class, value.getId());
			
			if(pEntity != null) {
				for(ProductAttributeValueEntity e: pEntity.getAttributes()) {
					entityManager.remove(e);
				}
				
				entityManager.remove(pEntity);
			}
			
			entityManager.flush();
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public ProductEntitySearchResult searchProduct(ProductSearch value, Integer first, Integer max) throws EntityAccessException {
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    List<Predicate> and = new ArrayList<Predicate>();
		    CriteriaQuery<ProductAttributeValueIndexEntity> criteria = builder.createQuery(ProductAttributeValueIndexEntity.class);
		    Root<ProductAttributeValueIndexEntity> root = criteria.from(ProductAttributeValueIndexEntity.class);
		    Join<ProductIndexEntity, ProductAttributeValueIndexEntity> productIndex = root.join("productIndex");	
		    //Join<ProductAttributeValueIndexEntity, ProductIndexEntity> attributes = productIndex.joinList("attributes");	

		    addGenericfilter(value, and, builder, productIndex);

		    if(value.getFilters() != null && !value.getFilters().isEmpty()) {
			    //addFilters(value.getFilters(), attributes, builder, and);
		    	Predicate predicate = ProductIndexUtil.addFilters(value.getFilters(), productIndex, builder);
		    	if(predicate != null) {
		    		and.add(predicate);
		    	}
		    }
		    
		    criteria.select(root);
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.asc(productIndex.get("name")));
	    	
		    TypedQuery<ProductAttributeValueIndexEntity> typed = entityManager.createQuery(criteria);


		    List<ProductAttributeValueIndexEntity> list = (List<ProductAttributeValueIndexEntity>)typed.getResultList();
		    Set<Integer> idCache = new HashSet<>();
		    List<Integer> ids = new ArrayList<>();
		    Set<AttributeFilter> filtersSet = productFilterToFilterSet(value);
		    
		    int count = 0;
		    
		    Map<Integer,ProductMetadataSearchResultEntityFilter> productGroupFilter = new HashMap<>();
		    
		    for(ProductAttributeValueIndexEntity e: list) {
		    	
		    	if(!idCache.contains(e.getId().getProductID())) {
		    		idCache.add(e.getId().getProductID());
		    		
			    	if((first == null || (count >= first)) && (max == null || ids.size() < max)) {
			    		ids.add(e.getId().getProductID());
			    	}
		    		
			    	count++;
		    	}
		    	
		    	ProductMetadataSearchResultEntityFilter group = productGroupFilter.get(e.getProductMetadataID());
		    	
		    	if(group == null) {
		    		group = new ProductMetadataSearchResultEntityFilter();
		    		group.setFilters(new HashMap<>());
		    		group.setProductMetadata(e.getProductMetadata().toEntity());
		    		productGroupFilter.put(e.getProductMetadataID(), group);
		    	}
		    	
		    	ProductMetadataAttributeSearchResultEntityFilter filter = group.getFilters().get(e.getId().getProductMetadataAttributeID());
		    	
		    	if(filter == null) {
		    		filter = new ProductMetadataAttributeSearchResultEntityFilter();
		    		filter.setProductMetadataAttribute(group.getProductMetadata().getAttributes().get(e.getAttributeID()));
		    		filter.setOptions(new HashMap<>());
		    		group.getFilters().put(e.getId().getProductMetadataAttributeID(), filter);
		    	}
		    	
		    	
		    	Object v = e.parseValue();
		    	
		    	if(!filter.getOptions().containsKey(v)) {
		    		
			    	if(!filter.getProductMetadataAttribute().getOptions().isEmpty()) {
			    		
				    	List<ProductMetadataAttributeOption> opts = filter.getProductMetadataAttribute().getOptions();
				    	
				    	for(ProductMetadataAttributeOption o: opts) {
				    		if(v.equals(o.getValue())) {
				    			ProductMetadataAttributeSearchResultOptionEntityFilter op = 
				    					new ProductMetadataAttributeSearchResultOptionEntityFilter(o);
				    			
				    			op.setSelected(filtersSet.contains(new AttributeFilter(filter.getProductMetadataAttribute().getId(), v)));
				    			
				    			filter.getOptions().put(v, op);
				    		}
				    	}
				    	
			    	}
			    	else {
			    		
			    		String description = e.getId().getValue();
			    		ProductAttributeValueType type = filter.getProductMetadataAttribute().getValueType();
			    		ProductMetadataAttributeSearchResultOptionEntityFilter op = 
			    				new ProductMetadataAttributeSearchResultOptionEntityFilter(v, description, type, false);
			    		
		    			op.setSelected(filtersSet.contains(new AttributeFilter(filter.getProductMetadataAttribute().getId(), v)));
			    		
		    			filter.getOptions().put(v, op);
		    			
			    	}
			    	
		    	}
		    	
		    }
		    
		    List<Product> itens = new ArrayList<Product>();
		    
		    for(Integer id: ids) {
		    	Product p = new Product();
		    	p.setId(id);
		    	itens.add(p);
		    }
		    
		    ProductEntitySearchResult result = new ProductEntitySearchResult();
		    result.setFilters(productGroupFilter);
		    result.setItens(itens);
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}		
	}
	
	public List<Product> getProductByType(ProductType productType) throws EntityAccessException{
		
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductEntity> criteria = 
		    		builder.createQuery(ProductEntity.class);
		    Root<ProductEntity> from = 
		    		criteria.from(ProductEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), productType.getCode())
    				)
    		);
		    
		    TypedQuery<ProductEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductEntity> list = (List<ProductEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}

	public List<Product> getProductByCode(String code) throws EntityAccessException{
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    CriteriaQuery<ProductEntity> criteria = 
		    		builder.createQuery(ProductEntity.class);
		    Root<ProductEntity> from = 
		    		criteria.from(ProductEntity.class);
		    
		    criteria.select(from);
		    
		    criteria.where(
		    		builder.and(
		    				builder.equal(from.get("productType"), code)
    				)
    		);
		    
		    TypedQuery<ProductEntity> typed = 
		    		entityManager.createQuery(criteria);


		    List<ProductEntity> list = (List<ProductEntity>)typed.getResultList();
		    List<Product> result = new ArrayList<Product>();
    
		    for(ProductEntity e: list) {
		    	result.add(e.toEntity());
		    }
		    
			return result;
		}
		catch (Throwable e) {
			throw new EntityAccessException(e);
		}

	}
	
	/*
	private void addFilters(Set<ProductSearchFilter> productFilters, 
			From<ProductAttributeValueIndexEntity, ProductIndexEntity> from, CriteriaBuilder builder, List<Predicate> and) {
		
		Set<ProductSearchAttributeFilter> filters = new HashSet<>();
		productFilters.stream()
			.map((e)->e.getAttributeFilters())
			.filter((e)->e != null)
			.forEach((e)->filters.addAll(e));
		
		In<Integer> productAttributeIn = builder.in(from.get("id").get("productMetadataAttributeID"));
		
		for(ProductSearchAttributeFilter attr: filters) {
			if(attr.getValue() != null && !attr.getValue().isEmpty()) {
				productAttributeIn.value(attr.getProductMetadataAttribute().getId());
			}
		}
		
		List<Predicate> productFiltersOr = new ArrayList<>();
		
		for(ProductSearchFilter filter: productFilters) {
			
			List<Predicate> productAttributeFiltersAnd = new ArrayList<>();
			
			for(ProductSearchAttributeFilter attrFilter: filter.getAttributeFilters()) {

				ProductMetadataAttribute productMetadataAttribute = attrFilter.getProductMetadataAttribute();
	    		ProductAttributeValueEntityType entityType = ProductAttributeValueEntityType.valueOf(productMetadataAttribute.getValueType().name());

	    		Set<Object> vals = attrFilter.getValue();
	    		
	    		if(vals == null || vals.isEmpty()) {
	    			continue;
	    		}
	    		
	    		if(vals.size() == 1) {
	    			
	    			if(entityType == ProductAttributeValueEntityType.TEXT) {
			    		productAttributeFiltersAnd.add(
			    				builder.and(
				    				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
				    				builder.equal(from.get("id").get("value"), entityType.toValue(vals.iterator().next()))
	    						)
	    				);
	    			}
	    			else {
			    		productAttributeFiltersAnd.add(
			    				builder.and(
				    				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
				    				builder.equal(from.get("number"), entityType.toValue(vals.iterator().next()))
	    						)
			    		);
	    			}
	    			
	    		}
	    		else
	    		if(vals.size() > 1){
	    			
	    			In<Object> optionsIn;
	    			
	    			if(entityType == ProductAttributeValueEntityType.TEXT) {
		    			optionsIn = builder.in(from.get("id").get("value"));
	    			}
	    			else {
		    			optionsIn = builder.in(from.get("number"));
	    			}
	    			
	    			for(Object v: vals) {
	    				optionsIn.value(entityType.toValue(v));
	    				
	    			}

		    		productAttributeFiltersAnd.add(
		    				builder.and(
			    				builder.equal(from.get("id").get("productMetadataAttributeID"), productMetadataAttribute.getId()),
			    				optionsIn
    						)
    				);
	    			
	    		}
	    		
			}
			
			if(!productAttributeFiltersAnd.isEmpty()) {
				productFiltersOr.add(
						builder.and(
								builder.equal(from.get("productMetadataID"), filter.getProductMetadata().getId()),
								//builder.and(productAttributeFiltersAnd.stream().toArray(Predicate[]::new))
								builder.or(productAttributeFiltersAnd.stream().toArray(Predicate[]::new))
						)
				);
			}
			
		}
		
		if(!productFiltersOr.isEmpty()) {
			and.add(
				builder.and(
					productAttributeIn, 
					builder.or(productFiltersOr.stream().toArray(Predicate[]::new))
					//builder.and(productFiltersOr.stream().toArray(Predicate[]::new))
				)
			);
		}
	    
	}
	*/
	
	private void addGenericfilter(ProductSearch value, List<Predicate> and, CriteriaBuilder builder, From<ProductIndexEntity, ProductAttributeValueIndexEntity> from) {

	    if(value.getDisplay() != null) {
		    and.add(builder.equal(from.get("display"), value.getDisplay()));
	    }
		
	    if(value.getDescription() != null) {
	    	and.add(builder.like(from.get("description"), "%" + StringUtil.toSearch(value.getDescription(), "%") + "%"));
	    }

	    if(value.getName() != null) {
	    	and.add(builder.like(from.get("name"), "%" + StringUtil.toSearch(value.getName()) + "%" ));
	    }
	    
	    if(value.getMinCost() != null || value.getMaxCost() != null) {
	    	
	    	if(value.getMinCost() != null || value.getMaxCost() != null) {
			    and.add(builder.between(from.get("cost"), value.getMinCost(), value.getMaxCost()));
	    	}
	    	else
	    	if(value.getMinCost() != null) {
			    and.add(builder.greaterThanOrEqualTo(from.get("cost"), value.getMinCost()));
	    	}
	    	else
	    	if(value.getMaxCost() != null) {
			    and.add(builder.lessThanOrEqualTo(from.get("cost"), value.getMaxCost()));
	    	}
	    	
	    }

	    if(value.getCategory() != null) {
		    and.add(builder.equal(from.get("category"), value.getCategory().getId()));
	    }
	    
	    if(value.getProductType() != null) {
		    and.add(builder.equal(from.get("productType"), value.getProductType()));
	    }
		
	}
	
	private Set<AttributeFilter> productFilterToFilterSet(ProductSearch productSearch){
		Set<AttributeFilter> result = new HashSet<>();
		
		Set<ProductSearchFilter> filters = productSearch.getFilters();
		
		if(filters == null) {
			return result;
		}
		
		for(ProductSearchFilter filter: filters) {
			Set<ProductSearchAttributeFilter> attrFilters = filter.getAttributeFilters();
			
			if(attrFilters == null) {
				continue;
			}
			
			for(ProductSearchAttributeFilter attrFilter: attrFilters) {
				Set<Object> values = attrFilter.getValue();
				
				if(values == null) {
					continue;
				}
				
				for(Object value: values) {
					result.add(new AttributeFilter(attrFilter.getProductMetadataAttribute().getId(), value));
				}
				
			}
			
		}
		
		return result;
	}
	
	private static class AttributeFilter {
		
		public Object value;
		
		public int id;
		
		public AttributeFilter(int id, Object value) {
			this.id = id;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AttributeFilter other = (AttributeFilter) obj;
			return id == other.id && Objects.equals(value, other.value);
		}
		
	}
	
	@Override
	protected ProductEntity toPersistenceEntity(Product entity)
			throws Throwable {
		return new ProductEntity(entity);
	}

	@Override
	protected Product toEntity(ProductEntity entity) throws Throwable {
		return entity.toEntity();
	}

	@Override
	protected void setId(Product entity, Serializable id) throws Throwable {
		entity.setId((Integer) id);
	}

	@Override
	protected Serializable getPersistenceID(ProductEntity value)
			throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable getID(Product value) throws Throwable {
		return value.getId();
	}

	@Override
	protected Serializable toPersistenceID(Serializable value) throws Throwable {
		return value;
	}

}
