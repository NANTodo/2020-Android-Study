package com.example.framework.org;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AppManager {
    private GameView m_gameView;            //게임뷰 클래스를 접근하게해줄 변수
    private Resources m_resources;
    private Context m_context;
    public GameState m_gameState;       //GameState 클래스를 접근하게해줄 변수

    void setGameView(GameView _gameView) {
        m_gameView=_gameView;
    }

    void setResources(Resources _resources) {
        m_resources=_resources;
    }

    void setContext(Context _context) { m_context=_context;}

    GameView getGameView() {
        return m_gameView;
    }

    Resources getResources() {
        return m_resources;
    }

    Context getContext() { return m_context;}

    public Bitmap getBitmap(int resource) {
        return BitmapFactory.decodeResource(m_resources, resource);
    }

    private static AppManager s_instance;

    public static AppManager getinstance() {
        if(s_instance==null) s_instance=new AppManager();
        return s_instance;
    }

}