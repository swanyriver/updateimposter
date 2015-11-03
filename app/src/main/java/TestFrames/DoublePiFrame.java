package TestFrames;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import SwansonLibrary.OFFSET;
import SwansonLibrary.PointGenerator;
import SwansonLibrary.ViewTools;
import SwansonLibrary.WindyPath;
import SwansonLibrary.oneFingerMoveListener;

/**
 * Created by Brandon on 5/5/14.
 */

public class DoublePiFrame extends FrameLayout {

    private Context mContext;

    private PIFrame startofPath;
    private PIFrame endofPath;
    private oneFingerMoveListener mDoubleTouchDownListener;

    private PointGenerator mPointGenerator;




    public DoublePiFrame(Context context) {
        super(context);

        mContext=context;

        setWillNotDraw(false);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView textView = new TextView(context);

        startofPath=new PIFrame(mContext,textView);
        endofPath = new PIFrame(mContext,textView);

        addView(startofPath);
        addView(endofPath);



        endofPath.setOrigin(ViewTools.getRandomPoint(startofPath.getBounds()));
        endofPath.setCurrentPoint(ViewTools.getRandomPoint(startofPath.getBounds()));

        endofPath.setClickable(false);
        startofPath.setClickable(false);

        setClickable(true);



        mPointGenerator = new PointGenerator(startofPath.getmMinLength(),startofPath.getBounds(), WindyPath.MINIMUM_ANGLE_DEFAULT);


        mDoubleTouchDownListener = new oneFingerMoveListener() {
            @Override
            public void touchDown(float x, float y) {

                //check for wich button/point recieves touch in priority order
                if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),startofPath.getCurrentPoint(),startofPath.getmMinLength())){


                    setOnTouchListener(startofPath.getMovePointTouch());


                }
                else if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),startofPath.getOrigin(),startofPath.getmMinLength()/4)){


                    setOnTouchListener(startofPath.getMoveOrigonTouch());

                }else if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),endofPath.getCurrentPoint(),endofPath.getmMinLength())){


                    setOnTouchListener(endofPath.getMovePointTouch());
                }
                else if(!ViewTools.lengthLessThanDistance(new Point((int)x, (int)y),endofPath.getOrigin(),endofPath.getmMinLength()/4)){


                    setOnTouchListener(endofPath.getMoveOrigonTouch());
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

        endofPath.setTouchDownListener(mDoubleTouchDownListener);
        startofPath.setTouchDownListener(mDoubleTouchDownListener);
        startofPath.removeAllViews();
        endofPath.removeAllViews();

        endofPath.setmFrame(this);
        startofPath.setmFrame(this);



        setOnTouchListener(mDoubleTouchDownListener);


    }

    public void updatePoints(){




        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}


