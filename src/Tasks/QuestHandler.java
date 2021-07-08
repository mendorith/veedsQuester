package Tasks;

import Quests.*;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    CooksAssistant cooksAssistant;
    RomeoAndJuliet romeoAndJuliet;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;

        this.cooksAssistant = new CooksAssistant(ctx);
        this.romeoAndJuliet = new RomeoAndJuliet(ctx);

    }

    public void main() {
        if (Vars.currentQuest != null) {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT : {
                    cooksAssistant.main();
                } case ROMEO_AND_JULIET: {
                    romeoAndJuliet.main();
                }
            }
        } else {
            ctx.script().stop("All quests have been completed.");
        }

    }

}
