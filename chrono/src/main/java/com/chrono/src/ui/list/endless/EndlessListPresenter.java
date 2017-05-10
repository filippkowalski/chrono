package com.chrono.src.ui.list.endless;

import com.chrono.src.ui.list.DataDownloadLogic;
import com.chrono.src.Presenter;
import com.chrono.src.ui.states.error.ErrorTypeGenerator;
import com.chrono.src.ui.list.ListViewLogic;
import com.chrono.src.rest.responses.EndlessListResponse;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Filip Kowalski on 05.03.17.
 */

public abstract class EndlessListPresenter<D> implements Presenter, DataDownloadLogic {

	protected Call<EndlessListResponse<D>> call;
	private ListViewLogic<D> contract;

	public EndlessListPresenter(ListViewLogic<D> contract) {
		this.contract = contract;
	}

	@Override
	public void onAttach() {
		downloadDataFromApi(0);
	}

	@Override
	public void downloadDataFromApi(int offset) {
		contract.showLoadingView(true);
		contract.showErrorView(ErrorTypeGenerator.TYPE_UNSET, false);
	}

	@Getter
	private Callback<EndlessListResponse<D>> callback = new Callback<EndlessListResponse<D>>() {

		@Override
		public void onResponse(Call<EndlessListResponse<D>> call, Response<EndlessListResponse<D>> response) {
			if (response.isSuccessful()) {
				EndlessListResponse<D> listResponse = response.body();
				contract.onDataLoaded(listResponse.getData());
				contract.setHasLoadedAllData(listResponse.getNext() == null);
			} else {
				contract.showErrorView(ErrorTypeGenerator.TYPE_NO_CONTENT, true);
			}
			contract.showLoadingView(false);
			contract.showErrorView(ErrorTypeGenerator.TYPE_UNSET, false);
		}

		@Override
		public void onFailure(Call<EndlessListResponse<D>> call, Throwable t) {
			contract.showLoadingView(false);
			contract.showErrorView(ErrorTypeGenerator.TYPE_CONNECTION_PROBLEM, true);
		}
	};

	@Override
	public void onDetach() {
		if (call != null) {
			call.cancel();
		}
	}
}