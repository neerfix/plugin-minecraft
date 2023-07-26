package com.myspacecube.verifyAccountMyspacecube;

import com.myspacecube.verifyAccountMyspacecube.commands.CommandVerify;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin Enabled");

        getCommand("verify").setExecutor(new CommandVerify());
    }
}