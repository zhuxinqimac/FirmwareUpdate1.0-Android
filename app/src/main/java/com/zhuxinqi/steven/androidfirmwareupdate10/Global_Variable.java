package com.zhuxinqi.steven.androidfirmwareupdate10;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Steven on 6/2/16.
 */
public class Global_Variable extends Application {
    private OutputStream outStream;
    private InputStream inStream;

    public OutputStream GetOutStream() { return outStream; }

    public InputStream GetInStream() {
        return inStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    public void setInStream(InputStream inStream) {
        this.inStream = inStream;
    }
}
