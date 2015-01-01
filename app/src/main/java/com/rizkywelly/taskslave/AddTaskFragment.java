package com.rizkywelly.taskslave;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.rizkywelly.taskslave.Task.TITLE_ERROR_MESSAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment{
    static final String TAG = "addTaskFragment";

    private Task mTask = new Task();

    TextView textViewTitle, textViewDescription;
    EditText editTextTitle, editTextDescription;

    OnAddTaskFragmentInteractionListener mListener;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddTaskFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = (TextView) view.findViewById(R.id.text_view_title);
        textViewDescription = (TextView) view.findViewById(R.id.text_view_description);

        editTextTitle = (EditText) view.findViewById(R.id.edit_text_title);
        editTextDescription = (EditText) view.findViewById(R.id.edit_text_task_description);

        editTextTitle.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextTitle, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        mListener.onAddTaskFragment();
    }

    protected boolean setNewTask() {
        if (!mTask.setTitle(editTextTitle.getText().toString())) {
            Toast.makeText(getActivity().getApplicationContext(), TITLE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }

        mTask.setDescription(editTextDescription.getText().toString());

        return true;
    }

    public Task getTask(){
        return new Task(mTask);
    }

    public void clear(){
        editTextTitle.setText("");
        editTextDescription.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextTitle.getWindowToken(), 0);
    }

    public interface OnAddTaskFragmentInteractionListener{
        public void onAddTaskFragment();
    }
}
