import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.Flow;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::createAndShow);
    }

    public static void createAndShow() {
        JFrame frame = new JFrame("POMODORO TIMER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel titulo = new JLabel("¿LISTO PARA COMENZAR?");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tiempo = new JLabel("00:00");
        tiempo.setFont(tiempo.getFont().deriveFont(Font.BOLD, 24f));
        tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel select = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        select.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        select.setOpaque(true);
        JButton btnPomodoro = new JButton("POMODORO");
        JButton btnDescanso = new JButton("DESCANSO");
        select.add(btnPomodoro);
        select.add(btnDescanso);

        JButton boton = new JButton("INICIAR");
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setBorder(BorderFactory.createEmptyBorder(20, 15, 5, 15));
        boton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        final int[] segundos = { 0 };
        Timer timer = new Timer(1000, e -> {
            segundos[0]++;
            tiempo.setText(formatear(segundos[0]));
        });

        btnPomodoro.addActionListener(e -> {
            // hacer que el cuadro se quede azul y que funcione
            timer.stop();
            segundos[0] = 0;
            tiempo.setText("00:00");
            tiempo.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

            boton.setText("INICIAR");

        });

        btnDescanso.addActionListener(e -> {
            // hacer que el cuadro se quede verde y que funcione
            timer.stop();
            segundos[0] = 0;
            tiempo.setText("00:00");
            tiempo.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
            boton.setText("INICIAR");

        });

        boton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                boton.setText("INICIAR");
            } else {
                timer.start();
                segundos[0] = 0;
                tiempo.setText("00:00");
                boton.setText("Iniciar");
            }
        });

        content.add(titulo);
        content.add(select, new GridBagConstraints());
        content.add(tiempo);
        content.add(boton);

        frame.add(content);
        // frame.add(nav);
        frame.setVisible(true);
    }

    private static String formatear(int s) {
        int m = s / 60;
        int ss = s % 60;
        return String.format("%02d:%02d", m, ss);
    }

}