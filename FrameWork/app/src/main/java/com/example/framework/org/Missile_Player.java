package com.example.framework.org;

import com.example.framework.R;

public class Missile_Player extends Missile {

    //x, y는 미사일 생성 위치 GraphicObject에 있는 setPosition메소드로 좌표설정
    public Missile_Player(int x, int y) {
        super(AppManager.getinstance().getBitmap(R.drawable.missile_1));
        this.setPosition(x+10,y);
    }

    public void Update() {
        //미사일이 위로 발사되는 효과
        m_y-=10;

        if(m_y < 30) state = STATE_OUT;         //미사일이 화면 밖으로 나가기전에 사라지게함

        m_BoundBox.left=m_x;
        m_BoundBox.top=m_y;
        m_BoundBox.right=m_x+43;
        m_BoundBox.bottom=m_y+43;
    }
}