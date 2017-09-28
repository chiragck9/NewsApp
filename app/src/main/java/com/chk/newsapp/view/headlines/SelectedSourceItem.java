package com.chk.newsapp.view.headlines;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chira on 04-09-2017.
 */

public class SelectedSourceItem implements Parcelable {

    private String id;

    private String name;

    private boolean isSelected;

    public SelectedSourceItem() {
    }

    public SelectedSourceItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        isSelected = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SelectedSourceItem createFromParcel(Parcel in) {
            return new SelectedSourceItem(in);
        }

        public SelectedSourceItem[] newArray(int size) {
            return new SelectedSourceItem[size];
        }
    };


}
