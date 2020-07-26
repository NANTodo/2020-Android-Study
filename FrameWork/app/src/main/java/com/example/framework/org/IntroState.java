package com.example.framework.org;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.framework.R;

public class IntroState implements IState {

    Bitmap icon;
    int x,y;

    @Override
    public void Init() {
        icon=AppManager.getinstance().getBitmap(R.drawable.icon);
    }

    @Override
    public void Destroy() {  }

    @Override
    public void Update() {  }

    @Override
    public void Render(Canvas canvas) {
        canvas.drawBitmap(icon,x,y,null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AppManager.getinstance().getGameView().changeGameState(new CreditState());
        return true;
    }
}