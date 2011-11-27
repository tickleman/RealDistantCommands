package fr.crafter.tickleman.realdistantcommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.bukkit.plugin.java.JavaPlugin;

public class RealDistantCommandsPlugin extends JavaPlugin
{

	Long position;
	int schedule;

	//------------------------------------------------------------------------------------- onDisable
	@Override
	public void onDisable()
	{
		getServer().getScheduler().cancelTask(schedule);
		System.out.println(
			"[RealDistantCommands] version [" + getDescription().getVersion() + "] ("
			+ getDescription().getAuthors().toString() + ") un-loaded"
		);
	}

	//-------------------------------------------------------------------------------------- onEnable
	@Override
	public void onEnable()
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader("RealDistantCommands.pos"));
			position = Long.parseLong(reader.readLine());
		} catch (Exception e) {
			position = 0L;
			System.out.println("position to 0 because " + e.getMessage());
		}
		schedule = getServer().getScheduler().scheduleSyncRepeatingTask(
			this,
			new Runnable()
			{
				@Override
				public void run()
				{
					try {
						BufferedReader reader = new BufferedReader(new FileReader("RealDistantCommands.txt"));
						String buffer;
						String commands = "";
						if (position > 0L) reader.skip(position);
						while ((buffer = reader.readLine()) != null) {
							if (!buffer.equals("flush")) {
								commands += buffer + "\n";
							} else {
								for (String command : commands.split("\n")) {
									System.out.println("[RealDistantCommands] " + command);
									getServer().dispatchCommand(getServer().getConsoleSender(), command);
								}
								position += (commands + "flush\n").length();
								BufferedWriter writer = new BufferedWriter(new FileWriter("RealDistantCommands.pos"));
								try {
									writer.write(position.toString());
								} catch (Exception e) {
									System.out.println("write error " + e.getMessage());
								}
								commands = "";
							}
						}
					} catch (Exception e) {
						System.out.println("read error " + e.getMessage());
					}
				}
			}, 1L * 20L, 1L * 20L
		);
		System.out.println(
			"[RealDistantCommands] version [" + getDescription().getVersion() + "] ("
			+ getDescription().getAuthors().toString() + ") loaded"
		);
	}

}
