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
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.security.Principal;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.event.EventRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
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

	private static final Class<?>[] saveValidations = 
			new Class[] {DataValidation.class, ParentValidation.class};

	private static final Class<?>[] updateValidations = 
			new Class[] { IdValidation.class, DataValidation.class, ParentValidation.class};
	
	@Inject
	private InvoiceEntityAccess entityAccess;

	@Inject
	private EventRegistry throwSystemEventRegistry;
	
	@Inject
	private SystemUserRegistry systemUserRegistry;
	
	@Inject
	private ProductTypeRegistry productTypeRegistry;

	@Inject
	private SubjectProvider subjectProvider;
	
	
	@Override
	public void registerInvoice(Invoice entity) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getRegisterPermission());
		
		if(entity.getOrder() == null) {
			throw new InvoiceRegistryException("order is empty");
		}
		
		try{
			OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
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
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getRemovePermission());
		
		try {
			entityAccess.delete(entity);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	public Invoice findById(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, null);
	}

	public Invoice findById(String id, SystemUserID userID) throws InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());

		SystemUser systemUser = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		return unsafeFindById(id, systemUser);
		
	}
	
	public Invoice findById(String id, SystemUser systemUser) throws InvoiceRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		return unsafeFindById(id, systemUser);
	}
	
	private Invoice unsafeFindById(String id, SystemUser systemUser) throws InvoiceRegistryException {
		
		try{
			Invoice e = entityAccess.findById(id);
			
			if(e != null) {
				if(systemUser != null && e.getOwner() != systemUser.getId()) {
					return null;
				}
			}
			return e;
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}
	
	@Override
	public List<InvoiceResultSearch> searchInvoice(InvoiceSearch value, Integer first, Integer max) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.ORDER_REGISTRY.getSearchPermission());
		
		try{
			return entityAccess.search(value, first, max);
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(e);
		}
	}

	@Override
	public Invoice createInvoice(Order order, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, SystemUserRegistryException, InvoiceRegistryException{
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCreatePermission());
		
		SystemUserID userID = getSystemUserID();
		SystemUser user = getSystemUser(userID);
		
		try {
			return unsafeCreateInvoice(order, user, itens, message);
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
	public Invoice createInvoice(Order order, SystemUserID userID, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, SystemUserRegistryException, InvoiceRegistryException{
		
		SystemUser user = getSystemUser(userID);
		return createInvoice(order, user, itens, message);
	}
	
	@Override
	public Invoice createInvoice(Order order, SystemUser systemUser, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getCreatePermission());
		
		if(systemUser == null) {
			throw new NullPointerException("systemUser");
		}
		
		try {
			return unsafeCreateInvoice(order, systemUser, itens, message);
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
	
	public Invoice toInvoice(Order order
			) throws OrderNotFoundRegistryException, ItemNotFoundOrderRegistryException, 
				InvalidUnitsOrderRegistryException {
		
		Order actualOrder = null;
		
		try {
			OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
			actualOrder = orderRegistry.findById(order.getId());
		}
		catch(Throwable e) {
			throw new OrderNotFoundRegistryException(e);
		}
		
		if(actualOrder == null) {
			throw new OrderNotFoundRegistryException(order.getId());
		}
		
		return createInvoice(actualOrder);
	}
	
	@Override
	public List<Invoice> findByOrder(String id) throws InvoiceRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());
		
		try {
			return entityAccess.findByOrder(id, null);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}

	@Override
	public List<Invoice> findByOrder(String id, SystemUserID userID) throws InvoiceRegistryException{

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.INVOICE_REGISTRY.getFindPermission());

		SystemUser systemUser = null;
		
		try {
			if(SystemUserRegistry.CURRENT_USER.equals(userID)) {
				userID = getSystemUserID();
			}
			systemUser = getSystemUser(userID);
		}
		catch (SystemUserRegistryException e) {
			throw new InvoiceRegistryException(e);
		}
		
		try {
			return entityAccess.findByOrder(id, systemUser);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
		
	}
	
	private Invoice unsafeCreateInvoice(Order order, SystemUser systemUser, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, InvoiceRegistryException {

		OrderRegistry orderRegistry = EntityContextPlugin.getEntity(OrderRegistry.class);
		Order actualOrder = orderRegistry.findById(order.getId());
		
		if(actualOrder.getOwner() != systemUser.getId()) {
			throw new InvoiceRegistryException("invalid user: " + actualOrder.getOwner() + " != " + systemUser.getId());
		}
		
		Invoice i = createInvoice(actualOrder, itens);
		
		checkInvoice(actualOrder, i);
		
		SystemUser user;
		
		try{
			user = this.systemUserRegistry.findById(actualOrder.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + actualOrder.getOwner());
		}

		
		//Processa os itens do pedido

		for(ProductRequest productRequest: i.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.registryItem(user, actualOrder, productRequest);
			}
			catch(Throwable e){
				throw new InvoiceRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}


		try {
			entityAccess.save(i);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(
				"invoice error: " + actualOrder.getId(), e);
		}
		
		//Registra as alterações do pedido
		//this.registerOrder(order);
		
		//Registra o evento no log
		orderRegistry.registryLog(actualOrder.getId(), message != null? message : "Criada a fatura #" + i.getId() );
		
		return i;
	}

	private Invoice createInvoice(Order order) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {

		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: order.getItens()) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		if(order.getInvoice() != null) {
			for(Invoice i: order.getInvoice()) {
				
				for(ProductRequest pr: i.getItens()) {
					ProductRequest tpr = transientItens.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}
				}
				
			}
		}
		
		Invoice i = new Invoice();
		i.setId(null);
		i.setOwner(order.getOwner());
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(new ArrayList<ProductRequest>(transientItens.values()));
		i.setCurrency(order.getPayment().getCurrency());
		
		return i;
	}
	
	private Invoice createInvoice(Order order, Map<String, Integer> itens
			) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {

		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: order.getItens()) {
			ProductRequest tpr = new ProductRequest(pr);
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
		i.setOwner(order.getOwner());
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(invoiceItens);
		i.setCurrency(order.getPayment().getCurrency());
		
		return i;
	}
	
	private void checkInvoice(Order order, Invoice invoice
			) throws ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {

		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: order.getItens()) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		if(order.getInvoice() != null) {
			for(Invoice i: order.getInvoice()) {
				
				if(i.getId().equals(invoice.getId())) {
					continue;
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

			tpr.setUnits(tpr.getUnits() - pr.getUnits());
			
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

	private SystemUser getSystemUser(SystemUserID userID) throws SystemUserRegistryException {
		SystemUser user = systemUserRegistry.getBySystemID(String.valueOf(userID.getSystemID()));
		
		if(user == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return user;
	}

	private SystemUserID getSystemUserID() throws SystemUserRegistryException {
		Subject subject = subjectProvider.getSubject();
		
		if(!subject.isAuthenticated()) {
			throw new SystemUserRegistryException();
		}
		
		Principal principal = subject.getPrincipal();
		java.security.Principal jaaPrincipal = principal.getUserPrincipal();
		
		return ()->jaaPrincipal.getName();
	}
	
}
