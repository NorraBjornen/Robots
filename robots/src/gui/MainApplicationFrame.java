package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import log.Logger;
import logic.CloseableFrame;
import logic.MinimizationParam;
import logic.StateSaver;
import logic.WindowAdapterImpl;

public class MainApplicationFrame extends JFrame implements CloseableFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private StateSaver stateSaver;

    public MainApplicationFrame() {
        stateSaver = new StateSaver();
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        logWindow = createLogWindow();
        gameWindow = new GameWindow();

        stateSaver.restoreState(this, logWindow, gameWindow);
        


        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapterImpl(this));
    }

    private void restoreState(){

    }

    @Override
    public void setOperation(int operation) {
        this.setDefaultCloseOperation(operation);
    }

    @Override
    public void onClose(){
        stateSaver.save(logWindow, gameWindow);
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
    
    public void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

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
