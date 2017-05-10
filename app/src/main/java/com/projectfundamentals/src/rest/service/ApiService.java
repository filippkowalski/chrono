package com.projectfundamentals.src.rest.service;


import com.chrono.src.rest.responses.EndlessListResponse;
import com.projectfundamentals.src.rest.models.Channel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Filip Kowalski on 01.03.17.
 */

public interface ApiService {

	@GET("channels/")
	Call<EndlessListResponse<Channel>> getChannels(@Query("offset") int offset, @Query("limit") int limit);

}
