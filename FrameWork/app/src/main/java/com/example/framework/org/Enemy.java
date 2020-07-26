package com.example.framework.org;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Enemy extends SpriteAnimation {

    public static final int MOVE_PATTERN_1=0;
    public static final int MOVE_PATTERN_2=1;
    public static final int MOVE_PATTERN_3=2;

    protected int movetype;

    public int hp;
    public float speed;

    public static final int STATE_NORMAL=0;
    public static final int STATE_OUT=1;
    public int state=STATE_NORMAL;

    Rect m_BoundBox = new Rect();

    long LastShoot = System.currentTimeMillis();

    public Enemy(Bitmap bitmap) {
        super(bitmap);
    }

    void Move() {
        //움직이는 로직
        if(movetype==MOVE_PATTERN_1) {
            if(m_y<=500) m_y+=speed;        //중간지점까지 기본속도
            else        m_y+=speed*2;       //중감지점이후 빠름속도
        }
        else if(movetype==MOVE_PATTERN_2) {
            if(m_y<=500) m_y+=speed;        //중간지점까지 일자로 이동
            else {
                m_x+=speed;     //중간지점이후 대각선 이동
                m_y+=speed;
            }
        }
        else if(movetype==MOVE_PATTERN_3) {
            if(m_y<=500) m_y+=speed;
            else {
                m_x+=speed;
                m_y+=speed;
            }
        }
        if(m_y>1800) state=STATE_OUT;           //핸드폰 화면 밖을나가면 상태를 STATE_OUT으로 바꿔주어 성능저하를 줄인다.
    }

    public void Attack() {
        //공격하는 로직
        if(System.currentTimeMillis()-LastShoot>=1000) {
            LastShoot = System.currentTimeMillis();
            //미사일 발사 로직
            AppManager.getinstance().m_gameState.m_enemmslist.add(
                    new Missile_Enemy(m_x+10, m_y+230));
        }
    }

    @Override
    public void Update(long GameTime) {
        super.Update(GameTime);
        Attack();
        Move();
    }
}