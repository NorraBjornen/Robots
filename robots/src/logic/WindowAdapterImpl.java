package logic;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class WindowAdapterImpl extends WindowAdapter {

    private CloseableFrame frame;

    public WindowAdapterImpl(CloseableFrame jFrame){
        this.frame = jFrame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Locale.setDefault(new Locale("ru"));
        int result = JOptionPane.showOptionDialog(
                null,
                "Вы действительно хотите выйти?",
                "Выход",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Да", "Нет"},
                "Да");
        if (result == JOptionPane.YES_OPTION){
            frame.setOperation(JFrame.EXIT_ON_CLOSE);
            frame.onClose();
        }
        else if (result == JOptionPane.NO_OPTION)
            frame.setOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
