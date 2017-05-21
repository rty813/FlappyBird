package com.npu.zhang.flappybird;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static Handler handler;
    private FlappyBird flappyBird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FragmentManager fragmentManager = getFragmentManager();
        flappyBird = new FlappyBird();
        fragmentManager.beginTransaction()
                .replace(R.id.container, flappyBird)
                .commit();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    Toast.makeText(MainActivity.this, "菜鸡，点击屏幕重开游戏", Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == 1){
                    flappyBird.reloadGame();
                }
                super.handleMessage(msg);
            }
        };

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("开挂");
        builder.setMessage("阳哥帅吗？");
        builder.setPositiveButton("帅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flappyBird.mode = 1;
                flappyBird.reloadGame();
            }
        });
        builder.setNegativeButton("丑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flappyBird.mode = 2;
                flappyBird.reloadGame();
            }
        });

        findViewById(R.id.btn_cheat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });
    }
}
