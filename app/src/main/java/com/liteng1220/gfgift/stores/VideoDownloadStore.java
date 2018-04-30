package com.liteng1220.gfgift.stores;

import android.content.Intent;

import com.liteng1220.gfgift.GfApplication;
import com.liteng1220.gfgift.action.Action;
import com.liteng1220.gfgift.cmd.CmdListVodSactions;
import com.liteng1220.gfgift.conf.Constant;
import com.liteng1220.gfgift.db.LocalVideoDownloadDataSource;
import com.liteng1220.gfgift.db.VideoDownload;
import com.liteng1220.gfgift.listener.VideoSqliteDataSubject;
import com.liteng1220.gfgift.repository.VideoDownloadRepository;
import com.liteng1220.gfgift.service.VideoDownloadService;
import com.liteng1220.gfgift.util.YddUtil;
import com.liteng1220.lyt.utility.FileUtil;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoDownloadStore extends WebStore {

    private VideoDownloadRepository repository;
    private String name;

    public VideoDownloadStore() {
        super();
        dataList = new ArrayList<>();
        repository = new VideoDownloadRepository(LocalVideoDownloadDataSource.getInstance(GfApplication.getContext()));
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        switch (action.getType()) {
            case Constant.ActionType.ACTION_TYPE_START_DOWNLOAD:
                handleActionData(action.getData());
                emitStoreChange();
                break;
            case Constant.ActionType.ACTION_TYPE_STOP_DOWNLOAD:
                repository.deleteAllVideoDownload();
                GfApplication.getContext().stopService(new Intent(GfApplication.getContext(), VideoDownloadService.class));
                break;
        }
    }

    @Override
    protected void handleResponseResult(Object data, HashMap actionData) {
        if (data instanceof CmdListVodSactions.VodData) {
            name = (String) actionData.get(Constant.Bundle.BUNDLE_KEY_NAME);
            CmdListVodSactions.VodData vodData = (CmdListVodSactions.VodData) data;
            handleVodDataList(name, vodData);
        }
    }

    private void handleVodDataList(final String name, final CmdListVodSactions.VodData data) {
        if (data == null) {
            return;
        }

        final File folder = new File(YddUtil.getFileDownloadPath(), name);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        repository.getVideoDownload(name, new VideoDownloadRepository.VideoDownloadQueryCallback() {
            @Override
            public void onVideoDownloadQuery(VideoDownload videoDownload) {
                if (videoDownload == null) { // 仅保存数据库里未存在的数据
                    CmdListVodSactions.VodData vodData = (CmdListVodSactions.VodData) data;
                    List<CmdListVodSactions.DataItem> dataItemList = vodData.dataItemList;
                    int size = dataItemList.size();
                    int index = -1;
                    saveData2Db(dataItemList, size, index, name, folder);
                }
            }
        });
    }

    private void saveData2Db(final List<CmdListVodSactions.DataItem> dataItemList, final int size, final int index, final String name, final File folder) {
        final int newIndex = index + 1;

        if (newIndex == size) {
            return;
        }

        CmdListVodSactions.DataItem dataItem = dataItemList.get(newIndex);
        final VideoDownload newVideoDownload = new VideoDownload();
        newVideoDownload.name = name;
        newVideoDownload.uuid = dataItem.uuid;
        newVideoDownload.index = dataItem.num;
        newVideoDownload.totalNum = size;
        newVideoDownload.filename = FileUtil.createFilename(dataItem.oriUrl, String.valueOf(dataItem.num));
        newVideoDownload.folderPath = folder.getAbsolutePath();
        newVideoDownload.createTime = System.currentTimeMillis();

        repository.createVideoDownload(newVideoDownload, new VideoDownloadRepository.VideoDownloadCreateCallback() {
            @Override
            public void onVideoDownloadCreate() {
                VideoSqliteDataSubject.getInstance().notifyDataAdded(newVideoDownload);
                saveData2Db(dataItemList, size, newIndex, name, folder);
            }
        });
    }

    @Override
    protected StoreChangeEvent changeEvent() {
        return new VideoDownloadChangeEvent(status, msg, name);
    }

    public class VideoDownloadChangeEvent implements StoreChangeEvent {
        public int status;
        public String msg;
        public String name;

        public VideoDownloadChangeEvent(int searchResultStatus, String searchResultMsg, String name) {
            this.status = searchResultStatus;
            this.msg = searchResultMsg;
            this.name = name;
        }
    }
}
