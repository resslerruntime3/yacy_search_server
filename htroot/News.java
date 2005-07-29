// News.java
// -----------------------
// part of YaCy
// (C) by Michael Peter Christen; mc@anomic.de
// first published on http://www.anomic.de
// Frankfurt, Germany, 2005
// last major change: 29.07.2005
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Using this software in any meaning (reading, learning, copying, compiling,
// running) means that you agree that the Author(s) is (are) not responsible
// for cost, loss of data or any harm that may be caused directly or indirectly
// by usage of this softare or this documentation. The usage of this software
// is on your own risk. The installation and usage (starting/running) of this
// software may allow other people or application to access your computer and
// any attached devices and is highly dependent on the configuration of the
// software which must be done by the user of the software; the author(s) is
// (are) also not responsible for proper configuration and usage of the
// software, even if provoked by documentation provided together with
// the software.
//
// Any changes to this file according to the GPL as documented in the file
// gpl.txt aside this file in the shipment you received can be done to the
// lines that follows this copyright notice here, but changes must not be
// done inside the copyright notive above. A re-distribution must contain
// the intact and unchanged copyright notice.
// Contributions and changes to the program code must be marked as such.

// You must compile this file with
// javac -classpath .:../classes Network.java
// if the shell's current path is HTROOT

import java.util.Enumeration;
import java.util.HashMap;
import java.io.IOException;

import de.anomic.http.httpHeader;
import de.anomic.server.serverObjects;
import de.anomic.server.serverSwitch;
import de.anomic.server.serverDate;
import de.anomic.yacy.yacyCore;
import de.anomic.yacy.yacyNewsRecord;

public class News {
    
    public static serverObjects respond(httpHeader header, serverObjects post, serverSwitch sb) {
        // return variable that accumulates replacements
        serverObjects prop = new serverObjects();
        boolean overview = (post == null) || (((String) post.get("page", "0")).equals("0"));
        
        if (overview) {
            // show overview
            prop.put("table", 0);
            prop.put("page", 0);
        } else {
            // generate table
            int tableID = Integer.parseInt((String) post.get("page", "1")) - 1;
            prop.put("table", 1);
            prop.put("page", tableID + 1);
            
            if (yacyCore.seedDB == null) {
                
            } else {
                int maxCount = yacyCore.newsPool.size(tableID);
                if (maxCount > 500) maxCount = 500;
                
                yacyNewsRecord record;
                for (int i = 0; i < maxCount; i++) try {
                    record = yacyCore.newsPool.get(tableID, i);
                    prop.put("table_list_" + i + "_ori", record.originator());
                    prop.put("table_list_" + i + "_cre", yacyCore.universalDateShortString(record.created()));
                    prop.put("table_list_" + i + "_cat", record.category());
                    prop.put("table_list_" + i + "_rec", record.received());
                    prop.put("table_list_" + i + "_dis", record.attributes().toString());
                } catch (IOException e) {e.printStackTrace();}
                prop.put("table_list", maxCount);
            }
        }
        // return rewrite properties
        return prop;
    }

}
