package com.fourtysixand2.sucratrend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Home extends Activity {

    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TelephonyManager telephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String phone = "+" + telephony.getLine1Number();
        myTextView = (TextView)findViewById(R.id.myTextView);
        new GetSucratrend().execute(phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public void showEntry(View view) {
        Intent intent = new Intent(this, Entry.class);
        startActivity(intent);
    }

    private class GetSucratrend extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... phone) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String url = "https://www.sucratrend.com/entries/fulljson/+16155563180";
            System.out.println(url);
            HttpGet httpGet = new HttpGet(url);
            StringBuilder entries_json = new StringBuilder();
            String line = null;

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                InputStream content = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                while ((line = buffer.readLine()) != null) {
                    entries_json.append(line);
                }
                return entries_json.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(String entries) {
            System.out.print("onPostExecute");
            System.out.print(entries);
            myTextView.setText(entries);
        }
    }
}


