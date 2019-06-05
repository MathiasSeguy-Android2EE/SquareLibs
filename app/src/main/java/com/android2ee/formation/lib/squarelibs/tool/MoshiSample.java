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

package com.android2ee.formation.lib.squarelibs.tool;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Mathias Seguy - Android2EE on 16/04/2015.
 * This explains Moshi which is a simple JSon writer/reader for streaming
 */
public class MoshiSample {

    /***************************************************************/
    /************                WriteJson            *******/
    /**************************************************************/
    /***
     * Write a Json : create file and write
     * @param ctx
     */
    public void writeJson(Context ctx) {
        //open
        File myFile = new File(ctx.getCacheDir(), "myJsonFile");
        try {
            //then write
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            BufferedSink okioBufferSink = Okio.buffer(Okio.sink(myFile));
            //do the Moshi stuff:
            JsonWriter jsonW = new JsonWriter(okioBufferSink);
            writeJson(jsonW);
            //don't forget to close, else nothing appears
            okioBufferSink.close();
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "Fuck FileNotFoundException occurs", e);
        } catch (IOException e) {
            Log.e("MainActivity", "Fuck IOException occurs", e);
        }
    }

    /**
     * Write Json in the JSonWriter gave as parameter
     * @param jsonW
     */
    private void writeJson(JsonWriter jsonW) {
        try {
            jsonW.beginObject();
            jsonW.name("Attribute1").value("Has a Value");
            jsonW.name("Attribute2").value(12);
            jsonW.name("Attribute3").value(true);
            jsonW.name("AttributeObject").value("AnotherObject")
                    .beginObject()
                    .name("Attribute1").value("Has a Value")
                    .name("Attribute2").value(12)
                    .name("Attribute3").value(true)
                    .endObject();
            jsonW.name("Array")
                    .beginArray()
                    .value("item1")
                    .value("item2")
                    .value("item3")
                    .endArray();
            jsonW.endObject();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /***************************************************************/
    /************                ReadJSon            *******/
    /**************************************************************/
    public void readJson() {

    }
}
