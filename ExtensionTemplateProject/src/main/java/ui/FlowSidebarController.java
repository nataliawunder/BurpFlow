package ui;

import manager.Flow;
import manager.FlowDisplayManager;
import manager.FlowManager;
import ui.FlowListSidebar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

public class FlowSidebarController {
    private final FlowManager flowManager;
    private final FlowDisplayManager displayManager;
    private final FlowListSidebar sidebar;
    private List<String> allFlows;

    public FlowSidebarController(FlowManager flowManager, FlowDisplayManager displayManager, FlowListSidebar sidebar) {
        this.flowManager = flowManager;
        this.displayManager = displayManager;
        this.sidebar = sidebar;

        setupListeners();
        //setupSearchListener();
    }

    private void setupListeners() {
        sidebar.getAddButton().addActionListener(e -> {
            String name = JOptionPane.showInputDialog(sidebar, "Enter new flow name:", "Add Flow", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                flowManager.createFlow(name.trim());
                displayManager.refreshFlowList();
            }
        });

        sidebar.getEditButton().addActionListener(e -> {
            String selected = sidebar.getFlowList().getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(sidebar, "Select a flow to rename.", "Edit Flow", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String newName = JOptionPane.showInputDialog(sidebar, "Enter new name for flow:", selected);
            if (newName != null && !newName.trim().isEmpty() && !newName.equals(selected)) {
                flowManager.renameFlow(selected, newName.trim());
                displayManager.refreshFlowList();
            }
        });

        sidebar.getDeleteButton().addActionListener(e -> {
            String selected = sidebar.getFlowList().getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(sidebar, "Select a flow to delete.", "Delete Flow", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(sidebar, "Are you sure you want to delete Flow: " + selected + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                flowManager.deleteFlow(selected);
                displayManager.refreshFlowList();
                displayManager.clearRequests();
            }
        });

        sidebar.getRefreshButton().addActionListener(e -> {
            allFlows = flowManager.getAllFlows().keySet().stream().collect(Collectors.toList());
            displayManager.refreshFlowList();
            displayManager.refreshCurrentFlowRequests();
        });
    }
    
    // works, but buggy
    // private void setupSearchListener() {
    //     JTextField searchField = sidebar.getSearchField();
    //     allFlows = flowManager.getAllFlows().keySet().stream().toList();
        
    //     searchField.addActionListener(e -> {
    //         String text = searchField.getText().trim().toLowerCase();
    //         List<String> filtered = allFlows.stream()
    //                 .filter(name -> name.toLowerCase().contains(text))
    //                 .toList();

    //         DefaultListModel<String> model = new DefaultListModel<>();
    //         for (String name : filtered) {
    //             model.addElement(name);
    //         }
    //         sidebar.getFlowList().setModel(model);
    //     });
    // }
}