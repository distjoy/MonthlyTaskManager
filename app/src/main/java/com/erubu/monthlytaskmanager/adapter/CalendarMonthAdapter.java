package com.erubu.monthlytaskmanager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erubu.monthlytaskmanager.R;
import com.erubu.monthlytaskmanager.model.Task;
import com.erubu.monthlytaskmanager.ui.activity.TasksActivityNew;
import com.erubu.monthlytaskmanager.ui.customviews.TaskCanvasLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by distjoy on 10/11/2014.
 */
public class CalendarMonthAdapter extends PagerAdapter implements View.OnClickListener {


    private List<Task> tasks;
    private Context context;

    private TasksActivityNew mParent;

    private int startYear = 2016;
    private int endYear = 2100;

    private int[] weekIds = {R.id.calendar_week_one, R.id.calendar_week_two, R.id.calendar_week_three, R.id.calendar_week_four, R.id.calendar_week_five};
    private int[] weekFrameIds = {R.id.calendar_week_one_canvas, R.id.calendar_week_two_canvas, R.id.calendar_week_three_canvas, R.id.calendar_week_four_canvas, R.id.calendar_week_five_canvas};
    private int[] dayIds = {R.id.calendar_day_one, R.id.calendar_day_two, R.id.calendar_day_three, R.id.calendar_day_four, R.id.calendar_day_five, R.id.calendar_day_six, R.id.calendar_day_seven};


    public CalendarMonthAdapter(List<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
        mParent = (TasksActivityNew) context;


    }


    private void initView(int position, View layout) {
        int currentMonth, previousMonth, nextMonth, year, weekEnd = 0;
        Calendar currentMonthDate, nextMonthDate, previousMonthDate;
        year = position / 12;
        year = year + startYear;

        currentMonth = position % 12;
        previousMonth = currentMonth - 1;
        nextMonth = currentMonth + 1;

        currentMonthDate = new GregorianCalendar(year, currentMonth, 1);

        if (currentMonth == 11)
            nextMonthDate = new GregorianCalendar(year + 1, 0, 1);
        else
            nextMonthDate = new GregorianCalendar(year, nextMonth, 1);

        if (currentMonth == 0)
            previousMonthDate = new GregorianCalendar(year - 1, 11, 1);
        else
            previousMonthDate = new GregorianCalendar(year, previousMonth, 1);

        weekEnd = loadFirstWeek(currentMonthDate, previousMonthDate, layout);

        weekEnd = loadWeek(currentMonthDate, 2, weekEnd, layout);
        weekEnd = loadWeek(currentMonthDate, 3, weekEnd, layout);
        weekEnd = loadWeek(currentMonthDate, 4, weekEnd, layout);
        loadLastWeek(currentMonthDate, nextMonthDate, weekEnd, layout);

//        for(int i = 1; i<=5; i++){
//            if(i==1)
//               weekEnd =  loadFirstWeek(currentMonthDate, previousMonthDate,layout);
//            if(i==5)
//                loadLastWeek(currentMonthDate, weekEnd, layout);
//            else
//                weekEnd = loadWeek(i,weekEnd,layout);
//        }
    }


    private int loadFirstWeek(Calendar currentDate, Calendar previousDate, View layout) {


        Calendar[] weekDays = new Calendar[7];

        int previousDateMaxCount = previousDate.getActualMaximum(Calendar.DATE);

        previousDate.set(Calendar.DATE, previousDate.getActualMaximum(Calendar.DATE));
        int previousDateMax = previousDate.get(Calendar.DAY_OF_WEEK);

        int currentDateMax = currentDate.get(Calendar.DAY_OF_WEEK);

        View v = layout.findViewById(weekIds[0]);
        previousDateMaxCount -= (previousDateMax - 2);
        int currentDateCount = 0;
        for (int day = 0; day < 7; day++) {
            if ((day + 1) <= (previousDateMax - 1)) {
                View dayView = v.findViewById(dayIds[day]);

                weekDays[day] = new GregorianCalendar(previousDate.get(Calendar.YEAR),
                        previousDate.get(Calendar.MONTH), previousDateMaxCount);
                ((TextView) dayView.findViewById(R.id.date_holder)).setText(String.valueOf(previousDateMaxCount));
                dayView.setTag(weekDays[day]);
                dayView.setOnClickListener(this);


                previousDateMaxCount++;
            } else {
                currentDateCount++;
                View dayView = v.findViewById(dayIds[day]);
                weekDays[day] = new GregorianCalendar(currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH), currentDateCount);
                ((TextView) dayView.findViewById(R.id.date_holder)).setText(String.valueOf(currentDateCount));
                dayView.setTag(weekDays[day]);
                dayView.setOnClickListener(this);

            }

        }

        List<TaskCanvasLayout.TaskBoard> boards = loadWeekTask(weekDays);
        for (TaskCanvasLayout.TaskBoard board : boards) {
            ((TaskCanvasLayout) layout.findViewById(weekFrameIds[0])).addTask(board);

        }
        return currentDateCount;
    }


    public void onClick(View v) {
        Calendar date = (Calendar) v.getTag();
        mParent.setCurrentTaskList(getTasks(date));
        mParent.setCurrentDate(date);
        mParent.showTaskList();
    }

    public List<Task> getAdapterData() {
        return this.tasks;
    }


    private void loadLastWeek(Calendar currentDate, Calendar nextDate, int weekEnd, View layout) {

        Calendar[] weekDays = new Calendar[7];
        int currentDateMaxCount = currentDate.getActualMaximum(Calendar.DATE);
        View v = layout.findViewById(weekIds[4]);

        int nextDateCount = 1;
        for (int day = 0; day < 7; day++) {

            if ((weekEnd + day + 1) <= (currentDateMaxCount)) {
                View dayView = v.findViewById(dayIds[day]);

                weekDays[day] = new GregorianCalendar(currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH), weekEnd + day + 1);
                ((TextView) dayView.findViewById(R.id.date_holder)).setText(String.valueOf((weekEnd + day + 1)));
                dayView.setTag(weekDays[day]);
                dayView.setOnClickListener(this);
            } else {
                View dayView = v.findViewById(dayIds[day]);

                weekDays[day] = new GregorianCalendar(nextDate.get(Calendar.YEAR),
                        nextDate.get(Calendar.MONTH), nextDateCount);
                ((TextView) dayView.findViewById(R.id.date_holder)).setText(String.valueOf(nextDateCount));
                dayView.setTag(weekDays[day]);
                dayView.setOnClickListener(this);
                nextDateCount++;
            }

        }

        List<TaskCanvasLayout.TaskBoard> boards = loadWeekTask(weekDays);
        for (TaskCanvasLayout.TaskBoard board : boards) {
            ((TaskCanvasLayout) layout.findViewById(weekFrameIds[4])).addTask(board);

        }

    }

    private int loadWeek(Calendar currentDate, int week, int weekEnd, View layout) {


        Calendar[] weekDays = new Calendar[7];
        View v = layout.findViewById(weekIds[week - 1]);
        int day;

        for (day = 0; day < 7; day++) {
            View dayView = v.findViewById(dayIds[day]);
            currentDate.set(Calendar.DATE, (weekEnd + day + 1));
            weekDays[day] = new GregorianCalendar(currentDate.get(Calendar.YEAR),
                    currentDate.get(Calendar.MONTH), weekEnd + day + 1);
            ((TextView) dayView.findViewById(R.id.date_holder)).setText(String.valueOf(weekEnd + day + 1));
            dayView.setTag(weekDays[day]);
            dayView.setOnClickListener(this);
        }

        List<TaskCanvasLayout.TaskBoard> boards = loadWeekTask(weekDays);
        for (TaskCanvasLayout.TaskBoard board : boards) {
            ((TaskCanvasLayout) layout.findViewById(weekFrameIds[week - 1])).addTask(board);

        }

        return weekEnd + 7;

    }

    private List<TaskCanvasLayout.TaskBoard> loadWeekTask(Calendar[] days) {

        List<TaskCanvasLayout.TaskBoard> boards = new ArrayList<>();
        for (Task t : tasks) {
            String[] startDateArr = t.getStart_date().split("/");

            Calendar startDate = new GregorianCalendar(Integer.valueOf(startDateArr[2]), Integer.valueOf(startDateArr[0]) - 1,
                    Integer.valueOf(startDateArr[1]));

            String[] endDateArr = t.getEnd_date().split("/");
            int endMonth = Integer.valueOf(endDateArr[0]) - 1;

            Calendar endDate = new GregorianCalendar(Integer.valueOf(endDateArr[2]), endMonth,
                    Integer.valueOf(endDateArr[1]));

            boolean startPositionSet = false;
            int startPos = -1, endPos = -1;

            for (int d = 0; d < days.length; d++) {
                if ((days[d].after(startDate) || days[d].equals(startDate)) && (days[d].before(endDate) || days[d].equals(endDate))) {
                    if (!startPositionSet) {

                        startPos = d;
                        endPos = d;
                        startPositionSet = true;
                    } else
                        endPos = d;
                }
            }

            if (startPositionSet) {
                TaskCanvasLayout.TaskBoard board = new TaskCanvasLayout.TaskBoard(startPos, endPos, t.getTitle());

                Log.i("Week Task Found", t.getTitle());

                boards.add(board);
            }

        }

        return boards;

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private List<Task> getTasks(Calendar date) {
        List<Task> tasks = new ArrayList<>();
        for (Task t : this.tasks) {
            String[] startDateArr = t.getStart_date().split("/");

            Calendar startDate = new GregorianCalendar(Integer.valueOf(startDateArr[2]), Integer.valueOf(startDateArr[0]) - 1,
                    Integer.valueOf(startDateArr[1]));

            String[] endDateArr = t.getEnd_date().split("/");
            int endMonth = Integer.valueOf(endDateArr[0]) - 1;

            Calendar endDate = new GregorianCalendar(Integer.valueOf(endDateArr[2]), endMonth,
                    Integer.valueOf(endDateArr[1]));

            if ((date.after(startDate) || date.equals(startDate)) && (date.before(endDate) || date.equals(endDate))) {
                tasks.add(t);
            }
        }
        return tasks;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = null;
        layout = inflater.inflate(R.layout.calendar_month_view, container, false);

        initView(position, layout);
        container.addView(layout);


        return layout;
    }

    @Override
    public int getCount() {
        return 1020;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
