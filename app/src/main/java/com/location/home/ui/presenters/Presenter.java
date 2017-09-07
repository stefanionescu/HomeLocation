package com.location.home.ui.presenters;


import com.location.home.ui.views.View;

/**
 * Presenter with lifecycle
 *
 * @author stefan
 */
public interface Presenter<T extends View> {

    void onResumeView();

}

