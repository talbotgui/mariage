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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

	public static MultiValueMap<String, Object> creeMapParamRest(final Object... params) {
		final MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<String, Object>();
		if (params != null) {
			if (params.length % 2 != 0) {
				throw new IllegalArgumentException("Le nombre de parametres doit être pair");
			}
			for (int i = 1; i < params.length; i += 2) {
				if (!String.class.isInstance(params[i - 1])) {
					throw new IllegalArgumentException("Les parametres impair doivent être des String");
				}
				requestParam.add((String) params[i - 1], params[i]);
			}
		}
		return requestParam;
	}
}
