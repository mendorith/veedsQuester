package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class SheepShearer extends Quest {

    public SheepShearer(APIContext ctx) {
        super(ctx);
    }

    Area Fred_House = new Area(3188, 3275, 3192, 3270);
    Area Sheep_Pasture = new Area(3193, 3276, 3204, 3267);
    Area Spinning_Wheel = new Area(1, 3208, 3214, 3211, 3212);

    @Override
    public void main() {
        if (ctx.quests().isCompleted(IQuestAPI.Quest.SHEEP_SHEARER))
            Vars.currentQuest = null;

        System.out.println(getStage(IQuestAPI.Quest.SHEEP_SHEARER));

        switch (getStage(IQuestAPI.Quest.X_MARKS_THE_SPOT)) {
            case 0:
                if (!ctx.inventory().contains(1735)) {
                    pickupItem(Fred_House, 1735);
                } else {
                    String[] chatOptions = {"I'm looking for a quest.", "Yes."};
                    talkTo(732, Fred_House, chatOptions);
                }
                break;
            case 1:
                NPC sheep = ctx.npcs().query().id(2768,2699,2693).results().nearest();
                if (ctx.inventory().getCount(1737) < 20) {
                    if (sheep != null) {
                        if (sheep.interact("Shear"))
                            Time.sleep(3_000, () -> ctx.inventory().contains(1737));
                    }
                    ctx.webWalking().walkTo(Sheep_Pasture.getCentralTile());
                }
        }
    }
}