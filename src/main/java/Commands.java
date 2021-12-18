import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Commands extends ListenerAdapter {

    private final Random r = new Random();
    public static BuildBot h;

    private final Logger log = LoggerFactory.getLogger(Commands.class);

    private final HashMap<String, String> spieler = new HashMap<>();
    private final HashMap<Integer, String> spielerzahl = new HashMap<>();

    private final ArrayList<Integer> zahlen = new ArrayList<>();

    private static final String[] mitspieler = new String[8];


    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        // Calling the different commands
        switch (event.getName()) {
            case "loseaus" -> lose(event);
            case "gibmate" -> beginneWichteln(event);
            case "deldm" -> delDM(event, Objects.requireNonNull(event.getOption("amount")));
            case "clear" -> clear(event, Objects.requireNonNull(event.getOption("amount")));
            case "help" -> help(event);
            case "verschickedm" -> verschickeDM(event);
        }

    }

    public static void main(String[] args) {
        // When the bot is started, the teammates are entered.
        h = new BuildBot();
        mitspieler[0] = "Josh";
        mitspieler[1] = "Jan";
        mitspieler[2] = "Jannik";
        mitspieler[3] = "FB";
        mitspieler[4] = "Kai";
        mitspieler[5] = "Dave";
        mitspieler[6] = "Freddy";
    }

    // /clear command
    public void clear(SlashCommandEvent event, OptionMapping amount) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            log.info("/clear created from " + event.getUser().getAsMention());

            // Get the textchannel we are in rn
            TextChannel channel = event.getTextChannel();
            // List of msgs with a specific amount
            List<Message> msgs = channel.getHistory().retrievePast(Integer.parseInt(amount.getAsString())).complete();
            // purging list
            channel.purgeMessages(msgs);

            event.replyFormat("Nachrichten gelöscht.").setEphemeral(true).queue();
            log.info("Bot replied \"Nachrichten gelöscht\" to " + event.getUser().getAsMention());
        } else {
            event.replyFormat("Das kannst du nicht tun.").setEphemeral(true).queue();
            log.error(event.getUser().getAsMention() + " tried to use /clear with no permissions from tpye " + Permission.MESSAGE_MANAGE);
        }

    }

    // /deldm command
    public void delDM(SlashCommandEvent event, OptionMapping amount) {
        log.info("/deldm created from " + event.getUser().getAsMention());

        // DMs opened
        PrivateChannel channel = event.getUser().openPrivateChannel().complete();
        // List of Messegas with an amount from the DMs
        List<Message> msgs = channel.getHistory().retrievePast(Integer.parseInt(amount.getAsString())).complete();
        // purging messages
        channel.purgeMessages(msgs);

        event.reply("Nachrichten gelöscht.").setEphemeral(true).queue();
        log.info("Bot replied \"Nachrichten gelöscht\" to " + event.getUser().getAsMention());
    }


    // /loseaus command
    public void lose(SlashCommandEvent event) {
        if (event.getUser().getName().equals("RedEagle") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            // The players are assigned their partners.
            // "lose" is a german word and stands for "drawing"
            log.info("/loseaus created from " + event.getUser().getAsMention());
            zahlen.clear();
            spielerzahl.put(0, "Josh");
            spielerzahl.put(1, "Jan");
            spielerzahl.put(2, "Jannik");
            spielerzahl.put(3, "FB");
            spielerzahl.put(4, "Kai");
            spielerzahl.put(5, "Dave");
            spielerzahl.put(6, "Freddy");

            for (int i = 0; i < mitspieler.length; i++) {
                int rand = r.nextInt(7); // 7
                if (zahlen == null) {
                    while (rand == i) {
                        rand = r.nextInt(7); // 7
                    }
                    zahlen.add(rand);
                    spieler.put(mitspieler[i], spielerzahl.get(rand));
                } else {
                    while (zahlen.contains(rand) || rand == i) {
                        rand = r.nextInt(7); // 7
                    }
                    zahlen.add(rand);
                    spieler.put(mitspieler[i], spielerzahl.get(rand));
                    if (zahlen.size() == 7) {
                        break;
                    }
                }

            }
            log.info("Bot replied \"Auslosung wurde gestartet. Bitte /gibmate ausführen!\" to " + event.getUser().getAsMention());
            event.reply("Auslosung wurde gestartet. Bitte /gibmate ausführen! <a:happycat:888325204286271489>").queue();
        } else {
            log.error(event.getUser().getAsMention() + " tried to use /loseaus with no permissions of type " + Permission.ADMINISTRATOR);
            event.reply("Das kannst du nicht tun.").setEphemeral(true).queue();
        }

    }


    // Only one has to call the command and every user gonna get the dm.
    public void verschickeDM(SlashCommandEvent event) {
        boolean hatRole = false;
        for (int i = 0; i < event.getMember().getRoles().size(); i++) {
            if (event.getMember().getRoles().get(i).getName().equals("Wichteln")) {
                hatRole = true;
            }
        }
        if (hatRole) {
            //System.out.println(event.getGuild().retrieveMemberById(324890484944404480L).getJDA().openPrivateChannelById(324890484944404480L).complete());
            // Dave
            event.getGuild().retrieveMemberById(324890484944404480L).getJDA().openPrivateChannelById(324890484944404480L).queue(channel -> {
                channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit " + spieler.get("Dave") + " gezogen. Viel Spaß :P").queue();
            });
            // FB
            event.getGuild().retrieveMemberById(380828065871429634L).getJDA().openPrivateChannelById(380828065871429634L).queue(channel -> {
                channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit " + spieler.get("FB") + " gezogen. Viel Spaß :P").queue();
            });
            // Josh
            event.getGuild().retrieveMemberById(615955812065738763L).getJDA().openPrivateChannelById(615955812065738763L).queue(channel -> {
                channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit " + spieler.get("Josh") + " gezogen. Viel Spaß :P").queue();
            });
            // Kai
            event.getGuild().retrieveMemberById(379018675795263503L).getJDA().openPrivateChannelById(379018675795263503L).queue(channel -> {
                channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit " + spieler.get("Kai") + " gezogen. Viel Spaß :P").queue();
            });
            // TODO More participants

            log.info("Bot sent a private message to all users. Command initiated from " + event.getUser().getAsMention());
            event.reply("Die DM mit allen ausgelosten Teilnehmern wurde verschickt. Lasset die Spiele beginnen <:DaveHeart:911979369579315301>").queue();
        } else {
            log.error(event.getUser().getAsMention() + " tried to use \" + cmd + \" with no permissions from type ROLE: WICHTELN");
            event.reply("Das kannst du nicht tun.").setEphemeral(true).queue();
        }

    }

    // Hard coded part of the bot, it is tailored to the participants this year.
    // /gibmate command
    // Every user have to call the command personally and get the message then.
    public void beginneWichteln(SlashCommandEvent event) {
        log.info("/gibmate created from " + event.getUser().getAsMention());
        if (event.getUser().getName().equals("RedEagle")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Dave") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("AmSpielen")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Josh") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("Imp_Jan")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Jan") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("Kai123")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Kai") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("Sirulex")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Jannik") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("ÈFBE")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("FB") + " gezogen. Viel Spaß :P").queue());
        } else if (event.getUser().getName().equals("freddy")) {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Schön dass du bei Wichteln mitmachst! Du hast hiermit offiziell " + spieler.get("Freddy") + " gezogen. Viel Spaß :P").queue());
        } else {
            event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Nice Try \uD83E\uDD13").queue());
        }
        String reply = "Schau in einen DMs nach wen du gezogen hast " + event.getUser().getAsMention() + " <:DaveHeart:911979369579315301>";
        event.reply(reply).submit();
        log.info("Bot replied \"" + reply + "\" to " + event.getUser().getAsMention());
    }

    // /help command
    public void help(SlashCommandEvent event) {
        EmbedBuilder e = new EmbedBuilder();
        e.setTitle("Liste der Commands");
        e.setColor(Color.ORANGE);
        e.setDescription("Hier siehst du die Commands die du mit dem Prefix / aufrufen kannst.");
        e.addField("/loseaus", "Lose die Partner zum Wichteln aus.", false);
        e.addBlankField(true);
        e.addField("/gibmate", "Schickt dir eine DM mit dem Parter der dir zuvor mit /loseaus zugeteilt wurde.", false);
        e.addBlankField(true);
        e.addField("/deldm [amount]", "Löscht eine bestimmte Anzahl (amount) der DMs mit dir.", false);
        e.addBlankField(true);
        e.addField("/clear [amount]", "Löscht eine bestimmte Anzahl (amount) der Nachrichten in diesem Kanal.", false);
        e.setFooter("Made with ❤ by RedEagle#0400");
        event.getTextChannel().sendMessage(e.build()).queue();
        event.reply("Hier bitte.").setEphemeral(true).queue();
    }


}
