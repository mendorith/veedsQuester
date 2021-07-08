package Tasks;

import Quests.*;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    Quest cooksAssistant;
    Quest goblinDiplomacy;
    Quest romeoAndJuliet;
    Quest xMarksTheSpot;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;

        this.cooksAssistant = new CooksAssistant(ctx);
        this.goblinDiplomacy = new GoblinDiplomacy(ctx);
        this.romeoAndJuliet = new RomeoAndJuliet(ctx);
        this.xMarksTheSpot = new XMarksTheSpot(ctx);

    }

    public void main() {
        if (Vars.currentQuest != null) {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT :
                    cooksAssistant.main();
                    break;
                case GOBLIN_DIPLOMACY:
                    goblinDiplomacy.main();
                case ROMEO_AND_JULIET:
                    romeoAndJuliet.main();
                    break;
                case X_MARKS_THE_SPOT:
                    xMarksTheSpot.main();
                    break;
            }
        } else {
            ctx.script().stop("All quests have been completed.");
        }

    }

}
