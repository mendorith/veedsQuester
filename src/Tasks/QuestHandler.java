package Tasks;

import Quests.*;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    CooksAssistant cooksAssistant;
    DemonSlayer demonSlayer;
    GoblinDiplomacy goblinDiplomacy;
    TheRestlessGhost theRestlessGhost;
    RomeoAndJuliet romeoAndJuliet;
    SheepShearer sheepShearer;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;

        this.cooksAssistant = new CooksAssistant(ctx);
        this.demonSlayer = new DemonSlayer(ctx);
        this.goblinDiplomacy = new GoblinDiplomacy(ctx);
        this.theRestlessGhost = new TheRestlessGhost(ctx);
        this.romeoAndJuliet = new RomeoAndJuliet(ctx);
        this.sheepShearer = new SheepShearer(ctx);
    }

    public void main() {
        if (Vars.currentQuest != null) {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT : {
                    cooksAssistant.main();
                } case DEMON_SLAYER: {
                    demonSlayer.main();
                }case GOBLIN_DIPLOMACY: {
                    goblinDiplomacy.main();
                } case THE_RESTLESS_GHOST: {
                    theRestlessGhost.main();
                } case ROMEO_AND_JULIET: {
                    romeoAndJuliet.main();
                } case SHEEP_SHEARER: {
                    sheepShearer.main();
                }
            }
        } else {
            ctx.script().stop("All quests have been completed.");
        }

    }

}