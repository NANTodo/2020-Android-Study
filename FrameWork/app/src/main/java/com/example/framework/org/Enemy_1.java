package com.example.framework.org;

import com.example.framework.R;

public class Enemy_1 extends Enemy {
    public Enemy_1() {
        super(AppManager.getinstance().getBitmap(R.drawable.enemy1));
        this.initSpriteData((AppManager.getinstance().getBitmap(R.drawable.enemy1).getWidth()/6), AppManager.getinstance().getBitmap(R.drawable.enemy1).getHeight(), 3, 6);
        hp=10;
        speed=2.5f;

        //movetype=Enemy.MOVE_PATTERN_2;
    }

    @Override
    public void Update(long GameTime) {
        super.Update(GameTime);

        m_BoundBox.set(m_x,m_y,m_x+162,m_y+270);     //적의 크기
    }
}
