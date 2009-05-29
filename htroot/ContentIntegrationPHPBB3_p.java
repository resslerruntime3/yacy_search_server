// ContentIntegrationPHPBB3_p.java
// (C) 2009 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 27.05.2009 on http://yacy.net
//
// $LastChangedDate: 2006-04-02 22:40:07 +0200 (So, 02 Apr 2006) $
// $LastChangedRevision: 1986 $
// $LastChangedBy: orbiter $
//
// LICENSE
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

import de.anomic.content.dao.Dao;
import de.anomic.content.dao.PhpBB3Dao;
import de.anomic.http.httpRequestHeader;
import de.anomic.kelondro.util.DateFormatter;
import de.anomic.plasma.plasmaSwitchboard;
import de.anomic.server.serverObjects;
import de.anomic.server.serverSwitch;

public class ContentIntegrationPHPBB3_p {

    public static serverObjects respond(final httpRequestHeader header, final serverObjects post, final serverSwitch<?> env) {
        final serverObjects prop = new serverObjects();
        final plasmaSwitchboard sb = (plasmaSwitchboard) env;
        
        prop.put("check", 0);
        prop.put("export", 0);
        
        if (post != null) {
            
            String urlstub = post.get("content.phpbb3.urlstub", "");
            String dbtype = post.get("content.phpbb3.dbtype", "");
            String dbhost = post.get("content.phpbb3.dbhost", "");
            int    dbport = post.getInt("content.phpbb3.dbport", 3306);
            String dbname = post.get("content.phpbb3.dbname", "");
            String prefix = post.get("content.phpbb3.tableprefix", "");
            String dbuser = post.get("content.phpbb3.dbuser", "");
            String dbpw = post.get("content.phpbb3.dbpw", "");
            int    ppf = post.getInt("content.phpbb3.ppf", 1000);
            
            
            sb.setConfig("content.phpbb3.urlstub", urlstub);
            sb.setConfig("content.phpbb3.dbtype", dbtype);
            sb.setConfig("content.phpbb3.dbhost", dbhost);
            sb.setConfig("content.phpbb3.dbport", dbport);
            sb.setConfig("content.phpbb3.dbname", dbname);
            sb.setConfig("content.phpbb3.tableprefix", prefix);
            sb.setConfig("content.phpbb3.dbuser", dbuser);
            sb.setConfig("content.phpbb3.dbpw", dbpw);
            sb.setConfig("content.phpbb3.ppf", ppf);
            
            if (post.containsKey("check")) {
                try {
                    Dao db = new PhpBB3Dao(
                                            urlstub,
                                            dbtype,
                                            dbhost,
                                            dbport,
                                            dbname,
                                            prefix,
                                            dbuser,
                                            dbpw
                                            );
                    prop.put("check", 1);
                    prop.put("check_posts", db.size());
                    prop.putHTML("check_first", db.first().toString());
                    prop.putHTML("check_last", db.latest().toString());
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    prop.put("check", 2);
                    prop.put("check_error", e.getMessage());
                }
                                    
            }
            
            if (post.containsKey("export")) {
                try {
                    Dao db = new PhpBB3Dao(
                                            urlstub,
                                            dbtype,
                                            dbhost,
                                            dbport,
                                            dbname,
                                            prefix,
                                            dbuser,
                                            dbpw
                                            );
                    
                    int files = db.writeSurrogates(db.query(0, -1, 100), sb.surrogatesInPath, "fullexport-" + DateFormatter.formatShortSecond(), ppf);
                    prop.put("export", 1);
                    prop.put("export_files", files);
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    prop.put("export", 2);
                    prop.put("export_error", e.getMessage());
                }
            }
            
        }

        prop.putHTML("content.phpbb3.urlstub", sb.getConfig("content.phpbb3.urlstub", ""));
        prop.putHTML("content.phpbb3.dbtype", sb.getConfig("content.phpbb3.dbtype", ""));
        prop.putHTML("content.phpbb3.dbhost", sb.getConfig("content.phpbb3.dbhost", ""));
        prop.putHTML("content.phpbb3.dbport", sb.getConfig("content.phpbb3.dbport", ""));
        prop.putHTML("content.phpbb3.dbname", sb.getConfig("content.phpbb3.dbname", ""));
        prop.putHTML("content.phpbb3.tableprefix", sb.getConfig("content.phpbb3.tableprefix", ""));
        prop.putHTML("content.phpbb3.dbuser", sb.getConfig("content.phpbb3.dbuser", ""));
        prop.putHTML("content.phpbb3.dbpw", sb.getConfig("content.phpbb3.dbpw", ""));
        prop.putHTML("content.phpbb3.ppf", sb.getConfig("content.phpbb3.ppf", ""));

        return prop;
    }
}
