package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;

public class GadgetFatPlugin extends Gadget {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void process() {
        // TODO Auto-generated method stub

    }
    /*String fatplgname = "";
	int fatplgsize = 0; 
	@Override
	public String getName() {
		return "FatPlugin";
	}

	@Override
	public String getResultName() {
		return fatplgname;
	}

	@Override
	public int getResultValue() {
		return fatplgsize;
	}

	@Override
	public void init() {
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(plg, new Runnable(){
			@Override
			public void run() {
				
				Long size = 0L;
				String fplgname = "";
				for (Plugin jplg : Bukkit.getPluginManager().getPlugins()){
					Long psize = MemoryUtil.deepMemoryUsageOf(jplg);
					size = Math.max(psize, size);
					if (psize>size) {
						size = psize;
						fplgname = jplg.getName();
					}
				}
				fatplgname = fplgname;
				fatplgsize = (int) (size/1000); 
			}
		}, 20, 20*100);
		
	}*/

}
