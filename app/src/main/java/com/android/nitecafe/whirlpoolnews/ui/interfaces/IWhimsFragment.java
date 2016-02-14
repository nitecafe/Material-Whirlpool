package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Whim;

import java.util.List;

public interface IWhimsFragment {

    void ShowErrorMessage();

    void DisplayWhims(List<Whim> whims);

    void HideAllProgressLoader();
}
