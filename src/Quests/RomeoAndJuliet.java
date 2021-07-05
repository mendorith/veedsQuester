package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.methods.IQuestAPI;

public class RomeoAndJuliet {
    APIContext ctx;

    private boolean talkToRomeo = true;
    private boolean talkToJuliet = true;
    private boolean giveLetterToRomeo = true;
    private boolean talkToLawrence = true;
    private boolean talkToApothecary = true;
    private boolean givePotionToJuliet = true;

    public RomeoAndJuliet(APIContext ctx) { this.ctx = ctx; }

    public void main() {

        System.out.println(getStage(IQuestAPI.Quest.ROMEO_AND_JULIET));
    }

    private int getStage(IQuestAPI.Quest quest){
        if(quest.getVarPlayer() != null){
            return ctx.vars().getVarp(quest.getVarPlayer().getId());
        } else if(quest.getVarbit() != null){
            return ctx.vars().getVarbit(quest.getVarbit().getId());
        }
        return -1;
    }
}


//// If quest is finished move on to next quest
//        if (ctx.quests().isCompleted(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
//                return;
//                }
//                // If quest is not started start quest
//                if (!ctx.quests().isStarted(IQuestAPI.Quest.ROMEO_AND_JULIET)) {
//                return;
//                }
//                // If need to talk to romeo talk to him
//                if (talkToRomeo) {
//                return;
//                }
//                // If need to talk to juliet talk to her
//                if (talkToJuliet) {
//                return;
//                }
//                // If need to give letter to romeo give him the letter
//                if (giveLetterToRomeo) {
//                return;
//                }
//                // If need to talk to lawrence talk to him
//                if (talkToLawrence) {
//                return;
//                }
//                // If need to talk to apothecary talk to him
//                if (talkToApothecary) {
//                return;
//                }
//                // If need to give potion to juliet give her the potion
//                if (givePotionToJuliet) {
//                return;
//                }