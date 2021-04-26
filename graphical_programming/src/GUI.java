import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class GUI {

    private JPanel buttonPanel, dragPanel, runPanel;
    private JButton runButton, printButton, variableButton, forButton;
    private final ComponentMover cm;

    private ActionListener spawn = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            JButton clone = cloneSwingComponent(button);
            assert clone != null;
            clone.addMouseListener(new DeleteListener());
            dragPanel.add(clone);
            cm.registerComponent(clone);

            dragPanel.repaint();
            dragPanel.revalidate();
        }
    };

    public GUI() {
        // Initialising the frame for the program. This involves setting the size, creating the BorderLayout
        // as well as choosing the background colour.
        JFrame frame = new JFrame();
        frame.setSize(1300, 900);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        // Setting the JFrame to the center of any screen size.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        // Creating the panels for the application.
        buttonPanel = new JPanel();
        dragPanel = new JPanel(new DragLayout());
        JPanel titlePanel = new JPanel();
        runPanel = new JPanel();

        cm = new ComponentMover();
        cm.setAutoLayout(true);

        // Setting the background colours to white for the panels.
        buttonPanel.setBackground(Color.WHITE);
        titlePanel.setBackground(Color.WHITE);
        dragPanel.setBackground(Color.WHITE);
        runPanel.setBackground(Color.WHITE);

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
        createButtons();

        // Adding the panels to the frame, assigning them to their locations.
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(dragPanel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(runPanel, BorderLayout.SOUTH);

        // MAKING DREAMS COME TRUE!
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("It's graphic time");
        frame.setVisible(true);
        frame.setResizable(false);

        // Adding the interaction with the buttons.
        ActionListener frameWindow = e -> popUpFrame();
        ActionListener frameWindow2 = e -> popUpFrame();
        printButton.addActionListener(frameWindow);
        printButton.addMouseListener(new DeleteListener());
        variableButton.addActionListener(frameWindow2);
        runButton.addActionListener(this::runProcedure);
    }

    /**
     *
     * @param actionEvent
     */
    public void runProcedure(ActionEvent actionEvent) {
        Structure assign = new Structure();
        Component[] componentArray = dragPanel.getComponents();
        String[] stringSplit;

        if(componentArray.length != 0) {
            for (Component component : componentArray) {
                if (component instanceof JButton) {  // Checks if component is of Jbutton instance
                    JButton value = (JButton) component; // Casts it to JButton
                    String printText = value.getText(); // Gets the text in the button.

                    if(printText.contains("=")) {
                        stringSplit = printText.split("[\\\\s@&.?$+=]+");
                        assign.set(stringSplit[0], stringSplit[1]);
                        System.out.println(assign.getMap());
                    } else {
                        System.out.println(printText); // Prints the text.
                    }
                }
            }
            sortComponents(componentArray);
            System.out.println("----------------------------");
        }
    }

    public void sortComponents(Component[] array) {
        for(Component component : array) {
        }
    }

    /**
     *
     */
    public void createButtons() {
        ArrayList<JButton> buttonArray = new ArrayList();

        printButton = new JButton("Print");
        variableButton = new JButton("Variable");
        forButton = new JButton("For loop");

        buttonArray.add(printButton);
        buttonArray.add(variableButton);
        buttonArray.add(forButton);

        for(JButton button: buttonArray) {
            button.setPreferredSize(new Dimension(150, 40));
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setBorder(new RoundedBorder(10));

            // Adding the buttons to their respective panels.
            buttonPanel.add(button);
        }

        runButton = new JButton("Run");
        runButton.setPreferredSize(new Dimension(200, 58));
        runButton.setFont(new Font("Arial", Font.PLAIN, 30));
        runButton.setBorder(new RoundedBorder(10));
        runPanel.add(runButton);
    }

    /**
     * cloneSwingComponent(Component c) - copy constructor which writes the object
     * into ??? and then reads the object which is returned.
     *
     * @param c component to be cloned.
     * @return the cloned component.
     */
    private JButton cloneSwingComponent(Component c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (JButton) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Creating a small window frame for the print statement. The user
     * will be able to enter their statement that they wish to use for
     * their print block.
     */
    public void popUpFrame() {
        EventQueue.invokeLater(() -> {
            JFrame frameWindow = new JFrame();

            JPanel inputPanel = new JPanel();
            JTextField input = new JTextField(20);
            JButton enterButton = new JButton("Enter");

            inputPanel.add(input, BorderLayout.CENTER);
            inputPanel.add(enterButton, BorderLayout.CENTER);

            frameWindow.getContentPane().add(BorderLayout.CENTER, inputPanel);
            frameWindow.pack();
            frameWindow.setLocationByPlatform(true);
            frameWindow.setVisible(true);
            frameWindow.setResizable(false);
            input.requestFocus();

            frameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            enterButton.addActionListener(e -> {
                if(!input.getText().isEmpty()) {
                    JButton button = (JButton) e.getSource();
                    button.setText(input.getText());
                    Component clone = cloneSwingComponent(button);

                    dragPanel.add(clone);
                    cm.registerComponent(clone);
                    frameWindow.dispose();

                    dragPanel.repaint();
                    dragPanel.revalidate();
                }
            });
        });
    }
}

