package com.brandonswanson.imposter3;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import SwansonLibrary.ViewTools;
import SwansonLibrary.oneFingerMoveListener;
import TestFrames.PathFrame;


public class ImposterMainActivity extends Activity {

    private FrameLayout MasterLayout;


    private ArrayList<Face> mFaces;
    private BrainForImposter mBrain;

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imposter_main_layout);

        //if (savedInstanceState == null) {}

        MasterLayout = (FrameLayout) findViewById(R.id.container);
        //MasterLayout.setClickable(true);

        myGestureListener listener = new myGestureListener();
        listener.setMaster(MasterLayout);
        mDetector = new GestureDetectorCompat(this, listener);



        SetUp();


        //FrameLayout piframe = new TestFrames.PIFrame(this,(TextView) findViewById(R.id.readout));
        //piframe.setBackgroundColor(Color.BLACK);
        //MasterLayout.addView(piframe);


        //Path Visualzation stuff///////////
        ///////////////////////////////////


        //MasterLayout.addView(new TestFrames.drawFrame(this));


       //MasterLayout.addView(new TestFrames.PIFrame(this,(TextView) findViewById(R.id.readout)));

        //MasterLayout.addView(new DoublePiFrame(this));

        //MasterLayout.addView(new PathFrame(this));

        //Path Visualzation stuff///////////
        ///////////////////////////////////*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class myGestureListener extends GestureDetector.SimpleOnGestureListener{

        private FrameLayout master;
        private int index=0;
        private static final int NUMFRAMES = 4;
        private FrameLayout overframe = null;
        private static final int SWIPE_THRESHOLD = 3500;


        public void setMaster(FrameLayout m){
            master = m;
            //index = 0
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //ViewTools.makeToast(master.getContext(), "Swipe detected:" + velocityX);
            Log.d("MOTION","swipe detected:"+velocityX);

            if (velocityX < -SWIPE_THRESHOLD){
                index = (index+index-1)%NUMFRAMES;
                setFrame();
            } else if (velocityX > SWIPE_THRESHOLD){
                index = (index + 1)%NUMFRAMES;
                setFrame();
            }


            return true;
        }

        private void setFrame(){

            /*if (index==0){
                if(overframe !=null) master.removeView(overframe);
                overframe = null;
                return;
            }*/

            FrameLayout tmp = null;

            switch (index){
                case 1:
                    tmp = new TestFrames.drawFrame(master.getContext());
                    break;
                case 2:
                    tmp = new TestFrames.PIFrame(master.getContext(),
                            (TextView) findViewById(R.id.readout));
                    break;
                case 3:
                    tmp = new PathFrame(master.getContext());
                    break;
            }

            if (tmp !=null){
                tmp.setBackgroundColor(Color.BLACK);
                master.addView(tmp);
            }
            if(overframe !=null) master.removeView(overframe);
            overframe=tmp;
        }
    }

    private void SetUp() {
        mFaces = new ArrayList<Face>();

        Point size= ViewTools.getWindowSize(this);

        int width = size.x/4;
        int faceSize = (int) (width*.9);
        int margin = (int) (width*.1);
        int vstart = (size.y-size.x)/2;


        Log.d("PATH", "adding faces start");
        for(int x=margin/2;x<=size.x-faceSize-margin/2;x+=faceSize+margin){
            for(int y=vstart;y<=width*5;y+=faceSize+margin){
                final Face newFace = new Face(this,faceSize,faceSize);
                newFace.setX(x);
                newFace.setY(y);
                MasterLayout.addView(newFace);
                mFaces.add(newFace);
                newFace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("CONTOLLER", "Guess sent to brain");
                        mBrain.MakeGuess((Face) v);
                    }
                });
            }
        }
        Log.d("PATH", "adding faces end");

        Rect bounds = ViewTools.getWindowBounds(this);
        Rect flyBounds = new Rect(bounds);
        flyBounds=ViewTools.marginRectPercent(flyBounds,.10f);
        flyBounds=ViewTools.squareRect(flyBounds);

        //todo make sure no subclasses are doing layout measurments, pass in

        mBrain = new BrainForImposter(mFaces,bounds);


        Log.d("PATH", "adding fly");
        MasterLayout.addView(new Fly(this,mBrain,flyBounds,.75f));
        Log.d("PATH", "adding fly finished");

        //for testing
        //mBrain.setMAX_FLY_FOCUSES(0);


        ///finger touch

        MasterLayout.setOnTouchListener(new oneFingerMoveListener() {
            @Override
            public void touchDown(float x, float y) {
                mBrain.lookHere((int)x,(int)y,BrainForImposter.FINGER);
            }

            @Override
            public void touchAt(float x, float y) {
                mBrain.lookHere((int)x,(int)y,BrainForImposter.FINGER);

            }

            @Override
            public void touchOver() {

                mBrain.StopLookingAll(BrainForImposter.FINGER);
            }
        });


    }


}
