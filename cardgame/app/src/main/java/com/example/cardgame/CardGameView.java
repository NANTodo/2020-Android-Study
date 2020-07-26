package com.example.cardgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import static android.graphics.Bitmap.createScaledBitmap;

public class CardGameView extends View {
    Bitmap m_BackGroundImage,
            m_CardBackSide,
            m_Card_Red,
            m_Card_Green,
            m_Card_Blue;

    Card m_Shuffle[][];

    Paint paint;

    public static final int STATE_READY = 0;
    public static final int STATE_GAME = 1;
    public static final int STATE_END = 2;
    private int m_State = STATE_READY;


    private Card m_SelectedCard_1 = null;
    private Card m_SelectedCard_2 = null;

    MediaPlayer m_Sound_Bg;
    MediaPlayer m_Sound_effect;


    public CardGameView(Context context) {
        super(context);

        m_BackGroundImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.background, null);
        m_CardBackSide = BitmapFactory.decodeResource(getResources(),
                R.drawable.backside, null);

        m_Card_Red = BitmapFactory.decodeResource(getResources(),
                R.drawable.front_red, null);
        m_Card_Green = BitmapFactory.decodeResource(getResources(),
                R.drawable.front_green, null);
        m_Card_Blue = BitmapFactory.decodeResource(getResources(),
                R.drawable.front_blue, null);

        m_Sound_Bg = MediaPlayer.create(context, R.raw.background);
        m_Sound_effect = MediaPlayer.create(context, R.raw.effect1);

        m_Shuffle = new Card[3][2];

        setCardShuffle();
        m_Sound_Bg.start();

        CardGameThread _thread = new CardGameThread(this);
        _thread.start();
    }
    public boolean finish(){
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++)
                if (m_Shuffle[x][y].m_State != Card.CARD_MATCHED) {
                    return false;
                }
        }
        m_State = STATE_END;
        m_Sound_Bg.stop();
        return true;
    }
    public void reset() {
        setCardShuffle();
        m_State = STATE_READY;
        try {
            m_Sound_Bg.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m_Sound_Bg.start();

        CardGameThread thread = new CardGameThread(this);
        thread.start();
    }
    @Override
    public void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        m_BackGroundImage = createScaledBitmap(m_BackGroundImage, width, height, true);
        canvas.drawBitmap(m_BackGroundImage, 0, 0, null);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++)

                if (m_Shuffle[x][y].m_State == Card.CARD_SHOW ||
                        m_Shuffle[x][y].m_State == Card.CARD_PLAYEROPEN ||
                        m_Shuffle[x][y].m_State == Card.CARD_MATCHED) {
                    if (m_Shuffle[x][y].m_Color == Card.IMG_RED)
                        canvas.drawBitmap(m_Card_Red, 190 + x * 240, 600 + y * 300, null);
                    else if (m_Shuffle[x][y].m_Color == Card.IMG_GREEN)
                        canvas.drawBitmap(m_Card_Green, 190 + x * 240, 600 + y * 300, null);
                    else if (m_Shuffle[x][y].m_Color == Card.IMG_BLUE)
                        canvas.drawBitmap(m_Card_Blue, 190 + x * 240, 600 + y * 300, null);

                } else {
                    canvas.drawBitmap(m_CardBackSide, 190 + x * 240,
                            600 + y * 300, null);
                }
        }

    }

    public void setCardShuffle() {
       int index = 0;
        Random random = new Random();

        int[] array = {1,1,2,2,3,3};

        Vector<Integer> vector = new Vector<Integer>(6);

        while(true) {
            int r = random.nextInt(6);
            if (!vector.contains(r)) {
                vector.addElement(r);
            }

            if (vector.size() == 6) break;
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                m_Shuffle[x][y] = new Card(array[vector.elementAt(index)]);
                index++;
            }
        }

/*
        m_Shuffle[0][0] = new Card(Card.IMG_RED);
        m_Shuffle[0][1] = new Card(Card.IMG_BLUE);
        m_Shuffle[1][0] = new Card(Card.IMG_GREEN);
        m_Shuffle[1][1] = new Card(Card.IMG_GREEN);
        m_Shuffle[2][0] = new Card(Card.IMG_BLUE);
        m_Shuffle[2][1] = new Card(Card.IMG_RED);
        */

    }

    public void startGame() {
        m_Shuffle[0][0].m_State = Card.CARD_CLOSE;
        m_Shuffle[0][1].m_State = Card.CARD_CLOSE;
        m_Shuffle[1][0].m_State = Card.CARD_CLOSE;
        m_Shuffle[1][1].m_State = Card.CARD_CLOSE;
        m_Shuffle[2][0].m_State = Card.CARD_CLOSE;
        m_Shuffle[2][1].m_State = Card.CARD_CLOSE;

        invalidate(); // 화면을 갱신합니다.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(m_State == STATE_READY) {
            startGame(); // 게임을 시작합니다.
            m_State = STATE_GAME;
        } else if (m_State == STATE_GAME) {
            // 카드 뒤집는 처리
            int px = (int)event.getX();
            int py = (int)event.getY();

            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 3; x++ ) {

                    Rect box_card = new Rect(190 + x * 240, 600 + y * 300,
                            190 + x * 240 + 200, 600 + y * 300 + 300);

                    if (box_card.contains(px, py)) {
                        if (m_Shuffle[x][y].m_State != Card.CARD_MATCHED) {
                            if (m_SelectedCard_1 == null) {
                                m_Sound_effect.start();
                                m_SelectedCard_1 = m_Shuffle[x][y];
                                m_SelectedCard_1.m_State = Card.CARD_PLAYEROPEN;
                            } // if
                            else {
                                if (m_SelectedCard_1 != m_Shuffle[x][y]) {
                                    m_Sound_effect.start();
                                    m_SelectedCard_2 = m_Shuffle[x][y];
                                    m_SelectedCard_2.m_State = Card.CARD_PLAYEROPEN;
                                }
                            }
                        }
                    }
                }
            }
        } else if (m_State == STATE_END) {
            m_State = STATE_READY;
            Toast.makeText(getContext(), "타이틀을 드래그해서 시작", Toast.LENGTH_SHORT).show();
            reset();
        }
        invalidate();
        return true;
    }

    public void checkMatch() {

        if (m_SelectedCard_1 == null || m_SelectedCard_2 == null) return;

        if (m_SelectedCard_1.m_Color == m_SelectedCard_2.m_Color) {

            m_SelectedCard_1.m_State = Card.CARD_MATCHED;
            m_SelectedCard_2.m_State = Card.CARD_MATCHED;

            m_SelectedCard_1 = null;
            m_SelectedCard_2 = null;
        }
        else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) { }

            m_SelectedCard_1.m_State = Card.CARD_CLOSE;
            m_SelectedCard_2.m_State = Card.CARD_CLOSE;
            m_SelectedCard_1 = null;
            m_SelectedCard_2 = null;
        }

        postInvalidate();
    }

}
