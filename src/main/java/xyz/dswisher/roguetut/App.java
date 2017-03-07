package xyz.dswisher.roguetut;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;
import xyz.dswisher.roguetut.screens.Screen;
import xyz.dswisher.roguetut.screens.StartScreen;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class App extends JFrame implements KeyListener
{
    private static final long serialVersionUID = 1L;

    private AsciiPanel terminal;
    private Screen screen;

    public App() {
        super();
        terminal = new AsciiPanel();
        add(terminal);
        pack();
        screen = new StartScreen();
        addKeyListener(this);
        repaint();
    }

    public void repaint() {
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    public static void main( String[] args ) {
        App app = new App();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        app.setLocation(dim.width / 2 - app.getSize().width / 2, dim.height / 2 - app.getSize().height / 2);

        app.setVisible(true);
    }
}
