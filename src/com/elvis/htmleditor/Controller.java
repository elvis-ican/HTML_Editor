package com.elvis.htmleditor;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public void init() {
        createNewDocument();
    }

    public void exit() {
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void resetDocument() {
        if (document != null) document.removeUndoableEditListener(view.getUndoListener());
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
        document = htmlDoc;
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text) {
        resetDocument();
        try {
            StringReader reader = new StringReader(text);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            htmlKit.read(reader, document, 0);
        } catch (Exception e) {
            new ExceptionHandler().log(e);
        }
    }

    public String getPlainText() {
        StringWriter writer = new StringWriter();
        try {
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            htmlKit.write(writer, document, 0, document.getLength());
        } catch (Exception e) {
            new ExceptionHandler().log(e);
        }
        return writer.toString();
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML editor");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        HTMLFileFilter fileFilter = new HTMLFileFilter();
        chooser.setFileFilter(fileFilter);
        chooser.setDialogTitle("Open File");
        int returnVal = chooser.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            currentFile = new File (chooser.getSelectedFile().getAbsolutePath());
            currentFile = chooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            resetDocument();
            try {
                FileReader reader = new FileReader(currentFile);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                htmlKit.read(reader, document, 0);
                view.update();
                view.resetUndo();
                reader.close();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }

    }

    public void saveDocument() {
        view.selectHtmlTab();
        if (currentFile != null) {
            try {
                FileWriter writer = new FileWriter(currentFile);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                htmlKit.write(writer, document, 0, document.getLength());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        } else saveDocumentAs();
    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save File");
        HTMLFileFilter fileFilter = new HTMLFileFilter();
        chooser.setFileFilter(fileFilter);
        int returnVal = chooser.showSaveDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            if (currentFile != null) {
                view.setTitle(currentFile.getName());
                try {
                    FileWriter writer = new FileWriter(currentFile);
                    HTMLEditorKit htmlKit = new HTMLEditorKit();
                    htmlKit.write(writer, document, 0, document.getLength());
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    ExceptionHandler.log(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();

    }
}
