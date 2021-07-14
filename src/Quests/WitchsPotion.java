package Quests;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class WitchsPotion extends Quest {

    public WitchsPotion(APIContext ctx) {
        super(ctx);
    }

    Area hettyHouse = new Area(2965, 3208, 2970, 3203);
    Area ratHouse = new Area(2953, 3205, 2960, 3202);
    Area magicShop = new Area(3011, 3261, 3016, 3256);
    Area giantRats = new Area(2993, 3196, 3007, 3190);
    Area rangeHouse = new Area(2963, 3213, 2970, 3209);
    Area onionField = new Area(2945, 3254, 2955, 3247);

    @Override
    public void main() {
        SceneObject door = ctx.objects().query().id(1535).actions("Open").located(new Tile(2964, 3206, 0)).results().nearest();

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.WITCHS_POTION))
            Vars.currentQuest = null;

        switch (getStage(IQuestAPI.Quest.WITCHS_POTION)) {
            case 0:
                Vars.State = "Starting the Quest.";
                if (door != null) {
                    door.interact();
                    return;
                }
                String[] chatOptions = {"I am in search of a quest.", "Yes, help me become one with my darker side."};
                talkTo(4619, hettyHouse, chatOptions);
                break;
            case 1:
                if (!ctx.inventory().containsAll(2146,300,221,1957))
                    getRequirements();
                else {
                    if (door != null)
                        door.interact();
                    else talkTo(4619, hettyHouse);
                } break;
            case 2:
                interactObject(hettyHouse, 2024, "Drink From");
                if (ctx.dialogues().canContinue())
                    ctx.dialogues().selectContinue();
                break;

        }
    }

    private void getRequirements() {
        NPC rat = ctx.npcs().query().id(2855).results().nearest();
        if (!ctx.inventory().contains(300)) {
            Vars.State = "Getting the Rat Tail.";
            if (rat != null && rat.canReach(ctx)) {
                rat.interact();
                Time.sleep(10_000, rat::isDead);
                pickupItem(ratHouse, 300);
            } else ctx.webWalking().walkTo(ratHouse.getCentralTile());
        } else if (!ctx.inventory().contains(1957)) {
            Vars.State = "Getting the Onion.";
            interactObject(onionField, 3366, "Pick");
        } else if (!ctx.inventory().contains(221)) {
            Vars.State = "Getting the eye of newt.";
            if (!ctx.inventory().contains(995)) {
                withdraw("Coins", 10);
            } else buyItem(magicShop, 5905, "Eye of Newt");
        } else if (!ctx.inventory().contains(2146)) {
            Vars.State = "Getting the burnt meat.";
            NPC giantRat = ctx.npcs().query().id(2859, 2860).results().nearest();
            if (!ctx.inventory().contains(2134, 2142)) {
                if (giantRat != null && giantRat.canReach(ctx)) {
                    giantRat.interact();
                    Time.sleep(10_000, giantRat::isDead);
                    pickupItem(giantRats, 2134);
                } else ctx.webWalking().walkTo(giantRats.getCentralTile());
            } else if (ctx.inventory().contains(2134, 2142)) {
                SceneObject range = ctx.objects().query().id(9682).results().first();
                if (range != null) {
                    ctx.inventory().interactItem("Use", 2134);
                    range.interact();
                    if (!ctx.inventory().contains(2146)) {
                        ctx.inventory().interactItem("Use", 2142);
                        range.interact();
                    }
                } else ctx.webWalking().walkTo(rangeHouse.getCentralTile());
            }
        }
    }
}
