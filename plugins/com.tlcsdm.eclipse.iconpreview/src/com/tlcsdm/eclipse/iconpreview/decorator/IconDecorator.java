package com.tlcsdm.eclipse.iconpreview.decorator;

import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.tlcsdm.eclipse.iconpreview.Activator;

public class IconDecorator extends BaseLabelProvider implements ILightweightLabelDecorator, IResourceChangeListener {

	public static final String[] SUPPORTED_EXTENSIONS = { "png", "jpg", "jpeg", "gif", "bmp", "ico" };

	private static final Set<String> SUPPORTED_EXTENSIONS_SET = Set.of(SUPPORTED_EXTENSIONS);

	private static final ILog LOGGER = ILog.of(IconDecorator.class);

	private final ConcurrentMap<IFile, ImageDescriptor> imageCache = new ConcurrentHashMap<>();

	public IconDecorator() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void decorate(Object element, IDecoration decoration) {
		if (element instanceof IFile file) {
			String ext = file.getFileExtension();
			if (ext != null && isSupportedImage(ext)) {
				ImageDescriptor descriptor = imageCache.computeIfAbsent(file, this::createImageDescriptor);
				if (descriptor != null) {
					decoration.addOverlay(descriptor, IDecoration.TOP_LEFT);
				}
			}
		}
	}

	private boolean isSupportedImage(String ext) {
		return SUPPORTED_EXTENSIONS_SET.contains(ext.toLowerCase());
	}

	private ImageDescriptor createImageDescriptor(IFile file) {
		try (InputStream is = file.getContents()) {
			ImageData imageData = new ImageData(is);
			ImageData scaled = imageData.scaledTo(16, 16);
			return ImageDescriptor.createFromImageDataProvider(zoom -> scaled);
		} catch (Exception e) {
			LOGGER.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					"Failed to create image descriptor for: " + file.getFullPath(), e));
			return null;
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		if (delta != null) {
			try {
				delta.accept(resourceDelta -> {
					if (resourceDelta.getResource() instanceof IFile file) {
						String ext = file.getFileExtension();
						if (ext != null && isSupportedImage(ext)) {
							imageCache.remove(file);
							Display.getDefault().asyncExec(() -> {
								PlatformUI.getWorkbench().getDecoratorManager()
										.update(Activator.DECORATOR_ID);
							});
						}
					}
					return true;
				});
			} catch (CoreException e) {
				LOGGER.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						"Error processing resource change event", e));
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		imageCache.clear();
	}

}
