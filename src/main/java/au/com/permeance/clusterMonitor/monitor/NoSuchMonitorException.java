package au.com.permeance.clusterMonitor.monitor;

public class NoSuchMonitorException extends Exception {

    public NoSuchMonitorException() {
        super();
    }

    public NoSuchMonitorException(final String message) {
        super(message);
    }

    public NoSuchMonitorException(final Throwable cause) {
        super(cause);
    }

    public NoSuchMonitorException(final String message,
                                  final Throwable cause) {
        super(message, cause);
    }

}
