package com.rizkywelly.taskslave;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by MuhammadRizky on 12/27/2014.
 */
public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Task> mTaskList;

    private OnAdapterInteractionListener mListener;

    public TaskAdapter(Context context, ArrayList<Task> taskList){
        mInflater = LayoutInflater.from(context);
        mTaskList = taskList;
        mListener = (OnAdapterInteractionListener) context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        TaskViewHolder viewHolder;

        if(convertView == null){
            view = mInflater.inflate(R.layout.fragment_task_list_layout,parent,false);
            viewHolder = new TaskViewHolder();
            viewHolder.mTextViewTitle = (TextView) view.findViewById(R.id.row_task_title);
            viewHolder.mCheckBox = (CheckBox) view.findViewById(R.id.row_task_checkbox);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (TaskViewHolder) view.getTag();
        }

        Task task = mTaskList.get(position);
        viewHolder.mTextViewTitle.setText(task.getTitle());
        viewHolder.mCheckBox.setChecked(task.getStatus());

        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCheckBoxClicked(position, ((CheckBox)v).isChecked());
            }
        });

        return view;
    }

    private class TaskViewHolder{
        public TextView mTextViewTitle;
        public CheckBox mCheckBox;
    }

    public interface OnAdapterInteractionListener {
        public void onCheckBoxClicked(int position, boolean status);
    }
}
