package com.chrono.src.ui.list.endless;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chrono.R;
import com.chrono.src.common.constants.ConfigConstants;
import com.chrono.src.common.utils.VisibilityUtils;
import com.chrono.src.ui.BaseFragment;
import com.chrono.src.ui.list.ListViewLogic;
import com.chrono.src.ui.list.adapters.decoration.DefaultSpacingItemDecoration;
import com.chrono.src.ui.states.error.ErrorTypeGenerator;
import com.chrono.src.ui.states.error.ErrorView;
import com.chrono.src.ui.states.error.GenericErrorView;
import com.chrono.src.ui.states.loading.LoadingView;
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

	protected View loadingView;
	protected ErrorView errorView;
	protected FrameLayout containerLayout;

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
		initStateView();
		initList();
		initSwipeToRefresh();
		initEndlessLoadingListener();

		return view;
	}

	protected void initLayout(View view) {
		containerLayout = (FrameLayout) view.findViewById(R.id.container_layout);
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
	}

	protected void initStateView() {
		containerLayout.removeView(loadingView);
		containerLayout.removeView(errorView);

		loadingView = createLoadingView();
		errorView = createErrorView();

		containerLayout.addView(loadingView);
		containerLayout.addView(errorView);

		VisibilityUtils.hide(loadingView);
		VisibilityUtils.hide(errorView);
	}

	protected void initSwipeToRefresh() {
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	protected void initList() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(getItemDecorator());
	}

	protected RecyclerView.ItemDecoration getItemDecorator() {
		int defaultListSpacing = getResources().getDimensionPixelSize(R.dimen.list_spacing);
		return new DefaultSpacingItemDecoration(defaultListSpacing);
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

		swipeRefreshLayout.setRefreshing(false);
		setRefreshingState(false);

		VisibilityUtils.show(swipeRefreshLayout);
	}

	@Override
	public void setHasLoadedAllData(boolean loadedAllData) {
		this.loadedAllData = loadedAllData;
	}

	protected View createLoadingView() {
		return new LoadingView(getContext());
	}

	protected ErrorView createErrorView() {
		return new GenericErrorView(getContext(), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRefresh();
			}
		});
	}

	@Override
	public void showLoadingView(boolean show) {
		VisibilityUtils.hide(swipeRefreshLayout);
		VisibilityUtils.show(show, loadingView);
		setRefreshingState(show);
	}

	@Override
	public void showErrorView(@ErrorTypeGenerator.ErrorType int errorType, boolean show) {
		VisibilityUtils.hide(swipeRefreshLayout);
		VisibilityUtils.show(show, errorView);
		if (show) {
			errorView.setError(errorType);
		}
		setRefreshingState(false);
	}

	@Override
	public void onRefresh() {
		data.clear();

		getPresenter().downloadDataFromApi(0);
	}

	private void setRefreshingState(boolean loadingState) {
		loading = loadingState;
	}
}