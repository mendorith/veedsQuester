import Tasks.QuestHandler;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;

@ScriptManifest(name = "veedsQuester", gameType = GameType.OS)
public class Main extends LoopScript {

    QuestHandler questHandler;

    @Override
    protected int loop() {
        questHandler.main();
        return 500;
    }

    @Override
    public boolean onStart(String... strings) {
        questHandler = new QuestHandler(getAPIContext());
        return true;
    }
}
