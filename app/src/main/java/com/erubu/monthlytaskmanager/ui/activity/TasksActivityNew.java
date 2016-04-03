package com.erubu.monthlytaskmanager.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erubu.monthlytaskmanager.R;
import com.erubu.monthlytaskmanager.adapter.CalendarMonthAdapter;
import com.erubu.monthlytaskmanager.adapter.TaskAdapter;
import com.erubu.monthlytaskmanager.model.Task;
import com.erubu.monthlytaskmanager.ui.fragment.TaskListFragment;
import com.erubu.monthlytaskmanager.ui.fragment.TaskViewFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksActivityNew extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    private ProgressBar progress;

    private String classId;

    private CalendarMonthAdapter monthAdapter;
    private ViewPager months;

    private int[] monthBgIds = {R.mipmap.january, R.mipmap.february, R.mipmap.march, R.mipmap.april, R.mipmap.may, R.mipmap.june, R.mipmap.july,
            R.mipmap.august, R.mipmap.september, R.mipmap.october, R.mipmap.november, R.mipmap.december};
    private String[] monthString = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    private Task currentTask;
    private List<Task> currentTaskList;
    private Calendar currentDate;

    private TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_new);

        setSupportActionBar(((Toolbar) findViewById(R.id.tool_bar)));
        getSupportActionBar().setTitle("Tasks");
        months = (ViewPager) findViewById(R.id.months_pager);
        initViewData();

    }

    public String getClassId() {
        return this.classId;
    }

    public Task getCurrentTask() {
        return this.currentTask;
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
    }

    public Calendar getCurrentDate() {
        return this.currentDate;
    }

    public void setCurrentDate(Calendar date) {
        this.currentDate = date;
    }

    public List<Task> getCurrentTaskList() {
        return this.currentTaskList;
    }

    public void setCurrentTaskList(List<Task> tasks) {
        this.currentTaskList = tasks;
    }

    public TaskAdapter getCurrentTaskAdapter() {
        return this.taskAdapter;
    }

    public void showTaskView() {
        FragmentManager fm = getSupportFragmentManager();
        TaskViewFragment taskDialog = TaskViewFragment.newInstance(true);
        taskDialog.show(fm, "fragment_task_view");

    }

    public void addNewTask(View v) {

        FragmentManager fm = getSupportFragmentManager();
        TaskViewFragment taskDialog = TaskViewFragment.newInstance(false);
        taskDialog.show(fm, "fragment_task_view");
    }

    public void showTaskList() {
        taskAdapter = new TaskAdapter(this, this.currentTaskList);
        FragmentManager fm = getSupportFragmentManager();
        TaskListFragment taskListFrag = new TaskListFragment();
        taskListFrag.show(fm, "fragment_task_list");
    }


    public CalendarMonthAdapter getCalendarAdapter() {
        return this.monthAdapter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    private void initViewData() {


        monthAdapter = new CalendarMonthAdapter(getDummyTask(), this);
        //monthAdapter.
        months.setAdapter(monthAdapter);
        months.addOnPageChangeListener(this);
    }

    private List<Task> getDummyTask() {
        List<Task> tasks = new ArrayList<>();

        return tasks;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


        int year = 2016 + (position / 12);
        int month = position % 12;
        ((TextView) findViewById(R.id.month_title)).setText(year + " " + monthString[month]);
        findViewById(R.id.main_month_wr).setBackgroundResource(monthBgIds[month]);


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getTasks() {
        //Load Task from the api through a service call
//       findViewById(R.id.errorWr).setVisibility(View.GONE);
//       findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//        Intent i = new Intent(this,
//          AppMainService.class);
//         startService(i);

    }


}
