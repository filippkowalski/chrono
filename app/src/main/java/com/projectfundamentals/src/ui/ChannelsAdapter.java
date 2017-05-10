package com.projectfundamentals.src.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chrono.src.ui.list.OnItemClickListener;
import com.projectfundamentals.R;
import com.projectfundamentals.src.rest.models.Channel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Filip Kowalski on 25.02.17.
 */

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

	private Context context;
	private List<Channel> channels;
	private OnItemClickListener<Channel> listener;

	public ChannelsAdapter(Context context, List<Channel> channels, OnItemClickListener<Channel> listener) {
		this.context = context;
		this.channels = channels;
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Channel channel = channels.get(position);

		holder.layoutContainer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onItemClick(channel);
			}
		});
		holder.title.setText(channel.getName());
		holder.description.setText(channel.getDescription());
		holder.postCount.setText(String.valueOf(channel.getSubscriptionCount()));
		Glide.with(context).load(channel.getCoverPhoto()).into(holder.cover);
	}

	@Override
	public int getItemCount() {
		return channels.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.container_item_channel)
		View layoutContainer;

		@BindView(R.id.text_title_item_channel)
		TextView title;

		@BindView(R.id.text_description_item_channel)
		TextView description;

		@BindView(R.id.image_cover_item_channel)
		ImageView cover;

		@BindView(R.id.text_post_count_item_channel)
		TextView postCount;

		private ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}