package com.example.cardstatepattern;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.view.View;

public class SoundView extends View {
    SoundPool m_SoundPool;// 사운드 풀
    MediaPlayer m_Sound_BackGround;
    static int m_Sound_id_1; // 효과음1
    int m_Sound_id_2; // 효과음2

    public SoundView(Context context)  {
        super(context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            m_SoundPool = new SoundPool.Builder().build();
        }
        else
            m_SoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        m_Sound_BackGround = MediaPlayer.create(context, R.raw.background);
        m_Sound_id_1 = m_SoundPool.load(context, R.raw.effect1 ,1);

        m_Sound_BackGround.start();//재생
    }

    public int playSound(){
        return m_SoundPool.play(m_Sound_id_1,1,1,0,0,1);
    }
}
