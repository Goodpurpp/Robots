package robots.gui.log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import lombok.SneakyThrows;
import robots.gui.common.RobotsInternalFrameAdapter;
import robots.gui.common.RobotsJInternalFrame;
import robots.gui.common.RobotsJInternalFrameState;
import robots.gui.common.PathEnum;
import robots.gui.common.RobotsLocaleChangeAdapter;
import robots.localisation.RobotsLocalisation;
import robots.log.LogChangeListener;
import robots.log.LogEntry;
import robots.log.LogWindowSource;

public class LogWindow extends RobotsJInternalFrame implements LogChangeListener {
    private final LogWindowSource logSource;
    private final TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super(RobotsLocalisation.getString("log.message.start"), true, true, true, true,PathEnum.LOG_WINDOW_JSON_PATH.getPath());
        this.addInternalFrameListener(new LogWindowAdapter());
        this.logSource = logSource;
        logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry logEntry : logSource.all()) {
            content.append(logEntry.message()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void changeLocalisation() {
        this.setTitle(RobotsLocalisation.getString("log.message.start"));
    }

    private final class LogWindowAdapter extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent event) {
            LogWindow.this.logSource.unregisterListener(LogWindow.this);
        }
    }
}
