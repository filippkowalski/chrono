package com.projectfundamentals.src.rest.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


/**
 * Created by Filip Kowalski on 25.02.17.
 */

public class Channel implements Parcelable {

	@Getter
	@SerializedName("id")
	private long id;

	@Getter
	@SerializedName("name")
	private String name;

	@Getter
	@SerializedName("description")
	private String description;

	@Getter
	@SerializedName("owners")
	private List<User> owners = new ArrayList<>();

	@Getter
	@SerializedName("tags")
	private List<String> tags = new ArrayList<>();

	@Getter
	@SerializedName("subs")
	private int subscriptionCount;

	@Getter
	@SerializedName("fee")
	private double fee;

	@Getter
	@SerializedName("cover_photo")
	private String coverPhoto;

	public Channel() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeTypedList(this.owners);
		dest.writeStringList(this.tags);
		dest.writeInt(this.subscriptionCount);
		dest.writeDouble(this.fee);
		dest.writeString(this.coverPhoto);
	}

	protected Channel(Parcel in) {
		this.id = in.readLong();
		this.name = in.readString();
		this.description = in.readString();
		this.owners = in.createTypedArrayList(User.CREATOR);
		this.tags = in.createStringArrayList();
		this.subscriptionCount = in.readInt();
		this.fee = in.readDouble();
		this.coverPhoto = in.readString();
	}

	public static final Creator<Channel> CREATOR = new Creator<Channel>() {
		@Override
		public Channel createFromParcel(Parcel source) {
			return new Channel(source);
		}

		@Override
		public Channel[] newArray(int size) {
			return new Channel[size];
		}
	};
}