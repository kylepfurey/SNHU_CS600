// .java
// Theme Initializer
// by Kyle Furey

package com.snhu.mscs.cs600.salesguru.ui;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;

/**
 * Initializes Dark Mode
 */
@Component
public class ThemeInitializer implements VaadinServiceInitListener {
    /**
     * Constants
     */
    static final String js = """
            const root = document.documentElement;
            root.setAttribute('theme', $0);
            root.style.setProperty('--lumo-base-color', '#0b1120');
            root.style.setProperty('--lumo-primary-color', '#00e5ff');
            root.style.setProperty('--lumo-primary-color-50pct', 'rgba(0, 229, 255, 0.5)');
            root.style.setProperty('--lumo-primary-color-10pct', 'rgba(0, 229, 255, 0.1)');
            root.style.setProperty('--lumo-primary-text-color', '#00e5ff');
            root.style.setProperty('--lumo-primary-contrast-color', '#04121a');
            """;

    /**
     * Initialization
     */
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().getElement().executeJs(js, Lumo.DARK));
    }
}
