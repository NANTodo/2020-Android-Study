package com.example.framework.org;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private GameViewThread m_thread;

    private IState m_state;

    public GameView(Context context) {
        super(context);
        //getHolder().addCallback(this);      //surfaceholder 만드는 부분

        //키 입력 처리를 받기 위해서
        setFocusable(true);

        AppManager.getinstance().setGameView(this);
        AppManager.getinstance().setResources(getResources());
        AppManager.getinstance().setContext(context);

        changeGameState(new GameState());
        getHolder().addCallback(this);      //홀더를 만들고 홀더에 이 SurfaceView에 대한 콜백 인터페이스를 제공해준다.
        m_thread = new GameViewThread(getHolder(), this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        m_state.Render(canvas);
    }

    // surface가 첫번째로 생성되자마자 바로 호출되는 메소드
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //스레드를 실행 상태로 만든다.
        m_thread.setRunning(true);
        //스레드 실행
        m_thread.start();
    }

    // surface에 포멧이나 사이즈 등 어떠한 변화가 생기면 바로 호출되는 메소드
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    // surface가 종료되기 직전 바로 호출되는 메소드
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry) {
            try {
                //스레드를 중지시킨다.
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {
                //스레드가 종료되도록 계속 시도
            }
        }
    }

    public void Update() {

        long gameTime=System.currentTimeMillis();
        //m_spr.Update(gameTime);

        m_state.Update();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        m_state.onKeyDown(keyCode, event);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_state.onTouchEvent(event);
        return false;
    }

    public void changeGameState(IState _state) {
        if(m_state !=null)
            m_state.Destroy();
        _state.Init();
        m_state=_state;
    }
}
