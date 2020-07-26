package com.example.framework.org;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.framework.org.SpriteAnimation;

public class Player extends SpriteAnimation {
    Rect m_BoundBox = new Rect();
    int m_Life = 3;

    public Player(Bitmap m_bitmap) {
        super(m_bitmap);
        //애니메이션 정보 설정
        this.initSpriteData(162,270,3,6);
        //초기 위치 값을 설정
        this.setPosition(500,1000);
    }

    //프레임워크 Update에서 지속적으로 호출할 메서드
    @Override
    public void Update(long gameTime){
        super.Update(gameTime);
        //움직임이 비활성화 되어 있을 경우
       // if(bMove){
         //   this.m_x += _dirX;
           // this.m_y += _dirY;
        //}
        m_BoundBox.set(m_x, m_y, m_x+162, m_y+270);
    }
    public int getLife(){
        return m_Life;
    }
    public void addLife(){
        m_Life++;
    }
    public void destroyPlayer(){
        m_Life--;
    }

}
