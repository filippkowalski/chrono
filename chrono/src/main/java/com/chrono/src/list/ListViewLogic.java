package com.chrono.src.list;

import java.util.List;

public interface ListViewLogic<T> {

	void onDataLoaded(List<T> data);

	void setHasLoadedAllData(boolean loadedAllData);

	void showLoadingView(boolean show);

	void showErrorView(boolean show);
}