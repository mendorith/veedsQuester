import com.epicbot.api.shared.APIContext;
import data.Profile;
import data.QuestsInfo;
import data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Gui  extends JFrame {
    APIContext ctx;

    Profile profile;

    private JButton startButton = null;
    private JComboBox questSelector = null;
    private JTextField profileName = null;
    private JButton saveProfile = null;

    private final QuestsInfo[] quests = QuestsInfo.values();

    public Gui(APIContext ctx) {
        this.ctx = ctx;
        this.profile = new Profile(ctx);

        setTitle("veedsQuester");

        JPanel Normal = new JPanel();
        JPanel Center = new JPanel();
        JPanel Top = new JPanel();

        Normal.setLayout(new BorderLayout());
        Center.setLayout(new FlowLayout(FlowLayout.CENTER));
        Top.setLayout(new FlowLayout(FlowLayout.CENTER));

        Normal.add(Center, BorderLayout.CENTER);
        Normal.add(Top, BorderLayout.NORTH);

        Top.add(getProfileName());
        Top.add(getProfileButton());

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

    private JTextField getProfileName() {
        if (profileName == null) {
            profileName = new JTextField();
            profileName.setPreferredSize(new Dimension(90, 20));
        }
        return profileName;
    }

    private JButton getProfileButton() {
        if (saveProfile == null) {
            saveProfile = new JButton("Save Profile");

            ActionListener actionListener = e -> {
                QuestsInfo quest = (QuestsInfo) questSelector.getSelectedItem();
                String name = profileName.getText();
                profile.saveProfile(name, quest);
            };

            saveProfile.addActionListener(actionListener);
        }
        return saveProfile;
    }

    private boolean start() {
        Vars.start = true;
        Vars.currentQuest = (QuestsInfo) questSelector.getSelectedItem();
        return true;
    }
}
