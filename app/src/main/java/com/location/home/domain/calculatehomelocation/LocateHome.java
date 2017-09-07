package com.location.home.domain.calculatehomelocation;

import com.location.home.domain.model.Approximation;

public interface LocateHome {

    void execute(final LocateHome.Callback callback, String newLocation);

    interface Callback {

        void onHomeLocation(Approximation location);

        void onError();
    }

}
