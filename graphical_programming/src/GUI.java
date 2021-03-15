import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GUI {

    private JFrame frame;
    private JPanel buttonPanel, dragPanel, titlePanel, runPanel;
    private JButton printButton;

    public GUI() {
        // Initialising the frame for the program. This involves setting the size, creating the BorderLayout
        // as well as choosing the background colour.
        frame = new JFrame();
        frame.setSize(1300, 900);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        // Setting the JFrame to the center of any screen size.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Creating the panels for the application.
        buttonPanel = new JPanel();
        dragPanel = new JPanel();
        titlePanel = new JPanel();
        runPanel = new JPanel();

        // Setting the background colours to white for the panels.
        buttonPanel.setBackground(Color.WHITE);
        titlePanel.setBackground(Color.WHITE);
        dragPanel.setBackground(Color.WHITE);
        runPanel.setBackground(Color.WHITE);

        /** potential border so buttons don't touch the edge? try it out later. I think it is supposed to be called
         * on the buttons themselves.
         buttonPanel.setBorder(new EmptyBorder(10,10,10,10));
         */

        // Setting the size for the panels.
        buttonPanel.setPreferredSize(new Dimension(300, 100));
        titlePanel.setPreferredSize(new Dimension(200, 100));
        runPanel.setPreferredSize(new Dimension(100, 100));

        // Initialising the borders/colours for the panels.
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 0),
                BorderFactory.createLineBorder(Color.GRAY)));
        dragPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 10),
                BorderFactory.createLineBorder(Color.GRAY)));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 10),
                BorderFactory.createLineBorder(Color.GRAY)));
        runPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 10, 10, 10),
                BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY)));


        //Implementing the button blocks.
        printButton = new JButton("Print Statement");
        printButton.setPreferredSize(new Dimension(150, 30));
        //printButton.setBorder(new EmptyBorder(20, 3 ,3 ,2));
        buttonPanel.add(printButton);

        // Adding the panels to the frame, assigning them to their locations.
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(dragPanel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(runPanel, BorderLayout.SOUTH);

        // MAKING DREAMS COME TRUE!
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("It's graphic time");
        frame.setVisible(true);
        //frame.setResizable(false);

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                printButton.setVisible(true);
                dragPanel.add(cloneSwingComponent(printButton));

                dragPanel.repaint();
                dragPanel.revalidate();
            }
        });
    }
    
    /**
     * cloneSwingComponent(Component c) - copy constructor which writes the object
     * into ??? and then reads the object which is returned.
     *
     * @param c component to be cloned.
     * @return the cloned component.
     */
    private Component cloneSwingComponent(Component c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Component) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new GUI();
    }

}
