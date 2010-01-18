/*
 * File   : $Source$
 * Date   : $Date$
 * Version: $Revision$
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

package org.opencms.widgets;

import org.opencms.file.CmsObject;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;

/**
 * Provides a OpenCms Group selection widget, for use on a widget dialog.<p>
 *
 * @author Michael Moossen
 * 
 * @version $Revision$ 
 * 
 * @since 6.0.0 
 */
public class CmsGroupWidget extends A_CmsWidget {

    /** Configuration parameter to set the flags of the groups to display, optional. */
    public static final String CONFIGURATION_FLAGS = "flags";

    /** Configuration parameter to set the organizational unit of the groups to display, optional. */
    public static final String CONFIGURATION_OUFQN = "oufqn";

    /** Configuration parameter to set the user of the groups to display, optional. */
    public static final String CONFIGURATION_USER = "user";

    /** The the flags used in the popup window. */
    private Integer m_flags;

    /** The the organizational unit used in the popup window. */
    private String m_ouFqn;

    /** The the user used in the popup window. */
    private String m_userName;

    /**
     * Creates a new group selection widget.<p>
     */
    public CmsGroupWidget() {

        // empty constructor is required for class registration
        this("");
    }

    /**
     * Creates a new group selection widget with the parameters to configure the popup window behaviour.<p>
     * 
     * @param flags the group flags to restrict the group selection, can be <code>null</code>
     * @param userName the user to restrict the group selection, can be <code>null</code>
     */
    public CmsGroupWidget(Integer flags, String userName) {

        m_flags = flags;
        m_userName = userName;
        m_ouFqn = null;
    }

    /**
     * Creates a new group selection widget with the parameters to configure the popup window behaviour.<p>
     * 
     * @param flags the group flags to restrict the group selection, can be <code>null</code>
     * @param userName the user to restrict the group selection, can be <code>null</code>
     * @param ouFqn the organizational unit to restrict the group selection, can be <code>null</code>
     */
    public CmsGroupWidget(Integer flags, String userName, String ouFqn) {

        m_flags = flags;
        m_userName = userName;
        m_ouFqn = ouFqn;
    }

    /**
     * Creates a new group selection widget with the given configuration.<p>
     * 
     * @param configuration the configuration to use
     */
    public CmsGroupWidget(String configuration) {

        super(configuration);
    }

    /**
     * @see org.opencms.widgets.A_CmsWidget#getConfiguration()
     */
    @Override
    public String getConfiguration() {

        StringBuffer result = new StringBuffer(8);

        // append flags to configuration
        if (m_flags != null) {
            if (result.length() > 0) {
                result.append("|");
            }
            result.append(CONFIGURATION_FLAGS);
            result.append("=");
            result.append(m_flags);
        }
        // append user to configuration
        if (m_userName != null) {
            if (result.length() > 0) {
                result.append("|");
            }
            result.append(CONFIGURATION_USER);
            result.append("=");
            result.append(m_userName);
        }
        // append oufqn to configuration
        if (m_ouFqn != null) {
            if (result.length() > 0) {
                result.append("|");
            }
            result.append(CONFIGURATION_OUFQN);
            result.append("=");
            result.append(m_ouFqn);
        }
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogIncludes(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog)
     */
    @Override
    public String getDialogIncludes(CmsObject cms, I_CmsWidgetDialog widgetDialog) {

        StringBuffer result = new StringBuffer(16);
        result.append(getJSIncludeFile(CmsWorkplace.getSkinUri() + "components/widgets/groupselector.js"));
        return result.toString();
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#getDialogWidget(org.opencms.file.CmsObject, org.opencms.widgets.I_CmsWidgetDialog, org.opencms.widgets.I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {

        String id = param.getId();
        StringBuffer result = new StringBuffer(128);

        result.append("<td class=\"xmlTd\">");
        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"maxwidth\"><tr><td style=\"width: 100%;\">");
        result.append("<input style=\"width: 99%;\" class=\"xmlInput");
        if (param.hasError()) {
            result.append(" xmlInputError");
        }
        result.append("\" value=\"");
        result.append(param.getStringValue(cms));
        result.append("\" name=\"");
        result.append(id);
        result.append("\" id=\"");
        result.append(id);
        result.append("\"></td>");
        result.append(widgetDialog.dialogHorizontalSpacer(10));
        result.append("<td><table class=\"editorbuttonbackground\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");

        StringBuffer buttonJs = new StringBuffer(8);
        buttonJs.append("javascript:openGroupWin('");
        buttonJs.append(OpenCms.getSystemInfo().getOpenCmsContext());
        buttonJs.append("/system/workplace/commons/group_selection.jsp");
        buttonJs.append("','EDITOR',  '");
        buttonJs.append(id);
        buttonJs.append("', document, ");
        if (m_flags != null) {
            buttonJs.append("'");
            buttonJs.append(m_flags);
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(", ");
        if (m_userName != null) {
            buttonJs.append("'");
            buttonJs.append(m_userName);
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(", ");
        if (m_ouFqn != null) {
            buttonJs.append("'");
            buttonJs.append(m_ouFqn);
            buttonJs.append("'");
        } else {
            buttonJs.append("null");
        }
        buttonJs.append(");");

        result.append(widgetDialog.button(
            buttonJs.toString(),
            null,
            "group",
            org.opencms.workplace.Messages.GUI_DIALOG_BUTTON_SEARCH_0,
            widgetDialog.getButtonStyle()));
        result.append("</tr></table>");
        result.append("</td></tr></table>");

        result.append("</td>");

        return result.toString();
    }

    /**
     * Returns the flags, or <code>null</code> if all.<p>
     *
     * @return the flags, or <code>null</code> if all
     */
    public Integer getFlags() {

        return m_flags;
    }

    /**
     * Returns the organizational unit fqn, or <code>null</code> if all.<p>
     *
     * @return the organizational unit fqn
     */
    public String getOufqn() {

        return m_ouFqn;
    }

    /**
     * Returns the user name, or <code>null</code> if all.<p>
     *
     * @return the user name
     */
    public String getUserName() {

        return m_userName;
    }

    /**
     * @see org.opencms.widgets.I_CmsWidget#newInstance()
     */
    public I_CmsWidget newInstance() {

        return new CmsGroupWidget(getConfiguration());
    }

    /**
     * @see org.opencms.widgets.A_CmsWidget#setConfiguration(java.lang.String)
     */
    @Override
    public void setConfiguration(String configuration) {

        m_userName = null;
        m_flags = null;
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(configuration)) {
            int flagsIndex = configuration.indexOf(CONFIGURATION_FLAGS);
            if (flagsIndex != -1) {
                // user is given
                String flags = configuration.substring(CONFIGURATION_FLAGS.length() + 1);
                if (flags.indexOf('|') != -1) {
                    // cut eventual following configuration values
                    flags = flags.substring(0, flags.indexOf('|'));
                }
                try {
                    m_flags = Integer.valueOf(flags);
                } catch (Throwable t) {
                    // invalid flags
                }
            }
            int groupIndex = configuration.indexOf(CONFIGURATION_USER);
            if (groupIndex != -1) {
                // user is given
                String user = configuration.substring(groupIndex + CONFIGURATION_USER.length() + 1);
                if (user.indexOf('|') != -1) {
                    // cut eventual following configuration values
                    user = user.substring(0, user.indexOf('|'));
                }
                m_userName = user;
            }
            int oufqnIndex = configuration.indexOf(CONFIGURATION_OUFQN);
            if (oufqnIndex != -1) {
                // user is given
                String oufqn = configuration.substring(oufqnIndex + CONFIGURATION_OUFQN.length() + 1);
                if (oufqn.indexOf('|') != -1) {
                    // cut eventual following configuration values
                    oufqn = oufqn.substring(0, oufqn.indexOf('|'));
                }
                m_ouFqn = oufqn;
            }
        }
        super.setConfiguration(configuration);
    }
}