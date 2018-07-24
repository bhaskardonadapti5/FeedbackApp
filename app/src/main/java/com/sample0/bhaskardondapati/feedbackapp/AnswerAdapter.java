package com.sample0.bhaskardondapati.feedbackapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {

    AnswerUpdateInterface mListner;
    private List<String> answersList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView answer;

        public MyViewHolder(View view) {
            super(view);
            answer = (TextView) view.findViewById(R.id.answer);
        }
    }
    public AnswerAdapter(List<String> answers , AnswerUpdateInterface listener) {
        this.answersList = answers;
        this.mListner = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       String answer = answersList.get(position);
        holder.answer.setText(answer);
        holder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListner.onAnswerUpdate(answersList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }
}
