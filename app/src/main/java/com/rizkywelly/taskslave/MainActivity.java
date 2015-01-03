package com.rizkywelly.taskslave;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends ActionBarActivity implements TaskListFragment.OnTaskListFragmentInteractionListener, AddTaskFragment.OnAddTaskFragmentInteractionListener, TaskDetailFragment.OnTaskDetailFragmentInteractionListener {
    static final String FILENAME = "TaskList.txt";

    ArrayList<Task> mTaskList = new ArrayList<Task>();

    FragmentManager mFragManager = getFragmentManager();

    AddTaskFragment mAddTaskFragment = new AddTaskFragment();
    TaskListFragment mTaskListFragment = new TaskListFragment();
    TaskDetailFragment mTaskDetailFragment = new TaskDetailFragment();

    MenuItem mMenuItemNewTask, mMenuItemEditTask, mMenuItemAccept, mMenuItemDeleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_layout);

        try{
            mTaskList = loadTaskData();
        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
        fragmentTransaction.add(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
        fragmentTransaction.addToBackStack(TaskListFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try{
            saveTaskData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        mMenuItemNewTask = menu.findItem(R.id.action_add_new_task);
        mMenuItemEditTask = menu.findItem(R.id.action_edit_task);
        mMenuItemAccept = menu.findItem(R.id.action_accept);
        mMenuItemDeleteTask = menu.findItem(R.id.action_delete_task);

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
                if (mAddTaskFragment.isResumed()) {
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

                if (mTaskDetailFragment.isResumed()) {
                    if (mTaskDetailFragment.setNewTask()) {
                        mTaskList.set(mTaskDetailFragment.getPosition(), mTaskDetailFragment.getTask());
                        mTaskListFragment.notifyAdapter();

                        mFragManager.popBackStackImmediate();
                        fragmentTransaction = mFragManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
                        fragmentTransaction.commit();

                        switchMenuItem(R.id.action_add_new_task);
                    }
                }

                return true;

            case R.id.action_edit_task:
                mTaskDetailFragment.enableTaskEdit();

                switchMenuItem(R.id.action_accept);

                return true;

            case R.id.action_delete_task:
                mTaskList.remove(mTaskDetailFragment.getPosition());
                Toast.makeText(this, mTaskDetailFragment.getDeleteMessage(), Toast.LENGTH_SHORT).show();
                mTaskListFragment.notifyAdapter();

                mFragManager.popBackStackImmediate();
                fragmentTransaction = mFragManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_activity_layout, mTaskListFragment, TaskListFragment.TAG);
                fragmentTransaction.commit();

                switchMenuItem(R.id.action_add_new_task);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragManager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry backStackEntry = mFragManager.getBackStackEntryAt(mFragManager.getBackStackEntryCount() - 1);

            if (backStackEntry.getName().equals(AddTaskFragment.TAG)) {
                switchMenuItem(R.id.action_add_new_task);
            } else if (backStackEntry.getName().equals(TaskDetailFragment.TAG)) {
                switchMenuItem(R.id.action_add_new_task);
            }

            mFragManager.popBackStackImmediate();
            mFragManager.beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTaskSelected(int position) {
        mTaskDetailFragment = TaskDetailFragment.newInstance(new Task(mTaskList.get(position)), new Integer(position));

        FragmentTransaction fragmentTransaction = mFragManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_activity_layout, mTaskDetailFragment, TaskDetailFragment.TAG);
        fragmentTransaction.addToBackStack(TaskDetailFragment.TAG);
        fragmentTransaction.commit();

        switchMenuItem(R.id.action_edit_task);
        mMenuItemDeleteTask.setVisible(true);
    }

    @Override
    public void onTaskListFragment() {
        // for future development
    }

    @Override
    public void onAddTaskFragment() {
        // for future development
    }

    @Override
    public void onTaskDetailFragment() {
        // for future development
    }

    public void switchMenuItem(int uri) {
        switch (uri) {
            case R.id.action_add_new_task:
                mMenuItemNewTask.setVisible(true);
                mMenuItemEditTask.setVisible(false);
                mMenuItemAccept.setVisible(false);
                mMenuItemDeleteTask.setVisible(false);
                break;

            case R.id.action_edit_task:
                mMenuItemNewTask.setVisible(false);
                mMenuItemEditTask.setVisible(true);
                mMenuItemAccept.setVisible(false);
                mMenuItemDeleteTask.setVisible(false);
                break;

            case R.id.action_accept:
                mMenuItemNewTask.setVisible(false);
                mMenuItemEditTask.setVisible(false);
                mMenuItemAccept.setVisible(true);
                mMenuItemDeleteTask.setVisible(false);
                break;
        }
    }

    public ArrayList<Task> loadTaskData() {
        String data = "";
        int ch;
        StringBuffer fileContent = new StringBuffer("");
        FileInputStream fis;
        try {
            fis = openFileInput(FILENAME);
            try {
                while ((ch = fis.read()) != -1)
                    fileContent.append((char) ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            File file = new File(getFilesDir(), FILENAME);
            Log.e("DEBUG", file.toString());
        }

        data = new String(fileContent);

        return convertJSONToTaskList(data);
    }

    public void saveTaskData() {
        String data = convertTaskListToJSON();

        FileOutputStream fos;

        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            try {
                fos.write(data.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String convertTaskListToJSON() {
        Gson gson = new Gson();

        String taskListJSON = gson.toJson(mTaskList.toArray());

        return taskListJSON;
    }

    public ArrayList<Task> convertJSONToTaskList(String taskListJson){
        Gson gson = new Gson();

        ArrayList<Task> taskListArrayList = gson.fromJson(taskListJson, new TypeToken<List<Task>>(){}.getType());

        return taskListArrayList;
    }
}
