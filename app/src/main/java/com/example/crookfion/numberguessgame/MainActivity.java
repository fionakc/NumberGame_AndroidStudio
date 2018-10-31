package com.example.crookfion.numberguessgame;
 //note: tilt functionality not fully implemented
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView pastTxt;
    //generating the numbers to use
    private int lowNum=0;
    private int highNum=9;
    private int guessNum;
    private int compNum;
    private int layoutNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadStartScreen();
    }

    public void loadStartScreen(){
        layoutNum=1;
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        Button btn = (Button) findViewById(R.id.startBtn);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loadGameScreen();

            }
        });

    }

    public void loadGameScreen(){
        layoutNum=2;
        setContentView(R.layout.game_screen);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        clearValues();
        //click guess, perform action based on input
        Button gbtn = (Button) findViewById(R.id.guessBtn);
        gbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                guessNum();

            }
        });

        //click restart, go back to start screen
        Button rbtn = (Button) findViewById(R.id.restartBtn);
        rbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
//                clearValues();
                loadStartScreen();
            }
        });

    }

    //screen when have won game
    public void loadWinScreen(){
        layoutNum=3;
//        Log.d("step1","step1");
        setContentView(R.layout.win_screen);
//        Log.d("step2","step2");

        //click restart, go back to start screen

        Button offsitebtn = (Button) findViewById(R.id.offsiteBtn);
        offsitebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
               String url = "http://www.reddit.com";
                Intent launchWeb = new Intent(Intent.ACTION_VIEW);
                launchWeb.setData(Uri.parse(url));
                startActivity(launchWeb);
            }
        });

        Button rbtn2 = (Button) findViewById(R.id.restartBtn2);
        rbtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
//                clearValues();
                loadStartScreen();
            }
        });


    }

    //clear out all the values before restarting game
    public void clearValues(){
        //pull past guesses, reset
        TextView txt = (TextView) findViewById(R.id.gameInfoTxt2);
        txt.setText(R.string.guess_a_number);

        TextView pasttxt = (TextView) findViewById(R.id.pastTxt);
        pasttxt.setText(R.string.past_guesses);

        guessNum=0;
        compNum=lowNum+(int)(Math.random()*((highNum-lowNum)+1));
    }

    //from eclipse
    public void guessNum() {
        //uses isInteger to check input data is a valid Integer
        EditText getGuess = (EditText) findViewById(R.id.guessTxt);
        String guessed=getGuess.getText().toString();
        boolean validNum=isInteger(guessed);
        TextView txt = (TextView) findViewById(R.id.gameInfoTxt2);
        guessNum++;
        if(!validNum || Integer.valueOf(guessed)>highNum || Integer.valueOf(guessed)<lowNum) {
            //grab label, reset text
            txt.setText(R.string.valid_number);
            addToPastGuesses(guessNum,guessed);
        } else {
            if(compNum > Integer.valueOf(guessed)) { 		//if compNum higher than guess
                txt.setText(R.string.too_low);
                addToPastGuesses(guessNum,guessed);
            } else if(compNum < Integer.valueOf(guessed)) { 	//if compNum lower than guess
                txt.setText(R.string.too_high);
                addToPastGuesses(guessNum,guessed);
            } else {
                loadWinScreen();
            }

        }	//end if valid num
        getGuess.getText().clear();
    } //end guessNum

    //from eclipse
    public boolean isInteger(String num) { //or could use parseInt with try/catch exceptions or regular expressions
        //if num is null
        if(num.equals(null)) {
            return false;
        }
        //if nothing entered
        if(num.isEmpty()) {
            return false;
        }
        //convert string to individual chars, if any char is not a number
        //also prevents negative numbers
        for(int i=0;i<num.length();i++) {
            char c = num.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    //from eclipse
    public void addToPastGuesses(int guessNum, String num) {
        TextView txt = (TextView) findViewById(R.id.pastTxt);
        String pastGuess=txt.getText().toString();
//        String name=getName.getText().toString();
//        //txt.setText("Hello "+name+"!");
//        txtfld.setText("Hello "+name+"!")


        if(guessNum==1) {
            txt.setText(pastGuess+" "+num);
        } else {
            txt.setText(pastGuess+","+num);
        }
    }

    //when saving
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("onSaveInst","Saving instance");
        super.onSaveInstanceState(outState);

        //based on layoutnum, save selectively
        //save pastguesses, infotext2, guessnum, compnum

        //if layoutNum=1 or 3, then just reload screen, don't need to specify
        outState.putInt("layoutNum",layoutNum);
        Log.d("layoutNumS",Integer.toString(layoutNum));
        if(layoutNum==2){   //save states
            TextView pasttext=(TextView) findViewById(R.id.pastTxt);
            String pastGuess=pasttext.getText().toString();
            outState.putString("PastGuesses",pastGuess);
            Log.d("pastGuessS",pastGuess);

            TextView infotext=(TextView) findViewById(R.id.gameInfoTxt2);
            String info2=infotext.getText().toString();
            outState.putString("InfoText2",info2);
            Log.d("infoTextS",info2);

            outState.putInt("GuessNum",guessNum);
            Log.d("guessNumS",Integer.toString(guessNum));
            outState.putInt("CompNum",compNum);
            Log.d("compNumS",Integer.toString(compNum));
        }


    }

    //when restoring
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        //based on layoutnum, pull out as appropriate
        //pull out past guesses and redraw
        //pull out infotext2 and redraw
        //pull out guessnum and resave
        //pull out compnum and resave

        super.onRestoreInstanceState(savedInstanceState);
        int layout=savedInstanceState.getInt("layoutNum");
        Log.d("layoutNumR",Integer.toString(layout));
        if(layout==2){
            setContentView(R.layout.game_screen);
            Log.d("layout","Game_screen");
            String pastGuess=savedInstanceState.getString("PastGuesses");
            TextView pasttext = (TextView) findViewById(R.id.pastTxt);
            pasttext.setText(pastGuess);
            Log.d("pastGuessR",pastGuess);

            String infotext=savedInstanceState.getString("InfoText2");
            TextView info2=(TextView) findViewById(R.id.gameInfoTxt2);
            info2.setText(infotext);
            Log.d("infoTextR",infotext);

            int guessN=savedInstanceState.getInt("GuessNum");
            guessNum=guessN;
            Log.d("guessNumR",Integer.toString(guessN));

            int compN=savedInstanceState.getInt("CompNum");
            compNum=compN;
            Log.d("compNumR",Integer.toString(compN));

        }else if (layout==3){
            setContentView(R.layout.win_screen);
            Log.d("layout","Win_screen");
        }else { //if not game or win screen, go to start screen
            setContentView(R.layout.activity_main);
            Log.d("layout","Start_screen");
        }




    }
}
