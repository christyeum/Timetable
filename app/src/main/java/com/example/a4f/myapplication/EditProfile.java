package com.example.a4f.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by jungyun on 02/06/2017.
 */

public class EditProfile extends AppCompatActivity {
    public String getName;
    public String getUsername;
    public String getPassword;
    public String getDept;
    public String getGrade;

    private static String TAG = "jsontest";
    private String username;
    private static final String TAG_JSON="user";
    private static final String TAG_NAME = "name";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD ="password";
    private static final String TAG_DEPT ="dept";
    private static final String TAG_GRADE ="grade";
    EditText name,password,userName,dept,grade,passwordConfirm;
    ImageView img;
    Button edit;
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.edit);
        setContentView(R.layout.profile);
        name=(EditText)findViewById(R.id.etName);
        userName=(EditText)findViewById(R.id.etId);
        password = (EditText) findViewById(R.id.etPW);
        passwordConfirm = (EditText)findViewById(R.id.etPW2);
        dept=(EditText)findViewById(R.id.etDept);
        grade=(EditText)findViewById(R.id.etGrade);
        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        //mlistView = (ListView) findViewById(R.id.listView_main_list);
        img = (ImageView)findViewById(R.id.imageView4);
        edit = (Button)findViewById(R.id.btnEdit);

        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().equals(passwordConfirm.getText().toString())) {
                    img.setVisibility(View.INVISIBLE);
                    edit.setEnabled(true);
                } else {

                    img.setVisibility(View.VISIBLE);
                    edit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        Intent i =getIntent();
        username=i.getStringExtra("username");
        try {
            task.execute("http://timetable.dothome.co.kr/getUser.php?username="+URLEncoder.encode(username,"UTF-8"));
        }catch (Exception e){

        }

    }


    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EditProfile.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String name = item.getString(TAG_NAME);
                String username = item.getString(TAG_USERNAME);
                String password = item.getString(TAG_PASSWORD);
                String dept = item.getString(TAG_DEPT);
                String grade = item.getString(TAG_GRADE);

                getName = name;
                getUsername = username;
                getPassword = password;
                getDept = dept;
                getGrade = grade;


/*
                HashMap<String,String> hashMap = new HashMap<>();

                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_USERNAME, username);
                hashMap.put(TAG_PASSWORD, password);
                hashMap.put(TAG_DEPT, dept);
                hashMap.put(TAG_GRADE, grade);

                mArrayList.add(hashMap);
*/
            }
/*
            ListAdapter adapter = new SimpleAdapter(
                    EditProfile.this, mArrayList, R.layout.editprofile,
                    new String[]{TAG_NAME,TAG_USERNAME, TAG_PASSWORD,TAG_DEPT,TAG_GRADE},
                    new int[]{R.id.textView_list_name, R.id.textView_list_username, R.id.textView_list_password, R.id.textView_list_dept ,R.id.textView_list_grade}
            );

            mlistView.setAdapter(adapter);
*/
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
        profileInfo();
    }


    private void profileInfo(){


        EditText eName = (EditText)findViewById(R.id.etName);
        eName.setHint(getName);
        EditText eID = (EditText)findViewById(R.id.etId);
        eID.setHint(getUsername);
        EditText ePW = (EditText)findViewById(R.id.etPW);
        ePW.setHint("");
        EditText ePW2 = (EditText)findViewById(R.id.etPW2);
        ePW2.setHint("");
        EditText eDept = (EditText)findViewById(R.id.etDept);
        eDept.setHint(getDept);
        EditText eGrade = (EditText)findViewById(R.id.etGrade);
        eGrade.setHint(getGrade);

    }
    public void onEdit(View view){

        String strName=getName;
        String strUN=getUsername;
        String strPW=getPassword;
        String strDept=getDept;
        String strgrade=getGrade;


        if(name.getText().length()!=0){strName=name.getText().toString();}
        if(userName.getText().length()!=0){strUN = userName.getText().toString();}
        if(password.getText().length()!=0){strPW = password.getText().toString();}
        if(dept.getText().length()!=0){strDept = dept.getText().toString();}
        if(grade.getText().length()!=0){strgrade = grade.getText().toString();}


        String type="edit";
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        backgroundWorker.execute(type,strName,strUN,strPW,strDept,strgrade);
        Intent i= new Intent(EditProfile.this,MyAccount.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();

    }

    public void onBackPressed() {
        Intent i = new Intent(EditProfile.this, NavActivity.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();

    }
    public void btnEditCancelClick(View view) {
        Intent i = new Intent(EditProfile.this, NavActivity.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();
    }

}