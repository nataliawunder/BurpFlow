package ui;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {
    private FlowListSidebar flowListSidebar;
    private RequestGrid requestGrid;

    public FlowPanel() {
        setLayout(new BorderLayout());

        flowListSidebar = new FlowListSidebar();
        requestGrid = new RequestGrid();

        add(requestGrid, BorderLayout.CENTER);
        add(flowListSidebar, BorderLayout.EAST);
    }

    public FlowListSidebar getFlowListSidebar() {
        return flowListSidebar;
    }

    public RequestGrid getRequestGrid() {
        return requestGrid;
    }
}

