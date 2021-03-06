package com.freedom.lauzy.gankpro.function.constants;

/**
 * 获取数据类型
 * Created by Lauzy on 2017/1/20.
 */

public enum LoadData {
    INIT_DATA_TYPE(1), REFRESH_DATA_TYPE(2), LOAD_MORE_DATA_TYPE(3);

    private int mLoadType;

    LoadData(int loadType) {
        mLoadType = loadType;
    }

    @Override
    public String toString() {
        return "LoadData{" +
                "mLoadType=" + mLoadType +
                '}';
    }
}
