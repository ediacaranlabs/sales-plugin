package br.com.uoutec.community.ediacaran.sales.pub;

import javax.inject.Inject;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import br.com.uoutec.application.security.RuntimeSecurityPermission;
import br.com.uoutec.community.ediacaran.email.EmailTransport;
import br.com.uoutec.community.ediacaran.email.EmailTransportMock;
import br.com.uoutec.community.ediacaran.junit.sales.ProductTypeHandlerMock;
import br.com.uoutec.community.ediacaran.junit.user.SystemUserJUnit;
import br.com.uoutec.community.ediacaran.security.AuthenticationManagerImp;
import br.com.uoutec.community.ediacaran.system.cdi.ActiveRequestContext;
import br.com.uoutec.community.ediacaran.test.mock.Mock;
import br.com.uoutec.community.ediacaran.test.mock.PluginsLoaderMock;
import br.com.uoutec.community.ediacaran.test.mock.SecurityPolicyManagerMock;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.EdiacaranBootstrap;
import br.com.uoutec.ediacaran.core.ResourceBuilder;
import br.com.uoutec.ediacaran.junit.PluginContext;
import br.com.uoutec.ediacaran.junit.junit5.EdiacaranExt;
import br.com.uoutec.ediacaran.weld.tomcat.TomcatServerBootstrapBuilder;

@ExtendWith(EdiacaranExt.class)
@PluginContext("sales")
public class CartPubResourceTest {

	@Inject @Mock
	private EmailTransport emailTransport = new EmailTransportMock();

	@Inject @Mock
	private ProductTypeHandlerMock productTypeHandlerMock;

	public EdiacaranBootstrap getEdiacaranBootstrap() {
		return TomcatServerBootstrapBuilder.builder()
				.withSecurityPolicyManager(SecurityPolicyManagerMock.builder()
						.withLoadAllPermissions(true)
						.withPermission(
								"user",
								new RuntimeSecurityPermission("app.registry.*")
						)
				.build())
				.withPluginLoader(PluginsLoaderMock.builder()
						.withLoadAllPlugins(true)
						.withProperty("security", AuthenticationManagerImp.LOGIN_MODULE_PROPERTY, "systemuser")
						.withProperty("user", SystemUserRegistry.UNSUPERVISED_OPTION, "true")
						.withProperty("user", SystemUserRegistry.FROM_REPLY_EMAIL, "no-reply@uoutec.com.br")
						.withProperty("user", SystemUserRegistry.VALIDATE_MAIL_TEMPLATE, "")
						.withProperty("persistence", "data_source", "java:comp/env/ds/direct_database")
						.withProperty("persistence", "properties", 
							new StringBuilder()
								.append("javax.persistence.jdbc.driver=org.hsqldb.jdbcDriver").append("\n")
								.append("hibernate.dialect=org.hibernate.dialect.HSQLDialect").append("\n")
								.append("hibernate.allow_update_outside_transaction=true").append("\n")
								.append("hibernate.hbm2ddl.auto=update").append("\n")
							.toString()
						)
				.build())
				.withSystemProperty("java.naming.factory.url.pkgs", "org.apache.naming")
				.withSystemProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory")
				.withResource(ResourceBuilder.builder()
						.withName("java:comp/env/ds/database")
						.withType("javax.sql.DataSource")
						.withProperty("driverClassName", "com.arjuna.ats.jdbc.TransactionalDriver")
						.withProperty("url", "jdbc:arjuna:java:comp/env/ds/direct_database")
				.build())
				.withResource(ResourceBuilder.builder()
						.withName("java:comp/env/TransactionSynchronizationRegistry")
						.withFactory("com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple")
				.build())
				.withResource(ResourceBuilder.builder()
						.withName("java:comp/env/TransactionManager")
						.withFactory("com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple")
				.build())
				.withResource(ResourceBuilder.builder()
						.withName("java:comp/UserTransaction")
						.withFactory("com.arjuna.ats.internal.jta.transaction.arjunacore.UserTransactionImple")
				.build())
				.withResource(ResourceBuilder.builder()
						.withName("java:comp/env/ds/direct_database")
						.withType(JDBCDataSource.class.getName())
						.withProperty("url", "jdbc:hsqldb:mem:testdb")
						.withProperty("user", "sa")
						.withProperty("password", "")
				.build())
		.build();
	}
	
	@BeforeEach
	@ActiveRequestContext
	public void before() throws Throwable {
		SystemUserJUnit.clearData();		
		((EmailTransportMock)emailTransport).clear();
		SystemUserJUnit.registerBasicData();
	}

}
