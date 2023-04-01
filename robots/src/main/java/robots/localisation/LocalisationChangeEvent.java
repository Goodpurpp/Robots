package robots.localisation;

import java.beans.PropertyChangeEvent;

public class LocalisationChangeEvent extends PropertyChangeEvent {
    public LocalisationChangeEvent(Object source, String propertyName, Object oldValue, Object newValue) {
        super(source, propertyName, oldValue, newValue);
    }
    
    @Override
    public LocalisationEnum getNewValue() {
        return (LocalisationEnum) super.getNewValue();
    }

    @Override
    public LocalisationEnum getOldValue() {
        return (LocalisationEnum) super.getOldValue();
    }
}
