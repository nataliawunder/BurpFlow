package ui;

import config.Config;
import config.ConfigPanel;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;

public class UIManager {
    private final MontoyaApi montoyaApi;
    private final FlowPanel flowPanel;
    private final Config config = new Config();

    public UIManager(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
        this.flowPanel = new FlowPanel(montoyaApi);
    }

    public void registerUI() {
        JTabbedPane burpFlowTabs = new JTabbedPane();

        burpFlowTabs.addTab("Flow Manager", flowPanel);

        JPanel configPanel = new ConfigPanel(config);
        burpFlowTabs.addTab("Configurations", configPanel);

        JPanel helpPanel = new ReadmePanel();
        burpFlowTabs.addTab("Help", helpPanel);

        montoyaApi.userInterface().registerSuiteTab("BurpFlow", burpFlowTabs);
    }

    public FlowPanel getFlowPanel() {
        return flowPanel;
    }

    public Config getConfig() {
        return config;
    }
}