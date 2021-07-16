package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GroundItem;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.entity.WidgetChild;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class Quest {
    APIContext ctx;

    public Quest(APIContext ctx) {
        this.ctx = ctx;
    }

    public void main() {
        System.out.println("Quest not yet completed");
        Vars.quests = null;
    }




    // Interaction methods
    public void interactObject(Area location, int id, String interaction) {
        SceneObject s = ctx.objects().query().id(id).results().first();
        if (s != null && s.canReach(ctx)) {
            if (!s.isVisible()) {
                ctx.camera().turnTo(s);
            }
            if (interaction == null) {
                s.interact();
            } else {
                s.interact(interaction);
            }
            Time.sleep(3_000, () -> !ctx.localPlayer().isAnimating() && !ctx.localPlayer().isMoving());
        } else {
            ctx.webWalking().walkTo(location.getCentralTile());
        }
    }
    // Overload
    public void interactObject(Area location, int id) {
        interactObject(location, id, null);
    }

    public void talkTo(int id, Area location, String[] chatOptions) {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
                Time.sleep(4_000, () -> ctx.dialogues().isDialogueOpen());
            }
            if (ctx.dialogues().getOptions() != null) {
                handleOptions(chatOptions);
            }
            return;
        }
        if (!ctx.npcs().query().id(id).results().isEmpty()) {
            NPC n = ctx.npcs().query().id(id).results().nearest();
            if (n != null) {
                if (!n.isVisible()) {
                    ctx.camera().turnTo(n);
                }
                n.interact("Talk-to");
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(location.getCentralTile());
        }
    }
    //Overload
    public void talkTo(int id, Area location) {
        talkTo(id, location, new String[] {});
    }

    public void withdraw(String item, int amount) {
        if (ctx.bank().isReachable()) {
            if (ctx.bank().isOpen()) {
                if (ctx.bank().contains(item)) {
                    ctx.bank().withdraw(amount, item);
                } else {
                    ctx.script().stop("You don't have " + item + " in your bank.");
                }
            } else if (!ctx.bank().isOpen()) {
                ctx.bank().open();
            }
        } else  if (!ctx.bank().isReachable()) {
            ctx.webWalking().walkToBank();
        }
    }

    public void buyItem(Area location, String item) {
        if (ctx.inventory().contains("Coins")) {
            if (location.contains(ctx.localPlayer().getLocation())) {
                if (!ctx.store().isOpen()) {
                    NPC n = ctx.npcs().query().nameMatches("Shop keeper").results().nearest();
                    if (n != null) {
                        if (!n.isVisible()) {
                            ctx.camera().turnTo(n);
                        }
                        if (n.interact("Trade")) {
                            Time.sleep(1_000, () -> ctx.store().isOpen());
                        }
                    }
                } else if (ctx.store().isOpen()) {
                    ctx.store().buyOne(item);
                }
            } else if (!location.contains(ctx.localPlayer().getLocation())) {
                ctx.webWalking().walkTo(location.getRandomTile());
            }
        } else if (!ctx.inventory().contains("Coins")) {
            withdraw(item, 500);
        }
    }

    public void pickupItem(Area location, int item) {
        GroundItem i = ctx.groundItems().query().id(item).reachable().results().nearest();
        if (i != null && i.canReach(ctx)) {
            if (!i.isVisible()) {
                ctx.camera().turnTo(i);
            }
            if (i.interact("Take")) {
                Time.sleep(1_000, () -> ctx.inventory().contains(item));
            }
        } else if (!location.contains(ctx.localPlayer().getLocation())) {
            ctx.webWalking().walkTo(location.getCentralTile());
        }
    }




    // Dialogue methods
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




    // Stage methods
    public void cutscene() {
        if (ctx.dialogues().isDialogueOpen()) {
            if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
            }
        }
    }

    public int getStage(IQuestAPI.Quest quest){
        if(quest.getVarPlayer() != null){
            return ctx.vars().getVarp(quest.getVarPlayer().getId());
        } else if(quest.getVarbit() != null){
            return ctx.vars().getVarbit(quest.getVarbit().getId());
        }
        return -1;
    }
}