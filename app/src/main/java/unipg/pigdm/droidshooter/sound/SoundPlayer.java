package unipg.pigdm.droidshooter.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.SoundPool;
import android.os.Build;

import unipg.pigdm.droidshooter.R;

public class SoundPlayer {
    final int SOUND_POOL_MAX = 5;
    private int hitSound;
    private int genericButtonSound;
    private int startButtonSound;
    private int gameWonSound;
    private int gameLostSound;

    private SoundPool soundPool;
    private AudioAttributes audioAttributes;

    public SoundPlayer(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.audioAttributes = new Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setAudioAttributes(this.audioAttributes).setMaxStreams(SOUND_POOL_MAX).build();
        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, 3, 0);
        }

        hitSound = soundPool.load(context, R.raw.hit_sound, 1);
        genericButtonSound = soundPool.load(context, R.raw.click_sound, 1);
        startButtonSound = soundPool.load(context, R.raw.start_sound, 1);
        gameWonSound = soundPool.load(context, R.raw.won_sound, 1);
        gameLostSound = soundPool.load(context, R.raw.lost_sound, 1);
    }

    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGenericButtonSound() {
        soundPool.play(genericButtonSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playStartButtonSound() {
        soundPool.play(startButtonSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGameWonSound() {
        soundPool.play(gameWonSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGameLostSound() {
        soundPool.play(gameLostSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
