import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIMain {

    private Console console = new Console();
    private JPanel buttonPanel, dragPanel, runPanel, consolePanel;
    private JButton runButton, printButton, variableButton, expButton, forButton, whileButton;
    private JLabel instructions;
    private final ComponentMover cm;
    private HashMap<Character, Variable> variables;
    private ArrayList<JButton> sortedArray = new ArrayList<>();

    public GUIMain() {
        // Initialising the frame for the program. This involves setting the size, creating the BorderLayout
        // as well as choosing the background colour.
        JFrame frame = new JFrame();
        frame.setSize(1000, 700);
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
        consolePanel = new JPanel(new BorderLayout());
        consolePanel.add(console.getConsolePane(), BorderLayout.CENTER);

        cm = new ComponentMover();
        cm.setAutoLayout(true);

        // Setting the background colours to white for the panels.
        buttonPanel.setBackground(Color.WHITE);
        titlePanel.setBackground(Color.WHITE);
        dragPanel.setBackground(Color.WHITE);
        runPanel.setBackground(Color.WHITE);
        consolePanel.setBackground(Color.WHITE);

        // Setting the size for the panels.
        buttonPanel.setPreferredSize(new Dimension(200, 100));
        titlePanel.setPreferredSize(new Dimension(200, 100));
        runPanel.setPreferredSize(new Dimension(100, 100));
        consolePanel.setPreferredSize(new Dimension(300, 100));

        // Initialising the borders/colours for the panels.
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 0),
                BorderFactory.createLineBorder(Color.GRAY)));
        dragPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createLineBorder(Color.GRAY)));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 0, 10),
                BorderFactory.createLineBorder(Color.GRAY)));
        runPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 10, 10, 10),
                BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY)));
        consolePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 10),
                BorderFactory.createLineBorder(Color.GRAY)));

        //Implementing the button blocks.
        createButtons();

        instructions = new JLabel("<html>Click to spawn a button. Once spawned, click and hold to drag the block around the canvas.<br/>" +
                                       "Variables can only be named as a single character. Arithmetic expressions can be<br/>" +
                                       "performed through the expressions block. The for loop will loop the block<br/>" +
                                       "following it as many times as you enter, positive integers only.</html>");
        titlePanel.add(instructions);

        // Adding the panels to the frame, assigning them to their locations.
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(dragPanel, BorderLayout.CENTER);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(runPanel, BorderLayout.SOUTH);
        frame.add(consolePanel, BorderLayout.EAST);

        // MAKING DREAMS COME TRUE!
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("It's graphic time");
        frame.setVisible(true);
        frame.setResizable(true);

        // Adding the interaction with the buttons.
        printButton.setActionCommand("print");
        variableButton.setActionCommand("variable");
        expButton.setActionCommand("expression");
        forButton.setActionCommand("for");
        whileButton.setActionCommand("while");
        runButton.addActionListener(this::runProcedure);

        printButton.addActionListener(e -> {
            String action = e.getActionCommand();
            popUpFrame(action);
        });

        variableButton.addActionListener(e -> {
            String action = e.getActionCommand();
            popUpFrame(action);
        });

        expButton.addActionListener(e -> {
            String action = e.getActionCommand();
            popUpFrame(action);
        });

        forButton.addActionListener(e -> {
            String action = e.getActionCommand();
            popUpForLoop(action);
        });
    }

    /**
     * runProcedure method actually runs the program that the user creates with their blocks.
     *
     * @param actionEvent - the event that causes this procedure to take place, the click on the button
     *                    for example.
     */
    public void runProcedure(ActionEvent actionEvent) {
        try {
            Console.clearTextArea();
            variables = new HashMap<>();

            ArrayList<Component> componentArray = new ArrayList<>(Arrays.asList(dragPanel.getComponents()));
            sortedArray = sortComponents(componentArray);

            if(componentArray.size() != 0) {
                for (JButton block : sortedArray) {
                    handleBlockAction(block);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method handles each block and performs the actions that their serve their
     * purpose for.
     *
     * @param block - JButton block to be handled based on it's purpose/action command.
     */
    public void handleBlockAction(JButton block) throws Exception {
        String[] stringSplit;
        String stringText = block.getText(); // Gets the text in the button.
        String action = block.getActionCommand();
        JButton b = new JButton();

        if(action.equals("variable")) {
            stringSplit = stringText.split("[@&.?$=]");

            String leftSide = stringSplit[0];
            String rightSide = stringSplit[1];
            char key = leftSide.charAt(0);
            char val = rightSide.charAt(0);

            if (Character.isDigit(val)) {
                variables.put(key, new Variable(key, Integer.parseInt(rightSide)));
            } else {
                if(!variables.containsKey(val)) {
                    throw new NoSuchElementException(val + " has not been initialised.");
                }
                variables.put(key, new Variable(key, variables.get(val).value));
            }
        } else if(action.equals("print")) {
            System.out.println(stringText); // Prints the text.
        } else if(action.equals("expression")) {
            Parse p = new Parse(stringText, variables);
            Aexp e = p.parsePhrase();
            System.out.println(e.eval());
        } else if(block.getActionCommand().contains("for")) {
            for(int i = 0; i < sortedArray.size(); i -=-1) {
                if(block == sortedArray.get(i) && i != sortedArray.size() - 1) {
                    b = sortedArray.get(i+1);
                }
            }
            String[] loop = block.getActionCommand().split("\\|");
            for(int i = 0; i < Integer.parseInt(loop[1]) - 1; i -=- 1) {
                handleBlockAction(b);
            }
        }
    }

    /**
     * Sorts the components in the arraylist obtained by retrieving all components in the dragPanel
     * when the user runs the program. Components are then sorted by their Y-coordinate (top-down approach)
     * and by their X-coordinate if, and only if, their Y-coordinates are equal.
     *
     * @param array the initial array of the components captured from the dragPanel.
     * @return an arraylist of type <Component>, sorted.
     */
    public ArrayList<JButton> sortComponents(ArrayList<Component> array) {
        ArrayList<JButton> jButtonArray = new ArrayList<>();
        Collections.sort(array, Comparator.comparingInt(Component::getY).
                thenComparingInt(Component::getX)); // If both Y are equal -> compare X too

        for(int i = 0; i < array.size(); i -=-1) {
            JButton button = (JButton) array.get(i);
            jButtonArray.add(button);
        }
        return jButtonArray;
    }

    /**
     * Method called to simply create the buttons for the program.
     */
    public void createButtons() {
        ArrayList<JButton> buttonArray = new ArrayList();

        buttonArray.add(printButton = new JButton("Print"));
        buttonArray.add(variableButton = new JButton("Variable"));
        buttonArray.add(expButton = new JButton("Expression"));
        buttonArray.add(forButton = new JButton("For loop"));
        buttonArray.add(whileButton = new JButton("While loop"));

        for(JButton button: buttonArray) {
            button.setPreferredSize(new Dimension(150, 40));
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setBorder(new RoundedBorder(10));
            button.setFocusPainted(false);

            // Adding the buttons to their respective panels.
            buttonPanel.add(button);
        }

        runButton = new JButton("Run");
        runButton.setPreferredSize(new Dimension(200, 58));
        runButton.setFont(new Font("Arial", Font.PLAIN, 30));
        runButton.setBorder(new RoundedBorder(10));
        runButton.setFocusPainted(false);
        runPanel.add(runButton);
    }

    /**
     * cloneSwingComponent(Component c) - copy constructor which writes the object
     * into stream and then reads the object which is returned.
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
    public void popUpFrame(String actionCommand) {
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
                    JButton sourceButton = (JButton) e.getSource();
                    sourceButton.setText(input.getText());
                    sourceButton.setActionCommand(actionCommand);
                    Component clone = cloneSwingComponent(sourceButton);

                    clone.addMouseListener(new DeleteListener());
                    clone.setFont(new Font("Arial", Font.PLAIN, 20));

                    dragPanel.add(clone);
                    cm.registerComponent(clone);
                    frameWindow.dispose();

                    dragPanel.repaint();
                    dragPanel.revalidate();
                }
            });
        });
    }

    /**
     * Creating a small window frame for the for loop. The user
     * will be able to enter how many times they wish to loop.
     *
     * @param actionCommand the string name set for the button.
     */
    public void popUpForLoop(String actionCommand) {
        EventQueue.invokeLater(() -> {
            JFrame frameWindow = new JFrame("For Loop");

            JPanel inputPanel = new JPanel();
            JLabel label = new JLabel("Amount of loops: ");
            JTextField forLoop = new JTextField(20);
            JButton enterButton = new JButton("Enter");

            ((AbstractDocument) forLoop.getDocument()).setDocumentFilter(new DocumentFilter(){
                Pattern regEx = Pattern.compile("\\d*");

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    Matcher matcher = regEx.matcher(text);
                    if(!matcher.matches()){
                        return;
                    }
                    super.replace(fb, offset, length, text, attrs);
                }
            });

            inputPanel.add(label);
            inputPanel.add(forLoop, BorderLayout.CENTER);
            inputPanel.add(enterButton, BorderLayout.CENTER);

            frameWindow.getContentPane().add(BorderLayout.CENTER, inputPanel);
            frameWindow.pack();
            frameWindow.setLocationByPlatform(true);
            frameWindow.setVisible(true);
            frameWindow.setResizable(false);
            forLoop.requestFocus();

            frameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            enterButton.addActionListener(e -> {
                if(!forLoop.getText().isEmpty()) {
                    JButton sourceButton = (JButton) e.getSource();
                    sourceButton.setText("For Loop (" + forLoop.getText() + ")");
                    sourceButton.setActionCommand(actionCommand + "|" + forLoop.getText());
                    Component clone = cloneSwingComponent(sourceButton);

                    clone.addMouseListener(new DeleteListener());
                    clone.setFont(new Font("Arial", Font.PLAIN, 20));

                    dragPanel.add(clone);
                    cm.registerComponent(clone);
                    frameWindow.dispose();

                    dragPanel.repaint();
                    dragPanel.revalidate();
                }
            });
        });
    }

    public static void main(String[] args) {
        new GUIMain();
    }
}