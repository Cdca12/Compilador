package views;

import compilador.AnalizadorLexico;
import compilador.AnalizadorSemantico;
import compilador.CodigoIntermedio;
import compilador.TablaSimbolos;
import views.TablaSimbolosView;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class CompiladorFrame extends JFrame implements ActionListener {

    public static JTextArea area, consola;
    private JButton btnCompilar, btnAbrir, btnCerrar;

    public CompiladorFrame() {
        hazInterfaz();
    }

    private void hazInterfaz() {
        setLayout(null);
        setUndecorated(true);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 700, 20, 20));

        PanelGradiente panel = new PanelGradiente();
        panel.setBounds(0, 0, 500, 700);

        area = new JTextArea();
        consola = new JTextArea();
        consola.setEnabled(false);
        consola.setDisabledTextColor(Color.BLACK);
        btnCompilar = new JButton("Compilar");
        btnCompilar.setBounds(88, 5, 150, 40);
        btnCompilar.addActionListener(this);
        btnAbrir = new JButton("Abrir archivo");
        btnAbrir.setBounds(263, 5, 150, 40);
        btnAbrir.addActionListener(this);
        btnCerrar = new JButton("X");
        btnCerrar.setBounds(440, 5, 40, 40);
        btnCerrar.setBackground(Color.decode("#FFFFFF"));
        btnCerrar.addActionListener(this);

        JScrollPane scrollPaneArea = new JScrollPane(area);
        scrollPaneArea.setBounds(30, 50, 440, 300);
        JScrollPane scrollPaneConsola = new JScrollPane(consola);
        scrollPaneConsola.setBounds(30, 350, 440, 330);

        add(scrollPaneArea);
        add(scrollPaneConsola);
        add(btnAbrir);
        add(btnCompilar);
        add(btnCerrar);
        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCerrar) {
            System.exit(0);
        }

        if (e.getSource() == btnCompilar) {
            generarArchivo();
            compilar();
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showSaveDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();

            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String lineaActual;
                while ((lineaActual = br.readLine()) != null) {
                    area.append(lineaActual + "\n");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    private void generarArchivo() {
        String ruta = "codigo.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(area.getText());

            bw.close();
        } catch (Exception ex) {

        }
    }

    private void compilar() {
        if (area.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Favor de escribir su código");
            area.requestFocus();
            return;
        }

        AnalizadorLexico analizadorLexico = new AnalizadorLexico("codigo.txt");
        ArrayList<String> erroresLexicos = analizadorLexico.getErroresLexicos();

        
        consola.setText("");

        for (int i = 0; i < erroresLexicos.size(); i++) {
            consola.append(erroresLexicos.get(i) + "\n");
        }

        if (!analizadorLexico.getHayErrores()) {
//            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(analizadorLexico.getTokenRC());
        }

        TablaSimbolos tablaSimbolos = new TablaSimbolos(analizadorLexico.getTokenRC());

        // Analizador semántico
        AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico(tablaSimbolos);
        for (int i = 0; i < analizadorSemantico.getErroresSemanticos().size(); i++) {
            consola.append(analizadorSemantico.getErroresSemanticos().get(i) + "\n");
        }

        // Mostramos la tabla de símbolos
        TablaSimbolosView tablaSimbolosView = new TablaSimbolosView(tablaSimbolos.getListaSimbolos());
        tablaSimbolosView.setVisible(true);

        // Imprimir cuadruplos
        new CodigoIntermedio(tablaSimbolos.getListaSimbolos());
    }

    class PanelGradiente extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, Color.decode("#3333DD"), 500, 50, Color.decode("#4444EE")));
            g2.fillRect(0, 0, getWidth(), 50);
            g2.setPaint(new GradientPaint(0, 0, Color.decode("#000000"), 500, 700, Color.decode("#444444")));
            g2.fillRect(0, 50, getWidth(), 670);
        }
    }

}
