package com.location.home.domain.calculatehomelocation;

import com.location.home.domain.calculatehomelocation.utils.GetHomeLocation;
import com.location.home.domain.calculatehomelocation.utils.ProbableLocation;
import com.location.home.domain.model.Approximation;
import com.location.home.executor.PostExecutionThread;
import com.location.home.executor.ThreadExecutor;
import com.location.home.executor.UseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class LocateHome extends UseCase<Approximation, LocateHome.Params> {

    @Inject
    public LocateHome(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread) {

        super(threadExecutor, postExecutionThread);

    }

    @Override
    public Observable<Approximation> buildUseCaseObservable(LocateHome.Params params) {

        final Approximation approximation =
                new ProbableLocation()
                        .checkIfDominantExists(new GetHomeLocation().getLocationsList(params.newLocation));

        if (approximation != null)

            return Observable.just(approximation);

        return Observable.just(new Approximation(Math.PI, Math.PI));

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
