package ui;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {
    private FlowListSidebar flowListSidebar;
    private RequestGrid requestGrid;
    private boolean sidebarVisible = true;

    public FlowPanel() {
        setLayout(new BorderLayout());

        flowListSidebar = new FlowListSidebar();
        requestGrid = new RequestGrid();

        JButton toggleButton = new JButton("⇤ Hide Sidebar");
        toggleButton.addActionListener(e -> {
            sidebarVisible = !sidebarVisible;
            if (sidebarVisible) {
                add(flowListSidebar, BorderLayout.EAST);
                toggleButton.setText("⇤ Hide Sidebar");
            } else {
                remove(flowListSidebar);
                toggleButton.setText("⇥ Show Sidebar");
            }
            revalidate();
            repaint();
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(toggleButton);

        add(topBar, BorderLayout.NORTH);
        add(requestGrid, BorderLayout.CENTER);
        add(flowListSidebar, BorderLayout.EAST); // initially visible
    }

    public FlowListSidebar getFlowListSidebar() {
        return flowListSidebar;
    }

    public RequestGrid getRequestGrid() {
        return requestGrid;
    }
}

