package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import data.Vars;

public class XMarksTheSpot extends Quest{

    Area spadeLocation = new Area(3120, 3360, 3123, 3357);
    Area lumbridgePub = new Area(3228, 3241, 3232, 3239);
    Tile firstSpot = new Tile(3230, 3209, 0);
    Tile secondSpot = new Tile(3203, 3212, 0);
    Tile thirdSpot = new Tile(3108, 3264, 0);
    Tile fourthSpot = new Tile(3077, 3260, 0);
    Area dock = new Area(3051, 3248, 3055, 3245);

    public XMarksTheSpot(APIContext ctx) {
        super(ctx);
    }

    @Override
    public void main() {
        if (ctx.quests().isCompleted(IQuestAPI.Quest.X_MARKS_THE_SPOT)) {
            Vars.currentQuest = null;
        }

        System.out.println(getStage(IQuestAPI.Quest.X_MARKS_THE_SPOT));

        switch (getStage(IQuestAPI.Quest.X_MARKS_THE_SPOT)) {
            case 0:
                if (!ctx.inventory().contains(952)) {
                    pickupItem(spadeLocation, 952);
                } else {
                    String[] chatOptions = {"I'm looking for a quest.", "Yes."};
                    talkTo(8484, lumbridgePub, chatOptions);
                }
                break;
            case 1:
                talkTo(8484, lumbridgePub, new String[] {});
                break;
            case 2:
                digSpot(firstSpot);
                break;
            case 3:
                digSpot(secondSpot);
                break;
            case 4:
                digSpot(thirdSpot);
                break;
            case 5:
                digSpot(fourthSpot);
                break;
            case 6:
                talkTo(8484, dock, new String[] {});
                break;
            case 7:
                talkTo(8484, dock, new String[] {});
                break;
        }
    }

    private void digSpot(Tile tile) {
        System.out.println(ctx.localPlayer().getLocation());
        System.out.println(tile);
        if (tile.distanceTo(ctx) < 1) {
            System.out.println("digging");
            ctx.inventory().getItem(952).interact("Dig");
        } else {
            ctx.webWalking().walkTo(tile);
        }
    }
}
