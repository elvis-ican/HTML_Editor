package com.elvis.htmleditor;

import com.elvis.htmleditor.listeners.FrameListener;
import com.elvis.htmleditor.listeners.UndoListener;
import com.elvis.htmleditor.listeners.TabbedPaneChangeListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane;
    private JTextPane htmlTextPane;
    private JEditorPane plainTextPane;
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(null,"This is an HTML Editor.",
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        switch(action) {
            case "New": controller.createNewDocument(); break;
            case "Open": controller.openDocument(); break;
            case "Save": controller.saveDocument(); break;
            case "Save as...": controller.saveDocumentAs(); break;
            case "Exit": controller.exit(); break;
            case "About": showAbout(); break;
        }
    }

    public void init() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
        htmlTextPane = new JTextPane();
        plainTextPane = new JEditorPane();

        initGui();
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }

    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initEditMenu(this, menuBar);
        MenuHelper.initStyleMenu(this, menuBar);
        MenuHelper.initAlignMenu(this, menuBar);
        MenuHelper.initColorMenu(this, menuBar);
        MenuHelper.initFontMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane scrollPane1 = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", scrollPane1);
        JScrollPane scrollPane2 = new JScrollPane(plainTextPane);
        tabbedPane.add("Text", scrollPane2);
        Dimension paneSize = tabbedPane.getPreferredSize();
        tabbedPane.setPreferredSize(paneSize);
        TabbedPaneChangeListener paneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(paneChangeListener);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();
    }

    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            ExceptionHandler handler = new ExceptionHandler();
            handler.log(e);
        }
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler handler = new ExceptionHandler();
            handler.log(e);
        }
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public boolean isHtmlTabSelected() {
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void exit() {
        controller.exit();
    }
}
