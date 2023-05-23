import java.util.concurrent.Callable;

public class SystemSimulationTask implements Callable<SystemObserver> {
    private final ServiceSystem serviceSystem;
    private final SystemObserver systemObserver;

    public SystemSimulationTask(ServiceSystem serviceSystem, SystemObserver systemObserver) {
        this.serviceSystem = serviceSystem;
        this.systemObserver = systemObserver;
    }

    @Override
    public SystemObserver call() {
        this.serviceSystem.simulate();
        return this.systemObserver;
    }
}
