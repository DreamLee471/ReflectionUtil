/*
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
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


package org.cc.common.reflection.core.tools.javap;

import java.util.*;

import static org.cc.common.reflection.core.tools.javap.RuntimeConstants.*;

import java.io.*;

/* represents one entry of StackMapTable attribute
 */
class StackMapTableData {
    final int frameType;
    int offsetDelta;

    StackMapTableData(int frameType) {
        this.frameType = frameType;
    }

    void print(JavapPrinter p) {
        p.out.print("   frame_type = " + frameType);
    }

    static class SameFrame extends StackMapTableData {
        SameFrame(int frameType, int offsetDelta) {
            super(frameType);
            this.offsetDelta = offsetDelta;
        }
        void print(JavapPrinter p) {
            super.print(p);
            if (frameType < SAME_FRAME_BOUND) {
                p.out.println(" /* same */");
            } else {
                p.out.println(" /* same_frame_extended */");
                p.out.println("     offset_delta = " + offsetDelta);
            }
        }
    }

    static class SameLocals1StackItem extends StackMapTableData {
        final int[] stack;
        SameLocals1StackItem(int frameType, int offsetDelta, int[] stack) {
            super(frameType);
            this.offsetDelta = offsetDelta;
            this.stack = stack;
        }
        void print(JavapPrinter p) {
            super.print(p);
            if (frameType == SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
                p.out.println(" /* same_locals_1_stack_item_frame_extended */");
                p.out.println("     offset_delta = " + offsetDelta);
            } else {
                p.out.println(" /* same_locals_1_stack_item */");
            }
            p.printMap("     stack = [", stack);
        }
    }

    static class ChopFrame extends StackMapTableData {
        ChopFrame(int frameType, int offsetDelta) {
            super(frameType);
            this.offsetDelta = offsetDelta;
        }
        void print(JavapPrinter p) {
            super.print(p);
            p.out.println(" /* chop */");
            p.out.println("     offset_delta = " + offsetDelta);
        }
    }

    static class AppendFrame extends StackMapTableData {
        final int[] locals;
        AppendFrame(int frameType, int offsetDelta, int[] locals) {
            super(frameType);
            this.offsetDelta = offsetDelta;
            this.locals = locals;
        }
        void print(JavapPrinter p) {
            super.print(p);
            p.out.println(" /* append */");
            p.out.println("     offset_delta = " + offsetDelta);
            p.printMap("     locals = [", locals);
        }
    }

    static class FullFrame extends StackMapTableData {
        final int[] locals;
        final int[] stack;
        FullFrame(int offsetDelta, int[] locals, int[] stack) {
            super(FULL_FRAME);
            this.offsetDelta = offsetDelta;
            this.locals = locals;
            this.stack = stack;
        }
        void print(JavapPrinter p) {
            super.print(p);
            p.out.println(" /* full_frame */");
            p.out.println("     offset_delta = " + offsetDelta);
            p.printMap("     locals = [", locals);
            p.printMap("     stack = [", stack);
        }
    }

    static StackMapTableData getInstance(DataInputStream in, MethodData method)
                  throws IOException {
        int frameType = in.readUnsignedByte();

        if (frameType < SAME_FRAME_BOUND) {
            // same_frame
            return new SameFrame(frameType, frameType);
        } else if (SAME_FRAME_BOUND <= frameType && frameType < SAME_LOCALS_1_STACK_ITEM_BOUND) {
            // same_locals_1_stack_item_frame
            // read additional single stack element
            return new SameLocals1StackItem(frameType,
                                            (frameType - SAME_FRAME_BOUND),
                                            StackMapData.readTypeArray(in, 1, method));
        } else if (frameType == SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
            // same_locals_1_stack_item_extended
            return new SameLocals1StackItem(frameType,
                                            in.readUnsignedShort(),
                                            StackMapData.readTypeArray(in, 1, method));
        } else if (SAME_LOCALS_1_STACK_ITEM_EXTENDED < frameType  && frameType < SAME_FRAME_EXTENDED) {
            // chop_frame or same_frame_extended
            return new ChopFrame(frameType, in.readUnsignedShort());
        } else if (frameType == SAME_FRAME_EXTENDED) {
            // chop_frame or same_frame_extended
            return new SameFrame(frameType, in.readUnsignedShort());
        } else if (SAME_FRAME_EXTENDED < frameType  && frameType < FULL_FRAME) {
            // append_frame
            return new AppendFrame(frameType, in.readUnsignedShort(),
                                   StackMapData.readTypeArray(in, frameType - SAME_FRAME_EXTENDED, method));
        } else if (frameType == FULL_FRAME) {
            // full_frame
            int offsetDelta = in.readUnsignedShort();
            int locals_size = in.readUnsignedShort();
            int[] locals = StackMapData.readTypeArray(in, locals_size, method);
            int stack_size = in.readUnsignedShort();
            int[] stack = StackMapData.readTypeArray(in, stack_size, method);
            return new FullFrame(offsetDelta, locals, stack);
        } else {
            throw new ClassFormatError("unrecognized frame_type in StackMapTable");
        }
    }
}
