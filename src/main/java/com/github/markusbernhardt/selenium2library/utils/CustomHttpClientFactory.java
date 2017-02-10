package com.github.markusbernhardt.selenium2library.utils;

import org.apache.http.auth.Credentials;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.internal.ApacheHttpClient;
import org.openqa.selenium.remote.internal.HttpClientFactory;

import java.net.URL;

/**
 * Provides customized {@link HttpClient.Factory} instances.
 * {@link HttpClient} creation is via delegation to {@link ApacheHttpClient.Factory} as default behavior does.
 */
public class CustomHttpClientFactory implements HttpClient.Factory {
	private final HttpClient.Factory delegate;

	private CustomHttpClientFactory(HttpClientFactory factory) {
		delegate = new ApacheHttpClient.Factory(factory);
	}

	@Override
	public HttpClient createClient(URL url) {
		return delegate.createClient(url);
	}

	/**
	 * Creates a HttpClient.Factory with customized connection and socked timeouts.
	 * Default timeout values are 2 minutes for connection, and 3 hours for socket as hard-coded in default
	 * {@link HttpClientFactory} class.
	 *
	 * @param connectionTimeout the connection timeout in milliseconds
	 * @param socketTimeout the socket timeout in milliseconds
	 * @return a HttpClient.Factory instance that will create
	 */
	public static HttpClient.Factory createWithSpecificTimeout(int connectionTimeout, int socketTimeout) {
		HttpClientFactory factory = new CustomTimeoutHttpClientFactory(connectionTimeout, socketTimeout);
		return new CustomHttpClientFactory(factory);
	}

	/**
	 * {@link HttpClientFactory} is using hard-coded timeouts for connection timeout (2m) and socket timeout (3h).
	 * This behavior is undesired in some cases, when socket timeout keeps the webdriver blocked for 3 hours on too
	 * early connection attempt to opened port.
	 */
	private static class CustomTimeoutHttpClientFactory extends HttpClientFactory {
		private final int connectionTimeout;
		private final int socketTimeout;

		private CustomTimeoutHttpClientFactory(int connectionTimeout, int socketTimeout) {
			super(connectionTimeout, socketTimeout);
			this.connectionTimeout = connectionTimeout;
			this.socketTimeout = socketTimeout;
		}

		@Override
		public CloseableHttpClient createHttpClient(Credentials credentials) {
			return super.createHttpClient(credentials, connectionTimeout, socketTimeout);
		}
	}
}
