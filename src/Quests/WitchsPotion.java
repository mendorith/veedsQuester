package Quests;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import data.Vars;

public class WitchsPotion extends Quest {

    public WitchsPotion(APIContext ctx) {super(ctx);}

    @Override
    public void main() {

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.WITCHS_POTION))
            Vars.currentQuest = null;

        switch (getStage(IQuestAPI.Quest.WITCHS_POTION)) {
            case 0:
        }
    }
}
