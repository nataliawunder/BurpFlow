package ui;

import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.BorderLayout;

public class UIManager {
    private final MontoyaApi montoyaApi;
    private final FlowPanel flowPanel;

    public UIManager(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
        this.flowPanel = new FlowPanel(montoyaApi);
    }

    public void registerUI() {
        JTabbedPane burpFlowTabs = new JTabbedPane();

        burpFlowTabs.addTab("Flow Manager", flowPanel);

        // TO DO: SET UP CONFIGURATION TAB
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.add(new JLabel("To do", SwingConstants.CENTER), BorderLayout.CENTER);
        burpFlowTabs.addTab("Configurations", configPanel);

        // TO DO: SET UP HELP TAB WITH README
        JPanel helpPanel = new ReadmePanel();
        burpFlowTabs.addTab("Help", helpPanel);

        montoyaApi.userInterface().registerSuiteTab("BurpFlow", burpFlowTabs);
    }

    public FlowPanel getFlowPanel() {
        return flowPanel;
    }
}