package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.uoutec.community.ediacaran.persistence.entityaccess.jpa.AbstractEntityAccess;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttribute;
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityID;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityType;
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
			
			pEntity = (ProductEntity)entityManager.merge(pEntity);
			
			if(pEntity.getAttributes() != null) {
				
				for(ProductAttributeValueEntity e: pEntity.getAttributes()) {
					if(entityManager.find(ProductAttributeValueIndexEntity.class, e.getId()) == null) {
						entityManager.persist(e);
					}
					else {
						e = entityManager.merge(e);
					}
				}
				
				entityManager.flush();
				
				Map<ProductAttributeValueEntityID, ProductAttributeValueEntity> attrsMap = 
						pEntity.getAttributes().stream()
								.collect(Collectors.toMap((e)->e.getId(), (e)->e));
				
				ProductEntity actualEntity = entityManager.find(ProductEntity.class, value.getId());
				
				for(ProductAttributeValueEntity e: actualEntity.getAttributes()) {
					if(!attrsMap.containsKey(e.getId())) {
						entityManager.remove(e);
					}
				}
			}
			
			entityManager.flush();
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

		    addGenericfilter(value, and, builder, productIndex);

		    if(value.getFilters() != null && !value.getFilters().isEmpty()) {
			    addFilters(value.getFilters(), root, builder, and);
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
		    	
		    	
		    	if(!filter.getProductMetadataAttribute().getOptions().isEmpty()) {
			    	Object v = e.parseValue();
			    	
			    	List<ProductMetadataAttributeOption> opts = filter.getProductMetadataAttribute().getOptions();
			    	
			    	for(ProductMetadataAttributeOption o: opts) {
			    		if(v.equals(o.getValue())) {
			    			ProductMetadataAttributeSearchResultOptionEntityFilter op = new ProductMetadataAttributeSearchResultOptionEntityFilter();
			    			op.setOption(o);
			    			filter.getOptions().put(v, op);
			    		}
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
	
	private void addFilters(Set<ProductSearchFilter> productFilters, 
			From<ProductAttributeValueIndexEntity, ProductAttributeValueIndexEntity> from, CriteriaBuilder builder, List<Predicate> and) {
		
		Set<ProductSearchAttributeFilter> filters = new HashSet<>();
		productFilters.stream()
			.map((e)->e.getAttributeFilters())
			.forEach((e)->filters.addAll(e));
		
		In<Integer> productAttributeIn = builder.in(from.get("id.productMetadataAttributeID"));
		
		for(ProductSearchAttributeFilter attr: filters) {
			productAttributeIn.value(attr.getProductMetadataAttribute().getId());
		}
		
		List<Predicate> productFiltersOr = new ArrayList<>();
		
		for(ProductSearchFilter filter: productFilters) {
			
			List<Predicate> productAttributeFiltersAnd = new ArrayList<>();
			
			for(ProductSearchAttributeFilter attrFilter: filter.getAttributeFilters()) {

				ProductMetadataAttribute productMetadataAttribute = attrFilter.getProductMetadataAttribute();
	    		ProductAttributeValueEntityType entityType = ProductAttributeValueEntityType.valueOf(productMetadataAttribute.getName());

	    		Set<Object> vals = attrFilter.getValue();
	    		
	    		if(vals.size() == 1) {
	    			
	    			if(entityType == ProductAttributeValueEntityType.TEXT) {
			    		productAttributeFiltersAnd.add(
			    				builder.and(
				    				builder.equal(from.get("id.metadataAttributeID"), productMetadataAttribute.getId()),
				    				builder.equal(from.get("value"), entityType.toValue(vals.iterator().next()))
	    						)
	    				);
	    			}
	    			else {
			    		productAttributeFiltersAnd.add(
			    				builder.and(
				    				builder.equal(from.get("id.metadataAttributeID"), productMetadataAttribute.getId()),
				    				builder.equal(from.get("number"), entityType.toValue(vals.iterator().next()))
	    						)
			    		);
	    			}
	    			
	    		}
	    		else
	    		if(vals.size() > 1){
	    			
	    			In<Object> optionsIn;
	    			
	    			if(entityType == ProductAttributeValueEntityType.TEXT) {
		    			optionsIn = builder.in(from.get("value"));
	    			}
	    			else {
		    			optionsIn = builder.in(from.get("number"));
	    			}
	    			
	    			for(Object v: vals) {
	    				optionsIn.value(entityType.toValue(v));
	    				
	    			}

		    		productAttributeFiltersAnd.add(
		    				builder.and(
			    				builder.equal(from.get("id.metadataAttributeID"), productMetadataAttribute.getId()),
			    				optionsIn
    						)
    				);
	    			
	    		}
	    		
			}
			
			if(!productAttributeFiltersAnd.isEmpty()) {
				productFiltersOr.add(
						builder.and(
								builder.equal(from.get("productMetadataID"), filter.getProductMetadata().getId()),
								builder.and(productAttributeFiltersAnd.stream().toArray(Predicate[]::new))
						)
				);
			}
			
		}
		
		if(!productFiltersOr.isEmpty()) {
			and.add(
				builder.and(
					productAttributeIn, 
					builder.or(productFiltersOr.stream().toArray(Predicate[]::new))
				)
			);
		}
	    
	}
	
	private void addGenericfilter(ProductSearch value, List<Predicate> and, CriteriaBuilder builder, From<ProductIndexEntity, ProductAttributeValueIndexEntity> from) {
		
	    if(value.getDescription() != null) {
	    	and.add(builder.equal(from.get("description"), StringUtil.toSearch(value.getDescription())));
	    }

	    if(value.getName() != null) {
	    	and.add(builder.equal(from.get("name"), StringUtil.toSearch(value.getName())));
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

	    if(value.getProductType() != null) {
		    and.add(builder.equal(from.get("productType"), value.getProductType()));
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
