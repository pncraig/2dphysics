package graphics;

import javax.swing.RepaintManager;
import javax.swing.JComponent;

/**
 * The NullRepaint manager is a RepaintManager that doesn't
 * do any repainting. Useful when all the rendering is done 
 * manually by the application.
 */
public class NullRepaintManager extends RepaintManager {
	/**
	 * Installs the NullRepaintManager.
	 */
	public static void install() {
		RepaintManager repaintManager = new NullRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);
	}
	
	@Override
	public void addInvalidComponent(JComponent c) {}
	
	@Override
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {}
	
	@Override
	public void markCompletelyDirty(JComponent c) {}
	
	@Override
	public void paintDirtyRegions() {}
}
