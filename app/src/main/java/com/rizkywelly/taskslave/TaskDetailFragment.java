package com.rizkywelly.taskslave;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.rizkywelly.taskslave.Task.TITLE_ERROR_MESSAGE;

public class TaskDetailFragment extends Fragment {
    static final String TAG = "task_detail_fragment";
    static final String TASK_KEY = "task_key";
    static final String POSITION_KEY = "position_key";

    private Task mTask;
    private int mPosition;

    TextView textViewTitle, textViewTaskTitle, textViewDescription, textViewTaskDescription;
    EditText editTextTitle, editTextDescription;

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
        mTask = new Task((Task)getArguments().getParcelable(TASK_KEY));
        mPosition = new Integer(getArguments().getInt(POSITION_KEY));

        return inflater.inflate(R.layout.fragment_task_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = (TextView) view.findViewById(R.id.text_view_title);
        textViewDescription = (TextView) view.findViewById(R.id.text_view_description);
        textViewTaskTitle = (TextView) view.findViewById(R.id.text_view_task_title);
        textViewTaskDescription = (TextView) view.findViewById(R.id.text_view_task_description);

        editTextTitle = (EditText) view.findViewById(R.id.edit_text_title);
        editTextDescription = (EditText) view.findViewById(R.id.edit_text_task_description);

        textViewTaskTitle.setText(mTask.getTitle());
        textViewTaskDescription.setText(mTask.getDescription());

        editTextTitle.setText(mTask.getTitle());
        editTextDescription.setText(mTask.getDescription());

        mListener.onTaskDetailFragment();
    }

    @Override
    public void onPause() {
        super.onPause();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextTitle.getWindowToken(), 0);
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

    public void enableTaskEdit(){
        textViewTaskTitle.setVisibility(View.INVISIBLE);
        textViewTaskDescription.setVisibility(View.INVISIBLE);

        editTextTitle.setVisibility(View.VISIBLE);
        editTextDescription.setVisibility(View.VISIBLE);

        editTextTitle.requestFocus();
        editTextTitle.setSelection(editTextTitle.getText().length());

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextTitle, InputMethodManager.SHOW_IMPLICIT);
    }

    public int getPosition(){
        return mPosition;
    }

    public Task getTask(){
        return new Task(mTask);
    }

    protected boolean setNewTask() {
        if (!mTask.setTitle(editTextTitle.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), TITLE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }

        mTask.setDescription(editTextDescription.getText().toString());

        return true;
    }

    public String getDeleteMessage(){
        return mTask.getTitle() + " is deleted.";
    }
}
