import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;


public class BuildBot {
    public static JDA jda;

    public BuildBot() {
        try {
            // Creating a Logger for the bot
            Logger logger = LoggerFactory.getLogger(BuildBot.class);
            logger.info("Mainclass started.");

            // Bot Token from the class "Token"
            Token t = new Token();
            jda = JDABuilder.createDefault(t.getToken()).build();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.playing("/help"));
            jda.addEventListener(new Commands());

            // Log -> Giving the name of the application when its online
            logger.warn("Bot ist aktiv als " + jda.getSelfUser().getName());

            jda.awaitReady();

            Guild g = jda.getGuildById("756943363093037076");
            assert g != null;
            Role r = g.getRoleById(756968504506450000L);
            assert r != null;
            EnumSet<Permission> s = r.getPermissions();

            // creating / commands
            g.upsertCommand("gibmate", "Schickt dir deinen Partner dem du was schenken sollst.").queue();
            g.upsertCommand("loseaus", "Lose die Teilnehmer aus.").queue();
            g.upsertCommand("deldm", "Lösche deine DMs mit dem Bot.").addOption(OptionType.INTEGER, "amount", "Give the amount of the Messages you want to delete.").queue();
            g.upsertCommand("clear", "Lösche die Nachrichten in diesem Kanal.").addOption(OptionType.INTEGER, "amount", "Anzahl der zu löschenden Nachrichten").queue();
            g.upsertCommand("help", "Liste der Commands.").queue();
            g.upsertCommand("verschickedm", "Schicke DM an alle teilnehmenden.").queue();

        } catch (InterruptedException | LoginException i) {
            i.printStackTrace();
        }

    }


}
