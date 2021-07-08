package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.util.Random;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;


public class GoblinDiplomacy extends Quest {


    private final Area Aggie_House = (new Area(3083, 3261, 3089, 3256));
    private final Area Falador_Park =(new Area(3023, 3383, 3028, 3375));
    private final Area General_Crate =(new Area(2959, 3515, 2961, 3514));
    private final Area General_Hut =(new Area(2956, 3513, 2959, 3510));
    private final Area Ground_Level =(new Area(2953, 3498, 2959, 3496));
    private final Area Lumbridge_Field =(new Area(3186, 3269, 3192, 3265));
    private final Area Redberry_Bushes =(new Area(3265, 3375, 3277, 3367));
    private final Area Rimmington_Field =(new Area(2945, 3254, 2955, 3247));
    private final Area Upstairs =(new Area(2, 2955, 3497, 2955, 3497));
    private final Area Western_Hut =(new Area(2951, 3508, 2953, 3504));




    public GoblinDiplomacy(APIContext ctx) { super(ctx);}

    @Override
    public void main() {

        System.out.println(getStage(IQuestAPI.Quest.GOBLIN_DIPLOMACY));

        // Switches to next quest when completed
        if (ctx.quests().isCompleted(IQuestAPI.Quest.GOBLIN_DIPLOMACY)) {
            Vars.currentQuest = null;
            return;
        }

        switch (getStage(IQuestAPI.Quest.GOBLIN_DIPLOMACY)) {
            case 0 :
                // Starts quest if not started already
                startQuest();
                break;
            case 3:
                if (!ctx.inventory().contains(286,287,288)) {
                  if (!mail_dyed) {
                    if (!gathered_dyes) {
                      if (!gathered_ingredients) {
                        if (!gatheredMail) {
                          getMail();
                        } getIngredients();
                      } getDyes();
                    } dyeMail();
                  } give_Orange();
                } break;
                case 4:
                    give_Blue();
                    break;
                case 5:
                    give_Brown();
                    break;
        }
    }



    private void startQuest() {
        Vars.State = "Going to start the Quest.";
        String[] chatOptions = {"Yes, Wartface looks fat", "Do you want me to pick an armour colour for you?",
                                "What about a different colour?"};
        if (Random.nextInt(0, 100) > 50)
            talkTo(669,General_Hut,chatOptions);
        talkTo(670,General_Hut,chatOptions);
    } //Working

    private boolean gatheredMail;
    private void getMail() {
        Vars.State = "Gathering the Goblin Mail";
        if (!ctx.inventory().contains(288)) {
            interactObject(General_Crate,16559,"Search");
        } else if (ctx.inventory().getCount(288) == 1) {
            SceneObject Door = ctx.objects().query().id(12444).actions("Open").located(new Tile(2954, 3505, 0)).results().nearest();
            if (Door != null) {
                if (Door.interact()) {
                    Time.sleep(1_000, () -> !ctx.localPlayer().isMoving());
                }
                interactObject(Western_Hut, 16560, "Search");
            }
        } else if ((ctx.inventory().getCount(288) == 2)) {
            if (!Upstairs.contains(ctx.localPlayer().getLocation()))
                interactObject(Ground_Level,16450,"Climb-up");
            interactObject(Upstairs,16561,"Search");
        } else if ((ctx.inventory().getCount(288) == 3) && Upstairs.contains(ctx.localPlayer().getLocation()))
            interactObject(Upstairs,16556, "Climb-down");
        gatheredMail = true;
    }//Working

    private boolean gathered_ingredients;
    private void getIngredients() {
        if (!ctx.inventory().contains(995)) {
            withdraw("Coins", 35);
        } else if (ctx.inventory().getCount(1951) < 3) {
            SceneObject bush = ctx.objects().query().id(23628,26329).results().nearest();
            if (bush != null) {
                if (bush.interact("Pick-from"))
                    Time.sleep(1_500, () -> ctx.inventory().contains(1951));
            } ctx.webWalking().walkTo(Redberry_Bushes.getRandomTile());
        } else if (ctx.inventory().getItem(1793) == null) {
            String[] chatOptions = {"Yes please, I need woad leaves.", "How about 20 coins?"};
            talkTo(5422,Falador_Park,chatOptions);
        } else if ((ctx.inventory().getCount(1957) < 2)) {
            SceneObject Onion = ctx.objects().query().id(3366).results().nearest();
            if (Onion != null) {
                if (Onion.interact("Pick"))
                    Time.sleep(1_500, () -> ctx.inventory().contains(1957));
            } if (Random.nextInt(0, 100) > 50)
                ctx.webWalking().walkTo(Rimmington_Field.getRandomTile());
            ctx.webWalking().walkTo(Lumbridge_Field.getRandomTile());
        } gathered_ingredients = true;
    }//Working

    private boolean gathered_dyes = false;
    private void getDyes() {
        Vars.State = "Getting the Dyes.";
        if (!ctx.inventory().contains(1763)) {
            String[] chatOptions = {"Can you make dyes for me please?", "What do you need to make red dye?",
                        "Okay, make me some red dye please."};
            talkTo(4284,Aggie_House,chatOptions);
        } else if (!ctx.inventory().contains(1765)) {
            String[] chatOptions = {"Can you make dyes for me please?", "What do you need to make yellow dye?",
                        "Okay, make me some yellow dye please."};
            talkTo(4284,Aggie_House,chatOptions);
        } else if (ctx.inventory().containsAll(1763,1765)) {
            ctx.inventory().interactItem("Use", 1763);
            ctx.inventory().selectItem(1765);
        } else if (!ctx.inventory().contains(1767)) {
            String[] chatOptions = {"Can you make dyes for me please?", "What do you need to make blue dye?",
                        "Okay, make me some blue dye please."};
            talkTo(4284,Aggie_House,chatOptions);
        } gathered_dyes = true;
    }

    private boolean mail_dyed = false;
    private void dyeMail() {
        Vars.State = "Dyeing the Goblin mail";
        if (!ctx.inventory().containsAll(288, 287, 286)) {
            ctx.inventory().interactItem("Use", 1769);
            ctx.inventory().selectItem(288);
            ctx.inventory().interactItem("Use", 1767);
            ctx.inventory().selectItem(288);
        } mail_dyed = true;
    }//Working

    private void give_Orange() {
        Vars.State = "Turning in Orange Mail.";
        NPC goblin_general = ctx.npcs().query().id(669, 670).reachable().results().nearest();
        if (goblin_general != null) {
            if (!ctx.dialogues().isDialogueOpen()) {
                if (ctx.inventory().contains(286))
                    ctx.inventory().interactItem("Use", 286);
                if (goblin_general.interact("Use"))
                    Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            } else ctx.dialogues().selectContinue();
        }
    }

    private void give_Blue() {
        Vars.State = "Turning in Blue Mail.";
        NPC goblin_general = ctx.npcs().query().id(669, 670).reachable().results().nearest();
        if (goblin_general != null) {
            if (!ctx.dialogues().isDialogueOpen()) {
                if (ctx.inventory().contains(287))
                    ctx.inventory().interactItem("Use", 287);
                if (goblin_general.interact("Use"))
                    Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            } else ctx.dialogues().selectContinue();
        }
    }

    private void give_Brown() {
        Vars.State = "Turning in Brown Mail.";
        NPC goblin_general = ctx.npcs().query().id(669, 670).reachable().results().nearest();
        if (goblin_general != null) {
            if (!ctx.dialogues().isDialogueOpen()) {
                if (ctx.inventory().contains(288))
                    ctx.inventory().interactItem("Use", 288);
                if (goblin_general.interact("Use"))
                    Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            } else ctx.dialogues().selectContinue();
        }
    }
}
