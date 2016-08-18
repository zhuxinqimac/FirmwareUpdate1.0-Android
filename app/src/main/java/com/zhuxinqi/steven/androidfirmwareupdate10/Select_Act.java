package com.zhuxinqi.steven.androidfirmwareupdate10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Steven on 6/2/16.
 */
public class Select_Act extends AppCompatActivity {
    /************Constants**************/
    public static final byte COMMAND_PING = 0x20;
    public static final byte COMMAND_DOWNLOAD = 0x21;
    public static final byte COMMAND_RUN = 0x22;
    public static final byte COMMAND_GET_STATUS = 0x23;
    public static final byte COMMAND_SEND_DATA = 0x24;
    public static final byte COMMAND_RESET = 0x25;

    public static final byte COMMAND_RET_SUCCESS = 0x40;
    public static final byte  COMMAND_RET_UNKNOWN_CMD = 0x41;
    public static final byte  COMMAND_RET_INVALID_CMD = 0x42;
    public static final byte  COMMAND_RET_INVALID_ADDR = 0x43;
    public static final byte COMMAND_RET_FLASH_FAIL = 0x44;
    public static final byte  COMMAND_ACK = (byte)0xCC;
    public static final byte  COMMAND_NAK = 0x33;
    /************Constants**************/

    /************GlobalVariables**************/

    /*******Resources********/
    public File sdcard = Environment.getExternalStorageDirectory();
    public File demo0 = new File(sdcard, "LEDfollow.bin");
    public File demo1 = new File(sdcard, "LEDbreath.bin");
    public File demo2 = new File(sdcard, "CalculatorDEMO.bin");

    public static InputStream hFile = null;
    public static byte [] g_pui8Buffer= new byte[256];
    public static byte [] pui8FileBuffer=null;
    public static int g_ui32DownloadAddress=0x4000;
    public static int g_ui32DataSize=8;
    public static int ui32FileLength=0;
    public static int ui32TransferStart=0;
    public static int ui32TransferLength=0;
    public static int ui32Offset=0;
    /************GlobalVariables**************/

    /************AndroidVariables&Constants**************/
    private static final String TAG = "In Select_Act";
    private TextView text_info = null;
    private Button btn_selectfile0 = null;
    private Button btn_selectfile1 = null;
    private Button btn_selectfile2 = null;
    private Button btn_beready = null;
    private String Ready = "READY#";
    //private String show_screen = null;
    private String showstring;
    private boolean ready_press;

    private OutputStream outStream = null;
    private InputStream inStream = null;
    private boolean checkit = false;
    /************AndroidVariables&Constants**************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_file_boot);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*******GetIntent********/
        Intent intent = getIntent();
        checkit = intent.getBooleanExtra(Main_Act.IfConnected, false);
        if (checkit){
            outStream = ((Global_Variable)getApplication()).GetOutStream();
            inStream = ((Global_Variable)getApplication()).GetInStream();
        }
        /*******GetIntent********/

        ready_press = false;

        /*******MatchLayout********/

        //Info View
        text_info = (TextView)findViewById(R.id.textSelect);

        //Ready Button
        btn_beready = (Button)findViewById(R.id.btnBeReady);
        btn_beready.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i(TAG, "Sending Command to Enter Load Mode.");
                try {
                    ShowMessage("Sending Command to Enter Load Mode.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkit) {
                    try {
                        Log.e(TAG, "Try Sending Ready.");
                        ShowMessage("Try Sending Ready.");
                        ready_press = true;
                        outStream.write(Ready.getBytes(), 0, Ready.length());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        ShowMessage("Unconnected.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        //Send Button0
        btn_selectfile0 = (Button)findViewById(R.id.btnSend0);
        btn_selectfile0.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i(TAG, "Reading file(LEDfollow.bin).");
                try {
                    ShowMessage("Reading file(LEDfollow.bin).");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkit) {
                    if (ready_press) {
                        try {
                            hFile = new FileInputStream(demo0);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        UpdateFlashThread UpdateIt = new UpdateFlashThread();
                        UpdateIt.start();
                        //DoUpdate();
                    }
                    else {
                        try {
                            ShowMessage("Press BE READY first.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    try {
                        ShowMessage("Unconnected.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        //Send Button1
        btn_selectfile1 = (Button)findViewById(R.id.btnSend1);
        btn_selectfile1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i(TAG, "Reading file(LEDbreath.bin).");
                try {
                    ShowMessage("Reading file(LEDbreath.bin).");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (checkit) {
                    if (ready_press) {
                        try {
                            hFile = new FileInputStream(demo1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        UpdateFlashThread UpdateIt = new UpdateFlashThread();
                        UpdateIt.start();
                        //DoUpdate();
                    }
                    else {
                        try {
                            ShowMessage("Please press BE READY first.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    try {
                        ShowMessage("Unconnected.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        //Send Button2
        btn_selectfile2 = (Button)findViewById(R.id.btnSend2);
        btn_selectfile2.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.i(TAG, "Reading file(CalculatorDEMO.bin).");
                try {
                    ShowMessage("Reading file(CalculatorDEMO.bin).");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (checkit) {
                    if (ready_press) {
                        try {
                            hFile = new FileInputStream(demo2);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        UpdateFlashThread UpdateIt = new UpdateFlashThread();
                        UpdateIt.start();
                        //DoUpdate();
                    }
                    else {
                        try {
                            ShowMessage("Please press BE READY first.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    try {
                        ShowMessage("Unconnected.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        /*******MatchLayout********/

    }

    class UpdateFlashThread extends Thread{
        @Override
        public void run() {
            try {
                DoUpdate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    public void DoUpdate() throws InterruptedException {

        try {
            ShowMessage("Start Updating.");
            Log.e(TAG, "Start Updating.");
            if (UpdateFlash()<0){
                Log.e(TAG, "UpdateFlash Failed.");
                ShowMessage("UpdateFlash Failed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        g_pui8Buffer[0] = COMMAND_RESET;
        SendPacket(g_pui8Buffer, (byte)1, false);
        Log.e(TAG, "Done Reset.");
        ShowMessage("Done Reset.");

        try {
            hFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
            }
        }

    }

    public static byte[] readFully(InputStream input, int ui32TransferLength) throws IOException
    {
        byte[] buffer = new byte[ui32TransferLength];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    public int UpdateFlash() throws IOException, InterruptedException {
        //Transfer Length
        ui32TransferLength = hFile.available();
        ui32TransferStart = g_ui32DownloadAddress;
        pui8FileBuffer = new byte[ui32TransferLength];
        int times = 20;
        if (pui8FileBuffer==null){
            Log.e(TAG, "Buffer new failed.");
            ShowMessage("Buffer new failed.");
            return(-1);
        }

        pui8FileBuffer = readFully(hFile, ui32TransferLength);

        //Download command
        g_pui8Buffer[0] = COMMAND_DOWNLOAD;
        g_pui8Buffer[1] = (byte)(ui32TransferStart >> 24);
        g_pui8Buffer[2] = (byte)(ui32TransferStart >> 16);
        g_pui8Buffer[3] = (byte)(ui32TransferStart >> 8);
        g_pui8Buffer[4] = (byte)ui32TransferStart;
        g_pui8Buffer[5] = (byte)(ui32TransferLength>>24);
        g_pui8Buffer[6] = (byte)(ui32TransferLength>>16);
        g_pui8Buffer[7] = (byte)(ui32TransferLength>>8);
        g_pui8Buffer[8] = (byte)ui32TransferLength;

        ShowMessage("Start Updating.");
        Log.e(TAG, "Start Updating.");

        if(SendCommand(g_pui8Buffer, (byte)9) < 0)
        {
            Log.e(TAG, "Failed to Send Download Command.");
            ShowMessage("Failed to Send Download Command.");
            return(-1);
        }

        //Download content
        ui32Offset = 0;
        int iter_times = 0;
        do
        {
            byte ui8BytesSent;

            g_pui8Buffer[0] = COMMAND_SEND_DATA;

            showstring = "Remaining Bytes: " + String.valueOf(ui32TransferLength);
            //ShowMessage(showstring);
            Log.e(TAG, showstring);

            if (iter_times>5) {
                iter_times = 0;
                ShowMessage(showstring);

            }
            else {
                iter_times++;
            }
            //
            // Send out 8 bytes at a time to throttle download rate and avoid
            // overrunning the device since it is programming flash on the fly.
            //
            if(ui32TransferLength >= g_ui32DataSize)
            {
                //memcpy(&g_pui8Buffer[1], &pui8FileBuffer[ui32Offset], g_ui32DataSize); //g_ui32DataSize==8
                for (byte i = 0; i<g_ui32DataSize; i++){
                    g_pui8Buffer[i+1] = pui8FileBuffer[ui32Offset+i];
                }
                ui32Offset += g_ui32DataSize;
                ui32TransferLength -= g_ui32DataSize;
                ui8BytesSent = (byte)(g_ui32DataSize + 1);
            }
            else
            {
                //memcpy(&g_pui8Buffer[1], &pui8FileBuffer[ui32Offset], ui32TransferLength);
                for (byte i = 0; i<ui32TransferLength; i++){
                    g_pui8Buffer[i+1] = pui8FileBuffer[ui32Offset+i];
                }
                ui32Offset += ui32TransferLength;
                ui8BytesSent = (byte)(ui32TransferLength + 1);
                ui32TransferLength = 0;
            }
            //
            // Send the Send Data command to the device.
            //
            if(SendCommand(g_pui8Buffer, ui8BytesSent) < 0)
            {
                ShowMessage("Failed to Send Packet data.");
                Log.e(TAG, "Failed to Send Packet data.");
                break;
            }
        } while(ui32TransferLength>0);

        ShowMessage("Done.");
        Log.e(TAG, "Done.");

        return 0;

    }

    public int SendCommand(byte [] pui8Command, byte ui8Size) throws InterruptedException {
        byte ui8Status;
        //
        // Send the command itself.
        //
        if(SendPacket(pui8Command, ui8Size, true) < 0)
        {
            ShowMessage("Failed to Send Status");
            Log.e(TAG, "Failed to Send Status");
            return(-1);
        }

        //
        // Send the get status command to tell the device to return status to
        // the host.
        //
        ui8Status = COMMAND_GET_STATUS;
        ui8Size = 1;
        byte [] argsSize= new byte[]{ui8Size};
        byte [] argsStatus= new byte[]{ui8Status};

        if(SendPacket(argsStatus, (byte)1, true) < 0)
        {
            ShowMessage("Failed to Get Status");
            Log.e(TAG, "Failed to Get Status");
            return(-1);
        }
        ui8Status = argsStatus[0];

        //
        // Read back the status provided from the device.
        //


        if(GetPacket(argsStatus, argsSize) < 0)
        {
            ShowMessage("Failed to Get Packet.");
            Log.e(TAG, "Failed to Get Packet.");
            return(-1);
        }
        ui8Status = argsStatus[0];
        ui8Size = argsSize[0];

        if(ui8Status != COMMAND_RET_SUCCESS)
        {
            ShowMessage("Failed to Get Download Command Return Code.");
            Log.e(TAG, "Failed to Get Download Command Return Code.");
            return(-1);
        }
        return 0;
    }

    public int NakPacket()
    {
        byte ui8Nak;
        ui8Nak = COMMAND_NAK;
        byte [] argsNak= new byte[]{ui8Nak};
        return(UARTSendData(argsNak, 1));
    }

    public int AckPacket()
    {
        byte ui8Ack;
        ui8Ack = COMMAND_ACK;
        byte [] argsAck= new byte[]{ui8Ack};
        return(UARTSendData(argsAck, 1));
    }

    public byte CheckSum(byte [] pui8Data, byte ui8Size)
    {
        int i;
        byte ui8CheckSum;
        ui8CheckSum = 0;

        for(i = 0; i < ui8Size; ++i)
        {
            ui8CheckSum += pui8Data[i];
        }
        return(ui8CheckSum);
    }

    public int GetPacket(byte [] pui8Data, byte [] pui8Size){
        byte ui8CheckSum=0;
        byte ui8Size=0;
        byte [] argsCheckSum= new byte[]{ui8CheckSum};
        byte [] argsSize= new byte[]{ui8Size};
        //
        // Get the size and the checksum.
        //
        do
        {
            if(UARTReceiveData(argsSize, 1)!=0)
            {
                return(-1);
            }
            ui8Size = argsSize[0];
        }
        while(ui8Size == 0);

        if(UARTReceiveData(argsCheckSum, 1)!=0)
        {
            return(-1);
        }
        ui8CheckSum = argsCheckSum[0];
        pui8Size[0] = (byte)(ui8Size - 2);

        if(UARTReceiveData(pui8Data, pui8Size[0])!=0)
        {
            pui8Size[0] = 0;
            return(-1);
        }

        //
        // Calculate the checksum from the data.
        //
        if(CheckSum(pui8Data, pui8Size[0]) != ui8CheckSum)
        {
            pui8Size[0] = 0;
            return(NakPacket());
        }

        return(AckPacket());
    }

    public int SendPacket(byte [] pui8Data, byte ui8Size, boolean bAck){
        byte ui8CheckSum;
        byte ui8Ack=0;

        ui8CheckSum = CheckSum(pui8Data, ui8Size);

        //
        // Make sure that we add the bytes for the size and checksum to the total.
        //
        ui8Size += 2;

        //
        // Send the Size in bytes.
        //
        byte [] argsSize= new byte[]{ui8Size};
        if(UARTSendData(argsSize, 1)!=0)
        {
            return(-1);
        }
        ui8Size = argsSize[0];

        //
        // Send the CheckSum
        //
        byte [] argsCheckSum= new byte[]{ui8CheckSum};
        if(UARTSendData(argsCheckSum, 1)!=0)
        {
            return(-1);
        }
        ui8CheckSum = argsCheckSum[0];

        //
        // Now send the remaining bytes out.
        //
        ui8Size -= 2;

        //
        // Send the Data
        //
        if(UARTSendData(pui8Data, ui8Size)!=0)
        {
            return(-1);
        }

        //
        // Return immediately if no ACK/NAK is expected.
        //
        if(!bAck)
        {
            return(0);
        }

        //
        // Wait for the acknowledge from the device.
        //
        byte [] argsAck= new byte[]{ui8Ack};
        do
        {
            if(UARTReceiveData(argsAck, 1)!=0)
            {
                return(-1);
            }
            ui8Ack = argsAck[0];
        }
        while(ui8Ack == 0);

        if(ui8Ack != COMMAND_ACK)
        {
            return(-1);
        }
        return(0);
    }

    public int UARTSendData(byte [] pui8Data, int ui8Size)
    {
        try {
            outStream.write(pui8Data, 0, ui8Size);
            outStream.flush();

        } catch (IOException e) {
            Log.e(TAG, "Last Step : Writing Error.");
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public int UARTReceiveData(byte [] pui8Data, int ui8Size)
    {
        try {
            inStream.read(pui8Data, 0, ui8Size);
        } catch (IOException e) {
            Log.e(TAG, "Last Step : Reading Error.");
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public void ShowMessage(String message) throws InterruptedException {
        Message msg = new Message();
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

}
