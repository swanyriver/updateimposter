package com.brandonswanson.imposter3;


import android.content.Context;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import SwansonLibrary.ViewTools;

/**
 * Created by Brandon on 4/24/14.
 */
public class BrainForImposter {

    static final int UNFOCUSED = 00;
    static final int FLY = 10;
    static final int FROG = 20;
    static final int FINGER = 30;
    static final int REVEALED_FROG = 40;
    private static final int UNRESTRICTED = -1;

    private float FlyFocusDistanceNONSQRTD=0;

    public void setMAX_FLY_FOCUSES(int MAX_FLY_FOCUSES) {
        this.MAX_FLY_FOCUSES = MAX_FLY_FOCUSES;
    }

    private int MAX_FLY_FOCUSES = UNRESTRICTED;


    public void setMAXGUESSES(int MAXGUESSES) {
        this.MAXGUESSES = MAXGUESSES;
    }

    private int MAXGUESSES = 3;
    private int mGuessCount = 0;
    //todo count guesses and move frog after max guesses, add makeGuess Method!!, brain decides winning reaction!

    //public static float FLY_FOCUS_DISTANCE_PERCENT_DEFAULT=.2f*.01f;
    public static float FLY_FOCUS_DISTANCE_PERCENT_DEFAULT=.1f;

    private Random mRandom = new Random();

    private ArrayList<Face> mFaces;
    private Context mContext;

    private Rect mBounds;

    private int mFrogIndex;

    private Frog mFrog;

    private ArrayList<Integer> mGuessesMade = new ArrayList<Integer>();

    public BrainForImposter(ArrayList<Face> faces, Rect bounds){

        mFaces=faces;
        mBounds=bounds;
        mContext=mFaces.get(0).getContext();

        setFlyFocusDistace(FLY_FOCUS_DISTANCE_PERCENT_DEFAULT);

        //pick a frog

        mFrogIndex =mRandom.nextInt(mFaces.size());

        mFrog = new Frog(this,mFaces.get(mFrogIndex));

    }



    ////Initialization Methods////////
    ////////////////////////////////

    private void selectFrog() {

        Log.d("BRAIN", "NEW FROG SELECTED");

        ViewTools.makeToast(mContext, "THE FROG HAS MOVED");

        mFrogIndex =mRandom.nextInt(mFaces.size());

        //mFrog = new Frog(this,mFaces.get(mFrogIndex));
        mFrog.chooseFrog(mFaces.get(mFrogIndex));

    }

    public void setFlyFocusDistace(float percent) {

        if(percent<0||percent>1){
            setFlyFocusDistace(FLY_FOCUS_DISTANCE_PERCENT_DEFAULT);
            return;
        }

        double diagonalDistance = ViewTools.getHypotenuse(mBounds.width(),mBounds.height());

        Log.d("BRAIN","percent:"+percent);

        FlyFocusDistanceNONSQRTD= (float)Math.pow(diagonalDistance*percent,2);

    }

    ////Public Methods///////////
    ////////////////////////////
    /////Selection Brain////////

    public void lookHere(int x, int y, int ID){



        switch (ID){
            case FLY:
                //find faces close enough
                /*for(int i=0;i<mFaces.size();i++){
                    Face face = mFaces.get(i);
                    if(ViewTools.getDistancetoNonSQRTD(x,y,face.getCenterX(),face.getCenterY())<FlyFocusDistanceNONSQRTD){
                       lookHere(x,y,face,FLY);
                    }else StopLookingOne(FLY, face);
                }*/

                //pick one face
                class faceDistance{
                    public float distance;
                    public Face face;
                    faceDistance(float distance, Face face) {
                        this.distance = distance;
                        this.face = face;
                    }

                }
                ArrayList<faceDistance> distances = new ArrayList<faceDistance>();
                for(int i=0;i<mFaces.size();i++){
                    Face face = mFaces.get(i);
                    float flyDistance = ViewTools.getDistancetoNonSQRTD(x, y, face.getCenterX(), face.getCenterY());
                    //Log.d("BRAIN", "FLY DISTANCE SQUARED:" + FlyFocusDistanceNONSQRTD + " this face distance: "+ flyDistance);
                    if(flyDistance <FlyFocusDistanceNONSQRTD){
                       distances.add(new faceDistance(flyDistance,mFaces.get(i)));
                    }else StopLookingOne(FLY, face);

                }

                ArrayList<faceDistance> distancesSorted = new ArrayList<faceDistance>();
                if(distances.size()>0)distancesSorted.add(distances.get(0));
                for(int i=1;i<distances.size();i++){
                    int distanceSortedSize=distancesSorted.size();
                    for(int i2=0;i2<distanceSortedSize;i2++){
                        if(i2==distancesSorted.size()-1)distancesSorted.add(distances.get(i));
                        else if(distances.get(i).distance<distancesSorted.get(i2).distance){
                            distancesSorted.add(i2,distances.get(i));
                            i2=distancesSorted.size();
                        }
                    }

                }


                for(int i=0;i<distancesSorted.size();i++){
                    if(i< MAX_FLY_FOCUSES||MAX_FLY_FOCUSES==UNRESTRICTED)lookHere(x,y,distancesSorted.get(i).face,FLY);
                    else StopLookingOne(FLY,distancesSorted.get(i).face);

                }

                ///picking one face


                break;
            case FINGER:
                for(int i=0;i<mFaces.size();i++){
                    Face face = mFaces.get(i);
                    if(ViewTools.getDistancetoNonSQRTD(x,y,face.getCenterX(),face.getCenterY())<FlyFocusDistanceNONSQRTD*2){
                       lookHere(x,y,face,FINGER);
                    }else StopLookingOne(FINGER, face);
                }
                break;
            case FROG:
                ///select a face to look at the frog
                //command to look only sent once, eyes stay looking at frog, until relase code sent
                int faceSelected = mRandom.nextInt(mFaces.size()-1);
                if(faceSelected== mFrogIndex)faceSelected++;
                mFaces.get(faceSelected).SetEyeAdjustDuration(true);
                lookHere(x, y, mFaces.get(faceSelected), FROG);

                new lookAway(mFaces.get(faceSelected),mFrog.getFrogLookAttentionLength());

                //mFrog.setFrogLookAttentionLength(mFrog.getFrogLookAttentionLength()*2);
                //mFrog.setFrogLookInterval((long) (mFrog.getFrogLookInterval()*.90));
                //Log.d("Brain", "frog look interval:"+ mFrog.getFrogLookInterval());
                //Log.d("Brain", "frog look lenght:"+ mFrog.getFrogLookAttentionLength());

                break;
            case REVEALED_FROG:
                ///
                break;

        }

    }

    public void MakeGuess(Face touchedFace){

        int indexOfGuess = mFaces.indexOf(touchedFace);
        if(indexOfGuess==mFrogIndex){
            Log.d("Brain", "you found the frog");
            ViewTools.makeToast(mContext, "GOOD JOB YOU FOUND THE FROG");
            mGuessCount=0;
            selectFrog();

        }else{
            if(!mGuessesMade.contains(indexOfGuess)){
                mGuessCount++;
                mGuessesMade.add(indexOfGuess);
            }

            if(mGuessCount>=MAXGUESSES){
                mGuessCount=0;
                ViewTools.makeToast(mContext,"TOO MANY GUESSES");
                selectFrog();

                //tooManyGuesses, shuffleing


            }else {
                ViewTools.makeToast(mContext,"NOT THAT ONE");
            }

            Log.d("Brain", "Guesses Made: " + mGuessCount + " out of:"+MAXGUESSES);


        }



    }

    private class lookAway{
        private Face mDelayFace;
        lookAway(Face face, long delay) {
            mDelayFace=face;
            face.postDelayed(new Runnable() {
                @Override
                public void run() {

                    StopLookingOne(FROG, mDelayFace);
                    mDelayFace.SetEyeAdjustDuration(false);
                }
            },delay);

        }
    }



    public void StopLookingAll(int ID){
        for(int i=0;i<mFaces.size();i++){
            if(mFaces.get(i).getFocus()==ID) mFaces.get(i).setFocus(UNFOCUSED);
        }
    } 
    public void StopLookingOne(int ID, Face face){

        if(face.getFocus()==ID)face.setFocus(UNFOCUSED);

    }





    ////Private Methods///////////
    ////////////////////////////
    /////Priority Brain////////

    private void lookHere(int x, int y, Face face, int ID){
        if(ID==face.getFocus()){
            face.Eyes().focusHere(x,y);
        }else if(ID>face.getFocus()){
            face.Eyes().focusHere(x,y);
            face.setFocus(ID);
        }
    }


    ///////////////////////////////
    //Frog interface///////////////
    //////////////////////////////
    public long getFrogLookInterval() {
        return mFrog.getFrogLookInterval();
    }
    public void setFrogLookInterval(long frogLookInterval) {
        mFrog.setFrogLookInterval(frogLookInterval);

    }

    public long getFrogLookAttentionLength() {
        return mFrog.getFrogLookAttentionLength();
    }

    public void setFrogLookAttentionLength(long frogLookAttentionLength) {
        mFrog.setFrogLookAttentionLength(frogLookAttentionLength);
    }

}
