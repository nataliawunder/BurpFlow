package config;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {
    private final JCheckBox useProxyNumberingCheckbox;

    public ConfigPanel(Config config) {
        setLayout(new BorderLayout());

        JPanel checkboxPanel = new JPanel();
        useProxyNumberingCheckbox = new JCheckBox("Enable Proxy Numbers for Requests");
        useProxyNumberingCheckbox.setSelected(config.isUsingProxyNumbers());
        useProxyNumberingCheckbox.addActionListener(e -> {
            config.setUsingProxyNumbers(useProxyNumberingCheckbox.isSelected());
        });

        checkboxPanel.add(useProxyNumberingCheckbox);
        add(checkboxPanel, BorderLayout.NORTH);
    }
}