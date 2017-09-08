package com.location.home.executor;


public abstract class UseCaseVoid <Params>{

    private final ThreadExecutor threadExecutor;

    public UseCaseVoid(ThreadExecutor threadExecutor){

        this.threadExecutor = threadExecutor;

    }

    public abstract void buildUseCase(Params params);

    public void execute(Params params) {

        this.buildUseCase(params);

    }

}
