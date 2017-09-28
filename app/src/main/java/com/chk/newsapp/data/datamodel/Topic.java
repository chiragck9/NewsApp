package com.chk.newsapp.data.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chira on 18-08-2017.
 */

public class Topic implements Parcelable {

    private Category category;
    private String title;
    private String url;

    public Topic() {
    }

    Topic(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        this.category = Category.fromString(data[0]);
        this.title = data[1];
        this.url = data[2];
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        if (category != topic.category) return false;
        if (title != null ? !title.equals(topic.title) : topic.title != null) return false;
        return url != null ? url.equals(topic.url) : topic.url == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.category.getCategory(),
                this.title,
                this.url});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
}
