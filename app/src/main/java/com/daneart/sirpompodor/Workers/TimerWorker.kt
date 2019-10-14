package com.daneart.sirpompodor.Workers

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.nfc.Tag
import android.os.CountDownTimer
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.daneart.sirpompodor.TimerAutoStartReceiver
import com.daneart.sirpompodor.helpers.ProcessState
import com.daneart.sirpompodor.helpers.TimerState
import com.daneart.sirpompodor.models.Timer
import com.daneart.sirpompodor.utils.NotificationUtil
import kotlin.concurrent.timer


class TimerWorker(val context: Context, params: WorkerParameters) : Worker(context, params)
, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {


    private val TAG = TimerWorker::class.java.simpleName

    private var timerObject: Timer = Timer

    private var mediaPlayer: MediaPlayer? = null

    override fun doWork(): Result {

        Log.e(TAG, "Worker started!")

        for (i in timerObject.secondsRemaining * 1000 downTo 0 step 1000) {
            Thread.sleep(1000)
            timerObject.secondsRemaining = i / 1000
            if (isStopped) return Result.failure()
        }

        if (timerObject.processState != ProcessState.Work)
            timerObject.currentCycle++

        timerObject.updateProcessState()

        timerObject.secondsRemaining = timerObject.currentTimerLength * 60L

        if (!timerObject.isAutostart) {
            NotificationUtil.showTimeExpired(context = applicationContext)
            timerObject.timerState = TimerState.Stopped
        } else {
            val intent = Intent("com.daneart.sirpompodor.TimerAutoStartReceiver")
            context.sendBroadcast(intent)
            Log.e(TAG, "Broadcast sent")
        }

        val fileDescriptor =
            applicationContext.assets.openFd("notification_sound.mp3")

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(fileDescriptor.fileDescriptor,
            fileDescriptor.startOffset,
            fileDescriptor.length)
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
        mediaPlayer!!.setOnPreparedListener(this)
        mediaPlayer!!.prepareAsync()

        return Result.success()
    }

    override fun onPrepared(player: MediaPlayer?) {
        player?.start()
    }

    override fun onCompletion(player: MediaPlayer?) {
        mediaPlayer?.let { p ->
            p.release()
            mediaPlayer = null
        }
    }

}