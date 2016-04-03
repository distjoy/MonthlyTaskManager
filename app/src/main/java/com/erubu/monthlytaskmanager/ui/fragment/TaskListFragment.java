package com.erubu.monthlytaskmanager.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erubu.monthlytaskmanager.R;
import com.erubu.monthlytaskmanager.ui.activity.TasksActivityNew;

import java.util.Calendar;
import java.util.Locale;

public class TaskListFragment extends DialogFragment implements View.OnClickListener {

    ImageButton backButton, saveButton, deleteButton, settingsButton, shareButton;
    EditText taskTitle, taskContent;
    TasksActivityNew mParentActivity;
    private ProgressBar progress;
    private ProgressDialog progressDialog;

    private ListView taskListView;


    public TaskListFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        // mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        // getDialog().setTitle("Hello");
        progressDialog = new ProgressDialog(getActivity());
        mParentActivity = (TasksActivityNew) getActivity();

        backButton = (ImageButton) view.findViewById(R.id.nav_back);
        taskListView = (ListView) view.findViewById(R.id.day_tasks_list);
        taskListView.setAdapter(mParentActivity.getCurrentTaskAdapter());

        ((TextView) view.findViewById(R.id.date_day))
                .setText(mParentActivity.getCurrentDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH));
        ((TextView) view.findViewById(R.id.date_date))
                .setText(String.valueOf(mParentActivity.getCurrentDate().get(Calendar.DATE)));


        setViewListeners();
        return view;
    }

    public void setViewListeners() {


        backButton.setOnClickListener(this);


    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_back:
                this.dismiss();
                // this.onDestroy();
                break;

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


}