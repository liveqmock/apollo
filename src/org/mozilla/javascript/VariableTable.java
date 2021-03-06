/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1997-1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s):
 * Roger Lawrence
 * Igor Bukanov
 *
 * Alternatively, the contents of this file may be used under the
 * terms of the GNU Public License (the "GPL"), in which case the
 * provisions of the GPL are applicable instead of those above.
 * If you wish to allow use of your version of this file only
 * under the terms of the GPL and not to allow others to use your
 * version of this file under the NPL, indicate your decision by
 * deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL.  If you do not delete
 * the provisions above, a recipient may use your version of this
 * file under either the NPL or the GPL.
 */

package org.mozilla.javascript;

import java.io.*;

public class VariableTable {

    public int size() {
        return itsVariables.size();
    }

    public int getParameterCount(){
        return varStart;
    }

    public Object getVariable(int index) {
        return itsVariables.get(index);
    }

    public boolean hasVariable(String name) {
        return itsVariableNames.has(name);
    }

    public Object getVariable(String name) {
        int vIndex = itsVariableNames.get(name, -1);
        if (vIndex != -1)
            return itsVariables.get(vIndex);
        else
            return null;
    }

    public int getOrdinal(String name) {
        return itsVariableNames.get(name, -1);
    }

    public void getAllVariables(Object[] destination) {
        itsVariables.toArray(destination);
    }

    public void addParameter(String pName, Object paramObj) {
        // Check addParameter is not called after addLocal
        if (varStart != itsVariables.size()) Context.codeBug();
        int pIndex = itsVariableNames.get(pName, -1);
        if (itsVariableNames.has(pName)) {
            String message = Context.getMessage1("msg.dup.parms", pName);
            Context.reportWarning(message, null, 0, null, 0);
        }
        int index = varStart++;
        itsVariables.add(paramObj);
        itsVariableNames.put(pName, index);
    }

    public void addLocal(String vName, Object varObj) {
        int vIndex = itsVariableNames.get(vName, -1);
        if (vIndex != -1) {
            // There's already a variable or parameter with this name.
            return;
        }
        int index = itsVariables.size();
        itsVariables.add(varObj);
        itsVariableNames.put(vName, index);
    }

    // This should only be called very early in compilation
    public void removeLocal(String name) {
        int i = itsVariableNames.get(name, -1);
        if (i != -1) {
            itsVariables.remove(i);
            itsVariableNames.remove(name);
            ObjToIntMap.Iterator iter = itsVariableNames.newIterator();
            for (iter.start(); !iter.done(); iter.next()) {
                int v = iter.getValue();
                if (v > i) {
                    iter.setValue(v - 1);
                }
            }
        }
    }

    // a list of the formal parameters and local variables
    private ObjArray itsVariables = new ObjArray();

    // mapping from name to index in list
    private ObjToIntMap itsVariableNames = new ObjToIntMap(11);

    private int varStart;               // index in list of first variable

}
