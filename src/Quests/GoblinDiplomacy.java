package Quests;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.entity.SceneObject;
import com.epicbot.api.shared.methods.IQuestAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.Random;
import com.epicbot.api.shared.util.time.Time;
import data.Vars;


public class GoblinDiplomacy {
    APIContext ctx;
    QuestMethods qm;

    private enum Locations {
        Aggie_House(new Area(3083, 3261, 3088, 3256)),
        Falador_Park(new Area(3023, 3383, 3028, 3375)),
        General_Crate(new Area(2959, 3515, 2961, 3514)),
        General_Hut(new Area(2956, 3512, 2959, 3510)),
        Ground_Level(new Area(2953, 3498, 2959, 3496)),
        Lumbridge_Field(new Area(3186, 3269, 3192, 3265)),
        Redberry_Bushes(new Area(3265, 3375, 3277, 3367)),
        Rimmington_Field(new Area(2945, 3254, 2955, 3247)),
        Upstairs(new Area(2,2955, 3497, 2955, 3497)),
        Western_Hut(new Area(2951, 3508, 2953, 3504))
        ;

        final Area area;

        Locations(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }

    }

    private boolean gatheredItems = false;
    private final String[] requirements = {"Goblin mail", "Orange goblin mail", "Blue goblin mail"};

    public GoblinDiplomacy(APIContext ctx) {
        this.ctx = ctx;
        this.qm = new QuestMethods(ctx);
    }

    public void main() {
        Vars.State = "Doing quest Goblin Diplomacy";
        if (ctx.quests().isCompleted(IQuestAPI.Quest.GOBLIN_DIPLOMACY))
            Vars.currentQuest = null;
        else if (!ctx.quests().isStarted(IQuestAPI.Quest.GOBLIN_DIPLOMACY))
            startQuest();
        else if (!gatheredItems || ctx.quests().isStarted(IQuestAPI.Quest.GOBLIN_DIPLOMACY))
            getRequirements();
        else give_stuff();
    }

    private boolean fatty = false;
    private final String [] options = {"that! You stupid!", "we busy", "bigger general", "pick red!","Um.."};
    private void startQuest() {
        if (Locations.General_Hut.getArea().contains(ctx.localPlayer().getLocation())) {
            if (!ctx.dialogues().isDialogueOpen()) {
                NPC goblin_general = ctx.npcs().query().nameContains("General ").reachable().results().nearest();
                if (goblin_general != null && goblin_general.interact("Talk-to")) {
                    Time.sleep(2_000, () -> ctx.dialogues().isDialogueOpen());
                }
            } else {
                if (ctx.dialogues().getText().contains((options[4]))) {
                    ctx.dialogues().selectContinue();
                }else if (!fatty){
                    ctx.dialogues().selectOption(0);
                    fatty = true;
                } else if ((ctx.dialogues().getText().contains(options[0])) ||
                          (ctx.dialogues().getText().contains(options[1])) ||
                          (ctx.dialogues().getText().contains(options[2])) ||
                          (ctx.dialogues().getText().contains(options[3]))) {
                    ctx.dialogues().selectContinue();
                    System.out.println(ctx.dialogues().getOptions());
                    ctx.dialogues().selectOption(2);
                } else {
                    ctx.dialogues().selectContinue();
                }
            }
        }else ctx.webWalking().walkTo(Locations.General_Hut.getArea().getRandomTile());
    } //Working

    private void getRequirements() {
        if ((!gatheredMail)|| (ctx.inventory().getCount("Goblin mail") != 3)) {
            getMail();
        } else if (!gathered_ingredients || !ctx.inventory().contains(ingredients)) {
            getIngredients();
        } else if (!gathered_dyes || !ctx.inventory().contains(dyes) && ctx.inventory().contains(ingredients)){
            getDye();
        } else if (!gatheredItems) {
            dyeMail();
        } else {
            give_stuff();
        }
    }

    private void give_stuff(){
        if (Locations.General_Hut.getArea().contains(ctx.localPlayer().getLocation())) {
            if (ctx.inventory().contains(requirements)) {
                if (ctx.inventory().contains("Orange goblin mail")) {
                    if (!ctx.dialogues().isDialogueOpen()) {
                        NPC goblin_general = ctx.npcs().query().nameContains("General").reachable().results().nearest();
                        if (goblin_general != null) {
                            ctx.inventory().interactItem("Orange goblin mail", "Use");
                            goblin_general.interact("Use");
                            Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                        }
                    } else ctx.dialogues().selectContinue();
                } else if (ctx.inventory().contains("Blue goblin mail")) {
                    if (!ctx.dialogues().isDialogueOpen()) {
                        NPC goblin_general = ctx.npcs().query().nameContains("General").reachable().results().nearest();
                        if (goblin_general != null) {
                            ctx.inventory().interactItem("Blue goblin mail", "Use");
                            goblin_general.interact("Use");
                            Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                        }
                    } else ctx.dialogues().selectContinue();
                } else if (ctx.inventory().contains("Goblin mail")) {
                    if (!ctx.dialogues().isDialogueOpen()) {
                        NPC goblin_general = ctx.npcs().query().nameContains("General").reachable().results().nearest();
                        if (goblin_general != null) {
                            ctx.inventory().interactItem("Goblin mail", "Use");
                            goblin_general.interact("Use");
                            Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                        }
                    } else ctx.dialogues().selectContinue();
                }
            } else if (ctx.quests().isCompleted(IQuestAPI.Quest.GOBLIN_DIPLOMACY)) {
                ctx.script().stop("Quest goblin diplomacy has been completed!");
            }
        } else ctx.walking().walkTo(Locations.General_Hut.getArea().getRandomTile());
    }

    private boolean gatheredMail = false;
    private void getMail() {
        if (!gatheredMail) {
            if (ctx.inventory().getCount("Goblin mail") == 0) {
                if (Locations.General_Crate.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject crate1 = ctx.objects().query().id(16559).reachable().results().first();
                    if (crate1 != null && crate1.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount("Goblin Mail") == 1);
                } else {
                    ctx.webWalking().walkTo(Locations.General_Crate.getArea().getRandomTile());
                }
            } else if (ctx.inventory().getCount("Goblin Mail") == 1) {
                if (Locations.Western_Hut.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject crate2 = ctx.objects().query().id(16560).reachable().results().nearest();
                    if (crate2 != null && crate2.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount("Goblin Mail") == 2);
                } else {
                    ctx.webWalking().walkTo(Locations.Western_Hut.getArea().getRandomTile());
                }
            } else if ((ctx.inventory().getCount("Goblin Mail") == 2)) {
                if (!Locations.Upstairs.getArea().contains(ctx.localPlayer().getLocation())) {
                    ctx.webWalking().walkTo(Locations.Ground_Level.getArea().getRandomTile());
                    SceneObject ladder_up = ctx.objects().query().id(16450).results().first();
                    if (ladder_up != null) {
                        if (ladder_up.click() && !ctx.localPlayer().isMoving())
                            Time.sleep(1_500, () -> !ctx.localPlayer().isMoving());
                    }
                } else {
                    SceneObject crate3 = ctx.objects().query().id(16561).results().nearest();
                    if (crate3 != null && crate3.interact("Search"))
                        Time.sleep(1_000, () -> ctx.inventory().getCount("Goblin mail") == 3);
                        }
            } else if ((ctx.inventory().getCount("Goblin mail") == 3) &&
                       Locations.Upstairs.getArea().contains(ctx.localPlayer().getLocation())) {
                    SceneObject ladder_down = ctx.objects().query().id(16556).results().nearest();
                    if (ladder_down != null)
                        if (ladder_down.interact("Climb-down"))
                            Time.sleep(1_500, () -> !ctx.localPlayer().isMoving());
            } else gatheredMail = true;
        }
    }  //Working

    private boolean gathered_ingredients = false;
    private final String [] ingredients  = {"Redberries", "Woad leaf", "Onion"};
    private void getIngredients() {
        if (!ctx.inventory().contains("Coins") ||
        (ctx.inventory().contains("Coins") && (35 > ctx.inventory().getItem("Coins").getStackSize()))) {
            qm.withdraw("Coins", 35);
        } //Working
        else if (ctx.inventory().getCount(ingredients[0]) != 3) {
            if (Locations.Redberry_Bushes.getArea().contains(ctx.localPlayer().getLocation())) {
                SceneObject Bush = ctx.objects().query().id(23628).results().nearest();
                if (Bush != null) {
                    if (Bush.interact("Pick-from")) {
                        Time.sleep(1_500, () -> ctx.inventory().contains("Redberries"));
                    }
                }
            } else ctx.webWalking().walkTo(Locations.Redberry_Bushes.getArea().getRandomTile());
        }//Working
        else if (ctx.inventory().contains(ingredients[1]) || (ctx.inventory().getItem(ingredients[1]).getStackSize() < 2)) {
            if (Locations.Falador_Park.getArea().contains(ctx.localPlayer().getLocation())) {
                if (!ctx.dialogues().isDialogueOpen()) {
                    NPC gardener = ctx.npcs().query().nameContains("Wyson").results().nearest();
                    if (gardener != null && gardener.interact("Talk-to"))
                        Time.sleep(4_000, () -> ctx.dialogues().isDialogueOpen());
                }
                else if (ctx.dialogues().getText().contains("I'm the head gardener")) {
                        ctx.dialogues().selectContinue();
                        ctx.dialogues().getOptions();
                        ctx.dialogues().selectOption(1);
                }
                else if (ctx.dialogues().getText().contains("How much are you")) {
                    ctx.dialogues().selectContinue();
                    ctx.dialogues().getOptions();
                    ctx.dialogues().selectOption(4);
                    Time.sleep(1_000, () -> ctx.inventory().contains("Woad leaf"));
                }
                else {
                    ctx.dialogues().selectContinue();
                }
            }
            else {
                ctx.webWalking().walkTo(Locations.Falador_Park.getArea().getRandomTile());
            }
        } else if (ctx.inventory().getCount(ingredients[2]) != 2) {
            if (Locations.Rimmington_Field.getArea().contains(ctx.localPlayer().getLocation()) ||
                Locations.Lumbridge_Field.getArea().contains(ctx.localPlayer().getLocation())) {
                SceneObject Onion = ctx.objects().query().id(3366).results().nearest();
                if (Onion != null) {
                    if (Onion.interact("Pick")) {
                        Time.sleep(2_000, () -> ctx.inventory().contains("Onion"));
                    }
                }
            } else {
                if (Random.nextInt(0, 100) > 50) {
                    ctx.webWalking().walkTo(Locations.Rimmington_Field.getArea().getRandomTile());
                }
                else {
                    ctx.webWalking().walkTo(Locations.Lumbridge_Field.getArea().getRandomTile());
                }
            }
        } else gathered_ingredients = true;
    }

    private boolean gathered_dyes = false;
    private final String [] dyes = {"Orange dye", "Blue dye"};
    private void getDye() {
        if (!ctx.inventory().contains(dyes)) {
            if (Locations.Aggie_House.getArea().contains(ctx.localPlayer().getLocation())) {
                if (!ctx.dialogues().isDialogueOpen()) {
                    NPC witch = ctx.npcs().query().nameMatches("Aggie").results().first();
                    if (witch != null)
                        if (witch.interact("Talk-to"))
                            Time.sleep(1_000, () -> ctx.dialogues().isDialogueOpen());
                } else if (ctx.dialogues().getText().equalsIgnoreCase("What can I help you with?")) {
                    ctx.dialogues().selectContinue();
                    ctx.dialogues().getOptions();
                    ctx.dialogues().selectOption(4);
                } else if (ctx.dialogues().getText().startsWith("What sort of dye")) {
                    ctx.dialogues().selectContinue();
                    if (!ctx.inventory().contains("Red dye")) {
                        ctx.dialogues().getOptions();
                        ctx.dialogues().selectOption(1);
                        if (ctx.dialogues().getText().contains("lots of redberries")) {
                            ctx.dialogues().selectContinue();
                            ctx.dialogues().selectOption(1);
                        }
                    } else if (!ctx.inventory().contains("Yellow dye")) {
                        ctx.dialogues().getOptions();
                        ctx.dialogues().selectOption(2);
                        if (ctx.dialogues().getText().toLowerCase().contains("yellow is a strange")) {
                            ctx.dialogues().selectContinue();
                            ctx.dialogues().getOptions();
                            ctx.dialogues().selectOption(1);
                        } else ctx.dialogues().selectContinue();
                    } else if (!ctx.inventory().contains(dyes[1])) {
                        ctx.dialogues().selectContinue();
                        ctx.dialogues().getOptions();
                        ctx.dialogues().selectOption(3);
                        if (ctx.dialogues().getText().toLowerCase().contains("woad leaves and")) {
                            ctx.dialogues().selectContinue();
                            ctx.dialogues().selectOption(1);
                        } else ctx.dialogues().selectContinue();
                    }
                    else {
                        ctx.inventory().interactItem("Red dye", "Use");
                        ctx.inventory().selectItem("Yellow dye");
                    }
                }
            } else ctx.webWalking().walkTo(Locations.Aggie_House.getArea().getRandomTile());
        } else gathered_dyes = true;
    }

    private void dyeMail() {
        if (ctx.inventory().contains("Goblin mail")) {
            if (ctx.inventory().contains(dyes)) {
                if (!ctx.inventory().contains(requirements)) {
                    ctx.inventory().interactItem("Orange dye", "Use");
                    ctx.inventory().selectItem("Goblin mail");
                    ctx.inventory().interactItem("Blue dye", "Use");
                    ctx.inventory().selectItem("Goblin mail");
                } else gatheredItems = true;
            }
        }
    }
}