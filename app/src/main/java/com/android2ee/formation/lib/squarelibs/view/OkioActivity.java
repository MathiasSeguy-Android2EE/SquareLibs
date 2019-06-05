/**<ul>
 * <li>ForecastRestYahooSax</li>
 * <li>com.android2ee.formation.restservice.sax.forecastyahoo</li>
 * <li>28 mai 2014</li>
 *
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage but can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 *
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */

package com.android2ee.formation.lib.squarelibs.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.android2ee.formation.lib.squarelibs.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSink;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;

/**
 * Created by Mathias Seguy - Android2EE on 10/04/2015.
 */
public class OkioActivity extends ActionBarActivity {

    /***************************************************************/
    /*************         Attributes        ***************/
    /**
     * ***********************************************************
     */
    private TextView txvMain;
    private TextView txvCach;
    private TextView txvJakeWharton;
    /***************************************************************/
    /*************         Managing LifeCycle        ***************/
    /**
     * ***********************************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txvMain = (TextView) findViewById(R.id.txvResult);
        txvCach = (TextView) findViewById(R.id.txvCachFile);
        txvJakeWharton = (TextView) findViewById(R.id.txvJakeWharton);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readRawFile();
        writeCachFile();
        readCachFile();
        jakeSample();
        //And you can have more than that reading bytes by bytes it's just magic to read stream
        //or to create itv with this lib
    }

    /***************************************************************/
    /*************         Managing LifeCycle        ***************/
    /**
     * ***********************************************************
     */
    String str = null;

    /**
     * Read a Raw file using okio
     */
    private void readRawFile() {
        InputStream is = getResources().openRawResource(R.raw.intern_file);
        BufferedSource okioBufferSrce = Okio.buffer(Okio.source(is));
        try {
            str = okioBufferSrce.readUtf8();
        } catch (IOException e) {
            Log.e("MainActivity", "Fuck IOException occurs", e);
            str = "Fuck occurs, read the logs";
        } finally {
            txvMain.setText(str);
        }
        //And you can have more than that reading bytes by bytes it's just magic to read stream
    }

    /**
     * Write a file in my Cach
     */
    private void writeCachFile() {
        //open
        File myFile = new File(getCacheDir(), "myFile");
        try {
            //then write
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            BufferedSink okioBufferSink = Okio.buffer(Okio.sink(myFile));
            okioBufferSink.writeUtf8(str);
            //don't forget to close, else nothing appears
            okioBufferSink.close();
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "Fuck FileNotFoundException occurs", e);
        } catch (IOException e) {
            Log.e("MainActivity", "Fuck IOException occurs", e);
        }
    }

    /**
     * Read the file you just create
     */
    private void readCachFile() {
        //open
        File myFile = new File(getCacheDir(), "myFile");
        try {
            BufferedSource okioBufferSrce = Okio.buffer(Okio.source(myFile));
            str = okioBufferSrce.readUtf8();
            Log.e("MainActivity", "readCachFile returns" + str);
        } catch (IOException e) {
            Log.e("MainActivity", "Fuck FileNotFoundException occurs", e);
            str = "Fuck occurs, read the logs";
        } finally {
            txvCach.setText(str);
        }
    }

    private void jakeSample() {
//open
        File myFile = new File(getCacheDir(), "myJakeFile");
        try {
            //then write
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            Sink fileSink = Okio.sink(myFile);
            Sink gzipSink = new GzipSink(fileSink);
            BufferedSink okioBufferSink = Okio.buffer(gzipSink);
            okioBufferSink.writeUtf8(str);
            //don't forget to close, else nothing appears
            okioBufferSink.close();
            //then read
            myFile = new File(getCacheDir(), "myJakeFile");
            GzipSource gzipSrc = new GzipSource(Okio.source(myFile));
            BufferedSource okioBufferSrce = Okio.buffer(gzipSrc);
            //if you want to see the zip stream
//            BufferedSource okioBufferSrce=Okio.buffer(Okio.source(myFile));
            str = okioBufferSrce.readUtf8();
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "Fuck FileNotFoundException occurs", e);
        } catch (IOException e) {
            Log.e("MainActivity", "Fuck IOException occurs", e);
        } finally {
            txvJakeWharton.setText(str);
        }
    }
}
