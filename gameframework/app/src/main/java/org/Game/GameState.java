package org.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.gameframework.AppManager;
import com.example.gameframework.GraphicObject;
import com.example.gameframework.IState;
import com.example.gameframework.R;
import com.example.gameframework.SoundManager;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {
    private Player m_player;
    private BackGround m_background;
    private GraphicObject m_keypad;
    int playertype;
    int killcnt=0;


    Random randEnem = new Random();

    ArrayList<Enemy> m_enemlist = new ArrayList<Enemy>();//적
    ArrayList<Missile_Player> m_pmslist = new ArrayList<Missile_Player>();//미사일 위치
    ArrayList<Missile> m_enemmslist = new ArrayList<Missile>();

    long LastRegenEnemy = System.currentTimeMillis();

    private static GameState game = new GameState();

    public static GameState getInstance(){
        return game;
    }

    public GameState(){
        playertype = 3;
    }

    public void makeEnemy(){

        if(System.currentTimeMillis() - LastRegenEnemy >= 1000){
            LastRegenEnemy = System.currentTimeMillis();

            int enemytype = randEnem.nextInt(3);
            Enemy enem = null;

            if(enemytype == 0)
                enem = new Enemy_1();
            else if(enemytype == 1)
                enem = new Enemy_2();
            else if(enemytype == 2)
                enem = new Enemy_3();

            enem.setPosition(randEnem.nextInt(1000), -60);
            enem.movetype = randEnem.nextInt(3);

            m_enemlist.add(enem);
        }

    }

    @Override
    public void Init() {
        if(playertype == 0) {
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air1));
        }
        else if(playertype == 1){
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air2));
        }
        else if(playertype == 2)
            m_player = new Player(AppManager.getInstance().getBitmap(R.drawable.air3));
        m_keypad = new GraphicObject(AppManager.getInstance().getBitmap(R.drawable.keypad));
        m_background = new BackGround(1);
        m_keypad.setPosition(0, 1200);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        long GameTime = System.currentTimeMillis();
        m_player.Update(GameTime);
        m_background.Update(GameTime);
        for(int i = m_pmslist.size() - 1; i>=0; i--) {
            Missile_Player pms = m_pmslist.get(i);
            pms.Update();
            if(pms.state == Missile.STATE_OUT)m_pmslist.remove(i);
        }
        for(int i =m_enemlist.size() - 1; i>=0; i--){
            Enemy enem = m_enemlist.get(i);
            enem.Update(GameTime);
            if(enem.state == Missile.STATE_OUT)m_enemlist.remove(i);
        }
        for(int i =m_enemmslist.size() - 1; i>=0; i--){
            Missile enemms = m_enemmslist.get(i);
            enemms.Update();
            if(enemms.state == Missile.STATE_OUT)
                m_enemmslist.remove(i);
        }
        makeEnemy();
        CheckCollision();
    }

    @Override
    public void Render(Canvas canvas) {
        m_background.Draw(canvas);
        for(Missile_Player pms : m_pmslist)
            pms.Draw(canvas);
        for(Missile enemms: m_enemmslist)
            enemms.Draw(canvas);
        for(Enemy enem : m_enemlist){
            enem.Draw(canvas);
        }
        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        Paint p = new Paint();
        p.setTextSize(40);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        p.setColor(Color.BLACK);
        canvas.drawText("남은 목숨 : " +String.valueOf(m_player.getLife()),100, 100, p);
        canvas.drawText("쓰러뜨린 적 : "+ String.valueOf(killcnt),100, 150,p);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int x = m_player.getX();
        int y = m_player.getY();

        //방향 움직일 때 위치 변경
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            m_player.setPosition(x - 20, y);
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            m_player.setPosition(x + 20, y);
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
            m_player.setPosition(x, y - 20);
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            m_player.setPosition(x, y + 20);
        if(keyCode == KeyEvent.KEYCODE_SPACE) {
            m_pmslist.add(new Missile_Player(x + 10, y));
            SoundManager.getInstance().play(1);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
/*
            int action = event.getAction();
            if(action==MotionEvent.ACTION_DOWN) {
                int _x = (int) event.getX();
                int _y = (int) event.getY();
                int x = m_player.getX();
                int y = m_player.getY();

                Rect rt = new Rect();
                //왼쪽 터치
                rt.set(5, 1245, 45, 1285);
                if (rt.contains(_x, _y)) {
                    m_player.setPosition(x - 20, y);
                }
                //위 터치
                rt.set(40, 1205, 80, 1245);
                if (rt.contains(_x, _y)) {
                    m_player.setPosition(x, y - 20);
                }
                //오른쪽터치
                rt.set(80, 1245, 120, 1285);
                if (rt.contains(_x, _y)) {
                    m_player.setPosition(x + 20, y);
                }
                //아래 터치
                rt.set(40, 1285, 80, 1325);
                if (rt.contains(_x, _y)) {
                    m_player.setPosition(x, y + 20);
                }
            }
            return true;
*///키패드 작성중이었으나 유철님이 하고 계시다하여 패스
            return false;
    }
    public void CheckCollision(){
        for(int i = m_pmslist.size() - 1; i>= 0; i--){//플레이어 미사일과 적 충돌
            for(int j = m_enemlist.size() - 1; j>=0; j--){
                if(CollisionManager.CheckBoxToBox(m_pmslist.get(i).m_BoundBox, m_enemlist.get(j).m_BoundBox)) {
                    m_pmslist.remove(i);
                    m_enemlist.remove(j);
                    SoundManager.getInstance().play(5);
                    killcnt++;

                    return;
                }
            }
        }

        for(int i = m_enemlist.size()-1; i>=0;i--){//적과 플레이어 충돌
            if(CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemlist.get(i).m_BoundBox)){
                m_enemlist.remove(i);
                m_player.destroyPlayer();
                if(m_player.getLife() <= 0)
                    System.exit(0);
            }
        }

        for(int i = m_enemmslist.size()-1;i>=0;i--){//적미사일과 플레이어 충돌
            if(CollisionManager.CheckBoxToBox(m_player.m_BoundBox, m_enemmslist.get(i).m_BoundBox)){
                m_enemmslist.remove(i);
                m_player.destroyPlayer();
                if(m_player.getLife() <= 0) AppManager.getInstance().getGameView().changeGameState(ExitState.getInstance());
            }
        }
    }
}
