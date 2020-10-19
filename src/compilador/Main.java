package compilador;

import views.CompiladorFrame;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Carlos Contreras
 * @author Mauricio García
 */
public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        new CompiladorFrame();
    }
}
