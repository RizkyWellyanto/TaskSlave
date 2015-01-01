package com.rizkywelly.taskslave;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 *
 * Activities containing this fragment MUST implement the {@link com.rizkywelly.taskslave.TaskListFragment.OnTaskListFragmentInteractionListener}
 * interface.
 */
public class TaskListFragment extends ListFragment {
    static final String TAG = "taskListFragment";
    static final String EMPTY_MESSAGE= "You don\'t have any Task in your list. Click the add task button on the upper right corner to add a new task.";

    OnTaskListFragmentInteractionListener mListener;
    TaskAdapter taskAdapter;

    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskAdapter = new TaskAdapter(getActivity(), ((MainActivity)getActivity()).mTaskList);
        setListAdapter(taskAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(EMPTY_MESSAGE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTaskListFragmentInteractionListener) activity;
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);

        Log.e("DEBUG","onListItemClick is called");

        if (null != mListener) {
            Log.e("DEBUG","about to call mListener.onTaskSelected");
            mListener.onTaskSelected(position);
            Log.e("DEBUG","mListener.onTaskSelected is called");
        }
    }

    public void notifyAdapter(){
        taskAdapter.notifyDataSetChanged();
    }

    public interface OnTaskListFragmentInteractionListener {
        public void onTaskSelected(int position);
    }
}
