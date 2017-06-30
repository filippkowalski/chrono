package com.projectfundamentals.src.ui;

import com.chrono.src.ui.list.OnItemClickListener;
import com.chrono.src.ui.list.endless.EndlessListFragment;
import com.projectfundamentals.src.rest.models.Channel;

import java.util.List;


/**
 * Created by Filip Kowalski on 25.02.17.
 */

public class ChannelsListFragment extends EndlessListFragment<Channel, Channel, ChannelsAdapter> implements
		OnItemClickListener<Channel> {

	public static ChannelsListFragment newInstance() {
		return new ChannelsListFragment();
	}

	@Override
	public ChannelsPresenter createPresenter() {
		return new ChannelsPresenter(this);
	}

	@Override
	public ChannelsAdapter createAdapter(List<Channel> data) {
		return new ChannelsAdapter(getContext(), data, this);
	}

	@Override
	protected void addItemsToAdapter(List<Channel> data, boolean firstRun) {

	}

	@Override
	public void onItemClick(Channel channel) {
	}
}