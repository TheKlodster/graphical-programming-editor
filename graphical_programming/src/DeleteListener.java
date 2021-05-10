import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeleteListener extends MouseAdapter {
    private boolean pressed;

    /**
     * Sets boolean true if mouse is pressed.
     *
     * @param e mouseclick event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
    }

    /**
     * If mouse is pressed, and it is right click, proceed to delete component. This
     * method serves as the delete function for the application.
     *
     * @param e mouseclick event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(pressed) {
            if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                JButton button = (JButton) e.getSource();
                Container parent = button.getParent();
                parent.remove(button);
                parent.revalidate();
                parent.repaint();
            }
        }
        pressed = false;
    }
}