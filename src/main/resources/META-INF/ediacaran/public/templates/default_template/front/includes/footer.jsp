<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/designer"   prefix="ed"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"                   prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"              prefix="fn"%>
<%--<ec:load-data context="/" file="/objects/menus/footer-menu" var="footerMenu" />--%>
<%--<ec:load-data context="/" file="/objects/menus/footer-menu2" var="footerMenu2" />--%>
<%--<ec:load-data context="/" file="/objects/social-links" var="socialLinks" />--%>
<%--<ec:load-data file="footer.json" var="objects" />--%>
<ec:import-object id="global/front/social_links" var="socialLinks"/>
<ec:import-object id="global/front/address" var="address"/>
<ec:import-object id="menubar/front/footer_menu2" var="footerMenu2"/>
<ec:import-object id="menubar/front/footer_menu" var="footerMenu"/>

<footer>
	<ed:container>
		<ed:row>
			<ed:col>
				<c:if test="${!empty socialLinks}">
					<ec:list style="inline">
						<c:forEach items="${socialLinks}" var="social_link">
							<ec:list-item>
								<a href="${social_link.src}">
									<ec:icon id="${social_link.id}" icon="${social_link.icon}" bg="square" bgSize="2" size="2" />
								</a>
							</ec:list-item>
						</c:forEach>
					</ec:list>
				</c:if>
			</ed:col>
			<ed:col classStyle="widget">
				<c:if test="${!empty footerMenu2}">
					<ec:menu-bar id="frontFooterMenu2" 
							expand="xl" 
							style="footer">
						<ed:container>
						<ec:menu-bar-brand>
							<h5>${footerMenu2.name}</h5>
						</ec:menu-bar-brand>
						<ec:menu-body collapse="false">
							<ec:menu-itens>
							<c:forEach items="${footerMenu2.itens}" var="menu">
								<c:choose>
			
									<c:when test="${not empty menu.body}">
										<ec:menu>
											<ec:menu-label>
												<c:if test="${not empty menu.icon}">
													<ec:icon icon="${menu.icon}" size="1"/>
												</c:if>
												${menu.fullName}
												<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
											</ec:menu-label>
											<ec:menu-itens resource="${menu.body}"/>
										</ec:menu>
									</c:when>
			
									<c:when test="${fn:length(menu.itens) != 0}">
										<ec:menu>
											<ec:menu-label>
												<c:if test="${not empty menu.icon}">
													<ec:icon icon="${menu.icon}" size="1"/>
												</c:if>
												${menu.fullName}
												<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
											</ec:menu-label>
											<ec:menu-itens id="menu_itens_xx">
												<c:forEach items="${menu.itens}" var="item">
													<ec:menu-item href="${item.resource}">
														<c:if test="${not empty item.icon}">
															<ec:icon icon="${item.icon}" size="1"/>
														</c:if>
														${item.fullName}
														<ec:badge id="${item.id}_badge" style="${empty item.badgeStyle? 'info' : item.badgeStyle}" type="navbar">${item.badge}</ec:badge>
													</ec:menu-item>
												</c:forEach>
											</ec:menu-itens>
										</ec:menu>
									</c:when>
			
									<c:otherwise>
		
										<ec:menu-item href="${menu.resource}">
											<c:if test="${not empty menu.icon}">
												<ec:icon icon="${menu.icon}" size="1"/>
											</c:if>
											${menu.fullName}
											<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
										</ec:menu-item>
									</c:otherwise>
									
								</c:choose>
							</c:forEach>
							</ec:menu-itens>
						</ec:menu-body>
						</ed:container>
					</ec:menu-bar>
				</c:if>
			</ed:col>
			<ed:col classStyle="widget">
				<c:if test="${!empty footerMenu}">
				
					<ec:menu-bar id="frontFooterMenu" 
							expand="xl" 
							style="footer">
						<ed:container>
						<ec:menu-bar-brand>
							<h5>${footerMenu.name}</h5>
						</ec:menu-bar-brand>
						<ec:menu-body collapse="false">
							<ec:menu-itens>
							<c:forEach items="${footerMenu.itens}" var="menu">
								<c:choose>
			
									<c:when test="${not empty menu.body}">
										<ec:menu>
											<ec:menu-label>
												<c:if test="${not empty menu.icon}">
													<ec:icon icon="${menu.icon}" size="1"/>
												</c:if>
												${menu.fullName}
												<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
											</ec:menu-label>
											<ec:menu-itens resource="${menu.body}"/>
										</ec:menu>
									</c:when>
			
									<c:when test="${fn:length(menu.itens) != 0}">
										<ec:menu>
											<ec:menu-label>
												<c:if test="${not empty menu.icon}">
													<ec:icon icon="${menu.icon}" size="1"/>
												</c:if>
												${menu.fullName}
												<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
											</ec:menu-label>
											<ec:menu-itens id="menu_itens_xx">
												<c:forEach items="${menu.itens}" var="item">
													<ec:menu-item href="${item.resource}">
														<c:if test="${not empty item.icon}">
															<ec:icon icon="${item.icon}" size="1"/>
														</c:if>
														${item.fullName}
														<ec:badge id="${item.id}_badge" style="${empty item.badgeStyle? 'info' : item.badgeStyle}" type="navbar">${item.badge}</ec:badge>
													</ec:menu-item>
												</c:forEach>
											</ec:menu-itens>
										</ec:menu>
									</c:when>
			
									<c:otherwise>
		
										<ec:menu-item href="${menu.resource}">
											<c:if test="${not empty menu.icon}">
												<ec:icon icon="${menu.icon}" size="1"/>
											</c:if>
											${menu.fullName}
											<ec:badge id="${menu.id}_badge" style="${empty menu.badgeStyle? 'info' : menu.badgeStyle}" type="navbar">${menu.badge}</ec:badge>
										</ec:menu-item>
									</c:otherwise>
									
								</c:choose>
							</c:forEach>
							</ec:menu-itens>
						</ec:menu-body>
						</ed:container>
					</ec:menu-bar>
				</c:if>
			</ed:col>
			<ed:col classStyle="widget">
				<c:if test="${!empty address}">
					<h5>${address.title}</h5>
					<address>${address.address}</address>
					<p>
					<c:if test="${!empty address.phone}">
						<ec:icon icon="phone" size="1"/> ${address.phone}<br>
					</c:if>
					<c:if test="${!empty address.email}">
						<ec:icon icon="envelope" size="1"/> ${address.email}<br>
					</c:if>
					</p>
				</c:if>			
			</ed:col>
		</ed:row>
	</ed:container>
	<div class="sub-footer">
		<ed:container>
			<ed:row>
				<ed:col size="12">
					<span>UoUTec - All right reserved.</span>
				</ed:col>
			</ed:row>
		</ed:container>
	</div>
    </footer>
    
  