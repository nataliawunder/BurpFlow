package ui;

import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import burp.api.montoya.MontoyaApi;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {
    private FlowListSidebar flowListSidebar;
    private RequestGrid requestGrid;
    private JSplitPane splitPane;
    private JSplitPane mainSplit;

    private final HttpRequestEditor requestEditor;
    private final HttpResponseEditor responseEditor;

    public FlowPanel(MontoyaApi montoyaApi) {
        super(new BorderLayout());

        flowListSidebar = new FlowListSidebar();
        requestGrid = new RequestGrid();
        requestEditor = montoyaApi.userInterface().createHttpRequestEditor();
        responseEditor = montoyaApi.userInterface().createHttpResponseEditor();

        JSplitPane editorsSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            requestEditor.uiComponent(),
            responseEditor.uiComponent()
        );
        editorsSplit.setResizeWeight(0.5);
        editorsSplit.setOneTouchExpandable(true);
        editorsSplit.setDividerSize(8);

        JSplitPane requestGridEditorsSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            requestGrid,
            editorsSplit
        );
        requestGridEditorsSplit.setResizeWeight(0.7);
        requestGridEditorsSplit.setOneTouchExpandable(true);
        requestGridEditorsSplit.setDividerSize(8);

        mainSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            requestGridEditorsSplit,
            flowListSidebar
        );
        mainSplit.setResizeWeight(1.0);
        mainSplit.setDividerLocation(800);
        mainSplit.setOneTouchExpandable(true);
        mainSplit.setDividerSize(16);

        add(mainSplit, BorderLayout.CENTER);
    }

    public FlowListSidebar getFlowListSidebar() {
        return flowListSidebar;
    }

    public RequestGrid getRequestGrid() {
        return requestGrid;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public JSplitPane getMainSplitPane() {
        return mainSplit;
    }

    public HttpRequestEditor getRequestEditor() {
        return requestEditor;
    }

    public HttpResponseEditor getResponseEditor() {
        return responseEditor;
    }
}