package ui;

import burp.api.montoya.MontoyaApi;

import javax.swing.*;

public class UIManager {
    private final MontoyaApi montoyaApi;
    private final FlowPanel flowPanel;

    public UIManager(MontoyaApi montoyaApi) {
        this.montoyaApi = montoyaApi;
        this.flowPanel = new FlowPanel();
    }

    public void registerUI() {
        montoyaApi.userInterface().registerSuiteTab("BurpFlow", flowPanel);
    }

    public FlowPanel getFlowPanel() {
        return flowPanel;
    }
}

