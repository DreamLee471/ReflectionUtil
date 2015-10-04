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

/* represents one entry of StackMap attribute
 */
class StackMapData {
    final int offset;
    final int[] locals;
    final int[] stack;

    StackMapData(int offset, int[] locals, int[] stack) {
        this.offset = offset;
        this.locals = locals;
        this.stack = stack;
    }

    StackMapData(DataInputStream in, MethodData method) throws IOException {
        offset = in.readUnsignedShort();
        int local_size = in.readUnsignedShort();
        locals = readTypeArray(in, local_size, method);
        int stack_size = in.readUnsignedShort();
        stack = readTypeArray(in, stack_size, method);
    }

    static final int[] readTypeArray(DataInputStream in, int length, MethodData method) throws IOException {
        int[] types = new int[length];
        for (int i=0; i<length; i++) {
            types[i] = readType(in, method);
        }
        return types;
    }

    static final int readType(DataInputStream in, MethodData method) throws IOException {
        int type = in.readUnsignedByte();
        if (type == ITEM_Object || type == ITEM_NewObject) {
            type = type | (in.readUnsignedShort()<<8);
        }
        return type;
    }

    void print(JavapPrinter p) {
        p.out.println("   " + offset + ":");
        p.printMap("    locals = [", locals);
        p.printMap("    stack = [", stack);
    }
}
