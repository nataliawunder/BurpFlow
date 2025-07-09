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

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        addButton = new JButton("Add Flow");
        editButton = new JButton("Edit Flow");
        deleteButton = new JButton("Delete Flow");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        searchField = new JTextField();
        searchField.setToolTipText("Search flows...");

        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchField, BorderLayout.SOUTH);

        // flow list inside
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

