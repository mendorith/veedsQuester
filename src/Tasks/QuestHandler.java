package Tasks;

import Quests.*;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    Quest cooksAssistant;
    Quest romeoAndJuliet;
    Quest xMarksTheSpot;
    Quest sheepShearer;
    Quest goblinDiplomacy;
    Quest runeMysteries;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;

        this.cooksAssistant = new CooksAssistant(ctx);
        this.romeoAndJuliet = new RomeoAndJuliet(ctx);
        this.xMarksTheSpot = new XMarksTheSpot(ctx);
        this.goblinDiplomacy = new GoblinDiplomacy(ctx);
        this.sheepShearer = new SheepShearer(ctx);
        this.runeMysteries = new RuneMysteries(ctx);

    }

    public void main() {
        if (Vars.currentQuest != null) {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT :
                    cooksAssistant.main();
                    break;
                case ROMEO_AND_JULIET:
                    romeoAndJuliet.main();
                    break;
                case X_MARKS_THE_SPOT:
                    xMarksTheSpot.main();
                    break;
                case GOBLIN_DIPLOMACY:
                    goblinDiplomacy.main();
                    break;
                case SHEEP_SHEARER:
                    sheepShearer.main();
                case RUNE_MYSTERIES:
                    runeMysteries.main();
            }
        } else {
            ctx.script().stop("All quests have been completed.");
        }

    }

}
