import com.epicbot.api.shared.APIContext;
import data.Profile;
import data.QuestsInfo;
import data.Vars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NewGui  extends JFrame {
    APIContext ctx;

    Profile profile;

    private JButton startButton = null;
    private JTextField profileName = null;
    private JButton saveProfile = null;

    private JList questList = null;
    private JList selectedQuestList = null;
    private JButton addQuest = null;
    private JButton removeQuest = null;

    private final QuestsInfo[] quests = QuestsInfo.values();

    public NewGui(APIContext ctx) {
        this.ctx = ctx;
        this.profile = new Profile(ctx);

        setTitle("veedsQuester");

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        listPanel.setSize(800, 500);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        main.add(listPanel);
        main.add(bottomPanel);

        listPanel.add(getQuestList());
        listPanel.add(buttonPanel);
        listPanel.add(getSelectedQuestList());

        buttonPanel.add(getAddQuest());
        buttonPanel.add(getRemoveQuest());

        bottomPanel.add(getStartButton());

        add(main);

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

    private JList getQuestList() {
        if (questList == null) {
            questList = new JList<>(QuestsInfo.values());
            questList.setPreferredSize(new Dimension(200, 400));
        }
        return questList;
    }

    private JList getSelectedQuestList() {
        if (selectedQuestList == null) {
            QuestsInfo[] q = new QuestsInfo[8];
            selectedQuestList = new JList<>(q);
            selectedQuestList.setFixedCellHeight(50);
            selectedQuestList.setFixedCellWidth(200);
            questList.setPreferredSize(new Dimension(200, 400));
        }
        return selectedQuestList;
    }

    private JButton getAddQuest() {
        if (addQuest == null) {
            addQuest = new JButton("->");

            ActionListener actionListener = e -> {
                questList.remove(1);
            };

            addQuest.addActionListener(actionListener);
        }
        return addQuest;
    }

    private JButton getRemoveQuest() {
        if (removeQuest == null) {
            removeQuest = new JButton("<-");

            ActionListener actionListener = e -> {
                return;
            };

            removeQuest.addActionListener(actionListener);
        }
        return removeQuest;
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
        int size = selectedQuestList.getModel().getSize(); // 4

        QuestsInfo[] quests = new QuestsInfo[50];
        // Get all item objects
        for (int i = 0; i < size; i++) {
            quests[i] = (QuestsInfo) selectedQuestList.getModel().getElementAt(i);
        }

        Vars.quests = quests;
        return true;
    }

    //    private JTextField getProfileName() {
//        if (profileName == null) {
//            profileName = new JTextField();
//            profileName.setPreferredSize(new Dimension(90, 20));
//        }
//        return profileName;
//    }
//
//    private JButton getProfileButton() {
//        if (saveProfile == null) {
//            saveProfile = new JButton("Save Profile");
//
//            ActionListener actionListener = e -> {
//                QuestsInfo quest = (QuestsInfo) questSelector.getSelectedItem();
//                String name = profileName.getText();
//                profile.saveProfile(name, quest);
//            };
//
//            saveProfile.addActionListener(actionListener);
//        }
//        return saveProfile;
//    }
}
