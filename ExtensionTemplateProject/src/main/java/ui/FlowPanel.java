package ui;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {
    private FlowListSidebar flowListSidebar;
    private RequestGrid requestGrid;
    private JPanel rightPanel;
    private JButton toggleButton;
    private boolean sidebarVisible = false;

    public FlowPanel() {
        setLayout(new BorderLayout());

        requestGrid = new RequestGrid();
        flowListSidebar = new FlowListSidebar();

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(flowListSidebar, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        toggleButton = new JButton("Flows");
        toggleButton.setMargin(new Insets(2, 4, 2, 4));
        toggleButton.setFocusable(false);
        toggleButton.setPreferredSize(new Dimension(20, 40));

        toggleButton.addActionListener(e -> {
            sidebarVisible = !sidebarVisible;
            updateSidebar();
        });

        JPanel toggleWrapper = new JPanel(new BorderLayout());
        toggleWrapper.add(toggleButton, BorderLayout.WEST);

        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.add(rightPanel, BorderLayout.CENTER);
        sidebarWrapper.add(toggleWrapper, BorderLayout.WEST);

        add(requestGrid, BorderLayout.CENTER);
        add(sidebarWrapper, BorderLayout.EAST);

        sidebarWrapper.setVisible(false);
    }

    private void updateSidebar() {
        Component sidebarWrapper = getComponent(1);
        sidebarWrapper.setVisible(sidebarVisible);
        toggleButton.setText(sidebarVisible ? "⇨" : "⇦");
        revalidate();
        repaint();
    }

    public FlowListSidebar getFlowListSidebar() {
        return flowListSidebar;
    }

    public RequestGrid getRequestGrid() {
        return requestGrid;
    }
}
