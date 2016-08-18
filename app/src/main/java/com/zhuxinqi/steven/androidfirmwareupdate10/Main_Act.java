package com.zhuxinqi.steven.androidfirmwareupdate10;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL;

public class Main_Act extends AppCompatActivity {


    /************AndroidVariables&Constants**************/
    public static final String IfConnected = "IfConnected";
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "20:15:09:15:20:73";
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private static final String TAG = "In Main_Act";
    private Button btn_connect = null;
    private Button btn_choose = null;
    private Button btn_talk = null;
    private Button btn_disconnect = null;
    private boolean checkit = false;
    private TextView text_info = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    /************AndroidVariables&Constants**************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*******MatchLayout********/
        //Info View
        text_info = (TextView)findViewById(R.id.textMain);

        btn_connect = (Button)findViewById(R.id.btnConnect);
        btn_connect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Connecting to 20:15:09:15:20:73.");
                ShowMessage("Connecting to 20:15:09:15:20:73.");
                ConnectThread ConnectIt = new ConnectThread();
                ConnectIt.start();
            }
        });

        btn_choose = (Button)findViewById(R.id.btnChooseFile);
        btn_choose.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Choosing.");
                ShowMessage("Choosing.");
                ChangeToChoose();
            }
        });

        btn_talk = (Button)findViewById(R.id.btnTalk);
        btn_talk.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Talking.");
                ShowMessage("Talking.");
                ChangeToTalk();
            }
        });

        btn_disconnect = (Button)findViewById(R.id.btnDisconnect);
        btn_disconnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "Disconnecting.");
                ShowMessage("Disconnecting.");
                DisconnectThread DisconnectSocket = new DisconnectThread();
                DisconnectSocket.start();
                //DisconnectIt();
            }
        });
    }

    class ConnectThread extends Thread{
        @Override
        public void run() {
            if (SetBluetoothAdapter()<0){
                Log.e(TAG, "Fail to set bluetooth adaper.");
                ShowMessage("Fail to set bluetooth adaper.");
            }
        }
    }

    class DisconnectThread extends Thread{
        @Override
        public void run() {
            DisconnectIt();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message;
            message = (String) msg.obj;
            text_info.setText(message);
        }
    };

    public void ChangeToChoose() {
        Intent intent = new Intent(this, Select_Act.class);
        intent.putExtra(IfConnected, checkit);
        startActivity(intent);
    }

    public void ChangeToTalk() {
        Intent intent = new Intent(this, Talk_Act.class);
        intent.putExtra(IfConnected, checkit);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void DisconnectIt(){
        if (checkit) {
            try {
                btSocket.close();
                checkit = false;
            } catch (IOException e2) {
                Log.e(TAG, "Unable to close socket.", e2);
                ShowMessage("Unable to close socket.");
            }
        }
        else
            ShowMessage("Nothing to disconnect.");
    }

    public int SetBluetoothAdapter(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth is not available.");
            ShowMessage("Bluetooth is not available.");
            return -1;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Please enable your Bluetooth.");
            ShowMessage("Please enable your Bluetooth.");
            return -1;
        }

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket creation failed.");
            ShowMessage("Socket creation failed.");
            return -1;
        }


        try {
            btSocket.connect();
            Log.e(TAG, "Bluetooth connection established, data transfer link open.");
            ShowMessage("Bluetooth connection established, data transfer link open.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "Unable to close socket during connection failure.", e2);
                ShowMessage("Unable to close socket during connection failure.");
            }
            return -1;
        }

        try {
            outStream = btSocket.getOutputStream();
            inStream = btSocket.getInputStream();
            checkit = true;
        } catch (IOException e) {
            Log.e(TAG, "Stream creation failed.", e);
            ShowMessage("Stream creation failed.");
        }


        ((Global_Variable)this.getApplication()).setOutStream(outStream);
        ((Global_Variable)this.getApplication()).setInStream(inStream);
        return 0;
    }

    public void ShowMessage(String message){
        Message msg = new Message();
        msg.obj = message;
        mHandler.sendMessage(msg);
    }
}