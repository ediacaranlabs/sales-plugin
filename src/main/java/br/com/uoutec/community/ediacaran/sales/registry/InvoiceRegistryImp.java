package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.RuntimeSecurityPermission;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.system.event.EventRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;
import br.com.uoutec.entity.registry.ParentValidation;
import br.com.uoutec.entity.registry.RegistryException;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

@Singleton
public class InvoiceRegistryImp implements InvoiceRegistry{

	private static final String ORDER_EVENT_GROUP = "ORDER";

	public static final String basePermission = "app.registry.sales.invoice.";

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private InvoiceEntityAccess entityAccess;

	@Inject
	private EventRegistry throwSystemEventRegistry;
	
	@Inject
	private OrderRegistry orderRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;
	
	@Override
	public void registerInvoice(Invoice entity) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "register"));
		
		if(entity.getOrder() == null) {
			throw new InvoiceRegistryException("order is empty");
		}
		
		try{
			Order order = orderRegistry.findById(entity.getOrder());
			
			if(order == null) {
				throw new InvoiceRegistryException("order not found #" + entity.getOrder());
			}
			
			if(entity.getId() == null){
				this.registryNewInvoice(entity, order);
			}
			else{
				this.updateInvoice(entity, order);
			}
		}
		catch(ValidationException e){
			throw new InvoiceRegistryException(e.getMessage());
		}
		catch(InvoiceRegistryException e){
			throw e;
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}

	@Override
	public void removeInvoice(Invoice entity) throws InvoiceRegistryException {
		try {
			entityAccess.delete(entity);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	public Invoice findById(String id) throws InvoiceRegistryException {
		try {
			return entityAccess.findById(id);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	public Invoice createInvoice(String orderID, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "invoice"));
		
		try{
			return this.safeCreateInvoice(orderID, itens, message);
		}
		catch(RegistryException e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw e;
		}
		catch(Throwable e){
			throwSystemEventRegistry.error(ORDER_EVENT_GROUP, null, "Falha ao criar a fatura", e);
			throw new OrderRegistryException(e);
		}
	}

	@Override
	public List<Invoice> findByOrder(String id) throws InvoiceRegistryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoice> getInvoices(Integer first, Integer max) throws InvoiceRegistryException {
		// TODO Auto-generated method stub
		return null;
	}

	
	private Invoice safeCreateInvoice(String orderID, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException{

		Order order = orderRegistry.findById(orderID);
		
		Invoice i = createInvoice(order, itens);
		
		checkInvoice(order, i);
		
		SystemUser user;
		
		try{
			user = this.systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}

		
		//Processa os itens do pedido

		for(ProductRequest productRequest: i.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.registryItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new OrderRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}


		try {
			entityAccess.save(i);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new OrderRegistryException(
				"invoice error: " + order.getId(), e);
		}
		
		//Registra as alterações do pedido
		//this.registerOrder(order);
		
		//Registra o evento no log
		orderRegistry.registryLog(order.getId(), message != null? message : "Criada a fatura #" + i.getId() );
		
		return i;
	}

	private Invoice createInvoice(Order order, Map<String, Integer> itens
			) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {

		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: order.getItens()) {
			ProductRequest tpr = new ProductRequest();
			tpr.setAddData(pr.getAddData());
			tpr.setAvailability(pr.isAvailability());
			tpr.setCost(pr.getCost());
			tpr.setCurrency(pr.getCurrency());
			tpr.setDescription(pr.getDescription());
			tpr.setMaxExtra(pr.getMaxExtra());
			tpr.setName(pr.getName());
			tpr.setPeriodType(pr.getPeriodType());
			tpr.setProduct(pr.getProduct());
			tpr.setProductID(pr.getProductID());
			tpr.setSerial(pr.getSerial());
			tpr.setShortDescription(pr.getShortDescription());
			tpr.setTaxes(pr.getTaxes());
			//tpr.setUnits(pr.getUnits());
			transientItens.put(pr.getSerial(), tpr);
		}
		
		//create productrequest invoice
		List<ProductRequest> invoiceItens = new ArrayList<>();
		
		for(Entry<String,Integer> e: itens.entrySet()) {
			ProductRequest tpr = transientItens.get(e.getKey());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(e.getKey());
			}
			
			tpr.setUnits(e.getValue().intValue());
			invoiceItens.add(tpr);
			transientItens.remove(e.getKey());
		}
		
		Invoice i = new Invoice();
		i.setId(null);
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(invoiceItens);
		
		return i;
	}
	
	private void checkInvoice(Order order, Invoice invoice
			) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {

		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: order.getItens()) {
			ProductRequest tpr = new ProductRequest();
			tpr.setAddData(pr.getAddData());
			tpr.setAvailability(pr.isAvailability());
			tpr.setCost(pr.getCost());
			tpr.setCurrency(pr.getCurrency());
			tpr.setDescription(pr.getDescription());
			tpr.setMaxExtra(pr.getMaxExtra());
			tpr.setName(pr.getName());
			tpr.setPeriodType(pr.getPeriodType());
			tpr.setProduct(pr.getProduct());
			tpr.setProductID(pr.getProductID());
			tpr.setSerial(pr.getSerial());
			tpr.setShortDescription(pr.getShortDescription());
			tpr.setTaxes(pr.getTaxes());
			tpr.setUnits(pr.getUnits());
			transientItens.put(pr.getSerial(), tpr);
		}
		
		boolean invoiceExist = false;
		
		if(order.getInvoice() != null) {
			for(Invoice i: order.getInvoice()) {
				
				if(invoice.getId() != null && i.getId().equals(invoice.getId())) {
					invoiceExist = true;
				}
				
				for(ProductRequest pr: i.getItens()) {
					ProductRequest tpr = transientItens.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}
				}
			}
		}
		
			for(ProductRequest pr: invoice.getItens()) {
				ProductRequest tpr = transientItens.get(pr.getSerial());
				
				if(tpr == null) {
					throw new ItemNotFoundOrderRegistryException(pr.getSerial());
				}

				if(!invoiceExist) {
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
				}
				
				if(tpr.getUnits() < 0) {
					throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
				}
			}
		
	}
	
	private void registryNewInvoice(Invoice entity, Order order) 
			throws PaymentGatewayException, EntityAccessException, 
			ValidationException, ItemNotFoundOrderRegistryException, 
			InvalidUnitsOrderRegistryException{
		validateInvoice(entity, saveValidations);
		checkInvoice(order, entity);
		entityAccess.save(entity);
		entityAccess.flush();
	}

	private void updateInvoice(Invoice entity, Order order
			) throws EntityAccessException, ValidationException, 
			ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException{
		validateInvoice(entity, updateValidations);
		checkInvoice(order, entity);
		entityAccess.update(entity);
		entityAccess.flush();
	}
	
	private void validateInvoice(Invoice e, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(e, groups);
	}
	
}
