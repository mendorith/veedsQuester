package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import data.Vars;

public class RuneMysteries extends Quest {

    public RuneMysteries(APIContext ctx) {
        super(ctx);
    }

    Area dukeRoom = new Area(1,3208, 3224, 3213, 3218);
    Area wizardTower = new Area(3107, 9566, 3102, 9574);
    Area auburyShop = new Area(3255, 3403, 3250, 3399);

    @Override
    public void main() {

        System.out.println(getStage(IQuestAPI.Quest.RUNE_MYSTERIES));

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.RUNE_MYSTERIES))
            Vars.currentQuest = null;

        switch (getStage(IQuestAPI.Quest.RUNE_MYSTERIES)) {
            case 0:
                SceneObject dukeDoor = ctx.objects().query().id(1543).actions("Open").located(new Tile(3207, 3222, 1)).results().nearest();
                Vars.State = "Going to start the Quest.";
                if (dukeDoor != null)
                    dukeDoor.interact();
                else {
                    String [] dukeOptions = {"Have you any quests for me?", "Yes."};
                    talkTo(815,dukeRoom,dukeOptions);
                }
                break;
            case 1:
                Vars.State = "Going to talk to the head wizard.";
                String [] wizardOptions = {"I'm looking for the head wizard.", "Ok, here you are."};
                talkTo(5034,wizardTower,wizardOptions);
                break;
            case 2:
                if (ctx.dialogues().canContinue())
                    ctx.dialogues().selectContinue();
                else ctx.dialogues().selectOption("Yes, certainly.");
                break;
            case 3:
                Vars.State = "Taking the package to Aubury.";
                SceneObject towerDoor = ctx.objects().query().id(23972).actions("Open").located(new Tile(3107, 3162, 0)).results().nearest();
                String [] auburyOptions = {"I have been sent here with a package for you."};
                if (towerDoor != null)
                    towerDoor.interact();
                else talkTo(2886,auburyShop,auburyOptions);
                break;
            case 4:
                talkTo(2886,auburyShop);
                break;
            case 5:
                Vars.State = "Taking Aubury's Notes to the head wizard.";
                talkTo(5034,wizardTower);
                break;
        }
    }
}
