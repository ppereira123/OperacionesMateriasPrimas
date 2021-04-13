package com.example.operacionesmteriasprimas.ui.reporte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReporteModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReporteModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}