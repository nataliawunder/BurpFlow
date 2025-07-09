package ui;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {
    private FlowListSidebar flowListSidebar;
    private RequestGrid requestGrid;
    private JSplitPane splitPane;

    public FlowPanel() {
        setLayout(new BorderLayout());

        flowListSidebar = new FlowListSidebar();
        requestGrid = new RequestGrid();

        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                requestGrid,
                flowListSidebar
        );

        splitPane.setDividerLocation(1500);
        splitPane.setResizeWeight(1.0); // try .5
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerSize(16);

        add(splitPane, BorderLayout.CENTER);
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
}