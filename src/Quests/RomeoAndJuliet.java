package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;

public class RomeoAndJuliet extends Quest{
    private final Area varrockSquare = new Area(3206, 3435, 3220, 3422);
    private final Area balcony = new Area(1, 3156, 3426, 3160, 3425);
    private final Area church = new Area(3253, 3483, 3256, 3480);
    private final Area berries = new Area(3268, 3374, 3275, 3364);
    private final Area potionShop = new Area(3193, 3405, 3197, 3403);

    public RomeoAndJuliet(APIContext ctx) {
        super(ctx);
    }

    @Override
    public void main() {
        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
            Vars.currentQuest = null;
            return;
        }

        switch (getStage(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
            case 0 :
                // Starts quest if not started already
                Vars.State = "Starting quest.";
                startQuest();
                break;
            case 10 :
                // If need to talk to juliet
                Vars.State = "Talking to Juliet.";
                talkToJuliet();
                break;
            case 20 :
                // If need to give letter to romeo
                Vars.State = "Giving letter to Romeo.";
                giveRomeoLetter();
                break;
            case 30 :
                // If need to talk to lawrence
                Vars.State = "Talking to Lawrence.";
                talkToLawrence();
                break;
            case 40 :
                // If need to talk to apothecary
                Vars.State = "Getting potion from Apothecary.";
                if (ctx.inventory().contains(753)) {
                    talkToApothecary();
                } else {
                    getBerries();
                }
                break;
            case 50 :
                Vars.State = "Giving potion to Juliet.";
                if (!ctx.inventory().contains(756)) {
                    // Get potion if it's not in inventory
                    talkToApothecary();
                } else {
                    //If need to give juliet potion
                    if (ctx.vars().getVarbit(IQuestAPI.QuestVarbits.CUTSCENE.getId()) == 1) {
                        cutscene();
                        return;
                    }
                    talkToJuliet();
                }
                break;
            case 60 :
                // Final convo with romeo
                Vars.State = "Finishing quest.";
                if (ctx.vars().getVarbit(IQuestAPI.QuestVarbits.CUTSCENE.getId()) == 1) {
                    cutscene();
                    return;
                }
                talkToRomeo();
                break;
        }
    }

    private void talkToRomeo() {
        talkTo(5037, varrockSquare, new String[] {});
    }

    private void talkToApothecary() {
        String[] chatOptions = {"Talk about something else.", "Talk about Romeo & Juliet."};
        talkTo(5036, potionShop, chatOptions);
    }

    private void getBerries() {
        if (ctx.calculations().distanceTo(berries.getCentralTile()) < 6) {
            SceneObject s = ctx.objects().query().id(23625).results().nearest();
            if (s != null) {
                if (!s.isVisible()) {
                    ctx.camera().turnTo(s);
                }
                s.click();
                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }
        } else {
            ctx.webWalking().walkTo(berries.getCentralTile());
        }
    }

    private void talkToLawrence() {
        talkTo(5038, church, new String[] {});
    }

    private void giveRomeoLetter() {
        talkTo(5037, varrockSquare, new String[] {});
    }

    private void talkToJuliet() {
        talkTo(5035, balcony, new String[] {});
    }

    private void startQuest() {
        String[] chatOptions = {"Yes, I have seen her actually!", "Yes."};
        talkTo(5037, varrockSquare, chatOptions);
    }
}