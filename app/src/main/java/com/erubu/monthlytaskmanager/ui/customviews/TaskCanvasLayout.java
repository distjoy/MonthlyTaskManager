package com.erubu.monthlytaskmanager.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TaskCanvasLayout extends LinearLayout {


    float[] dayMaxXPos = new float[7];
    float[] dayMinXPos = new float[7];
    float[] dayMaxYPos = new float[3];
    float[] dayMinYPos = new float[3];
    int[] dayExtraTasks = new int[7];
    TaskBoard[][] taskBoards;

    private TaskBoard currentTaskBoard;

    private List<TaskBoard> tasks = new ArrayList<>();
    private boolean canvasCache;


/*    TaskCanvasLayout(Context context) {
        this(context, null);
    }

    TaskCanvasLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }*/

    // public TaskCanvasLayout(Context context) {
    //     this(context, null);
    // }

    public TaskCanvasLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        initDayGridPositions();
//        if(canvasCache != null){
//
//            canvas = canvasCache;
//               Log.i("TaskCanvas", "canvasCache not null");
//        }
//        else{
//
//            canvasCache = canvas;
//               Log.i("TaskCanvas", "canvasCache  null");
//        }
//

        taskBoards = new TaskBoard[3][7];
        for (TaskBoard currentTask : tasks) {
            drawTask(currentTask.startPosition, currentTask.endPosition, currentTask.name, 0, canvas);
        }
        canvasCache = true;


//            Log.i("TaskCanvas", "currenttask not null");
//        }
//        else{
//          //;
//            Log.i("TaskCanvas", "currenttask null");
//        }
        super.onDraw(canvas);
    }


    public void addTask(TaskBoard task) {
        tasks.add(task);
        this.currentTaskBoard = task;
        // requestLayout();

    }


    private void initDayGridPositions() {
        float h = getHeight();
        float w = getWidth();
        float hDiv = h / 5;
        float wDiv = w / 7;
        for (int i = 0; i < 7; i++) {
            dayMinXPos[i] = wDiv * i;
            dayMaxXPos[i] = (wDiv * (i + 1)) - 3;

        }
        for (int i = 0; i < 3; i++) {
            dayMinYPos[i] = (hDiv * (i + 1)) + 2;
            dayMaxYPos[i] = (hDiv * (i + 2)) - 2;

        }

    }

    private void setBoardOnBoards(int start, int stop, int y, TaskBoard task) {
        for (int i = start; i <= stop; i++) {
            taskBoards[y][i] = task;
        }
    }

    private void clearBoardOnBoards(int start, int stop, int y) {
        for (int i = start; i <= stop; i++) {
            taskBoards[y][i] = null;
        }
    }

    private boolean drawTask(int xStart, int xStop, String name, int y, Canvas canvas) {

        switch (y) {
            case 0:
                if (taskBoards[0][xStart] == null) {
                    for (int x = xStart + 1; x <= xStop; x++) {
                        if (taskBoards[0][x] != null) {
                            //freeSpace(x,0);
                            clearTaskOnPane(x, taskBoards[0][x].endPosition, 0, canvas);
                            drawTask(x, taskBoards[0][x].endPosition, taskBoards[0][x].name, 1, canvas);
                            clearBoardOnBoards(x, taskBoards[0][x].endPosition, 0);
                        }

                    }
                    drawTaskOnPane(xStart, xStop, 0, canvas, name);
                    setBoardOnBoards(xStart, xStop, 0, new TaskBoard(xStart, xStop, name));

                } else
                    drawTask(xStart, xStop, name, 1, canvas);

                break;


            case 1:
                if (taskBoards[1][xStart] == null) {
                    for (int x = xStart + 1; x <= xStop; x++) {
                        if (taskBoards[1][x] != null) {
                            //freeSpace(x,0);
                            clearTaskOnPane(x, taskBoards[1][x].endPosition, 1, canvas);
                            drawTask(x, taskBoards[1][x].endPosition, taskBoards[1][x].name, 2, canvas);
                            clearBoardOnBoards(x, taskBoards[1][x].endPosition, 1);
                        }

                    }
                    drawTaskOnPane(xStart, xStop, 1, canvas, name);
                    setBoardOnBoards(xStart, xStop, 1, new TaskBoard(xStart, xStop, name));


                } else
                    drawTask(xStart, xStop, name, 2, canvas);

                break;

            case 2:
                if (taskBoards[2][xStart] == null) {
                    for (int x = xStart + 1; x <= xStop; x++) {
                        if (taskBoards[2][x] != null) {
                            //freeSpace(x,0);
                            clearTaskOnPane(x, taskBoards[2][x].endPosition, 2, canvas);
                            clearBoardOnBoards(x, taskBoards[2][x].endPosition, 2);
                            dayExtraTasks[x]++;
                            Log.i("day  ", x + " increased to " + dayExtraTasks[x] + " ");
                            // initDayViews();
                        }

                    }
                    drawTaskOnPane(xStart, xStop, 2, canvas, name);
                    setBoardOnBoards(xStart, xStop, 2, new TaskBoard(xStart, xStop, name));


                } else {

                    dayExtraTasks[xStart]++;
                    Log.i("day  ", xStart + " increased to " + dayExtraTasks[xStart] + " ");
                }

                break;
        }


        return true;
    }


    public void clearTaskOnPane(int xStart, int xStop, int yCount, Canvas canvas) {
        // the Paint instance(should be assign as a field of class).
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // the display area.
        Rect areaRect = new Rect((int) dayMinXPos[xStart], (int) dayMinYPos[yCount], (int) dayMaxXPos[xStop], (int) dayMaxYPos[yCount]);


        // draw the background style (pure color or image)
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(areaRect, mPaint);

    }


    private void drawTaskOnPane(int xStart, int xStop, int yCount, Canvas canvas, String name) {
        // the Paint instance(should be assign as a field of class).
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //mPaint.setTextSize(getResources().getDimension(12));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setFakeBoldText(true);

        // the display area.
        Rect areaRect = new Rect((int) dayMinXPos[xStart], (int) dayMinYPos[yCount], (int) dayMaxXPos[xStop], (int) dayMaxYPos[yCount]);


        // draw the background style (pure color or image)
        mPaint.setColor(0xff0a84cb);
        canvas.drawRect(areaRect, mPaint);

        String pageTitle = name;

        RectF bounds = new RectF(areaRect);
        // measure text width
        bounds.right = mPaint.measureText(pageTitle, 0, pageTitle.length());
        // measure text height
        bounds.bottom = mPaint.descent() - mPaint.ascent();

        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        mPaint.setColor(Color.WHITE);
        canvas.drawText(pageTitle, bounds.left, bounds.top - mPaint.ascent(), mPaint);

    }


    public static class TaskBoard {
        int startPosition, endPosition;
        String name;

        public TaskBoard(int x, int y, String n) {
            startPosition = x;
            endPosition = y;
            name = n;
        }

    }


}