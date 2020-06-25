package com.greycells.dateone.client.view.impl;

import org.waveprotocol.wave.client.widget.common.ImplPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.greycells.dateone.client.view.IMainView;
import com.greycells.dateone.shared.IUser;

public class MainViewImpl extends Composite implements IMainView {

	public IUser currentUser;

	interface Style extends CssResource {
	}

	private static MainPageLayoutUiBinder uiBinder = GWT
			.create(MainPageLayoutUiBinder.class);

	interface MainPageLayoutUiBinder extends UiBinder<DockLayoutPanel, MainViewImpl> {
	}

	public MainViewImpl() {		
	    DockLayoutPanel self = uiBinder.createAndBindUi(this);
	    RootPanel.get("app").add(self);
	    // DockLayoutPanel forcibly conflicts with sensible layout control, and
	    // sticks inline styles on elements without permission. They must be
	    // cleared.
	    self.getElement().getStyle().clearPosition();
	    //splitPanel.setWidgetMinSize(rightPanel, 300);
	    initWidget(self);
	}

	public void setPageElements(Widget runner, Widget main, Widget right) {
		runnerPanel.clear();
		contentPanel.clear();
		rightPanel.clear();

		runnerPanel.add(runner);
		contentPanel.add(main);
		rightPanel.add(right);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// this.presenter = presenter;
	}

	@UiField
	FlowPanel rightPanel;

	@UiField
	FlowPanel runnerPanel;

	@UiField
	ImplPanel contentPanel;

	@UiField(provided=true) 
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);

	@UiField
	Style style;
}
