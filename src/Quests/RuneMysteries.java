package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class RuneMysteries extends Quest {

    public RuneMysteries(APIContext ctx) {
        super(ctx);
    }

    Area dukeRoom = new Area(1,3208, 3224, 3213, 3218);

    @Override
    public void main() {

        System.out.println(getStage(IQuestAPI.Quest.RUNE_MYSTERIES));

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.RUNE_MYSTERIES))
            Vars.currentQuest = null;

        switch (getStage(IQuestAPI.Quest.RUNE_MYSTERIES)) {

            case 0:
                Vars.State = "Going to start the Quest.";
                String [] chatOptions = {"Have you any quests for me?", "Yes."};
                talkTo(815,dukeRoom,chatOptions);
            case 1:

        }
    }
}
