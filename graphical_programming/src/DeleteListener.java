import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DeleteListener extends MouseAdapter {
    private boolean pressed;

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
    }

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

/*
    private MouseListener remove = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                System.out.println("yes");
                System.out.println(e.getSource());
            }
        }
    };
*/