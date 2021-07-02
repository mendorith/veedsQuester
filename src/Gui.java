import com.epicbot.api.shared.APIContext;
import data.Constants;
import data.QuestsInfo;
import data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Gui  extends JFrame {
    APIContext ctx;

    private JButton startButton = null;
    private JComboBox questSelector = null;

    private final QuestsInfo[] quests = QuestsInfo.values();

    public Gui(APIContext ctx) {
        this.ctx = ctx;

        setTitle("veedsQuester");

        JPanel Normal = new JPanel();
        JPanel Center = new JPanel();

        Normal.setLayout(new BorderLayout());
        Center.setLayout(new FlowLayout(FlowLayout.CENTER));

        Normal.add(Center, BorderLayout.CENTER);

        Center.add(getQuestSelector());
        Center.add(getStartButton());

        JTabbedPane tp = new JTabbedPane();
        tp.add("Quest", Normal);

        add(tp);

        setSize(200, 400);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (!Vars.start) {
                    ctx.script().stop("Gui was closed.");
                }
                dispose();
            }
        });
    }

    private JComboBox getQuestSelector() {
        if (questSelector == null) {
            questSelector = new JComboBox<>(quests);
        }
        return  questSelector;
    }

    private JButton getStartButton() {
        if (startButton == null) {
            startButton = new JButton("Start");

            ActionListener actionListener = e -> {
                if (start()) {
                    dispose();
                }
            };

            startButton.addActionListener(actionListener);
        }
        return startButton;
    }

    private boolean start() {
        Vars.start = true;
        Vars.currentQuest = (QuestsInfo) questSelector.getSelectedItem();
        Constants.startTime = System.currentTimeMillis();
        return true;
    }
}
