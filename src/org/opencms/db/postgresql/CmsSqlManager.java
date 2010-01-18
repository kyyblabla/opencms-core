/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/db/postgresql/CmsSqlManager.java,v $
 * Date   : $Date: 2010/01/18 10:03:33 $
 * Version: $Revision: 1.11 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) 2002 - 2010 Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.db.postgresql;

/**
 * PostgreSql implementation of the SQL manager.<p>
 * 
 * @author Antonio Core 
 * 
 * @version $Revision: 1.11 $
 * 
 * @since 6.0.0 
 */
public class CmsSqlManager extends org.opencms.db.generic.CmsSqlManager {

    /** Serial version UID required for safe serialization. */
    private static final long serialVersionUID = 525126179172222238L;

    /** The filename/path of the SQL query properties. */
    private static final String QUERY_PROPERTIES = "org/opencms/db/postgresql/query.properties";

    /**
     * @see org.opencms.db.generic.CmsSqlManager#CmsSqlManager()
     */
    public CmsSqlManager() {

        super();
        loadQueryProperties(QUERY_PROPERTIES);
    }

}