/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/workplace/tools/jobs/Attic/CmsActionActivateJob.java,v $
 * Date   : $Date: 2005/05/10 15:58:42 $
 * Version: $Revision: 1.1 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2002 - 2005 Alkacon Software (http://www.alkacon.com)
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
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
package org.opencms.workplace.tools.jobs;

import org.opencms.file.CmsObject;
import org.opencms.main.OpenCms;
import org.opencms.scheduler.CmsScheduledJobInfo;
import org.opencms.workplace.list.A_CmsListTwoStatesAction;
import org.opencms.workplace.list.I_CmsListDirectAction;

/**
 * List action to activate a scheduled job, can be used as direct action for a single selected item and
 * as multi action for selected items.<p>
 * 
 * @author Michael Moossen (m.moossen@alkacon.com)
 * @author Andreas Zahner (a.zahner@alkacon.com)
 * 
 * @version $Revision: 1.1 $
 * @since 5.7.3
 */
public class CmsActionActivateJob  extends A_CmsListTwoStatesAction {
    
    /**
     * Default Constructor.<p>
     * 
     * @param listId the id of the associated list
     * @param id unique id
     * @param cms the cms context
     */
    protected CmsActionActivateJob(String listId, String id, CmsObject cms) {

        super(listId, id, cms);
    }
    
    /**
     * @see org.opencms.workplace.list.A_CmsListToggleAction#selectAction()
     */
    public I_CmsListDirectAction selectAction() {

        if (getItem() != null) {          
            String jobId = getItem().getId();
            CmsScheduledJobInfo job = OpenCms.getScheduleManager().getJob(jobId);
            if (! job.isActive()) {
                // activate job
                return getFirstAction();
            }
        }
        // deactivate job
        return getSecondAction();
    }
    
}
