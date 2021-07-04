package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IDialogueAPI;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.Random;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;


public class GoblinDiplomacy {
    APIContext ctx;
    QuestMethods qm;

    private enum Locations {
        Aggie_House(new Area(3083, 3261, 3089, 3256)),
        Falador_Park(new Area(3023, 3383, 3028, 3375)),
        General_Crate(new Area(2959, 3515, 2961, 3514)),
        General_Hut(new Area(2956, 3513, 2959, 3510)),
        Ground_Level(new Area(2953, 3498, 2959, 3496)),
        Lumbridge_Field(new Area(3186, 3269, 3192, 3265)),
        Redberry_Bushes(new Area(3265, 3375, 3277, 3367)),
        Rimmington_Field(new Area(2945, 3254, 2955, 3247)),
        Upstairs(new Area(2, 2955, 3497, 2955, 3497)),
        Western_Hut(new Area(2951, 3508, 2953, 3504));

        final Area area;

        Locations(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }

    }

    private boolean gatheredItems = false;

    public GoblinDiplomacy(APIContext ctx) {
        this.ctx = ctx;
        this.qm = new QuestMethods(ctx);
    }

    public void main() {
        Vars.State = "Doing quest Goblin Diplomacy";
        if (ctx.quests().isCompleted(IQuestAPI.Quest.GOBLIN_DIPLOMACY))
            Vars.currentQuest = null;
        else if (!ctx.quests().isStarted(IQuestAPI.Quest.GOBLIN_DIPLOMACY)) {
            Vars.State = "Starting Goblin Diplomacy";
            startQuest();
        } else if (!gatheredItems)
            getRequirements();
        else {
            Vars.State = "Finishing up the quest";
            give_stuff();
        }
    }

    //private final String[] options = {"that! You stupid!", "we busy", "bigger general", "pick red!", "Um.."};
    private void startQuest() {
        if (Locations.General_Hut.getArea().contains(ctx.localPlayer().getLocation())) {
            if (!ctx.dialogues().isDialogueOpen()) {
                NPC goblin_general = ctx.npcs().query().id(669,670).reachable().results().nearest();
                if (goblin_general != null && goblin_general.interact("Talk-to")) {
                    Time.sleep(2_000, () -> ctx.dialogues().isDialogueOpen());
                } else {
                    if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION))
                        ctx.dialogues().selectOption("Yes, Wartface looks fat!");
                    else if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION))
                        ctx.dialogues().selectOption("Do you want me to pick an armour colour for you?");
                    else if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION))
                        ctx.dialogues().selectOption("What about a different colour?");
                    else ctx.dialogues().selectContinue();
                }
            } else {
                SceneObject Door = ctx.objects().query().id(12446, 12448).reachable().results().nearest();
                if (Door != null && Door.interact("Open"))
                    Time.sleep(1_000, () -> !ctx.localPlayer().isMoving());
                else ctx.webWalking().walkTo(Locations.General_Hut.getArea().getCentralTile());
            }
        }
    }//Working

    private void getRequirements() {
        if (!gatheredMail && !ctx.inventory().contains(286, 287)) {
            Vars.State = "Gathering the Goblin Mail";
            getMail();
        } else if (!gathered_ingredients && !ctx.inventory().containsAll(1951, 1793, 1957) &&
                !ctx.inventory().contains(1763, 1765, 1767, 1769, 286, 287)) {
            Vars.State = "Getting Ingredients for Dye";
            getIngredients();
        } else if (!gathered_dyes && !ctx.inventory().contains(286, 287)) {
            Vars.State = "Getting the Dyes";
            getDye();
        } else {
            Vars.State = "Dyeing the Goblin mail";
            dyeMail();
        }
    }


    private boolean gatheredMail = false;
    private void getMail() {
        if (!gatheredMail) {
            if (ctx.inventory().getCount(288) == 0) {
                if (Locations.General_Crate.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject crate1 = ctx.objects().query().id(16559).reachable().results().first();
                    if (crate1 != null && crate1.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount(288) == 1);
                } else {
                    ctx.webWalking().walkTo(Locations.General_Crate.getArea().getCentralTile());
                }
            } else if (ctx.inventory().getCount(288) == 1) {
                if (Locations.Western_Hut.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject crate2 = ctx.objects().query().id(16560).reachable().results().nearest();
                    if (crate2 != null && crate2.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount(288) == 2);
                } else {
                    SceneObject Door = ctx.objects().query().id(12444).reachable().results().first();
                    if (Door != null && Door.interact("Open"))
                        Time.sleep(1_000, () -> !ctx.localPlayer().isMoving());
                    else
                        ctx.webWalking().walkTo(Locations.Western_Hut.getArea().getCentralTile());
                }
            } else if ((ctx.inventory().getCount(288) == 2)) {
                if (!Locations.Upstairs.getArea().contains(ctx.localPlayer().getLocation())) {
                    ctx.webWalking().walkTo(Locations.Ground_Level.getArea().getCentralTile());
                    SceneObject ladder_up = ctx.objects().query().id(16450).results().first();
                    if (ladder_up != null) {
                        if (ladder_up.click() && !ctx.localPlayer().isMoving())
                            Time.sleep(1_500, () -> !ctx.localPlayer().isMoving());
                    }
                } else {
                    SceneObject crate3 = ctx.objects().query().id(16561).results().nearest();
                    if (crate3 != null && crate3.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount(288) == 3);
                }
            } else if ((ctx.inventory().getCount(288) == 3) &&
                    Locations.Upstairs.getArea().contains(ctx.localPlayer().getLocation())) {
                SceneObject ladder_down = ctx.objects().query().id(16556).results().nearest();
                if (ladder_down != null)
                    if (ladder_down.interact("Climb-down"))
                        Time.sleep(1_500, () -> !ctx.localPlayer().isMoving());
            } else gatheredMail = true;
        }
    }  //Working

    private boolean gathered_ingredients = false;
    private void getIngredients() {
        if (!ctx.inventory().contains(995)) {
            qm.withdraw("Coins", 35);
        } else if (ctx.inventory().getCount(1951) != 3 &&
                !ctx.inventory().contains(1763, 1765, 1769)) {
            if (Locations.Redberry_Bushes.getArea().contains(ctx.localPlayer().getLocation())) {
                SceneObject bush = ctx.objects().query().id(23628).results().nearest();
                if (bush != null)
                    if (bush.interact("Pick-from"))
                        Time.sleep(1_500, () -> ctx.inventory().contains(1951));
            } else ctx.webWalking().walkTo(Locations.Redberry_Bushes.getArea().getRandomTile());
        } else if (!ctx.inventory().contains(1767) &&
                ctx.inventory().getItem(1793).getStackSize() < 2) {
            if (Locations.Falador_Park.getArea().contains(ctx.localPlayer().getLocation())) {
                if (!ctx.dialogues().isDialogueOpen()) {
                    NPC wyson = ctx.npcs().query().id(5422).results().nearest();
                    if (wyson != null)
                        if (wyson.interact("Talk-to"))
                            Time.sleep(2_000, () -> ctx.dialogues().isDialogueOpen());
                } else {
                    if (ctx.dialogues().getText().contains("I'm the head gardener")) {
                        ctx.dialogues().selectContinue();
                        Time.sleep(1_000);
                        ctx.dialogues().selectOption(1);
                    } else if (ctx.dialogues().getText().contains("How much are you")) {
                        ctx.dialogues().selectContinue();
                        Time.sleep(1_000);
                        ctx.dialogues().selectOption(4);
                        Time.sleep(1_000, () -> ctx.inventory().contains(1793));
                    } else {
                        ctx.dialogues().selectContinue();
                    }
                }
            } else ctx.webWalking().walkTo(Locations.Falador_Park.getArea().getCentralTile());
        } else if (ctx.inventory().getCount(1957) != 2 &&
                !ctx.inventory().contains(1765, 1769)) {
            if (Locations.Rimmington_Field.getArea().contains(ctx.localPlayer().getLocation()) ||
                    Locations.Lumbridge_Field.getArea().contains(ctx.localPlayer().getLocation())) {
                SceneObject Onion = ctx.objects().query().id(3366).results().nearest();
                if (Onion != null) {
                    if (Onion.interact("Pick")) {
                        Time.sleep(1_500, () -> ctx.inventory().contains(1957));
                    }
                }
            } else {
                if (Random.nextInt(0, 100) > 50) {
                    ctx.webWalking().walkTo(Locations.Rimmington_Field.getArea().getRandomTile());
                } else {
                    ctx.webWalking().walkTo(Locations.Lumbridge_Field.getArea().getRandomTile());
                }
            }
        } else {
            gathered_ingredients = true;
            System.out.println("Test");
        }
    }//Working

    private boolean gathered_dyes = false;
    private void getDye() {
        if (!ctx.inventory().containsAll(1769, 1767)) {
            if (ctx.inventory().containsAll(1763, 1765) || !ctx.inventory().contains(1769)) {
                ctx.inventory().interactItem("Use", 1763);
                ctx.inventory().selectItem(1765);
            } else {
                if (Locations.Aggie_House.getArea().contains(ctx.localPlayer().getLocation())) {
                    if (!ctx.dialogues().isDialogueOpen()) {
                        NPC witch = ctx.npcs().query().id(4284).results().nearest();
                        if (witch != null)
                            if (witch.interact("Talk-to"))
                                Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                    } else {
                        if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                            ctx.dialogues().selectOption("Can you make dyes for me please?");
                            if (!ctx.inventory().contains(1763, 1769)) {
                                ctx.dialogues().selectContinue();
                                if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                    ctx.dialogues().selectOption("What do you need to make red dye?");
                                    if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                        ctx.dialogues().selectOption("Okay, make me some red dye please.");
                                    } else ctx.dialogues().selectContinue();
                                } else ctx.dialogues().selectContinue();
                            } else if (!ctx.inventory().contains(1765, 1769)) {
                                ctx.dialogues().selectContinue();
                                if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                    ctx.dialogues().selectOption("What do you need to make yellow dye?");
                                    if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                        ctx.dialogues().selectOption("Okay, make me some yellow dye please.");
                                    } else ctx.dialogues().selectContinue();
                                } else ctx.dialogues().selectContinue();
                            } else if (!ctx.inventory().contains(1767)) {
                                ctx.dialogues().selectContinue();
                                if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                    ctx.dialogues().selectOption("What do you need to make blue dye?");
                                    if (ctx.dialogues().isDialogueOpen(IDialogueAPI.DialogueType.OPTION)) {
                                        ctx.dialogues().selectOption("Okay, make me some blue dye please.");
                                    } else ctx.dialogues().selectContinue();
                                } else ctx.dialogues().selectContinue();
                            } else ctx.dialogues().selectContinue();
                        } else ctx.dialogues().selectContinue();
                    }
                } else ctx.webWalking().walkTo(Locations.Aggie_House.getArea().getCentralTile());
            }
        } else gathered_dyes = true;
    } //Working

    private void dyeMail() {
        if (!ctx.inventory().containsAll(288, 287, 286) && ctx.inventory().containsAll(1767, 1769)) {
            ctx.inventory().interactItem("Use", 1769);
            ctx.inventory().selectItem(288);
            ctx.inventory().interactItem("Use", 1767);
            ctx.inventory().selectItem(288);
        } else {gatheredItems = true; System.out.println("Testing");}
    }//Working

    private void give_stuff() {
        if (Locations.General_Hut.getArea().contains(ctx.localPlayer().getLocation())
            && ctx.inventory().contains(286,287,288)) {
            if (!ctx.dialogues().isDialogueOpen()) {
                NPC goblin_general = ctx.npcs().query().id(669, 670).reachable().results().nearest();
                if (goblin_general != null)
                    if (ctx.inventory().interactItem("Use", 286))
                        if (goblin_general.interact("Use"))
                            Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                else if (ctx.inventory().interactItem("Use", 287)) {
                    if (goblin_general.interact("Use"))
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                else if(ctx.inventory().interactItem("Use", 288))
                    if (goblin_general.interact("Use"))
                        Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
            }else ctx.dialogues().selectContinue();
        } else if (ctx.quests().isCompleted(IQuestAPI.Quest.GOBLIN_DIPLOMACY))
            ctx.script().stop("Quest goblin diplomacy has been completed!");
        } else ctx.webWalking().walkTo(Locations.General_Hut.getArea().getRandomTile());
    }
}