package com.rizkywelly.taskslave;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements TaskListFragment.OnTaskListFragmentInteractionListener {
    ArrayList<Task> mTaskList = new ArrayList<Task>();
    FragmentManager mFragManager = getFragmentManager();
    AddTaskFragment mAddTaskFragment = new AddTaskFragment();
    TaskListFragment mTaskListFragment = new TaskListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);

        // show the taskListFragment
        FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
        fragmentTransaction.addToBackStack(TaskListFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save the tasks list here
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_task:
                // if taskListFragment is active, button will take the user to addTask fragment
                if (mTaskListFragment.isVisible()){
                    FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_layout, mAddTaskFragment, AddTaskFragment.TAG);
                    fragmentTransaction.addToBackStack(AddTaskFragment.TAG);
                    fragmentTransaction.commit();
                }

                // if addTaskFragment is active, button will add a new task
                if (mAddTaskFragment.isVisible()) {
                    if (mAddTaskFragment.setNewTask()) {

                        mTaskList.add(mAddTaskFragment.getTask());
                        mAddTaskFragment.clear();
                        mTaskListFragment.notifyAdapter();

                        mFragManager.popBackStackImmediate();
                        FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
                        fragmentTransaction.commit();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(mFragManager.getBackStackEntryCount() > 1){
            mFragManager.popBackStackImmediate();
            mFragManager.beginTransaction().commit();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTaskSelected(int position) {
        Log.e("DEBUG","onTaskSelected in MainActivity is called");
        // open a new task view fragment
    }
}
