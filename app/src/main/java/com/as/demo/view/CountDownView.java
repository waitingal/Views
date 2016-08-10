package com.as.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.as.demo.R;

/**
 * Created by waiting on 2016/8/8.
 *
 * Shader s = new SweepGradient(0, 0, mColors, null);
 * Paint.setShader(s);
 */
public class CountDownView extends View {

    private Paint paint,paint1;
    private Paint TextPaint;

    private Context mContext;
    private int radius=70;//半径
    private float lineWidth=7;//圆形线条宽度
    private float textSize=50;//字体大小
    private int roundColor=0xff1ABDE6;//圆形第一层颜色
    private int secondColor=0xff0C586B;//圆形第二层颜色
    private int textColor=0xff20CE66;//字体颜色

    private long time=1000*9;//倒数总时间
    private long lasttime;//剩余时间

    private String remaintext;
    private MyCountDownTimer mMyCountDownTimer;
    private boolean CountDowning=false;
    private CountDownCallback mCountDownCallback;
    private int progressing;//进度
    private int old_progressing=0;//暂停前进度


    private int countDownInterval=10;

    private int width,height;

    public CountDownView(Context context) {
        super(context,null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        paint =new Paint();
        paint1 = new Paint();
        TextPaint=new Paint();

        if(attrs!=null){
            TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.myCountDownView);
            Setradius((int)ta.getDimension(R.styleable.myCountDownView_radius,0));
            SetlineWidth((int)ta.getDimension(R.styleable.myCountDownView_lineWidth,0));
            SettextSize((int)ta.getDimension(R.styleable.myCountDownView_cd_textSize,0));
            SetroundColor((int)ta.getColor(R.styleable.myCountDownView_roundColor,0xff000000));
            SetsecondColor((int)ta.getColor(R.styleable.myCountDownView_secondColor,0xffffffff));
            SettextColor((int)ta.getColor(R.styleable.myCountDownView_cd_textColor,0xff000000));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

         width=radius*2+getPaddingLeft()+getPaddingRight()+(int)lineWidth;
         height=radius*2+getPaddingTop()+getPaddingBottom()+(int)lineWidth;

        setMeasuredDimension(width, height);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw(canvas);
        DrawRound(canvas);
        if(CountDowning){

            DrawText(canvas);
            DrawSecondRound(canvas);
        }

    }


    private void DrawRound(Canvas canvas){
       // paint=new Paint();
        paint.setColor(roundColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        canvas.drawCircle(width/2,height/2,radius,paint);

    }


    private void  DrawText(Canvas canvas){
       // TextPaint = new Paint();
        TextPaint.setTextSize(textSize);
        TextPaint.setAntiAlias(true);
        TextPaint.setColor(textColor);
        TextPaint.setTextAlign(Paint.Align.CENTER);//文字居中

        Paint.FontMetricsInt fontMetrics = TextPaint.getFontMetricsInt();
        int baseline = height/2 + (fontMetrics.descent- fontMetrics.ascent)/2 - fontMetrics.descent;
        float textWidth = TextPaint.measureText(remaintext);   //测量字体宽度

        canvas.drawText(remaintext,
                width/2,
                baseline,
                TextPaint);

    }


    private void DrawSecondRound(Canvas canvas){
        paint1.setColor(secondColor);
        paint1.setAntiAlias(true);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(lineWidth);
        paint1.setStrokeCap(Paint.Cap.ROUND);//圆角圆弧
        RectF rectF=new RectF();
        rectF.left=width/2-radius;
        rectF.top=width/2-radius;
        rectF.right=width/2+radius;
        rectF.bottom=width/2+radius;

        canvas.drawArc(rectF, -90, 360-progressing, false, paint1);

        if(progressing<=0){
            Stop();
        }
    }

    public void Setradius(int i){
        radius = i;
    }

    public int Getradius(){
        return radius;
    }

    public void SetlineWidth(int i){
         lineWidth=i;
    }

    public void SettextSize(int i){
        textSize = i;
    }

    public void SetroundColor(int i){
        roundColor = i;
    }

    public void SetsecondColor(int i){
        secondColor = i;
    }

    public void SettextColor(int i){
        textColor = i;
    }


    public void SetCountDownCallback(CountDownCallback cb){
        mCountDownCallback = cb;
    }

    public void Start(){
        if(!CountDowning && time>0){
            CountDowning = true;
            mMyCountDownTimer = new MyCountDownTimer(time,countDownInterval);
            mMyCountDownTimer.start();
        }
    }


    public void Stop(){
        if(CountDowning){
            CountDowning = false;
            old_progressing=0;
            time=0;
            lasttime=0;
            if(mMyCountDownTimer!=null){
                mMyCountDownTimer.cancel();
                mMyCountDownTimer.onFinish();
            }
        }
    }




    //设置总时间
    public void Settime(long l){
        time = l;
    }

    //设置剩余时间
    public void Setlasttime(long l){
        lasttime = l;
    }

    //获取剩余时间
    public long Getlasttime(){
        return  lasttime;
    }

    private synchronized  void SetProgress(long l){
        Setlasttime(l);
        if(time>0){
            remaintext =l/1000+"";
            progressing =(int)(l/(time/360));

            postInvalidate();
        }


    }

    public class MyCountDownTimer extends CountDownTimer{

        long mill,cdtime;
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
            //millisInFuture:总时间
            //countDownInterval：间隔时间
            mill = millisInFuture;
            cdtime = countDownInterval;
        }


        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            if(mCountDownCallback!=null){
                mCountDownCallback.lasttime(millisUntilFinished);
            }
            SetProgress(millisUntilFinished);
        }


        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            if(mCountDownCallback!=null){
                mCountDownCallback.end();
            }
            SetProgress(0);
        }
    }


    public interface CountDownCallback{
        void lasttime(long l);
        void end();
    }

}
