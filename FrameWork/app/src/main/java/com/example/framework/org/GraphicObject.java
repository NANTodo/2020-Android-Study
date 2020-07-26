package com.example.framework.org;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GraphicObject {

    public Bitmap m_bitmap;
    public int m_x;
    public int m_y;

    //생성자
    public GraphicObject(Bitmap bitmap) {
        m_bitmap=bitmap;
        m_x=0;
        m_y=0;
    }

    public GraphicObject(int x, int y) {
        m_x=x;
        m_y=y;
    }

    //이미지 그림
    public void Draw(Canvas canvas) {
        canvas.drawBitmap(m_bitmap, m_x, m_y, null);
    }

    //좌표 설정
    public void setPosition(int x,int y) {
        m_x=x;
        m_y=y;
    }

    // X,Y 각 좌표 반환
    public int getX() { return m_x; }
    public int getY() { return m_y; }
}