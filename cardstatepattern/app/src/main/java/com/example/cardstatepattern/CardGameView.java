package com.example.cardstatepattern;

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
    Bitmap m_BackGroundImage;
    Bitmap m_CardBackSide;

    Bitmap m_Card_Red;
    Bitmap m_Card_Green;
    Bitmap m_Card_Blue;

    Card m_Shuffle[][];

    CardGameView view;


    Game gameState;

    Card m_SelectCard_1 = null;
    Card m_SelectCard_2 = null;

    SoundView sound;
    int total = 0;

    public CardGameView(Context context){
        super(context);

        gameState = new Game();

        //배경 이미지
        m_BackGroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background, null);
        //뒷면 이미지
        m_CardBackSide = BitmapFactory.decodeResource(getResources(), R.drawable.backside, null);

        m_Card_Red = BitmapFactory.decodeResource(getResources(), R.drawable.front_red, null);
        m_Card_Green = BitmapFactory.decodeResource(getResources(), R.drawable.front_green, null);
        m_Card_Blue = BitmapFactory.decodeResource(getResources(), R.drawable.front_blue, null);

        //화면에 표시할 카드 할당
        m_Shuffle = new Card[3][2];

        //카드 초기화
        m_Shuffle[0][0] = new Card(Card.IMG_RED);
        m_Shuffle[0][1] = new Card(Card.IMG_BLUE);
        m_Shuffle[1][0] = new Card(Card.IMG_GREEN);
        m_Shuffle[1][1] = new Card(Card.IMG_GREEN);
        m_Shuffle[2][0] = new Card(Card.IMG_BLUE);
        m_Shuffle[2][1] = new Card(Card.IMG_RED);

        //섞고
        setCardShuffle();

        //사운드 객체 생성
        sound = new SoundView(context);

        //짝맞추기 검사하는 스레드
        CardGameThread _thread = new CardGameThread( this);
        _thread.start( );

    }

    public void onDraw(Canvas canvas){
        canvas.drawBitmap(m_BackGroundImage, 0, 0, null);

        for(int y = 0;y<2;y++){
            for(int x = 0;x<3;x++){
                if(m_Shuffle [x][y].m_State == Card.CARD_SHOW ||
                        m_Shuffle [x][y].m_State == Card.CARD_OPEN ||
                        m_Shuffle [x][y].m_State == Card.CARD_MATCHED)  {
                    if(m_Shuffle[x][y].m_Color == Card.IMG_RED){
                        canvas.drawBitmap(m_Card_Red, 100 + x*270, 600 + y*400, null);
                    }
                    else if(m_Shuffle[x][y].m_Color == Card.IMG_GREEN){
                        canvas.drawBitmap(m_Card_Green, 100 + x*270, 600 + y*400, null);
                    }
                    else if(m_Shuffle[x][y].m_Color == Card.IMG_BLUE){
                        canvas.drawBitmap(m_Card_Blue, 100 + x*270, 600 + y*400, null);
                    }
                } else {
                    // 카드 뒷면을 그려야 하는 경우
                    canvas.drawBitmap( m_CardBackSide, 100 + x*270, 600 + y*400, null);
                }
            }
        }
    }


    public void setCardShuffle(){
        int visit[][] = new int[3][2];

        for(int i = 0 ;i<3;i++){
            for(int j = 0;j<2;j++){
                visit[i][j] = 0;
            }
        }
        for(int i =0;i<3;){
            int y = (int)(Math.random()*3);
            int x = (int)(Math.random()*2);

            int gx = (int)(Math.random()*2);
            int gy = (int)(Math.random()*3);
            if(visit[y][x] == 0 && visit[gy][gx] == 0) {
                visit[y][x] = 1;
                visit[gy][gx] = 1;
                i++;
                Card tmp = m_Shuffle[y][x];
                m_Shuffle[y][x] = m_Shuffle[gy][gx];
                m_Shuffle[gy][gx] = tmp;
                m_Shuffle[y][x].m_State = Card.CARD_SHOW;
                m_Shuffle[gy][gx].m_State = Card.CARD_SHOW;
            }
            else
                continue;
        }
    }

    public void startGame( )  { // 모든 카드를 뒷면 상태로 만든다.

        m_Shuffle [0][0]. m_State = Card. CARD_CLOSE;
        m_Shuffle [0][1]. m_State = Card. CARD_CLOSE;
        m_Shuffle [1][0]. m_State = Card. CARD_CLOSE;
        m_Shuffle [1][1]. m_State = Card. CARD_CLOSE;
        m_Shuffle [2][0]. m_State = Card. CARD_CLOSE;
        m_Shuffle [2][1]. m_State = Card. CARD_CLOSE;

        invalidate( ); // 화면을 갱신합니다.
    }

    public void restart(){
        invalidate();
    }


    public boolean onTouchEvent(MotionEvent event)  {
        gameState.onTouch(gameState,this, event);
        invalidate();
        return true;
    }

    public void checkMatch( )  {
        //  두 카드 중 하나라도 선택이 안 되었다면 비교할 필요가 없습니다.
        if ( m_SelectCard_1 == null || m_SelectCard_2 == null)
            return;
        //  두 카드의 색상을 비교합니다.
        if (m_SelectCard_1.m_Color == m_SelectCard_2.m_Color)  {
            total++;
            // 두 카드의 색상이 같으면 두 카드를 맞춘 상태로 바꿉니다.
            m_SelectCard_1.m_State = Card.CARD_MATCHED;
            m_SelectCard_2.m_State = Card.CARD_MATCHED;
            //  다시 선택할 수 있도록 null로 설정
            m_SelectCard_1 = null;
            m_SelectCard_2 = null;
        }
        else {
            //
            try {
                Thread.sleep(500);
            } catch (InterruptedException e)  {  }
            //  두 카드의 색상이 다른 경우 두 카드를 이전처럼 뒷면으로 돌려줍니다.
            m_SelectCard_1.m_State = Card.CARD_CLOSE;
            m_SelectCard_2.m_State = Card.CARD_CLOSE;
            // 다시 선택할 수 있도록 null로 설정
            m_SelectCard_1 = null;
            m_SelectCard_2 = null;
        }
//        invalidate();
        postInvalidate();
    }

}
