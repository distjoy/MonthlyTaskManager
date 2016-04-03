package com.erubu.monthlytaskmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erubu.monthlytaskmanager.R;
import com.erubu.monthlytaskmanager.model.Task;
import com.erubu.monthlytaskmanager.ui.activity.TasksActivityNew;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends BaseAdapter {

    List<Task> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private String[] monthString = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};


    public TaskAdapter(Context context, List<Task> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Task getItem(int position) {
        return myList.get(position);
    }

    public List<Task> getAdapterData() {
        return this.myList;
    }


    public int getItemRemoteId(int position) {
        return position;
        // return Integer.valueOf(myList.get(position).id);
    }

    @Override
    public long getItemId(int position) {
        return position;
        // return Integer.valueOf(myList.get(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;


        convertView = inflater.inflate(R.layout.task_list_item, null);
        mViewHolder = new MyViewHolder(convertView);
        convertView.setTag(mViewHolder);


        String[] startDateArr = myList.get(position).getStart_date().split("/");

        String[] endDateArr = myList.get(position).getEnd_date().split("/");


        detail(convertView, R.id.task_name, myList.get(position).getTitle());
        detail(convertView, R.id.task_duration,
                monthString[Integer.valueOf(startDateArr[0]) - 1] + " " + startDateArr[1] + " - " +
                        monthString[Integer.valueOf(endDateArr[0]) - 1] + " " + endDateArr[1]);

        mViewHolder.position = position;
        mViewHolder.setClickHander();

        return convertView;
    }


    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    private class MyViewHolder implements View.OnClickListener {
        View parent;
        int position;

        public MyViewHolder(View view) {
            parent = view;
        }

        public void setClickHander() {
            this.parent.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            ((TasksActivityNew) context).setCurrentTask(getItem(position));
            ((TasksActivityNew) context).showTaskView();

        }
    }

}