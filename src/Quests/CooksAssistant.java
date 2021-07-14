package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class CooksAssistant extends Quest {
    public CooksAssistant(APIContext ctx) {
        super(ctx);
    }

    private enum Locations {
        CHICKEN_COOP(new Area(3170, 3300, 3184, 3290)),
        COW_PASTURE(new Area(3251, 3276, 3255, 3273)),
        GRAIN_FIELD(new Area(3157, 3300, 3162, 3295)),
        MILL_UPPPER(new Area(2, 3163, 3310, 3170, 3303)),
        MILL_LOWER(new Area(3162, 3310, 3170, 3303)),
        LUMBRIDGE_CASTLE(new Area(3206, 3216, 3211, 3213)),
        LUMBRIDGE_CELLAR(new Area(3208, 9616, 3218, 9624))
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

    @Override
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
        if (!ctx.npcs().query().nameMatches("Cook").results().isEmpty() && ctx.npcs().query().nameMatches("Cook").results().first().canReach(ctx)) {
            if (ctx.inventory().contains(requirements)) {
                NPC n = ctx.npcs().query().nameMatches("Cook").results().first();
                if (n != null) {
                    if (n.interact("Talk-to")) {
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                    }
                }
            } else if (ctx.dialogues().canContinue()) {
                ctx.dialogues().selectContinue();
            }
        } else {
            ctx.webWalking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getCentralTile());
        }
    }

    private boolean x = false;
    private boolean y = false;
    private void startQuest() {
        if (!ctx.npcs().query().nameMatches("Cook").results().isEmpty() && ctx.npcs().query().nameMatches("Cook").results().first().canReach(ctx)) {
            if (!ctx.dialogues().isDialogueOpen()) {
                NPC n = ctx.npcs().query().nameMatches("Cook").results().first();
                if (n != null) {
                    if (n.interact("Talk-to")) {
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                    }
                }
            } else {
                if (ctx.dialogues().canContinue()) {
                    ctx.dialogues().selectContinue();
                } else if (!x) {
                    ctx.dialogues().selectOption(0);
                    x = true;
                } else if (!y){
                    ctx.dialogues().selectOption("Yes.");
                    y = true;
                }
            }
        } else {
            ctx.webWalking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getCentralTile());
        }
    }

    private void milkCow() {
        if (!ctx.npcs().query().id(1172).results().isEmpty() && ctx.npcs().query().id(1172).results().first().canReach(ctx)) {
            if (ctx.inventory().contains("Bucket")) {
                NPC n = ctx.npcs().query().id(1172).reachable().results().nearest();
                if (n != null) {
                    if (n.interact("Milk")) {
                        Time.sleep(1_500, () -> ctx.inventory().contains(1927));
                    }
                }
            }
        } else {
            ctx.webWalking().walkTo(Locations.COW_PASTURE.getArea().getCentralTile());
        }
    }

    private boolean grainInHopper = false;
    private boolean pulledLever = false;
    private boolean collectedGrain = false;
    public void makeFlour() {
        if (collectedGrain) {
            if (pulledLever) {
                if (Locations.MILL_LOWER.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject s = ctx.objects().query().id(1781).results().first();
                    if (s != null) {
                        if (s.click()) {
                            Time.sleep(1_000, () -> ctx.inventory().contains("Pot of flour"));
                        }
                    }
                }
                else if (!Locations.MILL_LOWER.getArea().contains(ctx.localPlayer().getLocation())) {
                    ctx.webWalking().walkTo(Locations.MILL_LOWER.getArea().getCentralTile());
                }
            } else {
                if (Locations.MILL_UPPPER.getArea().contains(ctx.localPlayer().getLocation())) {
                    if (grainInHopper) {
                        SceneObject s = ctx.objects().query().id(24964).results().first();
                        if (s != null) {
                            if (s.click()) {
                                Time.sleep(1_000, () -> !ctx.localPlayer().isAnimating());
                                pulledLever = true;
                            }
                        }
                    } else {
                        if (!ctx.inventory().contains("Grain")) {
                            grainInHopper = true;
                        } else {
                            SceneObject s = ctx.objects().query().id(24961).results().first();
                            if (s != null) {
                                if (s.click()) {
                                    Time.sleep(1_000, () -> !ctx.inventory().contains("Grain"));
                                }
                            }
                        }
                    }
                } else if (!Locations.MILL_UPPPER.getArea().contains(ctx.localPlayer().getLocation())) {
                    ctx.webWalking().walkTo(Locations.MILL_UPPPER.getArea().getCentralTile());
                }
            }
        } else if (!collectedGrain) {
            if (ctx.inventory().contains("Grain")) {
                collectedGrain = true;
            } else {
                if (Locations.GRAIN_FIELD.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject s = ctx.objects().query().id(15508).reachable().results().nearest();
                    if (s != null) {
                        if (s.click()) {
                            Time.sleep(1_000, () -> ctx.inventory().contains("Grain"));
                        }
                    }
                } else if (!Locations.GRAIN_FIELD.getArea().contains(ctx.localPlayer().getLocation())) {
                    ctx.webWalking().walkTo(Locations.GRAIN_FIELD.getArea().getCentralTile());
                }
            }
        }
    }

    private void getRequirements() {
        if (!ctx.inventory().contains("Egg")) {
            pickupItem(Locations.CHICKEN_COOP.getArea(), 1944);
        } else if (!ctx.inventory().contains(1927)) {
            if (!ctx.inventory().contains("Bucket")) {
                pickupItem(Locations.LUMBRIDGE_CELLAR.getArea(), 1925);
            } else {
                milkCow();
            }
        } else if (!ctx.inventory().contains("Pot of flour")) {
            if (!ctx.inventory().contains("Pot")) {
                pickupItem(Locations.LUMBRIDGE_CASTLE.getArea(), 1931);
            } else if (ctx.inventory().contains("Pot")) {
                makeFlour();
            }
        } else {
            gatheredItems = true;
        }
    }
}