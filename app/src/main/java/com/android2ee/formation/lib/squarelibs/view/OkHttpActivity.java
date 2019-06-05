package com.android2ee.formation.lib.squarelibs.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarActivity;
import android.util.Log;

import com.android2ee.formation.lib.squarelibs.R;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Doc:
 * https://github.com/square/okhttp
 * https://github.com/square/okhttp/wiki/Connections
 * http://www.101apps.co.za/index.php/articles/android-cloud-connections-using-the-okhttp-library.html
 * https://github.com/square/okhttp/wiki/Recipes
 */
public class OkHttpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
    }

    private class MyAsyncOkHttp extends AsyncTask<String,String,String>{

        /***************************************************************/
        /************                 The Client                 *******/
        /***************************************************************/
        OkHttpClient client;
        private  OkHttpClient getClient(){
            client = new OkHttpClient();
            //Assigning a CacheDirectory
            File myCacheDir=new File(getCacheDir(),"OkHttpCache");
            //you should create it...
            int cacheSize=1024*1024;
            Cache cacheDir=new Cache(myCacheDir,cacheSize);
            client.setCache(cacheDir);
            //now it's using the cach
            return client;
        }

        /***************************************************************/
        /************                        GET                 *******/
        /***************************************************************/
        String urlGet="http://jsonplaceholder.typicode.com/posts";
        private String getAStuff() throws IOException {
            Request request = new Request.Builder()
                    .url(urlGet)
                    .build();

            Response response = getClient().newCall(request).execute();
            String ret=response.body().string();
            //you should always close the body to enhance recycling mechanism
            response.body().close();
            return ret;
        }

        /***************************************************************/
        /************                        POST                 *******/
        /***************************************************************/
        String urlPost="http://jsonplaceholder.typicode.com/posts";
        String json="data: {\n" +
                "    title: 'foo',\n" +
                "    body: 'bar',\n" +
                "    userId: 1\n" +
                "  }";
        private final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

        private String postAStuff() throws IOException {
            OkHttpClient client = new OkHttpClient();

//            RequestBody body = RequestBody.create(JSON, file);
//            RequestBody body = RequestBody.create(JSON, string);
//            RequestBody body = RequestBody.create(JSON, byte[]);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(urlPost)
                    .post(body)
                    .build();
            Call postCall=getClient().newCall(request);
            Response response = postCall.execute();
            //or making it in the MainThread you use
//            postCall.enqueue(new Callback() {
//                                 @Override
//                                 public void onFailure(Request request, IOException e) {
//                                     //oh, shit, i failed
//                                 }
//
//                                 @Override
//                                 public void onResponse(Response response) throws IOException {
//                                    //yes, I succeed
//                                 }
//                             }
//
//            );
            //you have your response code
            int httpStatusCode=response.code();
            //your responce body
            String ret=response.body().string();
            //and a lot of others stuff...
            //you should always close the body to enhance recycling mechanism
            response.body().close();
            return ret;
        }
        /***************************************************************/
        /************                 GET    PICTURE             *******/
        /***************************************************************/
        String urlGetPicture="http://jsonplaceholder.typicode.com/photos/1";
        public void urlGetPicture() throws IOException {
            Request request = new Request.Builder()
                    .url(urlPost)
                    .build();
            Call postCall=getClient().newCall(request);
            Response response = postCall.execute();
            if(response.code()==200){
                ResponseBody in=response.body();
                InputStream is=in.byteStream();
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                is.close();
                response.body().close();
            }
            //now do a stuff, for exemple store it
        }



        /***************************************************************/
        /************                        Interceptors                 *******/
        /***************************************************************/
        public void addInterceptor(){
                getClient().interceptors().add(new LoggingInterceptor());
        }
        class LoggingInterceptor implements Interceptor {
            //Code pasted from okHttp webSite itself
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long t1 = System.nanoTime();
                Log.i("Interceptor Sample",String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                Log.i("Interceptor Sample",String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

                return response;
            }
        }
        /** This interceptor compresses the HTTP request body. Many webservers can't handle this! */
        final class GzipRequestInterceptor implements Interceptor {
            @Override public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                    return chain.proceed(originalRequest);
                }

                Request compressedRequest = originalRequest.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .method(originalRequest.method(), gzip(originalRequest.body()))
                        .build();
                return chain.proceed(compressedRequest);
            }

            private RequestBody gzip(final RequestBody body) {
                return new RequestBody() {
                    @Override public MediaType contentType() {
                        return body.contentType();
                    }

                    @Override public long contentLength() {
                        return -1; // We don't know the compressed length in advance!
                    }

                    @Override public void writeTo(BufferedSink sink) throws IOException {
                        BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                        body.writeTo(gzipSink);
                        gzipSink.close();
                    }
                };
            }
        }



        @Override
        protected String doInBackground(String... params) {
            try {
                //so do the post
                StringBuilder strB=new StringBuilder();
                strB.append(getAStuff());
                strB.append(postAStuff());
                return strB.toString();
            } catch (IOException e) {
               Log.e("OkHttpActivity","An error occurs ",e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
