package com.example.framework.org;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread {

    //접근을 위한 멤버 변수
    private SurfaceHolder m_surfaceHolder;  //callback을 접근하기위한 변수
    private GameView m_gameView;        //surface에 접근하기위한 변수

    //스레드의 실행상태 멤버 변수
    private boolean m_run=false;

    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameView) {
        m_surfaceHolder=surfaceHolder;
        m_gameView=gameView;
    }

    public void setRunning(boolean run) {
        m_run=run;
    }

    @Override
    public void run() {
        //super.run();

        Canvas _canvas; //canvas는 view에 접근할 수 있는 객체
        while(m_run) {
            _canvas=null;
            try {
                //SurfaceHolder를 통해 Surface에 접근해서 가져옴
                m_gameView.Update();
                _canvas=m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder) {
                    m_gameView.onDraw(_canvas);     //그림을 그림
                }
            }finally {
                if(_canvas!=null) {
                    //surface에 화면에 표시함
                    m_surfaceHolder.unlockCanvasAndPost(_canvas);
                }
            }
        }
    }
}