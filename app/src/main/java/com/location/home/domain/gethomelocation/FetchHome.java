package com.location.home.domain.gethomelocation;

import com.location.home.domain.calculatehomelocation.utils.GetHomeLocation;
import com.location.home.domain.gethomelocation.utils.ProbableLocation;
import com.location.home.domain.model.Approximation;
import com.location.home.executor.PostExecutionThread;
import com.location.home.executor.ThreadExecutor;
import com.location.home.executor.UseCaseObservable;

import io.reactivex.Observable;

public class FetchHome extends UseCaseObservable<Approximation, Void> {


    public FetchHome(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public Observable<Approximation> buildUseCaseObservable(Void aVoid) {

        Approximation approximation = new ProbableLocation()
                .checkIfDominantExists(new GetHomeLocation()
                        .getLocationsList(" "));

        if (approximation != null){

            return Observable.just(new ProbableLocation()
                    .checkIfDominantExists(new GetHomeLocation()
                            .getLocationsList(" ")));

        }

        return Observable.just(new Approximation(Math.PI, Math.PI));

    }


}
