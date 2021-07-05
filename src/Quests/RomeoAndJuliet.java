package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.entity.WidgetChild;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class RomeoAndJuliet {
    APIContext ctx;

    private final Area varrockSquare = new Area(3206, 3435, 3220, 3422);
    private final Area balcony = new Area(1, 3156, 3426, 3160, 3425);
    private final Area church = new Area(3253, 3483, 3256, 3480);
    private final Area berries = new Area(3268, 3374, 3275, 3364);
    private final Area potionShop = new Area(3193, 3405, 3197, 3403);

    public RomeoAndJuliet(APIContext ctx) { this.ctx = ctx; }

    public void main() {

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
            Vars.currentQuest = null;
            return;
        }

        // Starts quest if not started already
        if (!ctx.quests().isStarted(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
            System.out.println("test");
            startQuest();
            return;
        }

        switch (getStage(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
            case 10 :
                // If need to talk to juliet
                talkToJuliet();
                break;
            case 20 :
                // If need to give letter to romeo
                giveRomeoLetter();
                break;
            case 30 :
                // If need to talk to lawrence
                talkToLawrence();
                break;
            case 40 :
                // If need to talk to apothecary
                if (ctx.inventory().contains(753)) {
                    talkToApothecary();
                } else {
                    getBerries();
                }
                break;
            case 50 :
                if (!ctx.inventory().contains(756)) {
                    // Get potion if it's not in inventory
                    talkToApothecary();
                } else {
                    //If need to give juliet potion
                    if (ctx.vars().getVarbit(IQuestAPI.QuestVarbits.CUTSCENE.getId()) == 1) {
                        cutscene();
                        return;
                    }
                    talkToJuliet();
                }
                break;
            case 60 :
                // Final convo with romeo
                if (ctx.vars().getVarbit(IQuestAPI.QuestVarbits.CUTSCENE.getId()) == 1) {
                    cutscene();
                    return;
                }
                talkToRomeo();
                break;
        }
    }

    private void cutscene() {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
            }
        }
    }

    private void talkToRomeo() {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
                Time.sleep(4_000, () -> ctx.dialogues().isDialogueOpen());
            }
            return;
        }
        if (!ctx.npcs().query().id(5037).results().isEmpty()) {
            NPC n = ctx.npcs().query().id(5037).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(varrockSquare.getCentralTile());
        }
    }

    private void handleOptions(String[] chatOptions){
        String bestOption = getBestDialogOption(chatOptions);
        if(bestOption != null) {
            ctx.dialogues().selectOption(bestOption);
        }
    }

    protected String getBestDialogOption(String[] dialogOptions){
        for(String chat : dialogOptions){
            for(WidgetChild option : ctx.dialogues().getOptions()){
                if(option.getText().equals(chat)){
                    return chat;
                }
            }
        }
        return null;
    }

    private void talkToApothecary() {
        String[] chatOptions = {"Talk about something else.", "Talk about Romeo & Juliet."};
        if (!ctx.npcs().query().id(5036).results().isEmpty()) {
            if (ctx.dialogues().isDialogueOpen()) {
                if (ctx.dialogues().canContinue()) {
                    ctx.dialogues().selectContinue();
                }
                if (ctx.dialogues().getOptions() != null) {
                    handleOptions(chatOptions);
                }
                return;
            }
            NPC n = ctx.npcs().query().id(5036).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(potionShop.getCentralTile());
        }
    }

    private void getBerries() {
        if (ctx.calculations().distanceTo(berries.getCentralTile()) < 6) {
            SceneObject s = ctx.objects().query().id(23625).results().nearest();
            if (s != null) {
                if (!s.isVisible()) {
                    ctx.camera().turnTo(s);
                }
                s.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(berries.getCentralTile());
        }
    }

    private void talkToLawrence() {
        if (!ctx.npcs().query().id(5038).results().isEmpty()) {
            if (ctx.dialogues().isDialogueOpen()) {
                if (ctx.dialogues().canContinue()) {
                    ctx.dialogues().selectContinue();
                }
                return;
            }
            NPC n = ctx.npcs().query().id(5038).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(church.getCentralTile());
        }
    }

    private void giveRomeoLetter() {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
            }
            return;
        }
        if (!ctx.npcs().query().id(5037).results().isEmpty()) {
            NPC n = ctx.npcs().query().id(5037).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(varrockSquare.getCentralTile());
        }
    }

    private void talkToJuliet() {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
                Time.sleep(3_000, () -> ctx.dialogues().isDialogueOpen());
            }
            return;
        }
        if (!ctx.npcs().query().id(5035).results().isEmpty() && ctx.npcs().query().id(5035).results().nearest().canReach(ctx)) {
            NPC n = ctx.npcs().query().id(5035).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(balcony.getCentralTile());
        }
    }

    private void startQuest() {
        String[] chatOptions = {"Yes, I have seen her actually!", "Yes."};
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
            }
            if (ctx.dialogues().getOptions() != null) {
                handleOptions(chatOptions);
            }
            return;
        }
        if (!ctx.npcs().query().id(5037).results().isEmpty()) {
            NPC n = ctx.npcs().query().id(5037).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(varrockSquare.getCentralTile());
        }
    }

    private int getStage(IQuestAPI.Quest quest){
        if(quest.getVarPlayer() != null){
            return ctx.vars().getVarp(quest.getVarPlayer().getId());
        } else if(quest.getVarbit() != null){
            return ctx.vars().getVarbit(quest.getVarbit().getId());
        }
        return -1;
    }
}