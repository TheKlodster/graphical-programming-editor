import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class Console {

    private JScrollPane consolePane;
    private static JTextArea textArea;

    public Console() {
        textArea = new JTextArea();
        consolePane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        consolePane.setBorder(null);

        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);

        redirectOut();
    }

    public void redirectOut() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                textArea.append(String.valueOf((char) b));
            }
        };
        PrintStream ps = new PrintStream(out);

        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * To return the console panel to be used in another class.
     *
     * @return the console panel.
     */
    public JScrollPane getConsolePane() {
        return consolePane;
    }

    /**
     * To clear the console log each time the program is re-run.
     */
    public static void clearTextArea() {
        textArea.setText(null);
    }
}