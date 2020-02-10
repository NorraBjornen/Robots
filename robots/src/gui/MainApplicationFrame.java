package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        HashMap<String, Integer> s = getSavedState();

        LogWindow logWindow = createLogWindow();
        GameWindow gameWindow = new GameWindow();

        boolean lHide = false;
        boolean mHide = false;

        if(s != null){
            logWindow.setBounds(s.get("log.x"), s.get("log.y"), s.get("log.width"), s.get("log.height"));
            int lVis = s.get("log.visible");
            if(lVis == 0)
                logWindow.setVisible(true);
            else{
                lHide = true;
            }

            gameWindow.setBounds(s.get("mod.x"), s.get("mod.y"), s.get("mod.width"), s.get("mod.height"));
            int mVis = s.get("mod.visible");
            if(mVis == 0)
                gameWindow.setVisible(true);
            else{
                mHide = true;
            }

        } else {

            gameWindow.setSize(400,  400);
        }
        
        addWindow(logWindow);
        addWindow(gameWindow);

        if(lHide){
            try{
                logWindow.setIcon(true);
            } catch (Exception e){}
        }

        if(mHide){
            try{
                gameWindow.setIcon(true);
            } catch (Exception e){}
        }

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
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
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    HashMap<String, Integer> state = new HashMap<>();

                    state.put("log.width", logWindow.getWidth());
                    state.put("log.height", logWindow.getHeight());
                    state.put("log.x", logWindow.getX());
                    state.put("log.y", logWindow.getY());

                    int lVis = -1;
                    if(logWindow.isDisplayable())
                        lVis = 0;

                    state.put("log.visible", lVis);



                    state.put("mod.width", gameWindow.getWidth());
                    state.put("mod.height", gameWindow.getHeight());
                    state.put("mod.x", gameWindow.getX());
                    state.put("mod.y", gameWindow.getY());


                    int mVis = -1;
                    if(gameWindow.isDisplayable())
                        mVis = 0;

                    state.put("mod.visible", mVis);
                    saveState(state);
                }
                else if (result == JOptionPane.NO_OPTION)
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }

    private void saveState(Serializable s){
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("C:\\Users\\Alexey.Muraveynik\\data");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(s);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/employee.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private HashMap<String, Integer> getSavedState(){
        HashMap e = null;
        try {
            FileInputStream fileIn = new FileInputStream("C:\\Users\\Alexey.Muraveynik\\data");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (HashMap<String, Integer>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("File class not found");
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Hashmap class not found");
            return null;
        }

        return e;
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }



        JMenu close = new JMenu("Закрыть");
        close.getAccessibleContext().setAccessibleDescription(
                "Закрыть приложение");
        close.addMenuListener(new MenuListenerImpl(this));



        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(close);
        return menuBar;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
