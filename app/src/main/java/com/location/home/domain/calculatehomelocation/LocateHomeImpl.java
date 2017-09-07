package com.location.home.domain.calculatehomelocation;

import com.location.home.device.FileManager;
import com.location.home.domain.calculatehomelocation.utils.UpdateLocationsList;
import com.location.home.domain.calculatehomelocation.utils.ConvertData;
import com.location.home.domain.calculatehomelocation.utils.ProbableLocation;
import com.location.home.domain.model.Approximation;
import com.location.home.domain.model.HomeLocation;
import com.location.home.executor.AbstractInteractor;
import com.location.home.executor.InteractorExecutor;
import com.location.home.executor.MainThreadExecutor;

import java.util.ArrayList;

import javax.inject.Inject;

public class LocateHomeImpl extends AbstractInteractor implements LocateHome {

    private String newLocation;
    private ConvertData convert;
    private FileManager manager;
    private Callback callback;
    private ArrayList<HomeLocation> homeLocations;

    @Inject
    public LocateHomeImpl(InteractorExecutor interactorExecutor,
                          MainThreadExecutor mainThreadExecutor) {

        super(interactorExecutor, mainThreadExecutor);

        manager = new FileManager();

        convert = new ConvertData();

        homeLocations = new ArrayList<>();

    }

    @Override
    public void execute(Callback callback, String newLocation) {

        this.callback = callback;
        this.newLocation = newLocation;

        getInteractorExecutor().run(this);

    }

    @Override
    public void run() {

        String currentLocations = manager.read();

        if (newLocation != " "){

            homeLocations = updateLocationsList( currentLocations );

            rewriteData( homeLocations );

        } else {

            homeLocations = convertToList( currentLocations );

        }

        final Approximation approximation = new ProbableLocation().checkIfDominantExists(homeLocations);

        showHomeLocation(approximation);

    }

    private void rewriteData(ArrayList<HomeLocation> homeLocations){

        manager.deleteFile();

        manager.write(convert.convertListToString(homeLocations));

    }

    private ArrayList<HomeLocation> updateLocationsList(String currentLocations){

        UpdateLocationsList updateLocationsList =
                new UpdateLocationsList(newLocation, convertToList(currentLocations));

        return updateLocationsList.getApproximation();

    }

    private void showHomeLocation(final Approximation approximation){

        if (approximation != null) {

            getMainThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {

                    callback.onHomeLocation(approximation);

                }
            });

        }

    }

    private ArrayList<HomeLocation> convertToList(String currentLocations){

        if (currentLocations != null && currentLocations.length() > 0)

            return convert.convertStringToList(currentLocations);

        return new ArrayList<HomeLocation>();

    }

}
