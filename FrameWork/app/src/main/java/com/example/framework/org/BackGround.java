package com.example.framework.org;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Display;
import android.view.WindowManager;

import com.example.framework.R;
import com.example.framework.org.AppManager;
import com.example.framework.org.GraphicObject;

public class BackGround extends GraphicObject {

    static final float SCROLL_SPEED = 5.0f;
    private float m_scroll = -3350;

    Bitmap m_layer2;
    Bitmap temp1;
    Bitmap temp2;

    static final float SCROLL_SPEED_2 = 5.0f;
    private float m_scroll_2 = -3350;

    Display display;

    public BackGround(int backtype) {
        super(null);
        display = ((WindowManager)AppManager.getinstance().getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if(backtype==0) {
            m_bitmap = AppManager.getinstance().getBitmap(R.drawable.background2);
            temp1= Bitmap.createScaledBitmap(m_bitmap, display.getWidth(), m_bitmap.getHeight(), false);
        }
        else if(backtype==1) {
            m_bitmap = AppManager.getinstance().getBitmap(R.drawable.background2);
            temp1= Bitmap.createScaledBitmap(m_bitmap, display.getWidth(), m_bitmap.getHeight(), false);
        }
        m_layer2=AppManager.getinstance().getBitmap(R.drawable.background_2);
        temp2=Bitmap.createScaledBitmap(m_layer2, display.getWidth(), m_bitmap.getHeight(), false);
        setPosition(0,(int)m_scroll);
    }

    void Update(long GameTime) {
        m_scroll = m_scroll + SCROLL_SPEED;
        if(m_scroll >= 0) m_scroll =  -3350;
        setPosition(0, (int)m_scroll);
        m_scroll_2 = m_scroll_2 + SCROLL_SPEED_2;
        if(m_scroll_2 >= 0) m_scroll_2 = -3350;
    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.drawBitmap(temp1, m_x, m_y, null);
        canvas.drawBitmap(temp2, m_x, m_scroll_2, null);
    }
}
