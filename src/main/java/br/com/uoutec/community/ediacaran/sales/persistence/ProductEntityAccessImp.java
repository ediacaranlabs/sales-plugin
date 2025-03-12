package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

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
import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadataAttributeOption;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchAttributeFilter;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueEntityType;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductAttributeValueIndexEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductEntity;
import br.com.uoutec.community.ediacaran.sales.persistence.entity.ProductIndexEntity;
import br.com.uoutec.community.ediacaran.system.util.StringUtil;
import br.com.uoutec.persistence.EntityAccessException;

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

	public void saveIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.persist(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void updateIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.merge(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public void deleteIndex(Product value) throws EntityAccessException {
		try{
			ProductIndexEntity pEntity = new ProductIndexEntity(value);
			entityManager.remove(pEntity);
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}

	public boolean ifIndexExist(Product value) throws EntityAccessException {
		try{
			Object o = entityManager.find(ProductIndexEntity.class, value.getId());
			return o != null;
    	}
    	catch(Throwable e){
    		throw new EntityAccessException(e);
    	}
	}
	
	public ProductEntitySearchResult searchProduct(ProductSearch value, Integer first, Integer max) throws EntityAccessException {
		
		try {
			Map<Integer, Set<ProductSearchAttributeFilter>> groups = groupFilters(value.getFilters());
			
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		    List<Predicate> and = new ArrayList<Predicate>();
		    CriteriaQuery<ProductAttributeValueIndexEntity> criteria = builder.createQuery(ProductAttributeValueIndexEntity.class);
		    Root<ProductAttributeValueIndexEntity> root = criteria.from(ProductAttributeValueIndexEntity.class);
		    Join<ProductIndexEntity, ProductAttributeValueIndexEntity> from = root.join("productIndex");	

		    addGenericfilter(value, and, builder, from);

		    if(!groups.isEmpty()) {
			    addFilters(groups, root, builder, and);
		    }
		    
		    criteria.select(from);
		    
		    if(!and.isEmpty()) {
			    criteria.where(
			    		builder.and(
			    				and.stream().toArray(Predicate[]::new)
    					)
	    		);
		    }
		    
	    	List<javax.persistence.criteria.Order> orderList = 
	    			new ArrayList<javax.persistence.criteria.Order>();
	    	orderList.add(builder.asc(from.get("name")));
	    	
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
	
	private void addFilters(Map<Integer, Set<ProductSearchAttributeFilter>> groups, 
			From<ProductAttributeValueIndexEntity, ProductAttributeValueIndexEntity> from, CriteriaBuilder builder, List<Predicate> and) {
		
		In<Integer> productAttributeIn = builder.in(from.get("productAttribute"));
		for(Integer attributeID: groups.keySet()) {
			productAttributeIn.value(attributeID);
		}
		
		and.add(productAttributeIn);
		
		List<Predicate> filtersProduct = new ArrayList<>();
		
	    for(Entry<Integer, Set<ProductSearchAttributeFilter>> filters: groups.entrySet()) {
	    	
			List<Predicate> filtersAnd = new ArrayList<>();
			
	    	for(ProductSearchAttributeFilter f: filters.getValue()) {
	    		
	    		List<Predicate> localEnds = new ArrayList<>();
	    		
	    		localEnds.add(builder.equal(from.get("id.metadataAttributeID"), f.getProductMetadata()));
	    		localEnds.add(builder.equal(from.get("id.metadataAttributeID"), f.getProductAttribute()));
	    		
	    		switch (f.getType()) {
	    		case TEXT:
	    			localEnds.add(builder.equal(from.get("value"), ProductAttributeValueEntityType.TEXT.toValue(f.getValue())));
	    			break;
	    		case INTEGER:
	    			localEnds.add(builder.equal(from.get("number"), ProductAttributeValueEntityType.INTEGER.toValue(f.getValue())));
	    			break;
	    		case DECIMAL:
	    			localEnds.add(builder.equal(from.get("number"), ProductAttributeValueEntityType.DECIMAL.toValue(f.getValue())));
	    			break;
	    		case DATE:
	    			localEnds.add(builder.equal(from.get("number"), ProductAttributeValueEntityType.DATE.toValue(f.getValue())));
	    			break;
	    		case DATE_TIME:
	    			localEnds.add(builder.equal(from.get("number"), ProductAttributeValueEntityType.DATE_TIME.toValue(f.getValue())));
	    			break;
	    		case TIME:
	    			localEnds.add(builder.equal(from.get("number"), ProductAttributeValueEntityType.TIME.toValue(f.getValue())));
	    			break;
	    		}		
	    		
	    		filtersAnd.add(builder.and(localEnds.stream().toArray(Predicate[]::new)));
	    	}
	    	
	    	filtersProduct.add(builder.and(filtersAnd.stream().toArray(Predicate[]::new)));
	    }
		
	    and.add(builder.or(filtersProduct.stream().toArray(Predicate[]::new)));
	    
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
	
	private Map<Integer, Set<ProductSearchAttributeFilter>> groupFilters(Set<ProductSearchAttributeFilter> filters){
		
		Map<Integer, Set<ProductSearchAttributeFilter>> filtersGroup = 
				filters.stream()
				.collect(Collectors.groupingBy(ProductSearchAttributeFilter::getProductMetadata, Collectors.toSet()));
		
		return filtersGroup;
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
