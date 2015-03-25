package com.jmrandombitz.librarygenesis;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {
    String currentUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        final EditText searchTerm = (EditText) findViewById(R.id.et_searchTerm);
        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("MyAPp", searchTerm.getText().toString());
                new Search().execute(currentUrl, searchTerm.getText().toString().replace(" ", "+"));
            }
        });
        ArrayList<String> mirrors = new ArrayList<String>(Arrays.asList("http://www.bookzz.org", "http://libgen.in"));
        currentUrl = mirrors.get(0);
        new URLReachable().execute(currentUrl);
        //String first = mirrors.get(0);
        //tv.setText(first);
    }
    private class URLReachable extends AsyncTask<String, Void, Boolean> {
        TextView tv = (TextView) findViewById(R.id.textView);
        @Override
        protected Boolean doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                Log.w("myApp", "works 1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                Log.w("myApp", "works 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
            int code = 0;
            try {
                code = connection.getResponseCode();
                Log.w("myApp", "works 3");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.w("myApp", Integer.toString(code));
            if (code == 200) {
                return true;
            }
            else {
                return false;
            }
        }
        protected void onPostExecute(Boolean result) {

            if (result) tv.setText("Works!!");
            else tv.setText("Nope");

        }
    }
    private class Search extends AsyncTask<String, Void, Integer> {
        ArrayList titleList = new ArrayList();
        ArrayList dlLinksList = new ArrayList();
        ArrayList<List<String>> together = new ArrayList<>();
        ArrayList<String> arrObjInner1= new ArrayList<>();
        @Override
        protected Integer doInBackground(String... params) {
            //index 0: currentUrl, index 1: search query
            try {
                Document doc = Jsoup.connect(params[0]+"/s/?q="+params[1]+"&t=0").get();
                Elements titles = doc.select("h3[itemprop=name]");
                for(Element title : titles) {
                    titleList.add(title.ownText());
                }
/*                Elements dlLinks = doc.select("a.ddownload");
                for (Element dlLink : dlLinks){
                    dlLinksList.add(dlLink.ownText());
                }*/
                Elements divs = doc.select(".actionsHolder");
                for (Element elem : divs) {
                    String divHtml = elem.html().replaceAll("\\s", "");
                    Log.v("My", divHtml);
                    Pattern pattern = Pattern.compile("http://bookzz.org/dl/\\w+/+\\w+");
                    Matcher matcher = pattern.matcher(divHtml);
                    if (matcher.find()) {
                        dlLinksList.add(matcher.group(0));
                    }
                    else if(!matcher.find()){
                        Pattern alt = Pattern.compile("Linkdeletedbylegalowner|");
                        Matcher matcher2 = alt.matcher(divHtml);
                        if (matcher2.find()) {
                            Log.v("MyApp", "Gone");
                            dlLinksList.add("Link deleted by legal owner");
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.v("Error", e.toString());
                e.printStackTrace();
            }
            for (int i = 0; i<titleList.size() && i < 50; i++){
                arrObjInner1 = new ArrayList<>();
                arrObjInner1.add(titleList.get(i).toString());
                arrObjInner1.add(dlLinksList.get(i).toString());
                together.add(arrObjInner1);
            }
            return 1;
        }
        protected void onPostExecute(Integer result) {
            ArrayList<Book> tg = Book.getBookInfo(together);
            MyAdapter theAdapter = new MyAdapter(getApplicationContext(), tg);
            ListView theListView = (ListView) findViewById(R.id.search_results);
            theListView.setAdapter(theAdapter);
            Log.v("After", "Done");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
