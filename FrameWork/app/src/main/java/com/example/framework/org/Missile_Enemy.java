package com.example.framework.org;

import com.example.framework.R;

public class Missile_Enemy extends Missile {
    public Missile_Enemy(int x, int y) {
        super(AppManager.getinstance().getBitmap(R.drawable.missile_2));
        this.setPosition(x,y);
    }

    public void Update() {
        //미사일이 아래로 발사되는 효과
        m_y+=4;
        if(m_y>1400) state=STATE_OUT;       //적 미사일의 y좌표가 1800이상일시 없애줌

        m_BoundBox.set(m_x,m_y,m_x+143,m_y+143);
    }

}
