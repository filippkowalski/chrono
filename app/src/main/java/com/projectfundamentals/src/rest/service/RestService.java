package com.projectfundamentals.src.rest.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.projectfundamentals.src.rest.ServerConstant.SERVER_URL;

/**
 * Created by Filip Kowalski on 25.02.17.
 */

public class RestService {

	private static RestService instance = new RestService();
	private ApiService apiService;

	public static RestService getInstance() {
		return instance;
	}

	private RestService() {
		apiService = createService(ApiService.class);
	}

	private <S> S createService(Class<S> serviceClass) {
		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl(SERVER_URL)
				.client(createClient())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		return restAdapter.create(serviceClass);
	}

	public ApiService getService() {
		return apiService;
	}

	private OkHttpClient createClient() {
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
		setTokenInterceptor(clientBuilder);
		setLoggingInterceptor(clientBuilder);

		return clientBuilder.build();
	}

	private static void setTokenInterceptor(OkHttpClient.Builder client) {
		client.interceptors().add(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				Request newRequest = request.newBuilder()
						.addHeader("Authorization", "Token d4d89d058cd8d0f6f53f4168786576a006450790")
						.build();

				return chain.proceed(newRequest);
			}
		});
	}

	private void setLoggingInterceptor(OkHttpClient.Builder clientBuilder) {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		clientBuilder.addInterceptor(loggingInterceptor);
	}
}