package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyRichPlayer extends Gadget {

    protected Economy economy = null;
    boolean vault_eco = false;

    @Override
    public String getName() {
        return "RichPlayer";
    }

    @SuppressWarnings("deprecation")
    public String getResultName() {
        String name = "";
        double balance = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            if (economy.getBalance(p.getName()) > balance) {
                balance = economy.getBalance(p.getName());
                name = p.getName();
            }
        }
        return "$ " + name + " $";
    }

    @SuppressWarnings("deprecation")
    public int getResultValue() {
        if (!vault_eco) return -1;
        double balance = 0;
        for (Player p : getServer().getOnlinePlayers()) {
            if (economy.getBalance(p.getName()) > balance) balance = economy.getBalance(p.getName());
        }
        return (int) balance;
    }


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    @Override
    public void process() {
        addResult(getResultName(), getResultValue());
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        try {
            vault_eco = setupEconomy();
        } catch (Throwable t) {
        }
        if (!vault_eco) log("Can't connect to Vault/Economy! You need to install Vault and economy plugin!");

    }


}
