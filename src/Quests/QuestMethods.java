package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GroundItem;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;

public class QuestMethods {
    APIContext ctx;

    public QuestMethods(APIContext ctx) {
        this.ctx = ctx;
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
                    NPC n = ctx.npcs().query().nameMatches("Shop keeper").results().first();
                    if (n != null) {
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

    public void pickupItem(Area location, String item) {
        if (location.contains(ctx.localPlayer().getLocation())) {
            GroundItem i = ctx.groundItems().query().nameMatches(item).reachable().results().nearest();
            if (i != null) {
                if (i.interact("Take")) {
                    Time.sleep(1_000, () -> ctx.inventory().contains(item));
                }
            }
        } else if (!location.contains(ctx.localPlayer().getLocation())) {
            ctx.webWalking().walkTo(location.getCentralTile());
        }
    }
}