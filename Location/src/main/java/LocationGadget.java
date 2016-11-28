import me.fromgate.okglass.Gadget;
import org.bukkit.Location;


/**
 * Created by Igor on 28.11.2016.
 */
public class LocationGadget extends Gadget {

    private int position= 1;

    boolean showX;
    boolean showY;
    boolean showZ;
    boolean showYaw;
    boolean showPitch;

    @Override
    public void onEnable() {
        position = loadInt("position-score", 100);
        showX = loadBoolean("show.x-coord", true);
        showY = loadBoolean("show.y-coord", true);
        showZ = loadBoolean("show.z-coord", true);
        showYaw = loadBoolean("show.yaw", true);
        showPitch = loadBoolean("show.pitch", true);

    }

    @Override
    public String getName() {
        return "Location";
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void process() {
        Location loc = getPlayer().getLocation();


        int pos = elementsToShow();
        if (showX) {
            addResult(String.format("x: %d", loc.getBlockX()), pos);
            pos--;
        }
        if (showY) {
            addResult(String.format("y: %d", loc.getBlockY()), pos);
            pos--;
        }
        if (showZ) {
            addResult(String.format("y: %d", loc.getBlockZ()), pos);
            pos--;
        }

        if (showYaw) {
            addResult(String.format("Yaw: %.2f", loc.getYaw()), pos);
            pos--;
        }

        if (showPitch) {
            addResult(String.format("Pitch: %.2f", loc.getPitch()), pos);
            pos--;
        }


    }

    public int elementsToShow () {
        int count = 0;
        if (showX) count++;
        if (showY) count++;
        if (showZ) count++;
        if (showYaw) count++;
        if (showPitch) count++;
        return count;
    }
}
