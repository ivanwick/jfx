/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.prism.sw;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.impl.PrismSettings;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicInteger;

final class SWPresentable extends SWRTTexture implements Presentable {

    private final PresentableState pState;
    private Pixels pixels;
    private IntBuffer pixBuf;
    private final AtomicInteger uploadCount = new AtomicInteger(0);

    public SWPresentable(PresentableState pState, SWResourceFactory factory) {
        super(factory, pState.getWidth(), pState.getHeight());
        this.pState = pState;
    }

    public boolean lockResources() {
        return false;
    }

    public boolean prepare(Rectangle dirtyregion) {
        if (!pState.isViewClosed()) {
            /*
             * RT-27374
             * TODO: make sure the imgrep matches the Pixels.getNativeFormat()
             * TODO: dirty region support
             */
            int w = getPhysicalWidth();
            int h = getPhysicalHeight();
            if (pixels == null || uploadCount.get() > 0) {
                pixBuf = IntBuffer.allocate(w*h);
                pixels = Application.GetApplication().createPixels(w, h, pixBuf);
            }
            IntBuffer buf = getSurface().getDataIntBuffer();
            assert buf.hasArray();
            System.arraycopy(buf.array(), 0, pixBuf.array(), 0, w*h);
            return true;
        } else {
            return false;
        }
    }

    public boolean present() {
        uploadCount.incrementAndGet();
        pState.uploadPixels(pixels, uploadCount);
        return true;
    }

    public float getPixelScaleFactor() {
        return 1.0f;
    }

    public boolean recreateOnResize() {
        return true;
    }

    public int getContentWidth() {
        return pState.getWidth();
    }

    public int getContentHeight() {
        return pState.getHeight();
    }
}