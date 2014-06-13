/*
 * Build system for LensKit, and open-source recommender systems toolkit.
 * Copyright 2010-2014 Regents of the University of Minnesota and contributors
 * Work on LensKit has been funded by the National Science Foundation under
 * grants IIS 05-34939, 08-08692, 08-12148, and 10-17697.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the
 *   distribution.
 *
 * - Neither the name of the University of Minnesota nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.grouplens.javadoc.taglets;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

import java.util.Map;

/**
 * Taglet for arbitrary inline elements. This taglet simply wraps its argument
 * in the HTML tag corresponding to the taglet name.
 *
 * @author Michael Ekstrand
 */
public class ExtraInlineTaglet implements Taglet {
    private static final String[] INLINE_TAGS = {"var"};

    private String tagName;

    /**
     * Register the extra inline taglet.
     * @param name The tag name.
     */
    public ExtraInlineTaglet(String name) {
        tagName = name;
    }

    /**
     * Register the taglet.
     * @param map The taglet map to register in.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void register(Map map) {
        for (String tag: INLINE_TAGS) {
            map.put(tag, new ExtraInlineTaglet(tag));
        }
    }

    @Override
    public boolean inField() {
        return true;
    }

    @Override
    public boolean inConstructor() {
        return true;
    }

    @Override
    public boolean inMethod() {
        return true;
    }

    @Override
    public boolean inOverview() {
        return true;
    }

    @Override
    public boolean inPackage() {
        return true;
    }

    @Override
    public boolean inType() {
        return true;
    }

    @Override
    public boolean isInlineTag() {
        return true;
    }

    @Override
    public String getName() {
        return tagName;
    }

    @Override
    public String toString(Tag tag) {
        StringBuilder bld = new StringBuilder();
        bld.append('<').append(tagName).append('>');
        bld.append(tag.text());
        bld.append("</").append(tagName).append('>');
        return bld.toString();
    }

    @Override
    public String toString(Tag[] tags) {
        StringBuilder bld = new StringBuilder();
        for (Tag tag : tags) {
            bld.append(toString(tag));
        }
        return bld.toString();
    }
}
