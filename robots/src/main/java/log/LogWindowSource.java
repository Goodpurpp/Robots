package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogWindowSource {
    private final int queueLength;
    private List<LogEntry> messages;
    private final List<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeListener;

    public LogWindowSource(int iQueueLength) {
        queueLength = iQueueLength;
        messages = new ArrayList<>(iQueueLength);
        listeners = new ArrayList<>();
    }

    public LogWindowListener registerListener(LogChangeListener changeListener) {
        return new LogWindowListener(changeListener);
    }

    private void innerRegisterListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            activeListener = null;
        }
    }

    private void unregisterListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            activeListener = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (messages.size() > queueLength)
            messages = messages.subList(1, messages.size() - 1);
        messages.add(entry);
        LogChangeListener[] activeListeners = activeListener;
        if (activeListeners == null) {
            synchronized (listeners) {
                if (activeListener == null) {
                    activeListeners = listeners.toArray(new LogChangeListener[0]);
                    activeListener = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return messages;
    }

    public class LogWindowListener implements AutoCloseable {
        private final LogChangeListener changeListener;

        private LogWindowListener(LogChangeListener changeListener) {
            this.changeListener = changeListener;
            innerRegisterListener(this.changeListener);
        }

        @Override
        public void close() {
            unregisterListener(this.changeListener);
        }
    }
}
