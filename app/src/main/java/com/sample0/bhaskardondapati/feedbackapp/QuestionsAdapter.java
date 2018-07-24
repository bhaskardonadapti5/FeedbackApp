package com.sample0.bhaskardondapati.feedbackapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

    private List<Question> questionsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question_title, answer, genre;

        public MyViewHolder(View view) {
            super(view);
            question_title = (TextView) view.findViewById(R.id.question_title);
            answer = (TextView) view.findViewById(R.id.question_answer);
        }
    }

    public QuestionsAdapter(List<Question> questionList) {
        this.questionsList = questionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Question question = questionsList.get(position);
        holder.question_title.setText(question.getQuestion());
        if(question.isQuestionAnswered)
        {
            holder.answer.setVisibility(View.VISIBLE);
            holder.answer.setText(question.getUserInputForQuestion());
        }
        else
        {
            holder.answer.setVisibility(View.GONE);
            holder.answer.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }
}
