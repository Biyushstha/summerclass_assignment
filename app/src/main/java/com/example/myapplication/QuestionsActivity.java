package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView question,noIndicator;
    private LinearLayout optionContainer;
    private Button nextBtn;
    private int count =0;
    private List<QuestionModel> list;
    private int position =0;
    private int score =0;
    private String category;
    private int setNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        question = findViewById(R.id.question);
        noIndicator= findViewById(R.id.no_indicator);
        optionContainer=findViewById(R.id.options_container);
        nextBtn = findViewById(R.id.next_btn);

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",1);



        list = new ArrayList<>();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getValue(QuestionModel.class));
                }
                    if (list.size() > 0) {

                        for (int i = 0; i < 4; i++) {
                            optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkAnswer((Button) v);
                                }
                            });
                        }
                        playAnim(question, 0, list.get(position).getQuestion());
                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                nextBtn.setEnabled(false);
                                nextBtn.setAlpha(0.7f);
                                enableOption(true);
                                position++;
                                if (position == list.size()) {
                                    Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                    scoreIntent.putExtra("score",score);
                                    scoreIntent.putExtra("total",list.size());
                                    startActivity(scoreIntent);
                                    finish();
                                    return;
                                }
                                count = 0;
                                playAnim(question, 0, list.get(position).getQuestion());
                            }
                        });
                    } else {
                        finish();
                        Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (value == 0 && count < 4){
                    String option="";
                    if (count == 0){
                        option = list.get(position).getOptionA();

                    }else if (count ==1){
                        option = list.get(position).getOptionB();

                    }
                    else if (count ==2){
                        option = list.get(position).getOptionC();

                    }
                    else if (count ==3){
                        option = list.get(position).getOptionD();

                    }
                    playAnim(optionContainer.getChildAt(count),0,option);
                    count++;
                }

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (value == 0){
                    try {
                        ((TextView)view).setText(data);
                        noIndicator.setText(position+1+"/"+list.size());

                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1, data);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private void checkAnswer( Button enableOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if (enableOption.getText().toString().equals(list.get(position).getCorrectANS())){
            score++;
            enableOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));

        }else {
            enableOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            Button correctOption =(Button) optionContainer.findViewWithTag(list.get(position).getCorrectANS());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));

        }

    }
    private  void enableOption(boolean enable){
        for (int i = 0; i < 4; i++){
            optionContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB6C1")));
            }
            }
        }

    }
