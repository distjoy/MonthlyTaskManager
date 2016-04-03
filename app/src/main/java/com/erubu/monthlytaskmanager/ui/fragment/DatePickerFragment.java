package com.erubu.monthlytaskmanager.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    private String callerTag = null;

    public static DatePickerFragment newInstance(Calendar startDate, String tag) {
        DatePickerFragment dP = new DatePickerFragment();
        Bundle b = new Bundle();
        b.putSerializable("date", startDate);
        b.putString("caller", tag);
        dP.setArguments(b);
        return dP;
    }

    public static DatePickerFragment newInstance(Calendar startDate) {
        DatePickerFragment dP = new DatePickerFragment();
        Bundle b = new Bundle();
        b.putSerializable("date", startDate);

        dP.setArguments(b);
        return dP;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = (Calendar) getArguments().getSerializable("date");
        callerTag = getArguments().getString("caller");
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dP = new DatePickerDialog(getActivity(), this, year, month, day);
        dP.getDatePicker().setMinDate(c.getTimeInMillis());
        return dP;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i("Caller tag >>> ", callerTag);

        if (callerTag != null) {
            ((TaskViewFragment) getActivity().getSupportFragmentManager().findFragmentByTag(callerTag)).setDate(year, month, day);
        } else {
            //((DatePickerCaller)getActivity()).setDate(year, month, day);
        }
    }
}