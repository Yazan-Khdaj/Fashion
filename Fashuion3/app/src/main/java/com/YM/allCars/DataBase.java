package com.YM.allCars;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.Context;

public class DataBase extends SQLiteAssetHelper
{
    private static final String DB_NAME = "MyData.db";
    private static final int DB_VERSION = 1;

    public static final String TB_NAME = "Fashuion";
    public static final String TB_CLN_ID = "Id";
    public static final String TB_CLN_MODEL = "Model";
    public static final String TB_CLN_COLOR = "Color";
    public static final String TB_CLN_PRICE = "Price";
    public static final String TB_CLN_IMAGE = "Image";
    public static final String TB_CLN_DESC = "Desc";
    public static final String TB_CLN_DATE = "Date";

    public DataBase( Context Context )
    {
        super( Context, DB_NAME, null, DB_VERSION );
    }

}
