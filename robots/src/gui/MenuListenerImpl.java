package gui;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.WindowEvent;

public class MenuListenerImpl implements MenuListener {

    private JFrame frame;

    public MenuListenerImpl(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        new Thread(() ->
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        ).start();
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
