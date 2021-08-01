import javax.swing.*;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import org.sikuli.script.*;

import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.LayoutManager;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.InputEvent;;

public class QAccept {
    private static boolean running = false;
    private static Screen s = new Screen();
    private static JLabel mainText;
    private static Robot bot;
    public static void main(String[] args) {
        setupUI();
        try {
            bot = new Robot();
        } catch (AWTException e) {
        }
    }

    private static void search() {
        try {
            Match found = s.find("/images/queue.png");
            if(found != null){
                bot.mouseMove(found.x + 50, found.y + 10);
                bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                mainText.setText("Queue Accepted");
                running = false;
            }
        } catch (FindFailed e) {
        }
    }

    private static void setupUI() {
        ImagePath.add(System.getProperty("user.dir"));
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new GridLayout(2,1));
        frame.setSize(300,200); 
        LayoutManager layout = new FlowLayout();  
        frame.setLayout(layout);
        frame.setTitle("QAccept");
        mainText = new JLabel("Disabled.", SwingConstants.CENTER);
        JToggleButton toggle = new JToggleButton("OFF");
        toggle.setPreferredSize(new Dimension(150,50));
        toggle.setUI(new BasicToggleButtonUI());
        toggle.setBackground(new Color(224, 67, 104));
        toggle.setFont(new Font("Mono", Font.BOLD, 20));
        toggle.setFocusPainted(false);
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent)
            {
                if (toggle.isSelected()) {
                    running = true;
                    mainText.setText("Waiting for Queue...");
                    new Thread(new Runnable() {
                        public void run() {
                            while(running){
                                search();
                            }
                        }
                    }).start();
                    toggle.setText("ON");
                    toggle.setBackground(new Color(127, 227, 104));
                }
                else {
                    running = false;
                    mainText.setText("Disabled.");
                    toggle.setText("OFF");
                    toggle.setBackground(new Color(224,  67, 104));
                }
            }
        };
        toggle.addItemListener(itemListener);
        panel.add(mainText);
        panel.add(toggle);
        frame.add(panel);
        frame.setVisible(true);
    }
}
