package com.example.framework.org;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.example.framework.org.GraphicObject;

public class Missile extends GraphicObject {

    public static final int STATE_NORMAL=0;
    public static final int STATE_OUT=1;
    public int state=STATE_NORMAL;

    Rect m_BoundBox = new Rect();

    public Missile(Bitmap bitmap) {
        super(bitmap);
    }

    //미사일이 화면밖으로 나가면 STATE_OUT으로 변경해줌 -- 화면에서 제거하기 위함(게임 속도가 저하되는 것을 방지)
    public void Update() {
        if(m_y<50) state=STATE_OUT;
    }

}
