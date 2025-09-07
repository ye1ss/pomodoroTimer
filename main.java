import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class main {
    enum Modo {
        NONE, POMODORO, DESCANSO
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::createAndShow);
    }

    public static void createAndShow() {
        JFrame frame = new JFrame("POMODORO TIMER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 360);
        frame.setLayout(new GridBagLayout());

        // ---- UI base
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        JLabel titulo = new JLabel("¿LISTO PARA COMENZAR?");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tiempo = new JLabel("00:00");
        tiempo.setFont(tiempo.getFont().deriveFont(Font.BOLD, 28f));
        tiempo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel select = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton btnPomodoro = new JButton("POMODORO");
        JButton btnDescanso = new JButton("DESCANSO");
        select.add(btnPomodoro);
        select.add(btnDescanso);

        JLabel txt = new JLabel("Ciclos completados:");
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPom = new JLabel("Pomodoro: ");
        JLabel lblDes = new JLabel("Descanso: ");
        lblPom.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton boton = new JButton("INICIAR");
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---- Estado
        final int[] durPom = { 20 * 60 }; // Pomodoro por defecto: 20 min
        final int[] durDes = { 5 * 60 }; // Descanso por defecto: 5 min
        final int[] restante = { durPom[0] };
        final Modo[] modo = { Modo.NONE };
        final int[] completadosPom = { 0 };
        final int[] completadosDes = { 0 };

        tiempo.setText(formatear(restante[0]));

        // Helper: poner duración del modo actual
        Runnable resetToModeDuration = () -> {
            restante[0] = (modo[0] == Modo.DESCANSO) ? durDes[0] : durPom[0];
            tiempo.setText(formatear(restante[0]));
        };

        // ---- Timer (creado sin acción para poder referenciarlo dentro)
        final Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            if (restante[0] > 0) {
                restante[0]--;
                tiempo.setText(formatear(restante[0]));
                if (restante[0] == 0) {
                    timer.stop();
                    boton.setText("INICIAR");
                    Toolkit.getDefaultToolkit().beep();

                    // sumar punto al ciclo completado
                    if (modo[0] == Modo.POMODORO) {
                        completadosPom[0]++;
                        lblPom.setText("Pomodoro: " + puntos(completadosPom[0]));
                    } else if (modo[0] == Modo.DESCANSO) {
                        completadosDes[0]++;
                        lblDes.setText("Descanso: " + puntos(completadosDes[0]));
                    }

                    // reinicia automáticamente a la duración del modo activo
                    resetToModeDuration.run();
                }
            }
        });

        // ---- Botón Iniciar/Parar
        ActionListener toggle = e -> {
            if (timer.isRunning()) {
                // Pausa: NO resetear, para reanudar donde quedó
                timer.stop();
                boton.setText("INICIAR");
            } else {
                // Si terminó un ciclo y quedó en 0, arranca desde duración del modo
                if (restante[0] <= 0) {
                    resetToModeDuration.run();
                }
                timer.start();
                boton.setText("PARAR");
            }
        };
        boton.addActionListener(toggle);

        // ---- Cambiar de modo
        btnPomodoro.addActionListener(
                e -> setModo(Modo.POMODORO, Color.BLUE, timer, tiempo, boton, restante, durPom[0], modo));
        btnDescanso.addActionListener(
                e -> setModo(Modo.DESCANSO, Color.GREEN, timer, tiempo, boton, restante, durDes[0], modo));

        // ---- Menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuAjustes = new JMenu("Ajustes");
        JMenu menuPomodoro = new JMenu("Pomodoro");
        JMenuItem miPom20 = new JMenuItem("20 min");
        JMenuItem miPom25 = new JMenuItem("25 min");
        JMenuItem miPom30 = new JMenuItem("30 min");
        menuPomodoro.add(miPom20);
        menuPomodoro.add(miPom25);
        menuPomodoro.add(miPom30);

        JMenu menuDescanso = new JMenu("Descanso");
        JMenuItem miDes5 = new JMenuItem("5 min");
        JMenuItem miDes10 = new JMenuItem("10 min");
        JMenuItem miDes15 = new JMenuItem("15 min");
        menuDescanso.add(miDes5);
        menuDescanso.add(miDes10);
        menuDescanso.add(miDes15);

        menuAjustes.add(menuPomodoro);
        menuAjustes.add(menuDescanso);

        JMenu menuSesion = new JMenu("Sesión");
        JMenuItem miReiniciarTemporizador = new JMenuItem("Reiniciar temporizador");
        JMenuItem miReiniciarMarcadores = new JMenuItem("Reiniciar marcadores");
        menuSesion.add(miReiniciarTemporizador);
        menuSesion.add(miReiniciarMarcadores);

        menuBar.add(menuAjustes);
        menuBar.add(menuSesion);
        frame.setJMenuBar(menuBar);

        // Acciones de menú: cambiar duraciones y aplicar si el modo está activo
        miPom20.addActionListener(e -> {
            durPom[0] = 20 * 60;
            aplicarNuevaDuracionSiActivo(Modo.POMODORO, durPom[0], modo, restante, timer, tiempo, boton);
        });
        miPom25.addActionListener(e -> {
            durPom[0] = 25 * 60;
            aplicarNuevaDuracionSiActivo(Modo.POMODORO, durPom[0], modo, restante, timer, tiempo, boton);
        });
        miPom30.addActionListener(e -> {
            durPom[0] = 30 * 60;
            aplicarNuevaDuracionSiActivo(Modo.POMODORO, durPom[0], modo, restante, timer, tiempo, boton);
        });

        miDes5.addActionListener(e -> {
            durDes[0] = 5 * 60;
            aplicarNuevaDuracionSiActivo(Modo.DESCANSO, durDes[0], modo, restante, timer, tiempo, boton);
        });
        miDes10.addActionListener(e -> {
            durDes[0] = 10 * 60;
            aplicarNuevaDuracionSiActivo(Modo.DESCANSO, durDes[0], modo, restante, timer, tiempo, boton);
        });
        miDes15.addActionListener(e -> {
            durDes[0] = 15 * 60;
            aplicarNuevaDuracionSiActivo(Modo.DESCANSO, durDes[0], modo, restante, timer, tiempo, boton);
        });

        miReiniciarTemporizador.addActionListener(e -> {
            timer.stop();
            resetToModeDuration.run();
            boton.setText("INICIAR");
        });
        miReiniciarMarcadores.addActionListener(e -> {
            completadosPom[0] = 0;
            completadosDes[0] = 0;
            lblPom.setText("Pomodoro: ");
            lblDes.setText("Descanso: ");
        });

        // ---- Layout
        content.add(titulo);
        content.add(select);
        content.add(tiempo);
        content.add(Box.createVerticalStrut(8));
        content.add(boton);
        content.add(Box.createVerticalStrut(12));
        content.add(txt);
        content.add(lblPom);
        content.add(lblDes);

        frame.add(content);

        setModo(Modo.POMODORO, Color.BLUE, timer, tiempo, boton, restante, durPom[0], modo);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void aplicarNuevaDuracionSiActivo(Modo objetivo, int nuevaDuracion,
            Modo[] modo, int[] restante,
            Timer timer, JLabel tiempo, JButton boton) {
        if (modo[0] == objetivo) {
            timer.stop();
            restante[0] = nuevaDuracion;
            tiempo.setText(formatear(restante[0]));
            boton.setText("INICIAR");
        }
    }

    private static void setModo(Modo nuevoModo, Color color,
            Timer timer, JLabel tiempo, JButton boton,
            int[] restante, int nuevaDuracionSeg, Modo[] modo) {
        modo[0] = nuevoModo;
        timer.stop();
        restante[0] = nuevaDuracionSeg;
        tiempo.setText(formatear(restante[0]));
        tiempo.setBorder(BorderFactory.createLineBorder(color, 2));
        boton.setText("INICIAR");
        boton.setEnabled(true);
        boton.setToolTipText(null);
    }

    private static String formatear(int s) {
        int m = s / 60;
        int ss = s % 60;
        return String.format("%02d:%02d", m, ss);
    }

    private static String puntos(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0)
                sb.append(' ');
            sb.append('●');
        }
        return sb.toString();
    }
}
