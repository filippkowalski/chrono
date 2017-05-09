package com.chrono.src.list.endless;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chrono.R;
import com.chrono.src.BaseFragment;
import com.chrono.src.common.constants.ConfigConstants;
import com.chrono.src.list.ListViewLogic;
import com.chrono.src.list.decoration.DefaultSpacingItemDecoration;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Filip Kowalski on 05.03.17.
 */

/**
 * Fragment containing endless scrolling functionality.
 * It covers loading, swipe refresh layout, showing loading screens, errors screens.
 *
 * @param <D> Data to be downloaded
 * @param <T> Class type to be listed
 * @param <A> Adapter for data
 */
public abstract class EndlessListFragment<D extends T, T, A extends RecyclerView.Adapter>
		extends BaseFragment<EndlessListPresenter<D>> implements ListViewLogic<D>, SwipeRefreshLayout.OnRefreshListener {

	@Getter
	protected RecyclerView recyclerView;

	@Getter
	protected SwipeRefreshLayout swipeRefreshLayout;

	private boolean loadedAllData;
	private boolean loading;
	private int offset = 0;

	@Getter
	private List<T> data = new ArrayList<>();

	@Getter
	private A adapter;

	protected abstract A createAdapter(List<T> data);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.layout_list, container, false);

		initLayout(view);
		initList();
		initSwipeToRefresh();
		initEndlessLoadingListener();

		return view;
	}

	private void initLayout(View view) {
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
	}

	private void initSwipeToRefresh() {
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	protected void initList() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		int defaultListSpacing = getResources().getDimensionPixelSize(R.dimen.list_spacing);
		recyclerView.addItemDecoration(new DefaultSpacingItemDecoration(defaultListSpacing));
	}

	protected void initEndlessLoadingListener() {
		Mugen.with(recyclerView, new MugenCallbacks() {
			@Override
			public void onLoadMore() {
				loading = true;
				getPresenter().downloadDataFromApi(offset += ConfigConstants.LIST_OFFSET_LIMIT);
			}

			@Override
			public boolean isLoading() {
				return loading;
			}

			@Override
			public boolean hasLoadedAllItems() {
				return loadedAllData;
			}
		}).loadMoreOffset(ConfigConstants.LIST_OFFSET_LIMIT).start();
	}

	@Override
	public void onDataLoaded(List<D> elements) {
		data.addAll(elements);
		adapter = createAdapter(data);
		recyclerView.setAdapter(adapter);

		showRefreshingState(false);
	}

	private void showRefreshingState(boolean loadingState) {
		loading = loadingState;
		swipeRefreshLayout.setRefreshing(loadingState);
	}

	@Override
	public void setHasLoadedAllData(boolean loadedAllData) {
		this.loadedAllData = loadedAllData;
	}

	@Override
	public void showLoadingView(boolean show) {

	}

	@Override
	public void showErrorView() {
		showRefreshingState(false);
	}

	@Override
	public void onRefresh() {
		data.clear();
		getPresenter().downloadDataFromApi(0);
	}
}