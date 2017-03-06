package xyz.dswisher.roguetut;

import javax.swing.JFrame;
import asciiPanel.AsciiPanel;

public class App extends JFrame
{
    private static final long serialVersionUID = 1L;

    private AsciiPanel terminal;

    public App() {
        super();
        terminal = new AsciiPanel();
        terminal.write("rl tutorial", 1, 1);
        add(terminal);
        pack();
    }

    public static void main( String[] args ) {
        App app = new App();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }
}
