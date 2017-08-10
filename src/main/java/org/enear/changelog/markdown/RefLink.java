package org.enear.changelog.markdown;

import java.net.MalformedURLException;
import java.net.URL;

public class RefLink {

    private String ref;
    private URL link;

    public RefLink(String ref, URL link) throws MalformedURLException {
        this.ref = ref;
        this.link = link;
    }

    public String toString() {
        return String.format("[%s]: %s", ref, link);
    }

}
