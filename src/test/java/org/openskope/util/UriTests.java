package org.openskope.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import  org.openskope.util.Uri;

public class UriTests {

    @Test
    public void testEnsureTerminalSlash_inputEndsWithSingleSlash() throws Exception {
        assertThat(Uri.ensureTerminalSlash("http:/foo/"), is("http:/foo/"));
    }

    @Test
    public void testEnsureTerminalSlash_inputEndsWithNonSlash() throws Exception {
        assertThat(Uri.ensureTerminalSlash("http:/foo"), is("http:/foo/"));
    }

    @Test
    public void testEnsureTerminalSlash_inputEndsWithDoubleSlash() throws Exception {
        assertThat(Uri.ensureTerminalSlash("http:/foo//"), is("http:/foo//"));
    }

    @Test
    public void testEnsureTerminalSlash_inputIsSlash() throws Exception {
        assertThat(Uri.ensureTerminalSlash("/"), is("/"));
    }

    @Test
    public void testEnsureTerminalSlash_inputIsEmptyString() throws Exception {
        assertThat(Uri.ensureTerminalSlash(""), is("/"));
    }

    @Test
    public void testEnsureTerminalSlash_inputIsNull() throws Exception {
        assertThat(Uri.ensureTerminalSlash(null), is("/"));
    }
}