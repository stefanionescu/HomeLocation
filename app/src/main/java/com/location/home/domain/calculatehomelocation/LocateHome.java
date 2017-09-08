package com.location.home.domain.calculatehomelocation;

import android.content.Context;
import android.content.Intent;

import com.location.home.domain.calculatehomelocation.utils.GetHomeLocation;
import com.location.home.executor.ThreadExecutor;
import com.location.home.executor.UseCaseVoid;

import javax.inject.Inject;

public class LocateHome extends UseCaseVoid<LocateHome.Params> {

    Context context;

    @Inject
    public LocateHome(ThreadExecutor threadExecutor,
                      Context context) {

        super(threadExecutor);

        this.context = context;

    }

    @Override
    public void buildUseCase(Params params) {

        new GetHomeLocation().getLocationsList(params.newLocation);

        Intent sendIntent = new Intent();
        sendIntent.setAction("com.location.home.device.GET_LOCATION");

        context.sendBroadcast(sendIntent);

    }

    public static final class Params {

        private String newLocation = "";

        private Params(String newLocation) {
            this.newLocation = newLocation;
        }

        public static Params forUser(String newLocation) {
            return new Params(newLocation);
        }
    }


}
