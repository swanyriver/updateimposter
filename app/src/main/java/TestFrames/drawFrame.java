package TestFrames;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;

import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;



/**
 * Created by Brandon on 4/28/14.
 */
public class drawFrame extends FrameLayout {


    private Paint mCurvePaint = new Paint();
    private Path mLinePath = new Path();
    private Paint mLinePaint = new Paint();

    private Random mRandom = new Random();

    private FrameLayout Myself;

    private Context mContext;

    private boolean lines = false;

    private DecimalFormat myFormat = new DecimalFormat("#");


    private Button myButton;

    private final WindyPath myWindyPath;


    public drawFrame(Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Myself = this;
        mContext=context;

        mLinePaint.setColor(Color.BLUE);
        mCurvePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(10);
        mCurvePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mCurvePaint.setStyle(Paint.Style.STROKE);

        setWillNotDraw(false);


        myButton = new Button(context);
        myButton.setText("Line Visualization");
        myButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        myButton.setX(20);
        myButton.setY(ViewTools.getWindowSize(context).y - 220);
        myButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lines) lines = false;
                else lines = true;
                Myself.invalidate();

            }
        });
        addView(myButton);

        Button otherbutton = new Button(context);
        otherbutton.setText("new path");
        otherbutton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        otherbutton.setX(ViewTools.getWindowSize(context).x - 300);
        otherbutton.setY(ViewTools.getWindowSize(context).y - 220);
        otherbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLinesAndCurves();
                Myself.invalidate();
            }
        });
        addView(otherbutton);

        /*Rect bounds = new Rect();
        int margin = (1280-720-60)/2;
        bounds.left=30;
        bounds.right=720-30;
        bounds.top=1280-margin;
        bounds.bottom=margin;*/

        Rect bounds=ViewTools.getWindowBounds(context);


        bounds.inset(40,40);
        bounds.bottom -= 200;


        myWindyPath = new WindyPath(bounds,7);
        //myWindyPath = new testWindy(bounds,10);
        //myWindyPath=new maxLengthWindy(bounds,15);



        //myWindyPath.generate();
        makeLinesAndCurves();
        Myself.invalidate();




    }


    private void makeLinesAndCurves(){

        myWindyPath.generate(true,mRandom.nextInt(10)+6);



        Point LinePoints[]= myWindyPath.getLinePoints();
        int LineNumber = myWindyPath.getLineNumber();


        mLinePath.rewind();
        mLinePath.moveTo(LinePoints[0].x, LinePoints[0].y);
        for(int x=1;x<LineNumber+1;x++){
            mLinePath.lineTo(LinePoints[x].x, LinePoints[x].y);
        }


        /*//TEMPORARY, REMOVE ANGLE LABELS
        removeAllViews();
        measureAngeles(LinePoints, LineNumber);
        addView(myButton);*/

    }

    private void measureAngeles(Point[] points, int LineNumber) {

        String logstring = new String();
        logstring="Angles:";

        for(int i=0;i<LineNumber-1;i++){
            TextView display = new TextView(mContext);
            this.addView(display);
            display.setX(points[i + 1].x);
            display.setY(points[i + 1].y);
            display.setTextColor(Color.WHITE);
            display.setText("" + myFormat.format(ViewTools.getAngleDotProduct(points[i], points[i + 1], points[i + 2])));

            logstring+=myFormat.format(ViewTools.getAngleDotProduct(points[i], points[i + 1], points[i + 2]));
            logstring+=",";

        }
        Log.d("PATH", logstring);

    }




    @Override
    protected void onDraw(Canvas canvas) {

        if(lines) canvas.drawPath(mLinePath,mLinePaint);
        canvas.drawPath(myWindyPath,mCurvePaint);

        //Log.d("PATH", "in on draw");

        super.onDraw(canvas);


    }
}
