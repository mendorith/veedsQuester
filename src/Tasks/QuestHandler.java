package Tasks;

import Quests.*;
import com.epicbot.api.shared.APIContext;
import data.Vars;

public class QuestHandler {
    APIContext ctx;

    CooksAssistant cooksAssistant;
    DemonSlayer demonSlayer;
    TheRestlessGhost theRestlessGhost;
    RomeoAndJuliet romeoAndJuliet;
    SheepShearer sheepShearer;
    ShieldOfArrav shieldOfArrav;

    public QuestHandler(APIContext ctx) {
        this.ctx = ctx;

        this.cooksAssistant = new CooksAssistant(ctx);
        this.demonSlayer = new DemonSlayer(ctx);
        this.theRestlessGhost = new TheRestlessGhost(ctx);
        this.romeoAndJuliet = new RomeoAndJuliet(ctx);
        this.sheepShearer = new SheepShearer(ctx);
        this.shieldOfArrav = new ShieldOfArrav(ctx);
    }

    public void main() {
            switch (Vars.currentQuest) {
                case COOKS_ASSISTANT : {
                    cooksAssistant.main();
                } case DEMON_SLAYER: {
                    demonSlayer.main();
                } case THE_RESTLESS_GHOST: {
                    theRestlessGhost.main();
                } case ROMEO_AND_JULIET: {
                    romeoAndJuliet.main();
                } case SHEEP_SHEARER: {
                    sheepShearer.main();
                } case SHIELD_OF_ARRAV: {
                    shieldOfArrav.main();
                }
            }
    }

}
