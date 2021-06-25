package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GroundItem;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IDialogueAPI;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CooksAssistant {
    APIContext ctx;

    private enum Locations {
        LUMBRIDGE_STORE(new Area(3214, 3251, 3208, 3242)),
        CHICKEN_COOP(new Area(3170, 3300, 3184, 3290)),
        COW_PASTURE(new Area(3251, 3278, 3258, 3270)),
        GRAIN_FIELD(new Area(3157, 3299, 3162, 3295)),
        MILL_UPPPER(new Area(2, 3163, 3310, 3170, 3303)),
        MILL_LOWER(new Area(3162, 3310, 3170, 3303)),
        LUMBRIDGE_CASTLE(new Area(3205, 3217, 3211, 3212))
        ;

        final Area area;

        Locations(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }
    }

    private boolean gatheredItems = true;
    private final String[] requirements = {"Bucket", "Pot", "Egg", "Bucket of milk", "Pot of flour"};

    public CooksAssistant(APIContext ctx) {
        this.ctx = ctx;
    }

    public void main() {
        if (!ctx.quests().isStarted(IQuestAPI.Quest.COOKS_ASSISTANT)) {
            startQuest();
        } else if (!gatheredItems) {
            getRequirements();
        } else {
            giveStuff();
        }
    }

    private void giveStuff() {
        if (Locations.LUMBRIDGE_CASTLE.getArea().contains(ctx.localPlayer().getLocation())) {
            if (ctx.inventory().contains(requirements)) {
                NPC n = ctx.npcs().query().nameMatches("Cook").results().first();
                if (n != null) {
                    if (n.click()) {
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                    }
                }
            } else if (ctx.quests().isCompleted(IQuestAPI.Quest.COOKS_ASSISTANT)) {
                ctx.script().stop("Quest cooks assistant has been completed!");
            } else {
                ctx.dialogues().selectContinue();
            }
        } else {
            ctx.walking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getRandomTile());
        }
    }

    private boolean x = false;
    private boolean y = false;
    private void startQuest() {
        if (Locations.LUMBRIDGE_CASTLE.getArea().contains(ctx.localPlayer().getLocation())) {
            if (!ctx.dialogues().isDialogueOpen()) {
                NPC n = ctx.npcs().query().nameMatches("Cook").results().first();
                if (n != null) {
                    if (n.click()) {
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                    }
                }
            } else {
                if (ctx.dialogues().getText().equalsIgnoreCase("what am i to do?")) {
                    ctx.dialogues().selectContinue();
                } else if (!x) {
                    ctx.dialogues().selectOption(0);
                    x = true;
                } else if (ctx.dialogues().getText().equalsIgnoreCase("what's wrong?")) {
                    ctx.dialogues().selectContinue();
                } else if (ctx.dialogues().getText().toLowerCase().startsWith("oh dear, oh dear")) {
                    ctx.dialogues().selectContinue();
                } else if (ctx.dialogues().getText().toLowerCase().startsWith("i've forgotten")) {
                    ctx.dialogues().selectContinue();
                } else if (!y){
                    System.out.println("test");
                    ctx.dialogues().selectOption("Yes.");
                    y = true;
                } else if (ctx.dialogues().getText().toLowerCase().contains("yes, i'll help")) {
                    ctx.dialogues().selectContinue();
                } else if (ctx.dialogues().getText().toLowerCase().contains("oh thank you")) {
                    ctx.dialogues().selectContinue();
                }
            }
        } else {
            ctx.webWalking().walkTo(Locations.LUMBRIDGE_CASTLE.getArea().getRandomTile());
        }
    }

    private void withdraw(String item, int amount) {
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

    private void buyItem(Locations location, String item) {
        if (ctx.inventory().contains("Coins")) {
            if (location.getArea().contains(ctx.localPlayer().getLocation())) {
                if (!ctx.store().isOpen()) {
                    NPC n = ctx.npcs().query().nameMatches("Shop keeper").results().first();
                    if (n != null) {
                        if (n.interact("Trade")) {
                            Time.sleep(1_000, () -> ctx.store().isOpen());
                        }
                    }
                } else if (ctx.store().isOpen()) {
                    ctx.store().buyOne(item);
                }
            } else if (!location.getArea().contains(ctx.localPlayer().getLocation())) {
                ctx.webWalking().walkTo(location.getArea().getRandomTile());
            }
        } else if (!ctx.inventory().contains("Coins")) {
            withdraw(item, 500);
        }
    }

    private void pickupItem(Locations location, String item) {
        if (location.getArea().contains(ctx.localPlayer().getLocation())) {
            GroundItem i = ctx.groundItems().query().nameMatches(item).reachable().results().nearest();
            if (i != null) {
                if (i.interact("Take")) {
                    Time.sleep(1_000, () -> ctx.inventory().contains(item));
                }
            }
        } else if (!location.getArea().contains(ctx.localPlayer().getLocation())) {
            ctx.webWalking().walkTo(location.getArea().getRandomTile());
        }
    }

    private void milkCow() {
        if (Locations.COW_PASTURE.getArea().contains(ctx.localPlayer().getLocation())) {
            if (ctx.inventory().contains("Bucket")) {
                NPC n = ctx.npcs().query().nameMatches("Dairy cow").reachable().results().nearest();
                if (n != null) {
                    if (n.interact("Milk")) {
                        Time.sleep(1_500, () -> ctx.inventory().contains("Bucket of milk"));
                    }
                }
            }
        } else if(!Locations.COW_PASTURE.getArea().contains(ctx.localPlayer().getLocation())) {
            ctx.webWalking().walkTo(Locations.COW_PASTURE.getArea().getRandomTile());
        }
    }

    private boolean grainInHopper = false;
    private boolean pulledLever = false;
    private boolean collectedGrain = false;
    public void makeFlour() {
        if (collectedGrain) {
            if (pulledLever) {
                if (Locations.MILL_LOWER.getArea().contains(ctx.localPlayer().getLocation())) {
                    System.out.println("test");
                    SceneObject s = ctx.objects().query().id(1781).results().first();
                    if (s != null) {
                        if (s.click()) {
                            Time.sleep(1_000, () -> ctx.inventory().contains("Pot of flour"));
                        }
                    }
                }
                else if (!Locations.MILL_LOWER.getArea().contains(ctx.localPlayer().getLocation())) {
                    System.out.println("test");
                    ctx.webWalking().walkTo(Locations.MILL_LOWER.getArea().getCentralTile());
                }
            } else {
                if (Locations.MILL_UPPPER.getArea().contains(ctx.localPlayer().getLocation())) {
                    if (grainInHopper) {
                        System.out.println("test");
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
                    ctx.webWalking().walkTo(Locations.MILL_UPPPER.getArea().getRandomTile());
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
                    ctx.webWalking().walkTo(Locations.GRAIN_FIELD.getArea().getRandomTile());
                }
            }
        }
    }

    private void getRequirements() {
        if (!ctx.inventory().contains("Egg")) {
            pickupItem(Locations.CHICKEN_COOP, "Egg");
        } else if (!ctx.inventory().contains("Bucket of milk")) {
            if (!ctx.inventory().contains("Bucket")) {
                buyItem(Locations.LUMBRIDGE_STORE, "Bucket");
            } else {
                milkCow();
            }
        } else if (!ctx.inventory().contains("Pot of flour")) {
            if (!ctx.inventory().contains("Pot")) {
                buyItem(Locations.LUMBRIDGE_STORE, "Pot");
            } else {
                makeFlour();
            }
        } else {
            gatheredItems = true;
        }
    }
}
