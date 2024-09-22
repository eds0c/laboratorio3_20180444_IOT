package com.example.laboratorio3_20180444_iot.activity.timer;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<>(false);
    private CountDownTimer countDownTimer;

    private long workTimeMillis = 1500000; // 25 minutos
    private long breakTimeMillis = 300000; // 5 minutos
    private boolean isBreak = false;

    public LiveData<Long> getTimeLeftMillis() {
        return timeLeftMillis;
    }

    public LiveData<Boolean> isTimerRunning() {
        return isTimerRunning;
    }

    // Método para iniciar o reiniciar el temporizador
    public void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(isBreak ? breakTimeMillis : workTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis.postValue(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                isTimerRunning.postValue(false);
                if (!isBreak) {
                    // Al finalizar el trabajo, iniciar descanso
                    isBreak = true;
                    startTimer();
                } else {
                    // Notifica que el descanso ha terminado
                    isBreak = false;
                }
            }
        };
        countDownTimer.start();
        isTimerRunning.postValue(true);
    }

    // Método para detener y resetear el temporizador
    public void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isBreak = false;
        timeLeftMillis.setValue(workTimeMillis);
        isTimerRunning.setValue(false);
    }
}
