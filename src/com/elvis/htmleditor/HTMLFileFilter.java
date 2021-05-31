package com.elvis.htmleditor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(".");
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        if (ext != null) {
            if (ext.equals("html") || ext.equals("htm")) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "HTML and HTM files";
    }
}
