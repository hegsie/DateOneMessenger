package com.greycells.dateone.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapperWithFactory;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
public interface AppPlaceHistoryMapper extends PlaceHistoryMapperWithFactory<AppPlaceFactory> {
}
