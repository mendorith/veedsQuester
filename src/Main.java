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

    QuestHandler questHandler;

    @Override
    protected int loop() {
        questHandler.main();
        return 500;
    }

    @Override
    public boolean onStart(String... strings) {
        Constants.startTime = System.currentTimeMillis();
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
            frame.draw(g, 0, 0, ctx);
        }
    }
}
