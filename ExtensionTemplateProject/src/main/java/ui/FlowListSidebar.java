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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        addButton = new JButton("Add Flow");
        editButton = new JButton("Edit Flow");
        deleteButton = new JButton("Delete Flow");

        Dimension buttonSize = new Dimension(100, 25);
        addButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        topPanel.add(buttonPanel);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        searchField = new JTextField();
        searchField.setToolTipText("Search flows...");
        searchField.setPreferredSize(new Dimension(240, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchPanel);

        add(topPanel, BorderLayout.NORTH);

        flowList = new JList<>(new DefaultListModel<>());
        flowList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flowList.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(flowList);
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
