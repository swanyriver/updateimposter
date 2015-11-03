package com.brandonswanson.imposter3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Point;

/**
 * Created by Brandon on 6/16/14.
 */
public class Frog {

    public long getFrogLookInterval() {
        return frogLookInterval;
    }
    public void setFrogLookInterval(long frogLookInterval) {
        this.frogLookInterval = frogLookInterval;

    }


    public long getFrogLookAttentionLength() {
        return frogLookAttentionLength;
    }

    public void setFrogLookAttentionLength(long frogLookAttentionLength) {
        this.frogLookAttentionLength = frogLookAttentionLength;
    }

    private long frogLookInterval = 1200*4;
    private long frogLookAttentionLength = 1000;

    private Point mCenter= new Point();
    private ValueAnimator mTimer;
    private BrainForImposter mBrain;
    private Face mFace;

    private Frog myself;


    public Frog(BrainForImposter brain, final Face face) {

        mBrain=brain;


        myself=this;


        //mFace=face;
        face.post(new Runnable() {
            @Override
            public void run() {
                chooseFrog(face);
            }
        });




        mTimer = new ValueAnimator().ofFloat(0, 1);
        mTimer.setDuration(frogLookInterval);
        mTimer.setRepeatCount(ValueAnimator.INFINITE);
        mTimer.start();
        mTimer.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

                mBrain.lookHere(mCenter.x,mCenter.y,BrainForImposter.FROG);

                mTimer.setDuration(frogLookInterval);
                //Log.d("Frog", "Frog grabing attention,  I am Frog:" + myself.toString() + " " + myself.hashCode());
            }
        });

    }

    public void chooseFrog(Face face) {
        mFace=face;

        mCenter.x=mFace.getCenterX();
        mCenter.y=mFace.getCenterY();

        if(mTimer.isRunning()){
            mTimer.cancel();
            mTimer.start();
        }
    }


}

