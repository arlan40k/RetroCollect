package edu.uco.retrocollect.retrocollect;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * Created by Garrett A. Clement on 11/15/2016.
 */

//The basis for this bubble widget was derived from https://droidqd.wordpress.com/2015/01/07/how-to-make-a-floating-face-bubble-on-home-screen-in-android/
public class BubbleWidgetService extends Service {

    private WindowManager wm;
    private ImageView bubbleImage;
    private static final String TAG = LocalMerchantActivity.class.getSimpleName();
    public static Thread myService;
    public  boolean keepListening = true;
    private int screenWidth;
    private int screenHeight;
    private long pressedTime;
    private final static long REMOVE_TIME = 3000;
    ActivityManager am;
    ComponentName cn;
    public void onCreate(){
        super.onCreate();
        bubbleImage = new ImageView(this);

        bubbleImage.setVisibility(View.INVISIBLE);
        bubbleImage.setAlpha((float) .4);
        //Sets our desired bubble image.
        bubbleImage.setImageResource(R.mipmap.ic_launcher);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        //Get screen dimensions
        Display display =  wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        bubbleImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                    wm.removeView(bubbleImage);
                    keepListening = false;
                    stopSelf();


                return true;
            }
        });

        bubbleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
            }
        });

        //Set Parameters
        final LayoutParams params = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT;


        //Adds our new icon to the wm
        wm.addView(bubbleImage, params);

        try{
           bubbleImage.setOnTouchListener(new View.OnTouchListener(){
            WindowManager.LayoutParams paramsT = params;
               private  int initX;
               private int initY;
               private float initTouchX;
               private float initTouchY;
               private long touchStartTime = 0;

               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   //Removes the bubble image on a long press.
                   pressedTime = System.currentTimeMillis();

                   switch(event.getAction())
                   {
                       case MotionEvent.ACTION_DOWN:
                           pressedTime = (int) System.currentTimeMillis();
                           bubbleImage.setAlpha((float) 1);

                           touchStartTime = System.currentTimeMillis();
                           initX = params.x;
                           initY = params.y;
                           initTouchX = event.getRawX();
                           initTouchY = event.getRawY();
                           break;
                       case MotionEvent.ACTION_UP:
                           event.getRawX();
                           long cTime = System.currentTimeMillis() - pressedTime;
                           if(System.currentTimeMillis() - pressedTime > REMOVE_TIME && initX == params.x)
                           {
                               wm.removeView(bubbleImage);
                               stopSelf();
                               return false;
                           }
                           bubbleImage.setAlpha((float) 0.4);

                       //   params.gravity = Gravity.TOP | Gravity.LEFT;
                       //    params.x = 0;

                           if(params.x <= screenWidth/2)
                               params.x = -40;
                           else
                                params.x = screenWidth - 40;
                           wm.updateViewLayout(v, params);

                           break;
                       case MotionEvent.ACTION_MOVE:
                           touchStartTime = System.currentTimeMillis();
                           params.x = initX + (int) (event.getRawX() - initTouchX);
                           params.y = initY + (int) (event.getRawY() - initTouchY);
                           wm.updateViewLayout(v, params);
                           break;
                   }
                   return false;
               }
           });

        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Bubble Functionallity failure");
        }

        Receiver re = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BubbleWidgetService.TAG);
        registerReceiver(re, intentFilter);

        //Set up service to hide widget.
        am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        myService = new Thread(new MyService());
        myService.start();



    }

    public class Receiver extends BroadcastReceiver {

        private final String TAG = "Receiver";

        @Override
        public void onReceive(Context context, Intent intent) {

/*            if(intent.getStringExtra("TIME").equals(null))
                System.out.print("HI");*/

            if(intent.getStringExtra("DISPLAY").equals("TRUE"))
                bubbleImage.setVisibility(View.VISIBLE);
            else
                bubbleImage.setVisibility(View.INVISIBLE);
            //((TextView)findViewById(R.id.clock)).setText(intent.getStringExtra("TIME"));

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyService implements Runnable{
        Intent intent = new Intent();
        @Override
        public void run() {
            while(keepListening){
                try {
                    Thread.sleep(1000);
                    cn = am.getRunningTasks(1).get(0).topActivity;
                    Log.d("Current_Activity",  cn.getPackageName());
                    Log.d("Current_Activity",  cn.getClassName());
                    if(cn.getPackageName().equals("edu.uco.retrocollect.retrocollect"))
                        intent.putExtra("DISPLAY", "FALSE");
                    else
                        intent.putExtra("DISPLAY", "TRUE");
                    intent.setAction(TAG);
                    sendBroadcast(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
