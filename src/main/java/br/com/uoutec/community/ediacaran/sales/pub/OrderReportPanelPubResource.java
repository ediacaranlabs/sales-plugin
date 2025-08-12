package br.com.uoutec.community.ediacaran.sales.pub;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.brandao.brutos.annotation.Action;
import org.brandao.brutos.annotation.Basic;
import org.brandao.brutos.annotation.Controller;
import org.brandao.brutos.annotation.DetachedName;
import org.brandao.brutos.annotation.MappingTypes;
import org.brandao.brutos.annotation.Result;
import org.brandao.brutos.annotation.ScopeType;
import org.brandao.brutos.annotation.Transient;
import org.brandao.brutos.annotation.View;
import org.brandao.brutos.annotation.web.ResponseErrors;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.pub.entity.OrderPubEntity;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.security.BasicRoles;
import br.com.uoutec.community.ediacaran.security.RequireAnyRole;
import br.com.uoutec.community.ediacaran.system.i18n.I18nRegistry;
import br.com.uoutec.ediacaran.web.EdiacaranWebInvoker;
import br.com.uoutec.pub.entity.InvalidRequestException;

@Singleton
@Controller(value="${plugins.ediacaran.front.panel_context}/orders/report")
@ResponseErrors(rendered=false)
public class OrderReportPanelPubResource {

	@Transient
	@Inject
	private I18nRegistry i18nRegistry;
	
	@Transient
	@Inject
	private OrderReportRegistry orderReportRegistry;
	
	@Action("/new/{id}")
	@View("${plugins.ediacaran.sales.template}/front/panel/order/report/edit")
	@Result("vars")
	@RequireAnyRole({BasicRoles.USER, BasicRoles.MANAGER, BasicRoles.CLIENT})
	public Map<String,Object> newEntity(
			@DetachedName
			OrderPubEntity orderPubEntity,
			@Basic(bean=EdiacaranWebInvoker.LOCALE_VAR, scope=ScopeType.REQUEST, mappingType=MappingTypes.VALUE)
			Locale locale
	) throws InvalidRequestException {
		
		Order order;
		
		try{
			order = orderPubEntity.rebuild(true, false, false);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportPanelPubResourceMessages.new_shipping.error.fail_load_entity, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}

		OrderReport orderReport = null;
		try{
			orderReport = orderReportRegistry.toOrderReport(order);
		}
		catch(Throwable ex){
			String error = i18nRegistry
					.getString(
							OrderReportPanelPubResourceMessages.RESOURCE_BUNDLE,
							OrderReportPanelPubResourceMessages.new_shipping.error.create_shipping, 
							locale);
			
			throw new InvalidRequestException(error + " (" + ex.getMessage() + ")", ex);
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderReport", orderReport);
		return map;
	}
	
}
