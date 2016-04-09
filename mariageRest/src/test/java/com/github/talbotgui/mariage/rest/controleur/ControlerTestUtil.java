package com.github.talbotgui.mariage.rest.controleur;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class ControlerTestUtil {

	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ControlerTestUtil.class);

	/** Log interceptor for all HTTP requests. */
	public static final List<ClientHttpRequestInterceptor> REST_INTERCEPTORS = Arrays
			.asList(new ClientHttpRequestInterceptor() {
				public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
						final ClientHttpRequestExecution execution) throws IOException {
					LOG.info("Request : {URI={}, method={}, headers={}, body={}}", request.getURI(),
							request.getMethod().name(), request.getHeaders(), new String(body));
					final ClientHttpResponse response = execution.execute(request, body);
					LOG.info("Response : {code={}}", response.getStatusCode());
					return response;
				}
			});

}
