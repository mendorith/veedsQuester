package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class SheepShearer extends Quest {

    public SheepShearer(APIContext ctx) {
        super(ctx);
    }

    Area Fred_House = new Area(3184, 3278, 3192, 3270);
    Area Sheep_Pasture = new Area(3193, 3276, 3204, 3260);
    Area Spinning_Wheel = new Area(1, 3208, 3214, 3211, 3212);

    @Override
    public void main() {
        NPC sheep = ctx.npcs().query().id(2786,2699,2693).results().nearest();
        SceneObject Door = ctx.objects().query().id(13001).actions("Open").located(new Tile(3189, 3275, 0)).results().nearest();
        SceneObject upstairsDoor = ctx.objects().query().id(1543).actions("Open").located(new Tile(3207, 3214, 1)).results().nearest();
        if (ctx.quests().isCompleted(IQuestAPI.Quest.SHEEP_SHEARER))
            Vars.currentQuest = null;

        switch (getStage(IQuestAPI.Quest.SHEEP_SHEARER)) {
            case 0:
                Vars.State = "Starting the Quest.";
                if (Door != null) {
                    Door.interact();
                    return;}
                String[] chatOptions = {"I'm looking for a quest.", "Yes."};
                talkTo(732, Fred_House, chatOptions);
                break;
            case 1:
                if (ctx.inventory().getCount(1759) < 20) {
                    if (!ctx.inventory().contains(1735)) {
                        Vars.State = "Getting some Shears.";
                        if (Door != null)
                            Door.interact();
                        else pickupItem(Fred_House,1735);
                    } else if (ctx.inventory().getCount(1737) < 20 && !ctx.inventory().contains(1759)) {
                        Vars.State = "Shearing the sheep.";
                        if (sheep != null && sheep.canReach(ctx)) {
                            sheep.interact();
                            Time.sleep(3_000, () -> !ctx.localPlayer().isAnimating() && !ctx.localPlayer().isMoving());
                        } ctx.webWalking().walkTo(Sheep_Pasture.getCentralTile());
                    } else {
                        Vars.State = "Spinning the Wool.";
                        interactObject(Spinning_Wheel, 14889, "Spin");
                        ctx.keyboard().holdKey(32, 40_000, ctx.inventory().getCount(1759) == 20);
                    }
                } else{
                    Vars.State = "Turning in the Quest.";
                    if(upstairsDoor != null)
                        upstairsDoor.interact();
                    else {
                        if (Door != null)
                            Door.interact();
                        else {
                            String[] turnIn = {"I need to talk to you about shearing these sheep!"};
                            talkTo(732, Fred_House, turnIn);
                        }
                    }
                }
                break;
            case 20:
                if (ctx.dialogues().canContinue())
                    ctx.dialogues().selectContinue();
                break;
        }
    }
}