package me.fromgate.okglass.gadgets;


import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.net.InetAddress;

public class GadgetPing extends Gadget {

    //Конфигурация
    private String url = "google.com";
    private int repeat = 5;
    private int pingdelay = 10; // задержка между "пингами"
    private boolean showhostname = true;
    private boolean useJavaPing = true;
    private String cmd;
    private BukkitTask bt;

    ////////////////////////////////////
    private int lastpingtime = 0;
    private boolean lastpingacc = false;

    @Override
    public void onEnable() {
        pingdelay = loadInt("delay-time", 30);
        repeat = loadInt("number-of-repeats", 5);
        url = loadStr("host", "google.com");
        showhostname = loadBoolean("show-host-name", true);
        useJavaPing = loadBoolean("use-java-ping", !System.getProperty("os.name").startsWith("Windows"));

        if (System.getProperty("os.name").startsWith("Windows")) cmd = "ping -n 1 " + url;
        else cmd = "ping -c 1 " + url;

        bt = Bukkit.getScheduler().runTaskTimerAsynchronously(getOkGlassPlugin(), new Runnable() {
            @Override
            public void run() {
                ping();
            }
        }, 20, 20 * pingdelay);
    }

    @Override
    public String getName() {
        return "Ping";
    }

    private String getResultName() {
        String pname = "&aPING:";
        if (showhostname) pname = "&a" + url;
        if (!lastpingacc) {
            if (showhostname) pname = "&4" + url;
            else pname = "&cPing failed";
        }
        return pname;
    }

    private int getResultValue() {
        if (!lastpingacc) return -1;
        return lastpingtime;
    }


    private void ping() {
        lastpingtime = -1;
        try {
            int time = 0;
            for (int i = 0; i < this.repeat; i++) {
                int tm = pingHost();
                if (tm >= 0) time += tm;
                else return;
            }
            lastpingtime = Math.max((int) (time / repeat), 1);
        } catch (Exception e) {
            this.lastpingacc = false;
        }
    }


    private int pingHost() {
        if (useJavaPing) return pingJava();
        else return pingSys();
    }


    private int pingJava() {
        Long start = System.currentTimeMillis();
        lastpingacc = isReachableJava();
        if (!lastpingacc) return -1;
        return (int) (System.currentTimeMillis() - start);
    }

    private int pingSys() {
        Long start = System.currentTimeMillis();
        lastpingacc = isReachableSys();
        if (!lastpingacc) return -1;
        return (int) (System.currentTimeMillis() - start);
    }


    private boolean isReachableJava() {
        try {
            return InetAddress.getByName(url).isReachable(2000);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isReachableSys() {
        try {
            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();
            if (myProcess.exitValue() == 0) return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onDisable() {
        bt.cancel();
    }

    @Override
    public void process() {
        addResult(getResultName(), getResultValue());
    }


}
