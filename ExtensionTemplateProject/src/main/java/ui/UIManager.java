package ui;

import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.BorderLayout;

public class UIManager {
    private final MontoyaApi montoyaApi;
    private final FlowPanel flowPanel;

    public UIManager(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
        this.flowPanel = new FlowPanel();
    }

    public void registerUI() {
        JTabbedPane burpFlowTabs = new JTabbedPane();

        // Main flow manager panel
        FlowPanel flowPanel = new FlowPanel();
        burpFlowTabs.addTab("Flow Manager", flowPanel);

        // Config tab (placeholder for now)
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.add(new JLabel("Configurations go here", SwingConstants.CENTER), BorderLayout.CENTER);
        burpFlowTabs.addTab("Configurations", configPanel);

        // Help tab
        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.add(new JLabel("<html><center><h2>BurpFlow Help</h2><p>Instructions go here.</p></center></html>", SwingConstants.CENTER), BorderLayout.CENTER);
        burpFlowTabs.addTab("Help", helpPanel);

        montoyaApi.userInterface().registerSuiteTab("BurpFlow", burpFlowTabs);
    }

    public FlowPanel getFlowPanel() {
        return flowPanel;
    }
}

