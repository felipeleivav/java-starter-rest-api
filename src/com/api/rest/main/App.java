package com.api.rest.main;

import com.api.rest.arq.exception.ConnectionException;
import com.api.rest.dao.InitDAO;
import com.api.rest.utils.Constants;
import com.api.rest.utils.PropLoader;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.api.rest.filter.Authenticator;
import com.api.rest.filter.CrossOrigin;

public class App {
	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		logger.debug("STARTING SERVER");
		PropLoader.initialize();
		Integer httpPort = Integer.valueOf(PropLoader.get(Constants.SERVER_PORT_HTTP));
		Integer httpsPort = Integer.valueOf(PropLoader.get(Constants.SERVER_PORT_HTTPS));
		String keystoreFile = PropLoader.get(Constants.SERVER_KEYSTORE_FILE);
		String keystorePass = PropLoader.get(Constants.SERVER_KEYSTORE_PASS);

		if (PropLoader.getB(Constants.DB_ENABLE)) {
			InitDAO initer = new InitDAO();
			if (!initer.testConnection()) {
				logger.fatal("Error connecting to database");
				throw new ConnectionException("Error connecting to database");
			}
			if (!initer.isDbInstalled()) {
				logger.fatal("Invalid database schema");
				throw new ConnectionException("Invalid database schema");
			}
			initer.close();
		}

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(httpPort);
		jettyServer.setHandler(context);

		ServerConnector sslConnector = null;
		try {
			HttpConfiguration https = new HttpConfiguration();
			https.addCustomizer(new SecureRequestCustomizer());
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(App.class.getResource("/"+keystoreFile).toExternalForm());
			sslContextFactory.setKeyStorePassword(keystorePass);
			sslContextFactory.setKeyManagerPassword(keystorePass);
			sslConnector = new ServerConnector(jettyServer, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
			sslConnector.setPort(httpsPort);
			// If no error setting https up, then disable plain http
			jettyServer.setConnectors(new Connector[]{sslConnector});
			logger.warn("Running on: "+httpsPort);
		} catch (Exception e) {
			logger.warn("Can't enable SSL ", e);
			logger.warn("Running on: "+httpPort);
		}

		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("org.glassfish.jersey.spi.container.ContainerRequestFilters", Authenticator.class.getCanonicalName());
		jerseyServlet.setInitParameter("org.glassfish.jersey.spi.container.ContainerResponseFilters", CrossOrigin.class.getCanonicalName());
		jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.api.rest.rest;com.api.rest.filter");
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", "org.glassfish.jersey.moxy.json.MoxyJsonFeature");

		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			logger.fatal("Error starting server ", e);
			if (sslConnector!=null) {
				sslConnector.close();
				sslConnector.destroy();
			}
			if (jettyServer!=null) {
				jettyServer.stop();
				jettyServer.destroy();
			}
		}
	}

}
