package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyBalance extends Gadget {
    protected Economy economy = null;
    boolean vault_eco = false;

    @Override
    public String getName() {
        return "Balance";
    }

    @SuppressWarnings("deprecation")
    public int getPlayerMoney() {
        if (!vault_eco) return -1;
        return (int) economy.getBalance(getPlayer().getName());
    }

    @Override
    public void onEnable() {
        try {
            vault_eco = setupEconomy();
        } catch (Throwable t) {
        }
        if (!vault_eco) log("Can't connect to Vault/Economy! You need to install Vault and economy plugin!");
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
        addResult("Money", getPlayerMoney());
    }

    @Override
    public void onDisable() {
    }


}
