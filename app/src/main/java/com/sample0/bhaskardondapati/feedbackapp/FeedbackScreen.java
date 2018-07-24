package com.sample0.bhaskardondapati.feedbackapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FeedbackScreen extends AppCompatActivity implements AnswerUpdateInterface{


    String myURL = "http://www.json-generator.com/api/json/get/cfndYlFOsy?indent=2";
    ProgressDialog mProgress;
    private Gson gson;
    ArrayList<Question> mTotalQuestions;
    ArrayList<Question> updatedQuestionsList;
    private RecyclerView questionsRecyclerView;
    private RecyclerView answersRecyclerView;
    private QuestionsAdapter questionsAdapter;
    private AnswerAdapter answerAdapter;
    EditText mInputText;
    TextView mBtnOK;
    TextView messageText;
    LinearLayout bottomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_screen);

        mInputText = findViewById(R.id.input_val);
        mBtnOK = findViewById(R.id.btnok);
        questionsRecyclerView =  findViewById(R.id.question_recycler_view);
        answersRecyclerView =  findViewById(R.id.answer_recycler_view);
        bottomView = findViewById(R.id.bottom_view);
        messageText = findViewById(R.id.message);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        mTotalQuestions = new ArrayList<>();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Fetching Questions.. Please wait!!");
        mProgress.setCancelable(false);
        new FetchQuestionsData().execute();

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mInputText.getText().toString().length()!=0 && mInputText.getText().toString()!="")
                {
                    if(updatedQuestionsList.size()>0)
                    {
                        updatedQuestionsList.get(updatedQuestionsList.size()-1).setQuestionAnswered(true);
                        updatedQuestionsList.get(updatedQuestionsList.size()-1).setUserInputForQuestion(mInputText.getText().toString());
                        mInputText.setText("");
                        hideSoftKeyBoard();
                    }

                    if(mTotalQuestions.size()!=updatedQuestionsList.size())
                    updatedQuestionsList.add(mTotalQuestions.get(updatedQuestionsList.size()));

                    if(questionsAdapter!=null)
                        questionsAdapter.notifyDataSetChanged();
                    questionsRecyclerView.scrollToPosition(updatedQuestionsList.size() - 1);

                    updateAnswersView();
                }else
                {
                    Toast.makeText(FeedbackScreen.this, "Please Say Something!!" , Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private class FetchQuestionsData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mProgress!=null)
            mProgress.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                  URL url = new URL(myURL);
                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;
            } catch (IOException e) {
                Log.e("debug", "Error ", e);
                // If the code didn't successfully get the  data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("debug", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if(mProgress!=null)
                mProgress.dismiss();
            Log.i("json", data);
            QuestionsData questionsData = gson.fromJson(data, QuestionsData.class);
            if(questionsData!=null)
                mTotalQuestions = questionsData.getQuestions();
            updateWidgets();
        }
    }

    private void updateWidgets() {

        updatedQuestionsList = new ArrayList<>();
        updatedQuestionsList.add(mTotalQuestions.get(0));

        questionsAdapter = new QuestionsAdapter(updatedQuestionsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        questionsRecyclerView.setLayoutManager(mLayoutManager);
        questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        questionsRecyclerView.setAdapter(questionsAdapter);
        updateAnswersView();
    }

    private void updateAnswersView() {

        if(updatedQuestionsList.size()>0)
            if(updatedQuestionsList.get(updatedQuestionsList.size()-1).getAnswertype() !=0)
            {
                mBtnOK.setVisibility(View.GONE);
                mInputText.setClickable(false);
                mInputText.setEnabled(false);
                mInputText.setHint("Select any Option from above");
            }
            else
            {
                mBtnOK.setVisibility(View.VISIBLE);
                mInputText.setClickable(true);
                mInputText.setEnabled(true);
                mInputText.setHint("Say Something Here");
            }

        if(  updatedQuestionsList.size()>0 && updatedQuestionsList.get(updatedQuestionsList.size()-1).getAnswers().size() !=0 )
        {
            answerAdapter = new AnswerAdapter(updatedQuestionsList.get(updatedQuestionsList.size()-1).getAnswers() , FeedbackScreen.this);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            answersRecyclerView.setLayoutManager(mLayoutManager1);
            answersRecyclerView.setItemAnimator(new DefaultItemAnimator());
            answersRecyclerView.setAdapter(answerAdapter);
        }
        else
        {
            answersRecyclerView.setAdapter(null);
        }
    }

    @Override
    public void onAnswerUpdate(String answer) {

        if(updatedQuestionsList.size()>0)
        {
            updatedQuestionsList.get(updatedQuestionsList.size()-1).setQuestionAnswered(true);
            updatedQuestionsList.get(updatedQuestionsList.size()-1).setUserInputForQuestion(answer);
        }
        if(mTotalQuestions.size()!=updatedQuestionsList.size())
        {
            updatedQuestionsList.add(mTotalQuestions.get(updatedQuestionsList.size()));
            if(questionsAdapter!=null)
                questionsAdapter.notifyDataSetChanged();
            questionsRecyclerView.scrollToPosition(updatedQuestionsList.size() - 1);
            updateAnswersView();
        }else
        {
            answersRecyclerView.setAdapter(null);
            messageText.setVisibility(View.VISIBLE);
            mBtnOK.setVisibility(View.GONE);
            mInputText.setVisibility(View.GONE);
            mInputText.setClickable(false);
            mInputText.setEnabled(false);
        }

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}