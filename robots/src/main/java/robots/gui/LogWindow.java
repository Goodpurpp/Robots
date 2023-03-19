package robots.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import robots.log.LogChangeListener;
import robots.log.LogEntry;
import robots.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private final LogWindowSource logSource;
    private final TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

    private final class LogWindowAdapter extends InternalFrameAdapter {
        @Override
        public void internalFrameClosing(InternalFrameEvent event) {
            int answer = GuiUtils.askUserForCloseComponentAndWaitAnswer(LogWindow.this);

            switch (answer) {
                case JOptionPane.YES_OPTION -> {
                    LogWindow.this.setVisible(false);
                    LogWindow.this.dispose();
                }
                case JOptionPane.NO_OPTION -> {

                }
            }
        }

        @Override
        public void internalFrameClosed(InternalFrameEvent event) {
            LogWindow.this.logSource.unregisterListener(LogWindow.this);
        }
    }
}
