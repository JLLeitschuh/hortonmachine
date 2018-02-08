/*
 * This file is part of HortonMachine (http://www.hortonmachine.org)
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * The HortonMachine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.hortonmachine.dbs.compat;

import java.sql.Savepoint;

/**
 * Interface wrapping db connections.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 *
 */
public interface IHMConnection extends AutoCloseable {
    public IHMStatement createStatement() throws Exception;

    public boolean getAutoCommit() throws Exception;

    public void setAutoCommit( boolean b ) throws Exception;

    public void commit() throws Exception;

    public IHMPreparedStatement prepareStatement( String sql ) throws Exception;

    public IHMPreparedStatement prepareStatement( String sql, int returnGeneratedKeys ) throws Exception;

    public Savepoint setSavepoint() throws Exception;

    public void rollback( Savepoint savepoint ) throws Exception;

    public void rollback() throws Exception;
}
