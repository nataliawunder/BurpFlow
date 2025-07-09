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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        addButton = new JButton("Add Flow");
        editButton = new JButton("Edit Flow");
        deleteButton = new JButton("Delete Flow");

        Dimension buttonSize = new Dimension(70, 25);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        searchField = new JTextField();
        searchField.setToolTipText("Search flows...");

        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        searchField.setBackground(Color.WHITE);
        searchField.setPreferredSize(new Dimension(240, 30));

        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchIcon = new JLabel("\uD83D\uDD0D "); // magnifying glass emoji
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

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
