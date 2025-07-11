package com.tlcsdm.eclipse.iconpreview.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tlcsdm.eclipse.iconpreview.Activator;
import com.tlcsdm.eclipse.iconpreview.decorator.IconDecorator;

/**
 * @author unknowIfGuestInDream
 */
public class HomePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		setDescription("Icon Preview Version: " + Activator.VERSION);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = createComposite(parent);
		createLabel(composite, "Icon files are directly showed in Package Explorer or other navigators.");
		createLabel(composite, "Supported image formats: " + String.join(", ", IconDecorator.SUPPORTED_EXTENSIONS));
		return composite;
	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 5;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return composite;
	}

	protected Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		GridData data = new GridData();
		data.horizontalSpan = 1;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}
}
