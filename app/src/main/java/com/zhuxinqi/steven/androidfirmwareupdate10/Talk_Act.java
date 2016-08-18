package com.zhuxinqi.steven.androidfirmwareupdate10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Steven on 6/2/16.
 */
public class Talk_Act extends AppCompatActivity {

    /************AndroidVariables&Constants**************/
    private static final String TAG = "In Talk_Act";
    private TextView text_info = null;
    private TextView text_talk = null;
    private EditText editText = null;
    private Button btn_send = null;
    private Button btn_up = null;
    private Button btn_down = null;
    private Button btn_left = null;
    private Button btn_right = null;
    private Button btn_b1 = null;
    private Button btn_b2 = null;
    private Button btn_b3 = null;
    private Button btn_b4 = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private boolean checkit = false;
    private int LEDspeed = 1;
    /************AndroidVariables&Constants**************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_to_device);

        /*******MatchLayout********/
        Intent intent = getIntent();
        checkit = intent.getBooleanExtra(Main_Act.IfConnected, false);
        if (checkit){
            outStream = ((Global_Variable)getApplication()).GetOutStream();
            inStream = ((Global_Variable)getApplication()).GetInStream();
        }

        //Text View
        text_talk = (TextView)findViewById(R.id.textTalk);
        text_talk.setMovementMethod(ScrollingMovementMethod.getInstance());

        //Info View
        text_info = (TextView)findViewById(R.id.info_message);

        //Edit Text
        editText = (EditText) findViewById(R.id.editText);

        //B1 Button
        btn_b1 = (Button)findViewById(R.id.b1);
        btn_b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "LED1.");
                ShowMessage("LED1.");
                if (checkit) {
                    try {
                        UartSend("1#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //B2 Button
        btn_b2 = (Button)findViewById(R.id.b2);
        btn_b2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "LED2.");
                ShowMessage("LED2.");
                if (checkit) {
                    try {
                        UartSend("2#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //B3 Button
        btn_b3 = (Button)findViewById(R.id.b3);
        btn_b3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "LED3.");
                ShowMessage("LED3.");
                if (checkit) {
                    try {
                        UartSend("3#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //B4 Button
        btn_b4 = (Button)findViewById(R.id.b4);
        btn_b4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "LED4.");
                ShowMessage("LED4.");
                if (checkit) {
                    try {
                        UartSend("4#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //Up Button
        btn_up = (Button)findViewById(R.id.btnUp);
        btn_up.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Add Speed.");
                ShowMessage("Add Speed.");
                if (checkit) {
                    try {
                        AddSpeed();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //Down Button
        btn_down = (Button)findViewById(R.id.btnDown);
        btn_down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Reduce Speed.");
                ShowMessage("Reduce Speed.");
                if (checkit) {
                    try {
                        ReduceSpeed();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //Left Button
        btn_left = (Button)findViewById(R.id.btnLeft);
        btn_left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "LEFT.");
                ShowMessage("LEFT.");
                if (checkit) {
                    try {
                        UartSend("L#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //Right Button
        btn_left = (Button)findViewById(R.id.btnRight);
        btn_left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "RIGHT.");
                ShowMessage("RIGHT.");
                if (checkit) {
                    try {
                        UartSend("R#");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });

        //Send Button
        btn_send = (Button)findViewById(R.id.btnSend);
        btn_send.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Sending Message.");
                ShowMessage("Sending Message.");
                if (checkit) {
                    try {
                        SendIt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    ShowMessage("Unconnected.");
            }
        });
        /*******MatchLayout********/

        ReceiveMessageThread Refresh = new ReceiveMessageThread();
        if (checkit)
            Refresh.start();
        else {
            Log.e(TAG, "Unconnected.");
            ShowMessage("Unconnected.");
        }
    }

    class ReceiveMessageThread extends Thread{
        @Override
        public void run() {
            byte [] databuffer = new byte[100];
            int datalength = 0;
            char [] buffer0;
            String buffer1;
            do {
                try {
                    datalength = inStream.available();
                    inStream.read(databuffer, 0, datalength);
                    buffer0 = new char[datalength];
                    for (int i = 0; i<datalength; i++){
                        buffer0[i] = (char)databuffer[i];
                    }
                    buffer1 = new String(buffer0);
                    if (buffer1.length()>0) {
                        Message msg = new Message();
                        msg.obj = buffer1;
                        mHandler.sendMessage(msg);
                    }
                    Thread.sleep(500);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message;
            message = (String) msg.obj;
            message = message + "\n";
            text_talk.append(message);
        }
    };

    private Handler m2Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message;
            message = (String) msg.obj;
            text_info.setText(message);
        }
    };

    public void SendIt() throws IOException {
        String message = editText.getText().toString();
        UartSend(message);
    }

    public void AddSpeed() throws IOException {
        LEDspeed++;
        String message = String.valueOf(LEDspeed)+"#";
        UartSend(message);
    }

    public void ReduceSpeed() throws IOException {
        LEDspeed--;
        String message = String.valueOf(LEDspeed)+"#";
        UartSend(message);
    }

    public void UartSend(String message) throws IOException {
        outStream.write(message.getBytes(), 0, message.length());
    }

    public void ShowMessage(String message){
        Message msg = new Message();
        msg.obj = message;
        m2Handler.sendMessage(msg);
    }
}
