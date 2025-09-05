import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::createAndShow);
    }

    public static void createAndShow() {
        JFrame frame = new JFrame("POMODORO TIMER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("¿LISTO PARA COMENZAR?");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tiempo = new JLabel("00:00");
        tiempo.setFont(tiempo.getFont().deriveFont(Font.BOLD, 24f));
        tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton boton = new JButton("INICIAR");
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        final int[] segundos = { 0 };
        Timer timer = new Timer(1000, e -> {
            segundos[0]++;
            tiempo.setText(formatear(segundos[0]));
        });

        boton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                boton.setText("INICIAR");
            } else {
                timer.start();
                segundos[0] = 0;
                tiempo.setText("00:00");
                boton.setText("PAUSAR");
            }
        });

        content.add(titulo);
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