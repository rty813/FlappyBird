package com.npu.zhang.flappybird;

import com.npu.zhang.flappybird.FlappyBird;

/**
 * Created by zhang on 2017/5/20.
 */

public class Column {
    private float height;
    private float x;

    public Column(){
        do{
            height = (float) ((Math.random() + 0.25) * FlappyBird.screenHeight);
        }while(height > 0.5 * FlappyBird.screenHeight);
        x = FlappyBird.screenWidth;
    }

    public Column(float x, float height){
        this.x = x;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setX(float x) {
        this.x = x;
    }
}

