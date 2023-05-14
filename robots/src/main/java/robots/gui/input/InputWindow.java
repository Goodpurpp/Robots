package robots.gui.input;

import robots.gui.common.PathEnum;
import robots.gui.common.RobotsJInternalFrame;
import robots.gui.common.RobotsLocaleChangeAdapter;
import robots.localisation.RobotsLocalisation;

public class InputWindow extends RobotsJInternalFrame {

    public InputWindow() {
        super(RobotsLocalisation.getString("input.field.name"), true, true, true, true, PathEnum.INPUT_JSON_PATH.getPath());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addPropertyChangeListener("localisation", new RobotsLocaleChangeAdapter(this));
        getContentPane().add(new InputVisualizer());
        pack();
    }

    @Override
    public void changeLocalisation() {
        setTitle(RobotsLocalisation.getString("input.field.name"));
    }
}
