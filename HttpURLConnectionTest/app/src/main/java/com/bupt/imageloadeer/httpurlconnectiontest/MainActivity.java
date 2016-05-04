package com.bupt.imageloadeer.httpurlconnectiontest;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
//                        httpUrlConnection("admin", "123456");
                        httpUrlConnGet("admin", "123456");
                        Looper.loop();
                    }
                });
                thread.start();
            }
        });

    }

    public void httpUrlConnection(String name, String password) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL("http://10.129.233.57:8080/Test/WelcomeUserServlet");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);//设置超时时间为3秒
            connection.setUseCaches(false);//不使用缓存
            connection.setInstanceFollowRedirects(true);//设置连接可以重定向
            connection.setDoInput(true);//设置这个连接可以输入数据
            connection.setDoOutput(true);//设置这个连接可以输出数据
            connection.setRequestMethod("POST");//设置请求的方法
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息类型
            connection.connect();//建立连接
            JSONObject json = new JSONObject();
            json.put("name", URLEncoder.encode(name, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));
            String jsonStr = json.toString();
            //使用字节流发送数据
//            OutputStream os = connection.getOutputStream();
//            BufferedOutputStream bos = new BufferedOutputStream(os);//缓冲字节流包装字节流
//            byte[] buffer = jsonStr.getBytes("UTF-8");//将字符串转化为字节数组
//            bos.write(buffer);
//            bos.flush();
//            bos.close();
//            os.close();

            //使用字符流发送数据
            OutputStream os2 = connection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os2));//创建字符流对象，并用缓冲流包装它，使得读写效率更高
            bw.write(jsonStr);
            bw.flush();
            os2.close();
            bw.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //字节流读取服务端返回的数据
//                InputStream  is = connection.getInputStream();
//                BufferedInputStream bis = new BufferedInputStream(is);
//                byte[] buffer = new byte[1024];
//                int len = -1;
//                StringBuffer sb = new StringBuffer();
//                while((len=bis.read(buffer))!=-1){
//                    sb.append(new String(buffer,0,len));
//                }
//
//                is.close();
//                bis.close();

                //使用字符流读取服务端返回的数据
                InputStream is2 = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is2));
                String str = null;
                StringBuffer sb2 = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    sb2.append(str);
                }
                is2.close();
                br.close();

                JSONObject newJson = new JSONObject(sb2.toString());

                boolean result = newJson.getBoolean("json");//从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                if (result) {//判断结果是否正确
                    Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
        } finally {
            connection.disconnect();//使用完关闭TCP连接，释放资源
        }
    }

    public void httpUrlConnGet(String name, String password) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            String urlStr = "http://10.129.233.57:8080/Test/WelcomeUserServlet?name="+name+"&password="+password;
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                in.close();
                br.close();
                JSONObject newJson = new JSONObject(buffer.toString());

                boolean result = newJson.getBoolean("json");//从rjson对象中得到key值为"json"的数据，这里服务端返回的是一个boolean类型的数据
                if (result) {//判断结果是否正确
                    Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
        } finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
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
