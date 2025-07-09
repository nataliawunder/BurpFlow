package ui;

import javax.swing.*;
import java.awt.*;

public class FlowListSidebar extends JPanel {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JList<String> flowList;

    public FlowListSidebar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));

        // Top panel with buttons and search
        JPanel topPanel = new JPanel(new BorderLayout());

        // Use a horizontal panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5)); // better spacing

        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        // Ensure same size (optional)
        Dimension buttonSize = new Dimension(70, 25);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        searchField = new JTextField();
        searchField.setToolTipText("Search flows...");
        searchField.setPreferredSize(new Dimension(250, 25));

        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchField, BorderLayout.SOUTH);

        flowList = new JList<>(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(flowList);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JList<String> getFlowList() {
        return flowList;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
