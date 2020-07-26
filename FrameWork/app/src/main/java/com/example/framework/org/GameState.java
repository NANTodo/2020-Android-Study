package com.example.framework.org;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.framework.R;

import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState {

    //멤버 변수 추가할 곳
    private Player m_player;
    private BackGround m_background;
    private GraphicObject m_keypad;

    ArrayList<Enemy> m_enemlist=new ArrayList<Enemy>();
    Long LastRegenEnemy = System.currentTimeMillis();
    Random randEnem=new Random();

    ArrayList<Missile_Player> m_pmslist=new ArrayList<Missile_Player>();    //미사일 구현
    ArrayList<Missile> m_enemmslist=new ArrayList<Missile> ();

    public GameState() {
        AppManager.getinstance().m_gameState=this;
    }

    @Override
    public void Init() {
        m_player=new Player(AppManager.getinstance().getBitmap(R.drawable.player));
        m_keypad=new GraphicObject(AppManager.getinstance().getBitmap(R.drawable.keypad));
        m_background=new BackGround(0);
        //키패드 위치
        m_keypad.setPosition(0,1100);

        //enem=new Enemy_1();
    }

    @Override
    public void Destroy() { }

    @Override
    public void Update() {
        long GameTime=System.currentTimeMillis();
        m_player.Update(GameTime);
        m_background.Update(GameTime);
        for(int index=m_pmslist.size()-1;index>=0;index--) {
            Missile_Player pms=m_pmslist.get(index);
            pms.Update();
            if(pms.state==Missile.STATE_OUT) { m_pmslist.remove(index); }
        }
        for(int index=m_enemmslist.size()-1;index>=0;index--) {
            Missile enemms=m_enemmslist.get(index);
            enemms.Update();
            if(enemms.state==Missile.STATE_OUT){ m_enemmslist.remove(index);}
        }
        for(int index=m_enemlist.size()-1;index>=0;index--) {
            Enemy enem=m_enemlist.get(index);
            enem.Update(GameTime);
            if(enem.state==Enemy.STATE_OUT) {m_enemlist.remove(index); }
        }
        MakeEnemy();
        CheckCollision();
    }

    @Override
    public void Render(Canvas canvas) {
        m_background.Draw(canvas);
        for(Missile_Player pms : m_pmslist){ pms.Draw(canvas); }
        for(Missile enemms: m_enemmslist) { enemms.Draw(canvas); }
        for(Enemy enem : m_enemlist) { enem.Draw(canvas); }
        m_player.Draw(canvas);
        m_keypad.Draw(canvas);

        Paint paint= new Paint();
        paint.setTextSize(80);
        paint.setColor(Color.BLACK);
        canvas.drawText("남은 목숨 : "+String.valueOf(m_player.getLife()),100,100,paint);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //키 입력에 따른 플레이어 이동
        int x=m_player.getX();
        int y=m_player.getY();

        if(keyCode== KeyEvent.KEYCODE_DPAD_LEFT)        //왼쪽
            m_player.setPosition(x-10,y);
        if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT)        //오른쪽
            m_player.setPosition(x+10,y);
        if(keyCode==KeyEvent.KEYCODE_DPAD_UP)           //위
            m_player.setPosition(x,y-10);
        if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN)         //아래
            m_player.setPosition(x,y+10);
        if(keyCode==KeyEvent.KEYCODE_SPACE)
            m_pmslist.add(new Missile_Player(x+10,y));
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //핸드폰으로만 해서 나가는지 확인하려고 추가함
       // int x=m_player.getX();
        //int y=m_player.getY();

        //m_pmslist.add(new Missile_Player(x+10,y));      //미사일 리스트에 미사일 추가
   //     return true;
/*
        int action=event.getAction();
        int _x,_y;
        _x=(int)event.getX();
        _y=(int)event.getY();
        Rect rt=new Rect();
        //왼쪽방향 터치
        rt.set(5,385,45,425);
        if(rt.contains(_x,_y)) { }
        //위쪽 방향 터치할 경우
        rt.set(40,345,80,385);
        if(rt.contains(_x,_y)) {}
        //오른쪽 방향 터치
        rt.set(80,385,120,425);
        if(rt.contains(_x,_y)) {}
        //아래쪽 방향 터치
        rt.set(40,425,80,465);
        if(rt.contains(_x,_y)) {}
  */      return true;
    }

    public void MakeEnemy() {
        if(System.currentTimeMillis()-LastRegenEnemy>=3000) {
            LastRegenEnemy=System.currentTimeMillis();

            int enemtype=randEnem.nextInt(3);
            Enemy enem=null;
            if(enemtype==0)         enem=new Enemy_1();
            else if(enemtype==1)    enem=new Enemy_2();
            else if(enemtype==2)    enem=new Enemy_3();

            enem.setPosition(randEnem.nextInt(800), -60);
            enem.movetype=randEnem.nextInt(3);

            m_enemlist.add(enem);
        }
    }

    public void CheckCollision() {
        for(int index0=m_pmslist.size()-1;index0>=0;index0--){
            for(int index1=m_enemlist.size()-1;index1>=0;index1--) {
                if(CollisionManager.CheckBoxToBox(
                        m_pmslist.get(index0).m_BoundBox,
                        m_enemlist.get(index1).m_BoundBox)) {
                    m_pmslist.remove(index0);
                    m_enemlist.remove(index1);

                    return;
                }

            }
        }

        for(int index=m_enemlist.size()-1;index>=0;index--) {
            if(CollisionManager.CheckBoxToBox(
                    m_player.m_BoundBox,
                    m_enemlist.get(index).m_BoundBox)) {
                m_enemlist.remove(index);       //충돌한 적 제거
                m_player.destroyPlayer();       //플레이어 생명 값 깎음
                if(m_player.getLife()<=0) System.exit(0);       //플레이어의 생명이 없을 시 게임 종료
            }
        }

        for(int index=m_enemmslist.size()-1;index>=0;index--) {
            if(CollisionManager.CheckBoxToBox(
                    m_player.m_BoundBox,
                    m_enemmslist.get(index).m_BoundBox)) {
                m_enemmslist.remove(index);
                m_player.destroyPlayer();
                if(m_player.getLife()<=0) System.exit(0);

            }
        }
    }
}
