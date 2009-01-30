// kelondroBufferedRA.java 
// -----------------------
// part of The Kelondro Database
// (C) by Michael Peter Christen; mc@yacy.net
// first published on http://www.anomic.de
// Frankfurt, Germany, 2005
// last major change: 13.09.2005
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

package de.anomic.kelondro.io;

import java.io.IOException;

import de.anomic.kelondro.util.ByteBuffer;

public class BufferedRandomAccess extends AbstractRandomAccess implements RandomAccessInterface {

    private ByteBuffer sbb;
    private long pos;
    
    public BufferedRandomAccess() {
        sbb = new ByteBuffer();
        pos = 0;
    }
    
    public BufferedRandomAccess(final ByteBuffer bb) {
        sbb = bb;
        pos = 0;
    }
    
    public ByteBuffer getBuffer() {
        return this.sbb;
    }
    
    public long available() throws IOException {
        return Integer.MAX_VALUE - sbb.length();
    }

    public void close() throws IOException {
        sbb = null;
    }

    public long length() throws IOException {
        return sbb.length();
    }

    public void setLength(long length) throws IOException {
        sbb.resize((int) length);
    }
    
    public int read() throws IOException {
        return 0xff & sbb.byteAt((int) pos++);
    }

    public void readFully(final byte[] b, final int off, final int len) throws IOException {
        final byte[] g = sbb.getBytes((int) pos, len);
        pos += g.length;
        System.arraycopy(g, 0, b, off, g.length);
    }

    public void seek(final long pos) throws IOException {
        this.pos = pos;
    }

    public void write(final int b) throws IOException {
        this.sbb.overwrite((int) pos, b);
        pos++;
    }

    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.sbb.overwrite((int) pos, b, off, len);
        pos += len;
    }

}
