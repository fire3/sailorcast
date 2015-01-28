package com.crixmod.sailorcast.view.adapters;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.SearchView;

import com.crixmod.sailorcast.siteapi.YouKuApi;
import com.crixmod.sailorcast.utils.HttpUtils;

/**
 * Created by fire3 on 15-1-28.
 */
public class SearchContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.crixmod.sailorcast.search_content_provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");

    // UriMatcher constant for search suggestions
    private static final int SEARCH_SUGGEST = 1;

    private static final UriMatcher uriMatcher;

    private static final String[] SEARCH_SUGGEST_COLUMNS = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA
    };
    private MatrixCursor asyncCursor;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
    }

    @Override
    public int delete(Uri uri, String arg1, String[] arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCreate() {
        asyncCursor = new MatrixCursor(SEARCH_SUGGEST_COLUMNS, 10);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
// Use the UriMatcher to see what kind of query we have
        switch (uriMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                String query = uri.getLastPathSegment().toLowerCase();
                Log.d("fire3", "query: " + query);
                try {
                    updateHTTP(findSuggestions(query));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return asyncCursor;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }


    /**
     * Returns a search result for the given book title.
     */
    private List<String> findSuggestions(String key) {
        // GoogleBooksProtocol is a wrapper for the Google Books API
        String url = new YouKuApi().getKeyWordsSuggestionUrl(key);
        String results = HttpUtils.syncGet(url);

        try {
            JSONObject retJson = new JSONObject(results);
            String status = retJson.optString("status");
            if (status != null && status.equals("success")) {
                List<String> ret = new ArrayList<>();
                JSONArray retsJson = retJson.optJSONArray("results");
                for (int i = 0; i < retsJson.length(); i++) {
                    JSONObject retJ = retsJson.getJSONObject(i);
                    String keyword = retJ.optString("keyword");
                    if (keyword != null)
                        ret.add(keyword);
                }
                if (ret.size() > 0)
                    return ret;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private void updateHTTP(List<String> suggestions) throws JSONException {
// Creates a new cursor when we get a HTTP response returns
        MatrixCursor nCursor = new MatrixCursor(SEARCH_SUGGEST_COLUMNS, 10);
        if(suggestions != null) {
            for (int i = 0; i < suggestions.size(); i++) {
                String j = suggestions.get(i);
                nCursor.addRow(new String[]{
                        "" + i, j, j
                });
            }
        }
        asyncCursor = nCursor;
    }

}
