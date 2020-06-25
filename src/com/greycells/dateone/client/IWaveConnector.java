package com.greycells.dateone.client;

import org.waveprotocol.box.webclient.client.RemoteViewServiceMultiplexer;
import org.waveprotocol.wave.model.id.IdGenerator;

import com.google.gwt.dom.client.Element;

public interface IWaveConnector {
	public void start();
	
	public IdGenerator getIdGenerator();
	
	public RemoteViewServiceMultiplexer getChannel();

	void setLocaleElement(Element element);
}
