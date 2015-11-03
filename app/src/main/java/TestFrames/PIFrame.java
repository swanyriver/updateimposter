package TestFrames;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import SwansonLibrary.OFFSET;
import SwansonLibrary.PathPlus;
import SwansonLibrary.PointGenerator;
import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;
import SwansonLibrary.oneFingerMoveListener;

/**
 * Created by Brandon on 4/29/14.
 */
public class PIFrame extends FrameLayout {

    public TextView mReadout;
    public Context mContext;

    public Point mOrigin = new Point(720/2,1280/2);
    public Point getOrigin(){return mOrigin;}
    public void setOrigin(Point origon){
        mOrigin=origon;
        pointCalcDisplay();

    }

    private Point mCurrentPoint = new Point();
    public Point getCurrentPoint(){return mCurrentPoint;}
    public void setCurrentPoint(Point point){
        mCurrentPoint=point;
        pointCalcDisplay();

    }


    private int mMargin=65;

    private float mMinLengthPercentage=.125F;



    ///DATA STRUCTURE
    //private Point mLinePoints[];
    private int mMaxLength;

    public int getmMinLength() {
        return mMinLength;
    }

    private int mMinLength;

    /*private int Bounds.right;
    private int Bounds.bottom;
    private int Bounds.left;
    private int Bounds.top;*/

    private ArrayList<Point> mRandomPoint = new ArrayList<Point>();
    private PointGenerator.RadianExclusionMap radianMap;




    private Rect Bounds = new Rect();
    public Rect getBounds(){return Bounds;}


    private ViewTools.Line BorderLines[];
    private Double mPreviousRadian;



    private Path mCurvePath = new Path();
    private Paint mCurvePaint = new Paint();
    private PathPlus mLinePath = new PathPlus();
    private Paint mLinePaint = new Paint();
    private Path mBorderPath = new Path();
    private Path mRadianPath= new Path();
    private Paint mRadianPaint = new Paint();

    private PathPlus mPointPath = new PathPlus();
    private Paint mPointPaint = new Paint();

    private PathPlus mLabelPath = new PathPlus();
    private ViewTools.Line mlableLines[] = new ViewTools.Line[8];
    private Paint mLabelPaint = new Paint();
    private float mLablelLineDegrees[] = new float[]{0,0.25f,0.5f,0.75f,1,1.25f,1.5f,1.75f};
    private int mLabelLineLength;

    private FrameLayout Myself;



    private PointGenerator mPointGenerator;

    private DecimalFormat OneDigit = new DecimalFormat("#.#");
    private DecimalFormat TwoDigit = new DecimalFormat("#.##");

    private oneFingerMoveListener movePointTouch;
    private oneFingerMoveListener moveOrigonTouch;

    public oneFingerMoveListener getMoveOrigonTouch() {
        return moveOrigonTouch;
    }

    public oneFingerMoveListener getMovePointTouch() {
        return movePointTouch;
    }

    private oneFingerMoveListener TouchDownListener;

    public void setTouchDownListener(oneFingerMoveListener touchDownListener){
        TouchDownListener=touchDownListener;
    }

    private FrameLayout mFrame;
    public void setmFrame(FrameLayout frame){mFrame=frame;}


    public PIFrame(Context context, TextView textView) {

        super(context);


        mFrame=this;


        mReadout=textView;
        mContext=context;

        mPreviousRadian=-1.0;



        Bounds = ViewTools.getWindowBounds(context);
        Bounds.inset(mMargin,mMargin);

        mMaxLength = (int) ViewTools.getHypotenuse(Bounds.width(),Bounds.height());
        mMinLength = (int) (mMaxLength*mMinLengthPercentage);

        BorderLines=ViewTools.rectToLines(Bounds);

        mCurrentPoint = ViewTools.vectorToPoint(Math.PI / 2, mMinLength * 1.5, mOrigin);




        mPointGenerator = new PointGenerator(mMinLength,Bounds,WindyPath.MINIMUM_ANGLE_DEFAULT);
        mPointGenerator.setmMaxAngle(WindyPath.MINIMUM_ANGLE_DEFAULT);



        Myself=this;

        mLinePaint.setColor(Color.BLUE);
        mCurvePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(8);
        mCurvePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mCurvePaint.setStyle(Paint.Style.STROKE);

        mLabelPaint.setColor(Color.WHITE);
        mLabelPaint.setStyle(Paint.Style.STROKE);
        mLabelPaint.setStrokeWidth(1);
        mLabelPaint.setTextSize(20);
        mLabelLineLength =45;

        mPointPaint.setColor(Color.MAGENTA);
        mPointPaint.setStrokeWidth(3);
        mPointPaint.setStyle(Paint.Style.STROKE);


        mRadianPaint.setColor(Color.GREEN);
        mRadianPaint.setStrokeWidth(4);
        mRadianPaint.setStyle(Paint.Style.STROKE);

        mBorderPath.moveTo(Bounds.left,Bounds.bottom);
        mBorderPath.lineTo(Bounds.left, Bounds.top);
        mBorderPath.lineTo(Bounds.right, Bounds.top);
        mBorderPath.lineTo(Bounds.right, Bounds.bottom);
        mBorderPath.lineTo(Bounds.left,Bounds.bottom);

        pointCalcDisplay();

        setWillNotDraw(false);






        //VISUALIZE MAX AND MIN DISTANCE
       /* mBorderPath.moveTo(Bounds.left,Bounds.bottom);
        Double radian = Math.atan2(Bounds.top-Bounds.bottom,Bounds.right-Bounds.left);
        Point newPoint = ViewTools.VectorBounds.topoint(radian,mMaxLength,new Point(Bounds.left,Bounds.bottom));
        mBorderPath.lineTo(newPoint.x,newPoint.y);
        mBorderPath.addCircle(mWidth/2,mHeight/2,mMinLength, Path.Direction.CCW);
        mBorderPath.addCircle(Bounds.left,Bounds.bottom,10, Path.Direction.CCW);*/


        ///move origin point button
        Button myButton = new Button(context);
        myButton.setText("Point");
        myButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        myButton.setX(20);
        myButton.setY(1100);
        myButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRandomPoint.clear();
                mRandomPoint.add(radianMap.getPoint());

                mPointPath.rewind();
                for(int i=0;i<mRandomPoint.size();i++){
                    mPointPath.makeLine(mCurrentPoint,mRandomPoint.get(i));
                }
                Myself.invalidate();
            }

        });
        addView(myButton);

        ///generate random radian
        Button myotherButton = new Button(context);
        myotherButton.setText("Many Points");
        myotherButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        myotherButton.setX(600);
        myotherButton.setY(1100);
        myotherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRandomPoint.clear();
                for(int x=0;x<30;x++){
                 mRandomPoint.add(radianMap.getPoint());
                }

                //debug
                mRandomPoint.clear();
                mRandomPoint=radianMap.getAllPoints();
                //

                mPointPath.rewind();
                for(int i=0;i<mRandomPoint.size();i++){
                    mPointPath.makeLine(mCurrentPoint,mRandomPoint.get(i));
                }
                Myself.invalidate();
            }
        });
        addView(myotherButton);





        TouchDownListener = new oneFingerMoveListener() {
            @Override
            public void touchDown(float x, float y) {



                //check for wich button/point recieves touch in priority order
                if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),mCurrentPoint,mMinLength)){

                    setOnTouchListener(movePointTouch);
                }
                else if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),mOrigin,mMinLength/4)){




                    setOnTouchListener(moveOrigonTouch);
                }


            }

            @Override
            public void touchAt(float x, float y) {

                //Log.d("PATH", "we went to far, touch listener changed");
            }

            @Override
            public void touchOver() {

            }
        };

        moveOrigonTouch = new oneFingerMoveListener() {
            private OFFSET Offset;

            @Override
            public void touchDown(float x, float y) {
                //shouldnt be reached
            }

            @Override
            public void touchAt(float x, float y) {

                //Log.d("PATH", "origon move touch");

                boolean inside = ViewTools.containsInner(x, y, Bounds);
                boolean lengthAllowed = ViewTools.lengthLessThanDistance(mCurrentPoint,new Point((int)x,(int)y), mMinLength);

                if(inside && lengthAllowed){
                    setOrigin(new Point((int)x,(int)y));
                }else {

                    touchOver();
                }
            }

            @Override
            public void touchOver() {

                Offset=null;
                mFrame.setOnTouchListener(TouchDownListener);

            }
        };

        movePointTouch = new oneFingerMoveListener() {
            private OFFSET Offset;
            @Override
            public void touchDown(float x, float y) {
                //shouldnt be reached

            }

            @Override
            public void touchAt(float x, float y) {

               // Log.d("PATH", "point move touch");

                if(Offset==null){
                    Offset=new OFFSET(mCurrentPoint.x,mCurrentPoint.y,(int)x,(int)y);
                }
                Point touchPoint = Offset.get((int)x,(int)y);

                boolean inside = ViewTools.containsInner(touchPoint.x, touchPoint.y, Bounds);
                boolean lengthAllowed = ViewTools.lengthLessThanDistance(mOrigin,touchPoint, mMinLength);

                if(inside && lengthAllowed){
                    setCurrentPoint(touchPoint);
                }else {
                    touchOver();
                }

            }

            @Override
            public void touchOver() {
                Offset=null;
                mFrame.setOnTouchListener(TouchDownListener);

            }

        };

        setClickable(true);
        setOnTouchListener(TouchDownListener);

    }

    private void pointCalcDisplay() {
        mLinePath.rewind();




        mLinePath.moveTo(mOrigin.x,mOrigin.y);
        mLinePath.lineTo(mCurrentPoint.x,mCurrentPoint.y);

        mLinePath.addCircle(mCurrentPoint.x,mCurrentPoint.y,mMinLength, Path.Direction.CCW);

        //Double radian = Math.atan2(y-mOrigin.y,x-mOrigin.x);
        Double radian = ViewTools.getArcTan2Mapped(mOrigin, mCurrentPoint);


        //Origin Point viz
        //Point vectorPoint = ViewTools.VectorBounds.topoint(radian,40,mOrigin);
        //mLinePath.addCircle(vectorPoint.x,vectorPoint.y,5, Path.Direction.CCW);
        mLinePath.addCircle(mOrigin.x,mOrigin.y,7, Path.Direction.CCW);
        /*int triangleSize = 20;

        Point outPoint = ViewTools.VectorBounds.topoint(radian,triangleSize,mOrigin);

        Point tip = ViewTools.VectorBounds.topoint(radian,triangleSize,outPoint);
        Point cornerL = ViewTools.VectorBounds.topoint(radian+Math.PI/2,triangleSize/2,outPoint);
        Point cornerR = ViewTools.VectorBounds.topoint(radian-Math.PI/2,triangleSize/2,outPoint);

        mLinePath.makeLine(new ViewTools.Line(cornerL,tip));
        mLinePath.makeLine(new ViewTools.Line(tip,cornerR));
        mLinePath.makeLine(new ViewTools.Line(cornerR,cornerL));*/





        mReadout.setText("Radian:" + TwoDigit.format(radian) + " PI:" + TwoDigit.format(radian / Math.PI));


        radianMap = mPointGenerator.makeMap(mOrigin,mCurrentPoint);
        radianMap.Visualize(mRadianPath, mReadout);



        //draw labels lines, cant draw  text here,  save off lines for text draw

        mLabelPath.rewind();
        for (int i=0;i<8;i++){
            mlableLines[i]=new ViewTools.Line(
                    ViewTools.vectorToPoint(Math.PI * mLablelLineDegrees[i], mMinLength - mLabelLineLength, mCurrentPoint),
                    ViewTools.vectorToPoint(Math.PI * mLablelLineDegrees[i], mMinLength + mLabelLineLength, mCurrentPoint)
            );
            mLabelPath.makeLine(mlableLines[i]);
        }

        mRandomPoint.clear();
        mPointPath.rewind();


        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mPointPath,mPointPaint);
        canvas.drawPath(mRadianPath,mRadianPaint);
        canvas.drawPath(mLinePath,mCurvePaint);
        canvas.drawPath(mBorderPath,mLinePaint);

        canvas.drawPath(mLabelPath,mLabelPaint);


        //draw label text here


        for (int i=0;i<8;i++){
            canvas.drawTextOnPath(""+mLablelLineDegrees[i],PathPlus.textLine(mlableLines[i]),0,-5,mLabelPaint);
            canvas.drawTextOnPath(""+TwoDigit.format(mLablelLineDegrees[i] * Math.PI),
                    PathPlus.textLine(ViewTools.reverseLine(mlableLines[i])),0,-5,mLabelPaint);
        }





    }



}
