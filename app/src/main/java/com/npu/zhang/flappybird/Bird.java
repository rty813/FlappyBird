package com.npu.zhang.flappybird;

/**
 * Created by zhang on 2017/5/21.
 */

public class Bird {
    private float height;
    private float velocity;

    public Bird(float height, float velocity){
        this.height = height;
        this.velocity = velocity;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getHeight() {
        return height;
    }

    public float getVelocity() {
        return velocity;
    }
}
