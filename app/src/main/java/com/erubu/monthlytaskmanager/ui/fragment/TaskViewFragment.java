package com.erubu.monthlytaskmanager.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.erubu.monthlytaskmanager.R;
import com.erubu.monthlytaskmanager.model.Task;
import com.erubu.monthlytaskmanager.ui.activity.TasksActivityNew;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class TaskViewFragment extends DialogFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


    ImageButton backButton, saveButton, deleteButton, settingsButton, shareButton;
    EditText taskTitle, taskContent;
    TasksActivityNew mParentActivity;
    boolean isEndDateSet;
    boolean isStartDateSet;
    boolean onEdit = false;
    private ProgressBar progress;
    private ProgressDialog progressDialog;
    private ImageButton startDateButton;
    private ImageButton endDateButton;
    private EditText startDate;
    private EditText endDate;
    private int currentDatePickerType = 0;

    public static TaskViewFragment newInstance(boolean onEdit) {
        TaskViewFragment tF = new TaskViewFragment();
        Bundle b = new Bundle();
        b.putBoolean("on_edit", onEdit);
        tF.setArguments(b);
        return tF;
    }

    private static void showDate(int year, int month, int day, EditText dateView) {
        dateView.setText(new StringBuilder().append(month + 1).append("/")
                .append(day).append("/").append(year));
    }

    private static Calendar getDateFromString(String dateString) {
        String[] dateStringArr = dateString.split("/");

        Calendar startDate = new GregorianCalendar(Integer.valueOf(dateStringArr[2]), Integer.valueOf(dateStringArr[0]) - 1,
                Integer.valueOf(dateStringArr[1]));
        return startDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_view, container, false);
        // mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // getDialog().setTitle("Hello");
        progressDialog = new ProgressDialog(getActivity());
        mParentActivity = (TasksActivityNew) getActivity();

        backButton = (ImageButton) view.findViewById(R.id.nav_back);
        saveButton = (ImageButton) view.findViewById(R.id.saveOrEdit);
        settingsButton = (ImageButton) view.findViewById(R.id.more_task_actions);
        taskTitle = (EditText) view.findViewById(R.id.task_title);
        taskContent = (EditText) view.findViewById(R.id.task_description);

        //startDateButton = (ImageButton)view.findViewById(R.id.start_date_button);
        //endDateButton = (ImageButton)view.findViewById(R.id.end_date_button);
        startDate = (EditText) view.findViewById(R.id.start_date);
        endDate = (EditText) view.findViewById(R.id.end_date);
        //progress = (ProgressBar)view.findViewById(R.id.progressBarSmall);

        Bundle args = getArguments();
        onEdit = args.getBoolean("on_edit");

        setViewListeners();
        if (onEdit) {
            taskTitle.setText(mParentActivity.getCurrentTask().getTitle());
            taskContent.setText(mParentActivity.getCurrentTask().getDescription());
            startDate.setText(mParentActivity.getCurrentTask().getStart_date());
            endDate.setText(mParentActivity.getCurrentTask().getEnd_date());
            isEndDateSet = true;
            isStartDateSet = true;
        }
        return view;
    }

    public void setViewListeners() {

        saveButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_back:
                this.dismiss();
                break;
            case R.id.saveOrEdit:
                //progress.setVisibility(View.VISIBLE);
                updateTaskState();
                break;
            case R.id.more_task_actions:
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.inflate(R.menu.note_popup);
                popupMenu.show();

                break;
            case R.id.start_date:
                currentDatePickerType = 1;
                DialogFragment newFragmentStartDate = DatePickerFragment.newInstance(Calendar.getInstance(), getTag());
                newFragmentStartDate.show(mParentActivity.getSupportFragmentManager(), "datePickerStartDate");

                break;

            case R.id.end_date:

                currentDatePickerType = 2;

                if (isStartDateSet) {
                    String defDateString = startDate.getText().toString();
                    DialogFragment newFragment = DatePickerFragment.newInstance(getDateFromString(defDateString), getTag());
                    newFragment.show(mParentActivity.getSupportFragmentManager(), "datePickerEndDate");
                } else {
                    // do nothing
                }


                break;


        }

    }

    public void setDate(int year, int month, int day) {
        switch (currentDatePickerType) {
            case 1:
                showDate(year, month, day, startDate);
                isStartDateSet = true;
                if (isEndDateSet) {
                    Calendar start = getDateFromString(startDate.getText().toString());
                    Calendar end = getDateFromString(endDate.getText().toString());

                    if (start.after(end)) {
                        endDate.setText("");
                        isEndDateSet = false;
                    }

                }

                break;
            case 2:

                showDate(year, month, day, endDate);
                isEndDateSet = true;
                break;
            case 0:
                // do nothing
                break;
        }
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:

                mParentActivity.getCalendarAdapter()
                        .getAdapterData().remove(mParentActivity.getCurrentTask());
                mParentActivity.getCalendarAdapter().notifyDataSetChanged();
                mParentActivity.getCurrentTaskAdapter().getAdapterData().remove(mParentActivity.getCurrentTask());
                mParentActivity.getCurrentTaskAdapter().notifyDataSetChanged();
                progressDialog.dismiss();
                this.dismiss();

                //connect to server
//            Intent i = new Intent(mParentActivity, AppMainService.class);
//            i.putExtra(AppMainService.SERVICE_ACTION,AppMainService.SERVICE_ACTION_DELETE_TASK);
//            i.putExtra(AppMainService.TASK_ID,mParentActivity.getCurrentTask().id);
//            getActivity().startService(i);
//            progressDialog.setMessage("Deleting Task....");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.show();

                return true;
        }

        return true;
    }

    public void updateTaskState() {

        // Intent i = new Intent(mParentActivity, AppMainService.class);


        String title, description, end, start;
        title = taskTitle.getText().toString();
        description = taskContent.getText().toString();
        end = endDate.getText().toString();
        start = startDate.getText().toString();
        if (!verifyString(title)) {
            Toast.makeText(getActivity(), "Please enter a title for the task", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyString(description)) {
            Toast.makeText(getActivity(), "Please describe your task ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyString(start)) {
            Toast.makeText(getActivity(), "Please enter a start date for your task", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyString(end)) {
            Toast.makeText(getActivity(), "Please enter a end date for your task", Toast.LENGTH_SHORT).show();
            return;
        }


        if (onEdit) {

            mParentActivity.getCurrentTask().setTitle(title);
            mParentActivity.getCurrentTask().setDescription(description);
            mParentActivity.getCurrentTask().setEnd_date(end);
            mParentActivity.getCurrentTask().setStart_date(start);
            mParentActivity.getCalendarAdapter().notifyDataSetChanged();
            mParentActivity.getCurrentTaskAdapter().notifyDataSetChanged();
            this.dismiss();

            //Connect to service for data update
//           i.putExtra(AppMainService.SERVICE_ACTION,AppMainService.SERVICE_ACTION_UPDATE_TASK);
//           i.putExtra(AppMainService.TASK_ID,mParentActivity.getCurrentTask().id);
//           i.putExtra(AppMainService.TASK_TITLE,taskTitle.getText().toString());
//           i.putExtra(AppMainService.TASK_DESCRIPTION, taskContent.getText().toString());
//           i.putExtra(AppMainService.TASK_START_DATE, startDate.getText().toString());
//           i.putExtra(AppMainService.TASK_END_DATE, endDate.getText().toString());
//           getActivity().startService(i);
        } else {
            Task newTask = new Task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setEnd_date(end);
            newTask.setStart_date(start);
            mParentActivity.getCalendarAdapter().getAdapterData().add(newTask);
            mParentActivity.getCalendarAdapter().notifyDataSetChanged();
            this.dismiss();


            //connect to service for data upload
//           i.putExtra(AppMainService.SERVICE_ACTION,AppMainService.SERVICE_ACTION_ADD_TASK);
//           i.putExtra(AppMainService.SERVICE_ACTION_CUSTOMIZATION_ID,String.valueOf(mParentActivity.getClassId()));
//           i.putExtra(AppMainService.TASK_TITLE,taskTitle.getText().toString());
//           i.putExtra(AppMainService.TASK_DESCRIPTION, taskContent.getText().toString());
//           i.putExtra(AppMainService.TASK_START_DATE, startDate.getText().toString());
//           i.putExtra(AppMainService.TASK_END_DATE, endDate.getText().toString());
//           getActivity().startService(i);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CUSTOM_DIALOG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean verifyString(String str) {
        if (str == null) {
            return false;
        } else if (str.isEmpty()) {
            return false;
        } else if (str.length() < 2) {
            return false;
        }

        return true;
    }

}