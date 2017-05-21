package com.npu.zhang.flappybird;

/**
 * Created by zhang on 2017/5/20.
 */

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Created by zhang on 2017/5/20.
 */

public class FlappyBird extends PApplet{

    private ArrayList<Column> columns;
    public static float screenHeight;
    public static float screenWidth;
    private float gap;
    private float columnWidth;
    private Bird bird;
    private boolean isStoped = false;
    private int score = 0;
    public int mode = 0;
    private PImage birdImage;
    private PImage backgoundImage;

    @Override
    public void settings() {
        super.settings();
        fullScreen();
    }

    @Override
    public void setup() {
        super.setup();
        Bundle bundle = getArguments();
        if (bundle != null){
            mode = bundle.getInt("mode");
        }
        birdImage = loadImage("http://vps.rty813.xyz:12345/wp-content/uploads/2017/05/zhang.png");
        backgoundImage = loadImage("http://vps.rty813.xyz:12345/wp-content/uploads/2017/05/bg_day.png");
        frameRate(60);
        screenHeight = height;
        screenWidth = width;
        gap = height / 4;
        columnWidth = width / 7;
        columns = new ArrayList<>();
        bird = new Bird(height / 2, -20);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    columns.add(new Column());
                    try {
                        if (mode == 2){
                            Thread.sleep(300);
                        }
                        else{
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean surfaceTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            if (isStoped){
                ((MainActivity)getActivity()).handler.sendEmptyMessage(1);
            }
        }
        return super.surfaceTouchEvent(motionEvent);
    }

    @Override
    public void draw() {
        System.out.println(frameRate);
        textSize(50);
        fill(255, 0, 0);
        text("LOADING!", width/2, height/2);
        background(backgoundImage);

        float birdHeight = bird.getHeight();
        float birdVelocity = bird.getVelocity();
        birdHeight += birdVelocity;
        if ((birdHeight <= 50)){
            birdHeight = 50;
        }
        if (birdHeight >= height - 50){
            noLoop();
            ((MainActivity)getActivity()).handler.sendEmptyMessage(0);
            isStoped = true;
        }
        if (mode == 2){
            birdVelocity += 2.5;
        }
        else{
            birdVelocity += 1.5;
        }
        bird.setHeight(birdHeight);
        bird.setVelocity(birdVelocity);
//        ellipse(width / 6, birdHeight, 100, 100);
        image(birdImage, width / 6 - 50, birdHeight - 50);
        if (mousePressed){
            bird.setVelocity(-20);
        }

        fill(0, 255, 64);
        ArrayList<Column> list = (ArrayList<Column>) columns.clone();
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            Column column = (Column) iterator.next();
            float x = column.getX();
            float columnHeight = column.getHeight();
            if (mode == 2){
                x -= 20;
            }
            else{
                x -= 10;
            }
            if (x + columnWidth < 0){
                iterator.remove();
                continue;
            }
            if ((x < width / 6 + 3) && (x > width / 6 - 3)){
                score++;
            }
            column.setX(x);
            rect(x, 0, columnWidth, columnHeight);
            rect(x, height, columnWidth, -(height - columnHeight - gap));
            if ((mode != 1) && (checkCollision(column))){
                noLoop();
                ((MainActivity)getActivity()).handler.sendEmptyMessage(0);
                isStoped = true;
            }
        }
        fill(255, 0, 0);
        text("Score: " + score, 10,60);
    }

    private enum orientationTypeEnum{
        HORIZON, VERTICAL
    }

    public void reloadGame(){
        columns.removeAll(columns);
        bird.setHeight(screenHeight / 2);
        bird.setVelocity(-20);
        score = 0;
        isStoped = false;
        loop();
    }

    boolean checkCollision(Column column){
        float birdX = width / 6;
        float birdY = bird.getHeight();
        float birdR = 50;
        Bundle bird = new Bundle();
        bird.putFloat("birdX", birdX);
        bird.putFloat("birdY", birdY);
        bird.putFloat("birdR", birdR);
        //检查上方柱子的两个顶点
        if ((checkPoint(bird, column.getX(), column.getHeight()))
                || (checkPoint(bird, column.getX() + columnWidth, column.getHeight()))){
            return true;
        }
        //检查下方柱子的两个顶点
        if (checkPoint(bird, column.getX(), column.getHeight() + gap)
                || (checkPoint(bird, column.getX() + columnWidth, column.getHeight() + gap))){
            return true;
        }
        //检查上方柱子的两条线段
        if ((checkLine(bird, orientationTypeEnum.HORIZON, column.getX(), column.getX() + columnWidth, column.getHeight()))
                || (checkLine(bird, orientationTypeEnum.VERTICAL, 0, column.getHeight(), column.getX()))){
            return true;
        }
        //检查下方柱子的两条线段
        if ((checkLine(bird, orientationTypeEnum.HORIZON, column.getX(), column.getX() + columnWidth, column.getHeight() + gap))
                || (checkLine(bird, orientationTypeEnum.VERTICAL, column.getHeight() + gap, height, column.getX()))){
            return true;
        }

        return false;
    }

    boolean checkPoint(Bundle bird, float pointX, float pointY){
        float birdX = bird.getFloat("birdX");
        float birdY = bird.getFloat("birdY");
        float birdR = bird.getFloat("birdR");
        if (sqrt(pow(birdX - pointX, 2) + (pow(birdY - pointY, 2))) < birdR){
            return true;
        }
        return false;
    }

    boolean checkLine(Bundle bird, orientationTypeEnum orientationType, float x1, float x2, float y){
        float birdX = bird.getFloat("birdX");
        float birdY = bird.getFloat("birdY");
        float birdR = bird.getFloat("birdR");
        if (orientationType == orientationTypeEnum.HORIZON){
            if ((birdX > x1) && (birdX < x2) && (abs(birdY - y) < birdR)){
                return  true;
            }
        }
        else{
            if ((birdY > x1) && (birdY < x2) && (abs(birdX - y) < birdR)){
                return true;
            }
        }
        return false;
    }
}
