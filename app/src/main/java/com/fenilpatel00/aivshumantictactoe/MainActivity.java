package com.fenilpatel00.aivshumantictactoe;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private String TAG = "MainActivity";

    private Button[][] boardCells = new Button[3][3];
    private int[][] board = new int[3][3];
    private TextView results;

    private Button restart;

    public static final int NO_PLAYER = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;
    public Point computerMove;
    public int movesDone = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardCells[0][0] = findViewById(R.id.button0);
        boardCells[0][1] = findViewById(R.id.button1);
        boardCells[0][2] = findViewById(R.id.button2);
        boardCells[1][0] = findViewById(R.id.button3);
        boardCells[1][1] = findViewById(R.id.button4);
        boardCells[1][2] = findViewById(R.id.button5);
        boardCells[2][0] = findViewById(R.id.button6);
        boardCells[2][1] = findViewById(R.id.button7);
        boardCells[2][2] = findViewById(R.id.button8);

        results = findViewById(R.id.results);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = 0;
                boardCells[i][j].setText("");
            }
        }

        restart = findViewById(R.id.button_restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });

        boardCells[0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isGameOver())
                    move(0,0);
            }
        });

        boardCells[0][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(0,1);
            }
        });

        boardCells[0][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(0,2);
            }
        });

        boardCells[1][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(1,0);
            }
        });

        boardCells[1][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(1,1);
            }
        });

        boardCells[1][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(1,2);
            }
        });

        boardCells[2][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(2,0);
            }
        });

        boardCells[2][1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(2,1);
            }
        });

        boardCells[2][2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGameOver())
                    move(2,2);
            }
        });
    }

    private void move(int x, int y){

        Point userMove = new Point(x,y);
        boolean moveOk = true;

        if(movesDone < 9){
            moveOk = PlaceAMove(userMove,PLAYER_O);

            if(!moveOk){
                Toast.makeText(MainActivity.this, "Cell already filled, choose another", Toast.LENGTH_LONG).show();
            }
            else{
                movesDone++;

                if(movesDone < 9){

                minimax(0,PLAYER_X);
                PlaceAMove(computerMove, PLAYER_X);
                movesDone++;
            }

                if(hasPlayerWon(PLAYER_X)){

                    findTiles(PLAYER_X);
                    results.setText("YOU LOST!");
                    results.setVisibility(View.VISIBLE);
                    results.setTextColor(getResources().getColor(R.color.red));
                }
                else if(hasPlayerWon(PLAYER_O)){

                    findTiles(PLAYER_O);
                    results.setText("YOU WON!");
                    results.setVisibility(View.VISIBLE);
                    results.setTextColor(getResources().getColor(R.color.green));
                }
            }
        }
        if(movesDone == 9){
            results.setText("TIE!");
            results.setVisibility(View.VISIBLE);
            results.setTextColor(getResources().getColor(R.color.yellow));

            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    boardCells[i][j].setTextColor(getResources().getColor(R.color.red));
                }
            }
        }
    }

    private int minimax(int depth, int turn){

        if(hasPlayerWon(PLAYER_X)){
            return 1;
        }
        else if (hasPlayerWon(PLAYER_O)){
            return -1;
        }

        List<Point> availableCells = getAvailableCells();

        if(availableCells.isEmpty())
            return 0;

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for(int i = 0; i < availableCells.size(); i++){
            Point point = availableCells.get(i);

            if(turn == PLAYER_X){
                PlaceAMove(point, PLAYER_X);
                int currentScore = minimax(depth + 1, PLAYER_O);
                max = Math.max(currentScore, max);

                if(currentScore >= 0)
                    if(depth == 0)
                        computerMove = point;

                if(currentScore == 1){
                    board[point.x][point.y] = NO_PLAYER;

                    boardCells[point.x][point.y].setText("");
                    break;
                }

                if(i == availableCells.size() - 1 && max < 0)
                    if(depth == 0)
                        computerMove = point;
            }
            else if(turn==PLAYER_O){
                PlaceAMove(point, PLAYER_O);

                int currentScore = minimax(depth + 1, PLAYER_X);

                min = Math.min(currentScore, min);

                if(min == -1){
                    board[point.x][point.y] = NO_PLAYER;
                    boardCells[point.x][point.y].setText("");
                    break;
                }
            }

            board[point.x][point.y] = NO_PLAYER;
            boardCells[point.x][point.y].setText("");
        }

        return turn == PLAYER_X ? max : min;
    }

    private boolean hasPlayerWon(int player){

        String[][] field = new String[3][3];

        String playerType;

        if(player == 1){
            playerType = "X";
        }
        else{
            playerType = "O";
        }

        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                field[i][j] = boardCells[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])
                    && field[i][0].equals(playerType)){
                return true;
            }
        }

        for(int i = 0; i < 3; i++){

            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])
                    && field[0][i].equals(playerType)){
                return true;
            }
        }

        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])
                && field[0][0].equals(playerType)) {
            return true;
        }

        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])
                && field[0][2].equals(playerType)) {
            return true;
        }

        return false;

    }

    private void findTiles(int player){

        String[][] field = new String[3][3];

        String playerType;

        if(player == 1){
            playerType = "X";
        }
        else{
            playerType = "O";
        }

        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                field[i][j] = boardCells[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2])
                    && field[i][0].equals(playerType)){
                boardCells[i][0].setTextColor(getResources().getColor(R.color.green));
                boardCells[i][1].setTextColor(getResources().getColor(R.color.green));
                boardCells[i][2].setTextColor(getResources().getColor(R.color.green));

            }
        }

        for(int i = 0; i < 3; i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i])
                    && field[0][i].equals(playerType)){
                boardCells[0][i].setTextColor(getResources().getColor(R.color.green));
                boardCells[1][i].setTextColor(getResources().getColor(R.color.green));
                boardCells[2][i].setTextColor(getResources().getColor(R.color.green));
            }
        }

        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2])
                && field[0][0].equals(playerType)) {
            boardCells[0][0].setTextColor(getResources().getColor(R.color.green));
            boardCells[1][1].setTextColor(getResources().getColor(R.color.green));
            boardCells[2][2].setTextColor(getResources().getColor(R.color.green));
        }
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0])
                && field[0][2].equals(playerType)) {
            boardCells[0][2].setTextColor(getResources().getColor(R.color.green));
            boardCells[1][1].setTextColor(getResources().getColor(R.color.green));
            boardCells[2][0].setTextColor(getResources().getColor(R.color.green));
        }
    }

    private List<Point> getAvailableCells(){
        List<Point> availableCells = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == NO_PLAYER){
                    availableCells.add(new Point(i ,j));
                }
            }
        }
        return availableCells;
    }

    private boolean PlaceAMove(Point point, int player){

        if(board[point.x][point.y] != NO_PLAYER)
            return false;

        board[point.x][point.y] = player;

        if(player == 1){
            boardCells[point.x][point.y].setText("X");
            boardCells[point.x][point.y].setTextSize(70);
            boardCells[point.x][point.y].setTextColor(getResources().getColor(R.color.white));
        }
        else if(player == 2){
            boardCells[point.x][point.y].setText("O");
            boardCells[point.x][point.y].setTextSize(70);
            boardCells[point.x][point.y].setTextColor(getResources().getColor(R.color.white));
        }


        return true;
    }

    private boolean isGameOver(){
        return hasPlayerWon(PLAYER_X) || hasPlayerWon(PLAYER_O) || getAvailableCells().isEmpty();
    }

    private void restart(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardCells[i][j].setText("");
                board[i][j] = 0;
            }
        }
        movesDone = 0;

        results.setVisibility(View.INVISIBLE);

    }
}
