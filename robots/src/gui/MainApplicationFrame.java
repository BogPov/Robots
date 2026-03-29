package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.*;

import log.Logger;

public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private static final String MENU_LOOK_AND_FEEL = "Режим отображения";
    private static final String MENU_TEST = "Тесты";
    private static final String MENU_EXIT = "Выход";


    
    public MainApplicationFrame(){
        setLocale("ru", "RU");
        setupMainWindow();
        initDesktop();
        initWindows();
        setJMenuBar(createMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // вынес в отдельный метод чтобы в будущем была возможность
    // улучшить локализацию
    private void setLocale(String lang, String country){
        Locale.setDefault(new Locale(lang, country));
    }

    // главное окна
    private void setupMainWindow() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
    }

    private void initDesktop() {
        setContentPane(desktopPane);
    }

    private void initWindows() {
        addWindow(createLogWindow());
        addWindow(createGameWindow());
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

    protected GameWindow createGameWindow(){
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    // метод создания меню
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenuItem());
        menuBar.add(createExitMenuItem());
        return menuBar;
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu(MENU_LOOK_AND_FEEL);

        menu.add(createMenuItem("Системная схема",
                e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName())));

        menu.add(createMenuItem("Универсальная схема",
                e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())));

        return menu;
    }

    private JMenu createTestMenuItem() {
        JMenu menu = new JMenu(MENU_TEST);

        menu.add(createMenuItem("Сообщение в лог",
                e -> Logger.debug("Новая строка")));

        return menu;
    }

    private JMenu createExitMenuItem() {
        JMenu menu = new JMenu(MENU_EXIT);

        menu.add(createMenuItem("Закрыть приложение",
                e -> exitCommand()));

        return menu;
    }

    // универсальный метод создания пункта меню -дублирование
    private JMenuItem createMenuItem(String title, ActionListener action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(action);
        return item;
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

    private void exitCommand(){
        Object[] options = {"Да",
                "Нет!"};
        JInternalFrame frame = new JInternalFrame();
        int n = JOptionPane.showOptionDialog(frame,
                "Вы точно хотите выйти?",
                "Окно выхода",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        if (n == 0){
            System.exit(0);
        }
    }
}
