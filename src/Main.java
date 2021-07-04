import Tasks.QuestHandler;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;
import com.epicbot.api.shared.util.time.Time;
import data.Constants;
import data.Vars;

import java.awt.*;

@ScriptManifest(name = "veedsQuester", gameType = GameType.OS)
public class Main extends LoopScript {

    private QuestHandler questHandler;
    private Gui gui;

    @Override
    protected int loop() {
        if (Vars.start) {
            questHandler.main();
        }
        return 500;
    }

    @Override
    public boolean onStart(String... strings) {
        gui = new Gui(getAPIContext());
        questHandler = new QuestHandler(getAPIContext());
        return true;
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        if (ctx.client().isLoggedIn()) {
            PaintFrame frame = new PaintFrame();
            frame.setTitle("veedsQuester");
            frame.addLine("Runtime: ", Time.getFormattedRuntime(Constants.startTime));
            frame.addLine("State: ", Vars.State);
            frame.draw(g, 0, 24, ctx);
        }
    }
}
