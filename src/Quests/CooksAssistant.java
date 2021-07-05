package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IDialogueAPI;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class CooksAssistant {
    APIContext ctx;
    QuestMethods qm;

    private enum Locations {
        CHICKEN_COOP(new Area(3170, 3300, 3184, 3290)),
        COW_PASTURE(new Area(3251, 3276, 3255, 3273)),
        GRAIN_FIELD(new Area(3157, 3300, 3162, 3295)),
        MILL_UPPPER(new Area(2, 3163, 3310, 3170, 3303)),
        MILL_LOWER(new Area(3162, 3310, 3170, 3303)),
        LUMBRIDGE_CASTLE(new Area(3205, 3217, 3211, 3212)),
        LUMBRIDGE_CELLAR(new Area(3216, 9625, 3213, 9623))
        ;

        final Area area;

        Locations(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }
    }

    private boolean gatheredItems = false;
    private final String[] requirements = {"Bucket", "Pot", "Egg", "Bucket of milk", "Pot of flour"};

    public CooksAssistant(APIContext ctx) {
        this.ctx = ctx;
        this.qm = new QuestMethods(ctx);
    }

    public void main() {
        Vars.State = "Doing quest Cooks Assistant";
        if (ctx.quests().isCompleted(IQuestAPI.Quest.COOKS_ASSISTANT)) {
            Vars.currentQuest = null;
        } else if (!ctx.quests().isStarted(IQuestAPI.Quest.COOKS_ASSISTANT)) {
            startQuest();
        } else if (!gatheredItems) {
            getRequirements();
        } else {
            giveStuff();
        }
    }

    private void giveStuff() {
        NPC n = ctx.npcs().query().nameMatches("Cook").results().first();
        if (n != null) {
            if (ctx.inventory().contains(requirements)) {
                if (n.click()) {
                    Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                }
            }
        } else if (ctx.quests().isCompleted(IQuestAPI.Quest.COOKS_ASSISTANT)) {
            ctx.script().stop("Quest cooks assistant has been completed!");
        } else if (ctx.dialogues().canContinue()) {
            ctx.dialogues().selectContinue();
        }
        else {
            ctx.webWalking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getCentralTile());
        }
    }

    private void startQuest() {
        NPC n = ctx.npcs().query().id(4696).results().first();
        if (n != null) {
            if (!ctx.dialogues().isDialogueOpen()) {
                if (n.interact("Talk-to"))
                    Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            } else {
                if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION))
                    ctx.dialogues().selectOption("What's wrong?");
                else if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION))
                    ctx.dialogues().selectOption("Yes.");
                else {
                    if(ctx.dialogues().canContinue())
                        ctx.dialogues().selectContinue();
                }
            }
        } else {
            ctx.webWalking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getCentralTile());
        }
    }

    private void milkCow() {
        NPC n = ctx.npcs().query().id(1172).reachable().results().nearest();
        if (n != null) {
            if (ctx.inventory().contains("Bucket"))
                if (n.interact("Milk"))
                    Time.sleep(1_500, () -> ctx.inventory().contains(1927));
        } else ctx.webWalking().walkTo(Locations.COW_PASTURE.getArea().getCentralTile());
    }

    private boolean grainInHopper = false;
    private boolean pulledLever = false;
    private boolean collectedGrain = false;
    public void makeFlour() {
        if (collectedGrain) {
            if (pulledLever) {
                SceneObject chute = ctx.objects().query().id(1781).results().first();
                if (chute != null) {
                    if (chute.click()) {
                        Time.sleep(1_000, () -> ctx.inventory().contains("Pot of flour"));
                    }
                }
                else ctx.webWalking().walkTo(Locations.MILL_LOWER.getArea().getCentralTile());
            } else {
                SceneObject lever = ctx.objects().query().id(24964).results().first();
                if (lever != null) {
                    if (grainInHopper) {
                        System.out.println("test");
                        if (lever.click()) {
                            Time.sleep(1_000, () -> !ctx.localPlayer().isAnimating());
                            pulledLever = true;
                        }
                    } else {
                        if (!ctx.inventory().contains("Grain")) {
                            grainInHopper = true;
                        } else {
                            SceneObject hopper = ctx.objects().query().id(24961).results().first();
                            if (hopper != null)
                                if (hopper.click())
                                    Time.sleep(1_000, () -> !ctx.inventory().contains("Grain"));
                        }
                    }
                } else ctx.webWalking().walkTo(Locations.MILL_UPPPER.getArea().getCentralTile());
            }
        } else if (!collectedGrain) {
            SceneObject s = ctx.objects().query().id(15508).reachable().results().nearest();
            if (s != null) {
                if (s.click())
                    Time.sleep(1_000, () -> ctx.inventory().contains("Grain"));
            } else ctx.webWalking().walkTo(Locations.GRAIN_FIELD.getArea().getCentralTile());
        }
    }

    private void getRequirements() {
        if (!ctx.inventory().contains("Egg")) {
            qm.pickupItem(Locations.CHICKEN_COOP.getArea(), "Egg");
        } else if (!ctx.inventory().contains(1927)) {
            if (!ctx.inventory().contains("Bucket")) {
                qm.pickupItem(Locations.LUMBRIDGE_CELLAR.getArea(), "Bucket");
            } else {
                milkCow();
            }
        } else if (!ctx.inventory().contains("Pot of flour")) {
            if (!ctx.inventory().contains("Pot")) {
                qm.pickupItem(Locations.LUMBRIDGE_CASTLE.getArea(), "Pot");
            } else if (ctx.inventory().contains("Pot")) {
                makeFlour();
            }
        } else {
            gatheredItems = true;
        }
    }
}