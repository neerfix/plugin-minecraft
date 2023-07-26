package com.myspacecube.verifyAccountMyspacecube.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandVerify implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Command Executed by " + sender.getName());

        if (args.length != 1) {
            sender.sendMessage("§c§lUsage: /verify <token>");
            return false;
        }

        try {
            call_me(args[0], sender.getName(), sender);
        } catch (Exception e) {
            sender.sendMessage(e.getMessage());
            throw new RuntimeException(e);
        }

        return false;
    }
    public static void call_me(String token, String username, CommandSender sender) throws Exception {
        String url = "http://localhost:9080/verify-minecraft/" + username + "/" + token;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        if (responseCode == 100) {
            sender.sendMessage("\t§c§lUn compte est déjà lié à ce compte Minecraft");
            return;
        }
        if (responseCode == 404) {
            sender.sendMessage("\t§c§lToken Invalide");
            return;
        }
        if (responseCode == 409) {
            sender.sendMessage("\t§c§lUn compte est déjà lié à ce compte Minecraft");
            return;
        }
        if (responseCode == 500) {
            sender.sendMessage("\t§c§lUne erreur est survenue, veuillez contacter un administrateur");
            return;
        }
        if (responseCode == 200) {
            sender.sendMessage("\t§2§lVotre compte a été vérifié avec succès");
            Inventory inventory = sender.getServer().getPlayer(sender.getName()).getInventory();
            ItemStack item = new ItemStack(Material.IRON_SWORD);
            inventory.addItem(item);
            return;
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print in String
        sender.sendMessage(response.toString());
        System.out.println(response.toString());
    }
}
