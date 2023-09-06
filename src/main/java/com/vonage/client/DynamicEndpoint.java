/*
 *   Copyright 2023 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client;

import com.vonage.client.auth.AuthMethod;
import com.vonage.client.common.HttpMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Enables convenient declaration of endpoints without directly implementing {@link AbstractMethod}.
 * This decouples the endpoint's implementation from the underlying HTTP library.
 *
 * @since 7.7.0
 */
@SuppressWarnings("unchecked")
public class DynamicEndpoint<T, R> extends AbstractMethod<T, R> {
	protected boolean applyBasicAuth;
	protected Collection<Class<? extends AuthMethod>> authMethods;
	protected String contentType, accept;
	protected HttpMethod requestMethod;
	protected BiFunction<DynamicEndpoint<T, R>, ? super T, String> pathGetter;
	protected Class<? extends RuntimeException> responseExceptionType;
	protected Class<R> responseType;
	protected T cachedRequestBody;

	protected DynamicEndpoint(Builder<T, R> builder) {
		super(builder.wrapper);
		applyBasicAuth = builder.applyBasicAuth;
		authMethods = builder.authMethods;
		requestMethod = builder.requestMethod;
		pathGetter = builder.pathGetter;
		responseExceptionType = builder.responseExceptionType;
		responseType = builder.responseType;
		contentType = builder.contentType;
		if ((accept = builder.accept) == null && Jsonable.class.isAssignableFrom(responseType)) {
			accept = ContentType.APPLICATION_JSON.getMimeType();
		}
	}

	public static <T, R> Builder<T, R> builder(Class<R> responseType) {
		return new Builder<>(responseType);
	}

	public static final class Builder<T, R> {
		private final Class<R> responseType;
		private Collection<Class<? extends AuthMethod>> authMethods;
		private HttpWrapper wrapper;
		private boolean applyBasicAuth = false;
		private String contentType, accept;
		private HttpMethod requestMethod;
		private BiFunction<DynamicEndpoint<T, R>, ? super T, String> pathGetter;
		private Class<? extends RuntimeException> responseExceptionType;

		Builder(Class<R> responseType) {
			this.responseType = responseType;
		}

		public Builder<T, R> wrapper(HttpWrapper wrapper) {
			this.wrapper = wrapper;
			return this;
		}

		public Builder<T, R> requestMethod(HttpMethod requestMethod) {
			this.requestMethod = requestMethod;
			return this;
		}

		public Builder<T, R> pathGetter(BiFunction<DynamicEndpoint<T, R>, T, String> pathGetter) {
			this.pathGetter = pathGetter;
			return this;
		}

		public Builder<T, R> authMethod(Class<? extends AuthMethod> primary, Class<? extends AuthMethod>... others) {
			authMethods = new ArrayList<>(2);
			authMethods.add(primary);
			if (others != null && others.length > 0) {
				authMethods.addAll(Arrays.asList(others));
			}
			return this;
		}

		public Builder<T, R> responseExceptionType(Class<? extends RuntimeException> responseExceptionType) {
			this.responseExceptionType = responseExceptionType;
			return this;
		}

		public Builder<T, R> contentTypeHeader(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder<T, R> acceptHeader(String accept) {
			this.accept = accept;
			return this;
		}

		public Builder<T, R> applyAsBasicAuth() {
			applyBasicAuth = true;
			return this;
		}

		public DynamicEndpoint<T, R> build() {
			return new DynamicEndpoint<>(this);
		}
	}

	static RequestBuilder createRequestBuilderFromRequestMethod(HttpMethod requestMethod) {
		switch (requestMethod) {
			case GET: return RequestBuilder.get();
			case POST: return RequestBuilder.post();
			case PATCH: return RequestBuilder.patch();
			case DELETE: return RequestBuilder.delete();
			case PUT: return RequestBuilder.put();
			default: throw new IllegalStateException("Unknown request method.");
		}
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return authMethods.toArray(new Class<?>[0]);
	}

	@Override
	protected RequestBuilder applyAuth(RequestBuilder request) throws VonageClientException {
		return applyBasicAuth ?
				getAuthMethod(getAcceptableAuthMethods()).applyAsBasicAuth(request) :
				super.applyAuth(request);
	}

	@Override
	public RequestBuilder makeRequest(T requestBody) throws UnsupportedEncodingException {
		if (requestBody instanceof Jsonable && requestBody.getClass().equals(responseType)) {
			cachedRequestBody = requestBody;
		}
		RequestBuilder rqb = createRequestBuilderFromRequestMethod(requestMethod);
		if (contentType != null || requestBody instanceof Jsonable) {
			rqb.setHeader("Content-Type", contentType != null ? contentType : "application/json");
		}
		if (accept != null) {
			rqb.setHeader("Accept", accept);
		}
		if (requestBody instanceof QueryParamsRequest) {
			Map<String, ?> params = ((QueryParamsRequest) requestBody).makeParams();
			params.forEach((k, v) -> {
				Consumer<Object> logic = obj -> rqb.addParameter(k, String.valueOf(obj));
				if (v instanceof Object[]) {
					for (Object nested : (Object[]) v) {
						logic.accept(nested);
					}
				}
				else if (v instanceof Iterable<?>) {
					for (Object nested : (Iterable<?>) v) {
						logic.accept(nested);
					}
				}
				else {
					logic.accept(v);
				}
			});
		}
		if (requestBody instanceof Jsonable) {
			rqb.setEntity(new StringEntity(((Jsonable) requestBody).toJson(), ContentType.APPLICATION_JSON));
		}
		else if (requestBody instanceof byte[]) {
			rqb.setEntity(new ByteArrayEntity((byte[]) requestBody));
		}
		return rqb.setUri(URI.create(pathGetter.apply(this, requestBody)));
	}

	protected R parseResponseFromString(String response) {
		return null;
	}

	@Override
	public R parseResponse(HttpResponse response) throws IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		try {
			if (statusCode >= 200 && statusCode < 300) {
				if (responseType.equals(Void.class)) {
					return null;
				}
				else if (byte[].class.equals(responseType)) {
					return (R) EntityUtils.toByteArray(response.getEntity());
				}
				else {
					String deser = basicResponseHandler.handleResponse(response);

					if (responseType.equals(String.class)) {
						return (R) deser;
					}

					if (cachedRequestBody instanceof Jsonable) {
						((Jsonable) cachedRequestBody).updateFromJson(deser);
						return (R) cachedRequestBody;
					}

					for (java.lang.reflect.Method method : responseType.getDeclaredMethods()) {
						boolean matching = Modifier.isStatic(method.getModifiers()) &&
								method.getName().equals("fromJson") &&
								responseType.isAssignableFrom(method.getReturnType());
						if (matching) {
							Class<?>[] params = method.getParameterTypes();
							if (params.length == 1 && params[0].equals(String.class)) {
								if (!method.isAccessible()) {
									method.setAccessible(true);
								}
								return (R) method.invoke(responseType, deser);
							}
						}
					}

					if (Jsonable.class.isAssignableFrom(responseType)) {
						Constructor<R> constructor = responseType.getDeclaredConstructor();
						if (!constructor.isAccessible()) {
							constructor.setAccessible(true);
						}
						R responseBody = constructor.newInstance();
						((Jsonable) responseBody).updateFromJson(deser);
						return responseBody;
					}
					else {
						R customParsedResponse = parseResponseFromString(deser);
						if (customParsedResponse == null) {
							throw new IllegalStateException("Unhandled return type: " + responseType);
						}
						else {
							return customParsedResponse;
						}
					}
				}
			}
			else {
				String exMessage = EntityUtils.toString(response.getEntity());
				if (responseExceptionType != null) {
					if (VonageApiResponseException.class.isAssignableFrom(responseExceptionType)) {
						Constructor<? extends Exception> constructor = responseExceptionType.getDeclaredConstructor();
						if (!constructor.isAccessible()) {
							constructor.setAccessible(true);
						}
						VonageApiResponseException varex = (VonageApiResponseException) constructor.newInstance();
						varex.updateFromJson(exMessage);
						if (varex.title == null) {
							varex.title = response.getStatusLine().getReasonPhrase();
						}
						varex.statusCode = response.getStatusLine().getStatusCode();
						throw varex;
					}
					else {
						for (Constructor<?> constructor : responseExceptionType.getDeclaredConstructors()) {
							Class<?>[] params = constructor.getParameterTypes();
							if (params.length == 1 && String.class.equals(params[0])) {
								if (!constructor.isAccessible()) {
									constructor.setAccessible(true);
								}
								throw (RuntimeException) constructor.newInstance(exMessage);
							}
						}
					}
				}
				R customParsedResponse = parseResponseFromString(exMessage);
				if (customParsedResponse == null) {
					throw new VonageApiResponseException(exMessage);
				}
				else {
					return customParsedResponse;
				}
			}
		}
		catch (InvocationTargetException ex) {
			Throwable wrapped = ex.getTargetException();
			if (wrapped instanceof RuntimeException) {
				throw (RuntimeException) wrapped;
			}
			else {
				throw new VonageUnexpectedException(wrapped);
			}
		}
		catch (InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
			throw new VonageUnexpectedException(ex);
		}
		finally {
			cachedRequestBody = null;
		}
	}
}
