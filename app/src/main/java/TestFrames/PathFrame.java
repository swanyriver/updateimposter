package TestFrames;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.brandonswanson.imposter3.R;

import SwansonLibrary.PathCrawler;
import SwansonLibrary.PathPlus;
import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;

/**
 * Created by Brandon on 5/6/14.
 */
public class PathFrame extends FrameLayout{

    public Context mContext;
    public FrameLayout mMyself;

    private Path mCurvePath = new Path();
    private Paint mCurvePaint = new Paint();
    private Path mLinePath = new Path();
    private Paint mLinePaint = new Paint();

    public PathFrame(Context context) {
        super(context);

        mMyself=this;
        mContext=context;
        setWillNotDraw(false);

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        mLinePaint.setColor(Color.BLUE);
        mCurvePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(6);
        mCurvePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mCurvePaint.setStyle(Paint.Style.STROKE);


        Rect bounds = ViewTools.getWindowBounds(context);
        bounds=ViewTools.marginRectPercent(bounds,.10f);

        //ViewTools.rectToLines(bounds,mLinePath);
        //bounds.inset(60,60);
        //ViewTools.rectToLines(bounds,mLinePath);


        //ViewTools.rectToLines(bounds,mCurvePath);


        bounds=ViewTools.squareRect(bounds);
        //ViewTools.rectToLines(bounds,mLinePath);
        //bounds=ViewTools.marginRectPercent(bounds,.90f);
        //ViewTools.rectToLines(bounds,mLinePath);


       /* mCurvePath.moveTo(bounds.centerX(),bounds.centerY());
        mCurvePath.lineTo(bounds.centerX()+20,bounds.centerY()+20);
        mCurvePath.lineTo(bounds.centerX()+50,bounds.centerY()+20);
        mCurvePath.lineTo(bounds.centerX()-60,bounds.centerY()-67);*/

        mCurvePath=new WindyPath(bounds,200);
        //mCurvePath.close();

        PathCrawler pathCrawler = new PathCrawler(mCurvePath) {
            @Override
            public void onCrawl(ViewTools.PathPosition pathPosition) {

                if(mCurvePaint.getColor()==Color.RED) {
                    Path segment = new Path();
                    mCrawlPathMeasure.getSegment(pathPosition.distance - 1600, pathPosition.distance + 1600, segment, true);
                    PathPlus.getDrawableSegment(segment, mLinePath, 2);
                }



                mCrawlingView.setRotation(mCrawlingView.getRotation()+90f);
                mMyself.invalidate();
            }
        };

        pathCrawler.setDuration(800*17*12);
        pathCrawler.setInterpolator(new LinearInterpolator());
        pathCrawler.setRepeatCount(ValueAnimator.INFINITE);
        pathCrawler.setRepeatMode(ValueAnimator.REVERSE);



        ImageView fly = new ImageView(context);

        fly.setImageResource(R.drawable.fly);
        fly.setLayoutParams(new LayoutParams(100,100));
        addView(fly);
        pathCrawler.setView(fly);

        pathCrawler.start();



        Button myButton = new Button(context);
        myButton.setText("Path Visualization");
        myButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        myButton.setX(300);
        myButton.setY(1100);
        myButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurvePaint.getColor()==Color.RED)mCurvePaint.setColor(Color.TRANSPARENT);
                else mCurvePaint.setColor(Color.RED);
            }
        });
        addView(myButton);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //canvas.drawPath(mCurvePath,mCurvePaint);
        canvas.drawPath(mLinePath,mCurvePaint);

    }
}
