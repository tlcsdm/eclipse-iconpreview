package com.tlcsdm.eclipse.iconpreview.decorator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class IconDecorator extends BaseLabelProvider implements ILightweightLabelDecorator, IResourceChangeListener {

	public static final String[] SUPPORTED_EXTENSIONS = { "png", "jpg", "jpeg", "gif", "bmp", "ico" };

	// 缓存缩略图
	private final Map<IFile, ImageDescriptor> imageCache = new HashMap<>();

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
		for (String supported : SUPPORTED_EXTENSIONS) {
			if (supported.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	private ImageDescriptor createImageDescriptor(IFile file) {
		try (InputStream is = file.getContents()) {
			ImageData imageData = new ImageData(is);
			ImageData scaled = imageData.scaledTo(16, 16);
			Image image = new Image(Display.getDefault(), scaled);
			return ImageDescriptor.createFromImage(image);
		} catch (Exception e) {
			e.printStackTrace();
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
										.update("com.tlcsdm.eclipse.iconpreview.icondecorator");
							});
						}
					}
					return true;
				});
			} catch (CoreException e) {
				e.printStackTrace();
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
