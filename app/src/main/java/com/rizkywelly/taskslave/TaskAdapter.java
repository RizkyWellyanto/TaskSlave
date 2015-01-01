package com.rizkywelly.taskslave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MuhammadRizky on 12/27/2014.
 */
public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Task> mTaskList;

    public TaskAdapter(Context context, ArrayList<Task> taskList){
        mInflater = LayoutInflater.from(context);
        mTaskList = taskList;
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TaskViewHolder viewHolder;

        if(convertView == null){
            view = mInflater.inflate(R.layout.fragment_task_list_layout,parent,false);
            viewHolder = new TaskViewHolder();
            viewHolder.mTextViewTitle = (TextView) view.findViewById(R.id.row_task_title);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (TaskViewHolder) view.getTag();
        }

        Task task = mTaskList.get(position);
        viewHolder.mTextViewTitle.setText(task.getTitle());

        return view;
    }

    private class TaskViewHolder{
        public TextView mTextViewTitle;
    }
}
