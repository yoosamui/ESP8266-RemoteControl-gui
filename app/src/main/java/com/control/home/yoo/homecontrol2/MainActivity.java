package com.control.home.yoo.homecontrol2;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
//import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

////http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
public class MainActivity extends AppCompatActivity
{
    // XML node keys
    static final String KEY_SONG = "target"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_STATE = "state";

    static final String KEY_THUMB_URL = "thumb_url";

    static final String KEY_COMMAND = "command";




    ListView list;
   // ListView listView;
    LazyAdapter adapter;

   // final List<Target> targetList = new ArrayList<Target>();


    final MainActivity context = this;
    boolean finishSet = false;
    boolean finishCommandSet = true;
    NodeList nl = null;

    final ArrayList<HashMap<String,String>> targetsList =
            new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);


        new Thread(){
            public void run(){

                try {

                    //URL url = new URL("http://192.168.78.160/lamp1/on");
                    URL url = new URL("http://192.168.78.160/xml/on");

                    //URL url = new URL("https://www.linuxmint.com/start/rafaela/");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    //    InputStream in;
                    // this opens a connection, then sends GET & headers
                    //   in = urlConnection.getInputStream();


                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Accept", "text/xml");
                    //urlConnection.setRequestProperty("Accept", "text/html");


                    int statusCode = urlConnection.getResponseCode();

                    Document doc = null;

                    InputStream xml = urlConnection.getInputStream();

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = null;
                    db = dbf.newDocumentBuilder();

                    if (statusCode == 200) {




                      //  doc = db.parse(xml);


                        InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                        InputStreamReader read = new InputStreamReader(it);
                        BufferedReader buff = new BufferedReader(read);
                        StringBuilder dta = new StringBuilder();
                        String chunks;
                        while ((chunks = buff.readLine()) != null) {
                            dta.append(chunks);
                        }

                        String sxml = "<targets>"+ dta.toString();
                        InputSource is = new InputSource();
                        is.setCharacterStream(new StringReader(sxml));
                        doc = db.parse(is);

                        nl = doc.getElementsByTagName(KEY_SONG);

                        /*
                        String[] parts = dta.toString().split(Pattern.quote("<br>")); // Split on period.
                        for (int i = 0; i < parts.length; i++) {
                            String[] parts2 = parts[i].split(Pattern.quote("=")); // Split on period.

                            if (parts2[0].indexOf("<") == -1) {
                                Target target = new Target();
                                target.Title = parts2[0];

                                String[] parts3 = parts2[1].split(Pattern.quote(",")); // Split on period.
                                target.SubTitle = parts3[0];
                                target.Command = parts3[0];


                                final boolean r = targetList.add(target);
                            }

                        }
                        */

                    } else {

                        //Handle else
                    }

                }catch (ParserConfigurationException e) {
                    Log.e("Error: ", e.getMessage());
                    return;

                } catch (SAXException e1) {
                        Log.e("Error: ", e1.getMessage());
                        return ;
                } catch (IOException e2) {
                    //swallow
                    Log.e("Error: ", e2.getMessage());
                    return ;

                } catch (Exception ex) {
                    Log.e("Error: ", ex.getMessage());
                    return;
                }
                finally
                {
                    finishSet = true;
                }
            }
        }.start();



        while( !finishSet)
        {
            ;
        }


        // looping through all song nodes &lt;song&gt;
        if(nl == null)
            return;

        for (int i = 0; i < nl.getLength(); i++)
        {
            HashMap<String,String> maprecord = new HashMap<String,String>();
            Element e = (Element) nl.item(i);

            maprecord.put(KEY_ID, this.getValue(e, KEY_ID));
            maprecord.put(KEY_TITLE, this.getValue(e, KEY_TITLE));
            maprecord.put(KEY_DURATION, this.getValue(e, KEY_STATE));
            maprecord.put(KEY_COMMAND, this.getValue(e, KEY_COMMAND));
            maprecord.put(KEY_ARTIST, this.getValue(e, "subtitle"));
            maprecord.put(KEY_THUMB_URL, this.getValue(e, "KEY_THUMB_URL"));




            targetsList.add(maprecord);
        }

        /*
        // LOOP
        for (Target t : targetList)
        {
            HashMap<String,String> maprecord = new HashMap<String,String>();
            maprecord.put(KEY_ID,"id");
            maprecord.put(KEY_TITLE,t.Title);
            maprecord.put(KEY_ARTIST,t.SubTitle);
            maprecord.put(KEY_DURATION,"duration");
            maprecord.put(KEY_THUMB_URL,"thumb_url");

            targetsList.add(maprecord);
        }
        // LOPEND
        */

        list=(ListView)findViewById(R.id.list);

        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, targetsList);
        list.setAdapter(adapter);

       // HashMap<String,String> maprecord2 = targetsList.get(1);
      //  String tt = maprecord2.get("title");

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id){

                // ListView Clicked item index
                final int itemPosition = position;

                // ListView Clicked item value
               // String itemValue = (String) list.getItemAtPosition(position);
               // Target targetItem = (Target) list.getItemAtPosition(position);
               // HashMap<String,String> targetItem = (HashMap<String,String>)list.getItemAtPosition(position);

                final HashMap<String,String> maprecord;
                maprecord = targetsList.get(position);

                String title = maprecord.get(KEY_TITLE);

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Turn ON or Off");

                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText(title);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonON);
                Button dialogButtonOff = (Button) dialog.findViewById(R.id.dialogButtonOFF);
                Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);

                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        sendRequest(maprecord,true);
                        maprecord.put(KEY_DURATION,"On");
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialogButtonOff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        sendRequest(maprecord,false);

                        maprecord.put(KEY_DURATION,"Off");
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


    }


    private void sendRequest(final HashMap<String,String> record, boolean on)
    {

        if(!finishCommandSet)
            return;

        final String command = String.format("http://192.168.78.160%s/%s",
                record.get(KEY_COMMAND),
                on?"on":"off" );


        new Thread(){
            public void run(){
                finishCommandSet = false;

                try {
                    URL url = new URL(command);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    int statusCode = urlConnection.getResponseCode();



                } catch (IOException e) {

                }
                finally
                {
                    finishCommandSet = true;
                }
            }
        }.start();
    }

    /** Getting node value
     * @param elem element
     */
    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
    /**
     * Getting node value
     * @param Element node
     * @param key string
     * */
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
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
