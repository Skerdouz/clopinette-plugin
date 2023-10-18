package io.papermc.clope;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

public class ClopePlugin extends JavaPlugin{

    @Override
    public void onEnable(){
        //commands
        getCommand("clope").setExecutor(new ClopeCommand(this));

        ClopeListener clopeListener = new ClopeListener(this);

        //Event listener
        getServer().getPluginManager().registerEvents(new ClopeListener(this),this);

        getServer().getPluginManager().registerEvents(new BambooPlantListener(), this);

    }

    @Override
    public void onDisable(){

    }
}
