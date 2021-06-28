package Tasks;

import Quests.CooksAssistant;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    CooksAssistant cooksAssistant;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;
        this.cooksAssistant = new CooksAssistant(ctx);
    }

    public void main() {
        if (Vars.currentQuest != null) {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT : {
                    cooksAssistant.main();
                }
            }
        } else {
            ctx.script().stop("All quests have been completed.");
        }

    }

}
