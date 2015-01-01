package com.rizkywelly.taskslave;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskDetailFragment extends Fragment {
    static final String TASK_KEY = "task_key";
    static final String POSITION_KEY = "position_key";

    Task mTask;
    int mPosition;

    OnTaskDetailFragmentInteractionListener mListener;

    public static TaskDetailFragment newInstance(Task task, int position){
        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TASK_KEY, task);
        bundle.putInt(POSITION_KEY, position);
        taskDetailFragment.setArguments(bundle);

        return taskDetailFragment;
    }

    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTask = getArguments().getParcelable(TASK_KEY);
        mPosition = getArguments().getInt(POSITION_KEY);

        return inflater.inflate(R.layout.fragment_task_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.onTaskDetailFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTaskDetailFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTaskDetailFragmentInteractionListener {
        public void onTaskDetailFragment();
    }

}
