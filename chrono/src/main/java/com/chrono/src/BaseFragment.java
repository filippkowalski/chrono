package com.chrono.src;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;

/**
 * Created by Filip Kowalski on 05.03.17.
 */

public abstract class BaseFragment<P extends Presenter> extends Fragment {

    @Getter
    private P presenter;

    public abstract P createPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        presenter = createPresenter();
        presenter.onAttach();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onDetach();
        }
    }
}