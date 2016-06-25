package com.codingblocks.hangmangame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private String currWord = "";
    private String[] array;
    private Random random = new Random();
    private LinearLayout wordContainer;

    private int numParts = 6;
    private ImageView[] bodyParts = new ImageView[numParts];

    private TextView[] charViews;

    private int currPart;
    private int numChars;
    private int numCorr;

    private LinearLayout keyboard;
    private String keys="QWERTYUIOPASDFGHJKLZXCVBNM";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //set up Keyboard
        keyboard=(LinearLayout)findViewById(R.id.keyboard);
        int index=0;

        LinearLayout ll_1,ll_2,ll_3;
        ll_1=createLinearLayoutForKeyboard();
        ll_2=createLinearLayoutForKeyboard();
        ll_3=createLinearLayoutForKeyboard();

        for(int i=0;i<10;i++){
            char c=keys.charAt(index++);
            Button b=createButtonForKeyboard(c);
            ll_1.addView(b);
        }
        keyboard.addView(ll_1);

        for(int i=0;i<9;i++){
            char c=keys.charAt(index++);
            Button b=createButtonForKeyboard(c);
            ll_2.addView(b);
        }
        keyboard.addView(ll_2);

        for(int i=0;i<7;i++){
            char c=keys.charAt(index++);
            Button b=createButtonForKeyboard(c);
            ll_3.addView(b);
        }
        keyboard.addView(ll_3);

        //initialise Body Parts
        initialiseParts();

        //initailise the array and the word container
        Intent i=getIntent();
        TextView categoryTextView=(TextView) findViewById(R.id.category);

        Bundle b=i.getExtras();
        if(b!=null) {

            String category=b.getString("category");
            categoryTextView.setText(category);
            if (category.equals("capitals"))
                array = getResources().getStringArray(R.array.capitals);
            else if (category.equals("cars"))
                array = getResources().getStringArray(R.array.cars);
            else if (category.equals("computer_parts"))
                array = getResources().getStringArray(R.array.computer_parts);
            else if (category.equals("fruits"))
                array = getResources().getStringArray(R.array.fruits);
            else if (category.equals("states"))
                array = getResources().getStringArray(R.array.states);

        }
        wordContainer = (LinearLayout) findViewById(R.id.wordContainer);

        playGame();

    }

    private LinearLayout createLinearLayoutForKeyboard(){
        LinearLayout linearLayout=new LinearLayout(this);
        LinearLayout.LayoutParams linearlayout_lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
        linearLayout.setLayoutParams(linearlayout_lp);

        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;

    }
    private Button createButtonForKeyboard(char c){

        LinearLayout.LayoutParams button_lp=new LinearLayout.LayoutParams(45, LinearLayout.LayoutParams.MATCH_PARENT);
        button_lp.setMargins(1, 2, 1, 2);

        Button b=new Button(this);
        b.setPadding(0, 0, 0, 0);
        b.setLayoutParams(button_lp);
        b.setText(String.valueOf(c));
        b.setOnClickListener(this);

        return b;
    }

    private void initialiseParts() {

        bodyParts[0] = (ImageView) findViewById(R.id.bodyPart1);
        bodyParts[1] = (ImageView) findViewById(R.id.bodyPart2);
        bodyParts[2] = (ImageView) findViewById(R.id.bodyPart3);
        bodyParts[3] = (ImageView) findViewById(R.id.bodyPart4);
        bodyParts[4] = (ImageView) findViewById(R.id.bodyPart5);
        bodyParts[5] = (ImageView) findViewById(R.id.bodyPart6);

    }

    public void playGame() {

        //set body parts back to invisible
        for (int p = 0; p < 6; p++)
            bodyParts[p].setVisibility(View.INVISIBLE);
        currPart = 0;

        //remove all the textviews from the wordContainer
        wordContainer.removeAllViews();

        //set back all alphabets
        int childCount=keyboard.getChildCount();

        for(int i=0;i<childCount;i++){
            ViewGroup v= (ViewGroup) keyboard.getChildAt(i);
            int buttonCount=v.getChildCount();

            for(int j=0;j<buttonCount;j++) {
                View v1= v.getChildAt(j);
                v1.setBackgroundResource(R.drawable.alphabet_down);
            }
        }

        //generate new word and set it as currWord
        String newWord = array[random.nextInt(array.length)];
        while (newWord.equals(currWord)) {
            newWord = array[random.nextInt(array.length)];
        }
        currWord = newWord;
        Log.i("Hangman", currWord);

        //Make char views for the new word and insert it in the container
        charViews = new TextView[currWord.length()];

        for (int i = 0; i < currWord.length(); i++) {
            char c = currWord.charAt(i);
            Log.i("Hangman", c + "");

            charViews[i] = new TextView(this);
            charViews[i].setText(String.valueOf(c));
            charViews[i].setTextColor(Color.WHITE);
            charViews[i].setGravity(Gravity.CENTER);
            charViews[i].setBackgroundResource(R.drawable.letter_bg);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50, 50);
            lp.setMargins(2, 2, 2, 2);

            charViews[i].setLayoutParams(lp);
            wordContainer.addView(charViews[i]);
        }

        //initialise numChars in word ,and numCorr guesses till now,numChances
        numChars = currWord.length();
        numCorr=0;


    }
    @Override
    public void onClick(View v) {
        String ltr = ((TextView) v).getText().toString();
        char ch = ltr.charAt(0);

        Log.i("Hangman",ch+"");
        Boolean correctGuess = false;   //to determine whether it is a correct guess or not
        v.setBackgroundResource(R.drawable.alphabet_up);

        for (int i = 0; i < currWord.length(); i++) {
            Log.i("Hangman",currWord.charAt(i)+"");

            if (currWord.charAt(i) == ch) {

                charViews[i].setTextColor(Color.BLACK);
                numCorr++;
                correctGuess = true;
            }
        }

        if(correctGuess==false){
            bodyParts[currPart].setVisibility(View.VISIBLE);
            currPart++;
        }

        if(numCorr==numChars){
            //user won
            Log.i("Hangman", "user won");
            AlertDialog.Builder winDialog=new AlertDialog.Builder(this);
            winDialog.setMessage("Congratulations!! You guessed the right word!");
            winDialog.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    playGame();
                }
            });
            winDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            winDialog.show();

            return;
        }

        if(currPart>=6){
            //game end user lost
            Log.i("Hangman", "user lost");
            AlertDialog.Builder loseDialog=new AlertDialog.Builder(this);
            loseDialog.setMessage("Oops!! You couldn't guess the right word \nThe answer is " + currWord);
            loseDialog.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    playGame();
                }
            });
            loseDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            loseDialog.show();
        }
    }
}