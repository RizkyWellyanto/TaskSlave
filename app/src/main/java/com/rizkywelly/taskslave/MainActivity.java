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

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements TaskListFragment.OnTaskListFragmentInteractionListener, AddTaskFragment.OnAddTaskFragmentInteractionListener, TaskDetailFragment.OnTaskDetailFragmentInteractionListener {
    ArrayList<Task> mTaskList = new ArrayList<Task>();
    FragmentManager mFragManager = getFragmentManager();
    AddTaskFragment mAddTaskFragment = new AddTaskFragment();
    TaskListFragment mTaskListFragment = new TaskListFragment();
    TaskDetailFragment mTaskDetailFragment = new TaskDetailFragment()   ;

    MenuItem mMenuItemNewTask, mMenuItemEditTask, mMenuItemAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        mMenuItemNewTask = menu.findItem(R.id.action_add_new_task);
        mMenuItemEditTask = menu.findItem(R.id.action_edit_task);
        mMenuItemAccept = menu.findItem(R.id.action_accept);

        switchMenuItem(R.id.action_add_new_task);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new_task:
                FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_layout, mAddTaskFragment, AddTaskFragment.TAG);
                fragmentTransaction.addToBackStack(AddTaskFragment.TAG);
                fragmentTransaction.commit();

                switchMenuItem(R.id.action_accept);

                return true;

            case R.id.action_accept:

                if (mAddTaskFragment.isResumed()){
                    if (mAddTaskFragment.setNewTask()) {

                        mTaskList.add(mAddTaskFragment.getTask());
                        mAddTaskFragment.clear();
                        mTaskListFragment.notifyAdapter();

                        mFragManager.popBackStackImmediate();
                        fragmentTransaction = mFragManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
                        fragmentTransaction.commit();

                        switchMenuItem(R.id.action_add_new_task);
                    }
                }


                if(mTaskDetailFragment.isResumed()){
                    mTaskList.set(mTaskDetailFragment.getPosition(),mAddTaskFragment.getTask());
                    mTaskListFragment.notifyAdapter();

                    mFragManager.popBackStackImmediate();
                    fragmentTransaction = mFragManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
                    fragmentTransaction.commit();

                    switchMenuItem(R.id.action_add_new_task);
                }

                return true;

            case R.id.action_edit_task:
                mTaskDetailFragment.enableTaskEdit();

                switchMenuItem(R.id.action_accept);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragManager.getBackStackEntryCount() > 1) {
            mFragManager.popBackStackImmediate();
            mFragManager.beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTaskSelected(int position) {
        mTaskDetailFragment = TaskDetailFragment.newInstance(new Task(mTaskList.get(position)),new Integer(position));

        FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_layout, mTaskDetailFragment, TaskDetailFragment.TAG);
        fragmentTransaction.addToBackStack(TaskDetailFragment.TAG);
        fragmentTransaction.commit();

        switchMenuItem(R.id.action_edit_task);
    }

    @Override
    public void onTaskListFragment() {

    }

    @Override
    public void onAddTaskFragment() {

    }

    @Override
    public void onTaskDetailFragment() {

    }

    public void switchMenuItem(int uri) {
        switch (uri) {
            case R.id.action_add_new_task:
                mMenuItemNewTask.setVisible(true);
                mMenuItemEditTask.setVisible(false);
                mMenuItemAccept.setVisible(false);
                break;

            case R.id.action_edit_task:
                mMenuItemNewTask.setVisible(false);
                mMenuItemEditTask.setVisible(true);
                mMenuItemAccept.setVisible(false);
                break;

            case R.id.action_accept:
                mMenuItemNewTask.setVisible(false);
                mMenuItemEditTask.setVisible(false);
                mMenuItemAccept.setVisible(true);
                break;
        }
    }
}
