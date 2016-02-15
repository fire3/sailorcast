package com.crixmod.sailorcast.utils;

import com.crixmod.sailorcast.model.SCVideoClip;
import com.crixmod.sailorcast.model.SCVideoClips;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;

/**
 * Created by fire3 on 16-2-14.
 */
public class M3UServer extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(M3UServer.class.getName());
    private String m3u8_normal = "";
    private String m3u8_high = "";
    private String m3u8_super = "";

    public void addNormalVideoClips(SCVideoClips videoClips)
    {
        m3u8_normal = "#EXTM3U\n" +
                    "#EXT-X-MEDIA-SEQUENCE:0\n";
        for (SCVideoClip clip: videoClips ) {
            m3u8_normal += "#EXTINF:" + clip.getDuration() + ",\n";
            m3u8_normal += clip.getSource() + "\n";
        }
        m3u8_normal += "#EXT-X-ENDLIST\n";

    }

    public void addHighVideoClips(SCVideoClips videoClips)
    {
        m3u8_high = "#EXTM3U\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";
        for (SCVideoClip clip: videoClips ) {
            m3u8_high += "#EXTINF:" + clip.getDuration() + ",\n";
            m3u8_high += clip.getSource() + "\n";
        }
        m3u8_high += "#EXT-X-ENDLIST\n";

    }

    public void addSuperVideoClips(SCVideoClips videoClips)
    {
        m3u8_super = "#EXTM3U\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n";
        for (SCVideoClip clip: videoClips ) {
            m3u8_super += "#EXTINF:" + clip.getDuration() + ",\n";
            m3u8_super += clip.getSource() + "\n";
        }
        m3u8_super += "#EXT-X-ENDLIST\n";

    }


    public M3UServer() {
        super(8080);
    }

    public M3UServer(int port) {
        super(port);
    }

    public static void main(String[] args) {
        ServerRunner.run(M3UServer.class);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        M3UServer.LOG.info(method + " '" + uri + "' ");
        if(session.getUri().contains("normal"))
            return new Response(Response.Status.OK, NanoHTTPD.MIME_HTML, m3u8_normal);
        else if(session.getUri().contains("high"))
            return new Response(Response.Status.OK, NanoHTTPD.MIME_HTML, m3u8_high);
        else if(session.getUri().contains("super"))
            return new Response(Response.Status.OK, NanoHTTPD.MIME_HTML, m3u8_high);
        else
            return new Response(Response.Status.OK, NanoHTTPD.MIME_HTML, "");
    }
}
