package com.as.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.as.demo.R;
import com.as.demo.view.AncientTextView;
import com.as.demo.view.ArcProgress;
import com.as.demo.view.CountDownView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by waiting on 2016/4/25.
 */
public class MainActivity extends AppCompatActivity {




    @Bind(R.id.myview)
    AncientTextView myview;

    @Bind(R.id.cdv)
    CountDownView cdv;
//==================
    @Bind(R.id.cd_start)
    Button cd_start;

    @Bind(R.id.cd_stop)
    Button cd_stop;

//======================
    @Bind(R.id.arcp)
    ArcProgress arcp;

    @Bind(R.id.arcp2)
    ArcProgress arcp2;

    @Bind(R.id.arcp3)
    ArcProgress arcp3;

    @Bind(R.id.arc_start)
    Button arc_start;

  protected  void onCreate(Bundle b){
      super.onCreate(b);
      setContentView(R.layout.main_activity);
      ButterKnife.bind(this);

     // myview.SetText("123456789,QWERTYUIOPLKJHGFDSAZXCVBNM");


      arcp2.SetMax(100);
      arcp2.SetProgress(89);

  }


    @OnClick({R.id.cd_start,R.id.cd_stop,R.id.arc_start})
    public void CDClick(View v){
        switch (v.getId()){
            case R.id.cd_start:
                cdv.Settime(30*1000);
                cdv.Start();
                break;
            case R.id.cd_stop:
                cdv.Stop();
                break;

            case R.id.arc_start:
                arcp.SetMax(100);
                arcp.SetProgress(39);

                arcp2.SetMax(100);
                arcp2.SetProgress(69);

                arcp3.SetMax(100);
                arcp3.SetProgress(79);
                break;

        }
    }


}
