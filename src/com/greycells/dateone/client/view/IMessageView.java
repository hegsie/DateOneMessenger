package com.greycells.dateone.client.view;

import java.util.Set;

import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import com.google.gwt.user.client.ui.Widget;

public interface IMessageView {
	public interface Presenter {
		void openWave(WaveId id, boolean isNewWave,
			Set<ParticipantId> participants);
	}

	void setPresenter(IMainView.Presenter mainPres, Presenter presenter);

	Widget asWidget();

	public abstract void openWave(WaveId ref, boolean isNewWave, Set<ParticipantId> participants);
}
