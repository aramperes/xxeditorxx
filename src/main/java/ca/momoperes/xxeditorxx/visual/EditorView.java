package ca.momoperes.xxeditorxx.visual;

import ca.momoperes.xxeditorxx.EditorMap;

import javax.swing.*;
import java.awt.*;

public class EditorView extends JPanel {

    private int ewidth;
    private int eheight;
    private EditorMap map;
    private boolean shown;

    public EditorView(int ewidth, int eheight, EditorMap map) {
        this.ewidth = ewidth;
        this.eheight = eheight;
        this.map = map;
        setSize(ewidth, eheight);
        shown = false;
    }

    public EditorMap getMap() {
        return map;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (shown) {
            System.out.println("Rendering " + getMap().getName());
            double xUnit = ((double)getWidth()) / 150.0;
            System.out.println("X Unit = " + xUnit + "px.");
            g.drawRect(10, 10, (int) xUnit, (int) xUnit);
        }
    }
}
