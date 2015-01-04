package com.rizkywelly.taskslave;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MuhammadRizky on 12/22/2014.
 *
 * Main object of the Task Slave App
 */
public class Task implements Parcelable {
    protected static final String DEFAULT_TITLE = "No Title";
    protected static final String DEFAULT_DESCRIPTION = "No Description";

    protected static final String TITLE_ERROR_MESSAGE = "You must set the title of the Task";

    protected String title;
    protected String description;
    protected boolean isDone;

    public Task() {
        this.title = DEFAULT_TITLE;
        this.description = DEFAULT_DESCRIPTION;
        this.isDone = false;
    }

    public Task(Task task) {
        this();
        setTitle(task.getTitle());
        setDescription(task.getDescription());
        setStatus(task.getStatus());
    }

    public Task(Parcel parcel) {
        this();
        setTitle(parcel.readString());
        setDescription(parcel.readString());
        setStatus(parcel.readByte() != 0);
    }

    public Task(String title, String description) {
        this();
        setTitle(title);
        setDescription(description);
    }

    public boolean setTitle(String title) {
        if (validate(title)) {
            this.title = title;
            return true;
        }

        return false;
    }

    public boolean setDescription(String description) {
        if (description != null) {
            this.description = description;
            return true;
        }

        return false;
    }

    public void setStatus(boolean input){
        this.isDone = input;
    }

    public boolean getStatus(){
        return this.isDone;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    private boolean validate(String input) {
        return (input.length() >= 1 && input.length() <= 50);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte((byte) (this.isDone ? 1 : 0));
    }

    public static Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
