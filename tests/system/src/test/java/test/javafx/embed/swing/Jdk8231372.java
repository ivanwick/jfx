import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// https://github.com/openjdk/jfx/pull/16


public class Jdk8231372 {
    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(200, 200));
        frame.getContentPane().setLayout(new BorderLayout());

        JButton btn = new JButton(new AbstractAction("Add JFXPanel") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFXPanel jfxPanel = new JFXPanel();  // initializes Toolkit
                Label label = new Label("JavaFX Label");
                StackPane root = new StackPane();
                root.getChildren().add(label);

                jfxPanel.setScene(new Scene(root));

                frame.getContentPane().add(jfxPanel, BorderLayout.CENTER);
            }
        });

        frame.getContentPane().add(btn, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Jdk8231372::createAndShowGUI);
    }
}
