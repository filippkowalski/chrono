package com.projectfundamentals.src.ui;

import com.chrono.src.common.constants.ConfigConstants;
import com.chrono.src.ui.list.ListViewLogic;
import com.chrono.src.ui.list.endless.EndlessListPresenter;
import com.chrono.src.rest.utils.WeakReferenceCallback;
import com.projectfundamentals.src.rest.models.Channel;
import com.projectfundamentals.src.rest.service.RestService;

/**
 * Created by Filip Kowalski on 05.03.17.
 */

class ChannelsPresenter extends EndlessListPresenter<Channel> {

    ChannelsPresenter(ListViewLogic<Channel> contract) {
        super(contract);
    }

    @Override
    public void downloadDataFromApi(int offset, boolean showLoadingView) {
        super.downloadDataFromApi(offset, showLoadingView);

        call = RestService.getInstance()
                .getService()
                .getChannels(offset, ConfigConstants.LIST_OFFSET_LIMIT);
        call.enqueue(new WeakReferenceCallback<>(getCallback()));
    }

}