/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/jsp/util/CmsJspContentAccessBean.java,v $
 * Date   : $Date: 2011/04/12 12:10:04 $
 * Version: $Revision: 1.6 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) 2002 - 2009 Alkacon Software GmbH (http://www.alkacon.com)
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

package org.opencms.jsp.util;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.types.CmsResourceTypeXmlPage;
import org.opencms.i18n.CmsLocaleManager;
import org.opencms.main.CmsException;
import org.opencms.main.CmsRuntimeException;
import org.opencms.util.CmsCollectionUtil;
import org.opencms.util.CmsConstantMap;
import org.opencms.xml.I_CmsXmlDocument;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.page.CmsXmlPageFactory;
import org.opencms.xml.types.I_CmsXmlContentValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.base.Function;

/**
 * Allows access to the individual elements of an XML content, usually used inside a loop of a 
 * <code>&lt;cms:contentload&gt;</code> tag.<p>
 * 
 * The implementation is optimized for performance and uses lazy initializing of the 
 * requested values as much as possible.<p>
 * 
 * @author Alexander Kandzior
 * 
 * @version $Revision: 1.6 $ 
 * 
 * @since 7.0.2
 * 
 * @see org.opencms.jsp.CmsJspTagContentAccess
 */
public class CmsJspContentAccessBean {

    /**
     * Provides Booleans that indicate if a specified locale is available in the XML content, 
     * the input is assumed to be a String that represents a Locale.<p>
     */
    public class CmsHasLocaleFunction implements Function<String, Boolean> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Boolean apply(String input) {

            return Boolean.valueOf(getRawContent().hasLocale(CmsJspElFunctions.convertLocale(input)));
        }
    }

    /**
     * Provides Booleans that indicate if a specified path exists in the XML content,  
     * the input is assumed to be a String that represents an xpath in the XML content.<p>
     */
    public class CmsHasLocaleValueFunction implements Function<String, Map<String, Boolean>> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Map<String, Boolean> apply(String input) {

            Locale locale = CmsJspElFunctions.convertLocale(input);
            Map<String, Boolean> result;
            if (getRawContent().hasLocale(locale)) {
                result = CmsCollectionUtil.makeComputingMap(new CmsHasValueFunction(locale));
            } else {
                result = CmsConstantMap.CONSTANT_BOOLEAN_FALSE_MAP;
            }
            return result;
        }
    }

    /**
     * Provides a Map with Booleans that indicate if a specified path exists in the XML content in the selected Locale,  
     * the input is assumed to be a String that represents an xpath in the XML content.<p>
     */
    public class CmsHasValueFunction implements Function<String, Boolean> {

        /** The selected locale. */
        private Locale m_selectedLocale;

        /**
         * Constructor with a locale.<p>
         * 
         * @param locale the locale to use
         */
        public CmsHasValueFunction(Locale locale) {

            m_selectedLocale = locale;
        }

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Boolean apply(String input) {

            return Boolean.valueOf(getRawContent().hasValue(input, m_selectedLocale));
        }
    }

    /**
     * Provides a Map which lets the user access the list of element names from the selected locale in an XML content, 
     * the input is assumed to be a String that represents a Locale.<p>
     */
    public class CmsLocaleNamesFunction implements Function<String, List<String>> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public List<String> apply(String input) {

            Locale locale = CmsLocaleManager.getLocale(input);

            return getRawContent().getNames(locale);
        }
    }

    /**
     * Provides a Map which lets the user access sub value Lists from the selected locale in an XML content, 
     * the input is assumed to be a String that represents a Locale.<p>
     */
    public class CmsLocaleSubValueListFunction
    implements Function<String, Map<String, List<CmsJspContentAccessValueWrapper>>> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Map<String, List<CmsJspContentAccessValueWrapper>> apply(String input) {

            Locale locale = CmsJspElFunctions.convertLocale(input);
            Map<String, List<CmsJspContentAccessValueWrapper>> result;
            if (getRawContent().hasLocale(locale)) {
                result = CmsCollectionUtil.makeComputingMap(new CmsSubValueListFunction(locale));
            } else {
                result = CmsConstantMap.CONSTANT_EMPTY_LIST_MAP;
            }
            return result;
        }
    }

    /**
     * Provides a Map which lets the user access a value from the selected locale in an XML content, 
     * the input is assumed to be a String that represents a Locale.<p>
     */
    public class CmsLocaleValueFunction implements Function<String, Map<String, CmsJspContentAccessValueWrapper>> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Map<String, CmsJspContentAccessValueWrapper> apply(String input) {

            Locale locale = CmsLocaleManager.getLocale(input);
            Map<String, CmsJspContentAccessValueWrapper> result;
            if (getRawContent().hasLocale(locale)) {
                result = CmsCollectionUtil.makeComputingMap(new CmsValueFunction(locale));
            } else {
                result = CONSTANT_NULL_VALUE_WRAPPER_MAP;
            }
            return result;
        }
    }

    /**
     * Provides a Map which lets the user access value Lists from the selected locale in an XML content, 
     * the input is assumed to be a String that represents a Locale.<p>
     */
    public class CmsLocaleValueListFunction
    implements Function<String, Map<String, List<CmsJspContentAccessValueWrapper>>> {

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public Map<String, List<CmsJspContentAccessValueWrapper>> apply(String input) {

            Locale locale = CmsJspElFunctions.convertLocale(input);
            Map<String, List<CmsJspContentAccessValueWrapper>> result;
            if (getRawContent().hasLocale(locale)) {
                result = CmsCollectionUtil.makeComputingMap(new CmsValueListFunction(locale));
            } else {
                result = CmsConstantMap.CONSTANT_EMPTY_LIST_MAP;
            }
            return result;
        }
    }

    /**
     * Provides a Map which lets the user access sub value Lists in an XML content, 
     * the input is assumed to be a String that represents an xpath in the XML content.<p>
     */
    public class CmsSubValueListFunction implements Function<String, List<CmsJspContentAccessValueWrapper>> {

        /** The selected locale. */
        private Locale m_selectedLocale;

        /**
         * Constructor with a locale.<p>
         * 
         * @param locale the locale to use
         */
        public CmsSubValueListFunction(Locale locale) {

            m_selectedLocale = locale;
        }

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public List<CmsJspContentAccessValueWrapper> apply(String input) {

            List<I_CmsXmlContentValue> values = getRawContent().getSubValues(input, m_selectedLocale);
            List<CmsJspContentAccessValueWrapper> result = new ArrayList<CmsJspContentAccessValueWrapper>();
            Iterator<I_CmsXmlContentValue> i = values.iterator();
            while (i.hasNext()) {
                // XML content API offers List of values only as Objects, must iterate them and create Strings 
                I_CmsXmlContentValue value = i.next();
                result.add(CmsJspContentAccessValueWrapper.createWrapper(getCmsObject(), value));
            }
            return result;
        }
    }

    /**
     * Provides a Map which lets the user access a value in an XML content, 
     * the input is assumed to be a String that represents an xpath in the XML content.<p>
     */
    public class CmsValueFunction implements Function<String, CmsJspContentAccessValueWrapper> {

        /** The selected locale. */
        private Locale m_selectedLocale;

        /**
         * Constructor with a locale.<p>
         * 
         * @param locale the locale to use
         */
        public CmsValueFunction(Locale locale) {

            m_selectedLocale = locale;
        }

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public CmsJspContentAccessValueWrapper apply(String input) {

            I_CmsXmlContentValue value = getRawContent().getValue(input, m_selectedLocale);
            return CmsJspContentAccessValueWrapper.createWrapper(getCmsObject(), value);
        }
    }

    /**
     * Provides a Map which lets the user access value Lists in an XML content, 
     * the input is assumed to be a String that represents an xpath in the XML content.<p>
     */
    public class CmsValueListFunction implements Function<String, List<CmsJspContentAccessValueWrapper>> {

        /** The selected locale. */
        private Locale m_selectedLocale;

        /**
         * Constructor with a locale.<p>
         * 
         * @param locale the locale to use
         */
        public CmsValueListFunction(Locale locale) {

            m_selectedLocale = locale;
        }

        /**
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        public List<CmsJspContentAccessValueWrapper> apply(String input) {

            List<I_CmsXmlContentValue> values = getRawContent().getValues(input, m_selectedLocale);
            List<CmsJspContentAccessValueWrapper> result = new ArrayList<CmsJspContentAccessValueWrapper>();
            Iterator<I_CmsXmlContentValue> i = values.iterator();
            while (i.hasNext()) {
                // XML content API offers List of values only as Objects, must iterate them and create Strings 
                I_CmsXmlContentValue value = i.next();
                result.add(CmsJspContentAccessValueWrapper.createWrapper(getCmsObject(), value));
            }
            return result;
        }
    }

    /** Constant Map that always returns the {@link CmsJspContentAccessValueWrapper#NULL_VALUE_WRAPPER}.*/
    protected static final Map<String, CmsJspContentAccessValueWrapper> CONSTANT_NULL_VALUE_WRAPPER_MAP = new CmsConstantMap<String, CmsJspContentAccessValueWrapper>(
        CmsJspContentAccessValueWrapper.NULL_VALUE_WRAPPER);

    /** The OpenCms context of the current user. */
    private CmsObject m_cms;

    /** The XML content to access. */
    private I_CmsXmlDocument m_content;

    /** The lazy initialized map for the "has locale" check. */
    private Map<String, Boolean> m_hasLocale;

    /** The lazy initialized map for the "has locale value" check. */
    private Map<String, Map<String, Boolean>> m_hasLocaleValue;

    /** The selected locale for accessing entries from the XML content. */
    private Locale m_locale;

    /** The lazy initialized with the locale names. */
    private Map<String, List<String>> m_localeNames;

    /** The lazy initialized with the locale sub value lists. */
    private Map<String, Map<String, List<CmsJspContentAccessValueWrapper>>> m_localeSubValueList;

    /** The lazy initialized with the locale value. */
    private Map<String, Map<String, CmsJspContentAccessValueWrapper>> m_localeValue;

    /** The lazy initialized with the locale value lists. */
    private Map<String, Map<String, List<CmsJspContentAccessValueWrapper>>> m_localeValueList;

    /** Resource the XML content is created from. */
    private CmsResource m_resource;

    /**
     * No argument constructor, required for a JavaBean.<p>
     * 
     * You must call {@link #init(CmsObject, Locale, I_CmsXmlDocument, CmsResource)} and provide the 
     * required values when you use this constructor.<p> 
     * 
     * @see #init(CmsObject, Locale, I_CmsXmlDocument, CmsResource)
     */
    public CmsJspContentAccessBean() {

        // must call init() manually later
    }

    /**
     * Creates a content access bean based on a Resource, using the current request context locale.<p>
     * 
     * @param cms the OpenCms context of the current user
     * @param resource the resource to create the content from
     */
    public CmsJspContentAccessBean(CmsObject cms, CmsResource resource) {

        this(cms, cms.getRequestContext().getLocale(), resource);
    }

    /**
     * Creates a content access bean based on a Resource.<p>
     * 
     * @param cms the OpenCms context of the current user
     * @param locale the Locale to use when accessing the content
     * @param resource the resource to create the content from
     */
    public CmsJspContentAccessBean(CmsObject cms, Locale locale, CmsResource resource) {

        init(cms, locale, null, resource);
    }

    /**
     * Creates a content access bean based on an XML content object.<p>
     * 
     * @param cms the OpenCms context of the current user
     * @param locale the Locale to use when accessing the content
     * @param content the content to access
     */
    public CmsJspContentAccessBean(CmsObject cms, Locale locale, I_CmsXmlDocument content) {

        init(cms, locale, content, content.getFile());
    }

    /**
     * Returns the OpenCms user context this bean was initialized with.<p>
     * 
     * @return the OpenCms user context this bean was initialized with
     */
    public CmsObject getCmsObject() {

        return m_cms;
    }

    /**
     * Returns the raw VFS file object the content accessed by this bean was created from.<p>
     * 
     * This can be used to access information from the raw file on a JSP.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     Root path of the resource: ${content.file.rootPath}
     * &lt;/cms:contentload&gt;</pre>
     * 
     * @return the raw VFS file object the content accessed by this bean was created from
     */
    public CmsFile getFile() {

        return getRawContent().getFile();
    }

    /**
     * Returns the site path of the current resource, that is the result of 
     * {@link CmsObject#getSitePath(CmsResource)} with the resource 
     * obtained by {@link #getFile()}.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     Site path of the resource: "${content.filename}";
     * &lt;/cms:contentload&gt;</pre>
     * 
     * @return the site path of the current resource
     * 
     * @see CmsObject#getSitePath(CmsResource)
     */
    public String getFilename() {

        return m_cms.getSitePath(getRawContent().getFile());
    }

    /**
     * Returns a lazy initialized Map that provides Booleans that indicate if a specified Locale is available 
     * in the XML content.<p>
     * 
     * The provided Map key is assumed to be a String that represents a Locale.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:if test="${content.hasLocale['de']}" &gt;
     *         The content has a "de" Locale! 
     *     &lt;/c:if&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides Booleans that indicate if a specified Locale is available 
     *      in the XML content
     */
    public Map<String, Boolean> getHasLocale() {

        if (m_hasLocale == null) {
            m_hasLocale = CmsCollectionUtil.makeComputingMap(new CmsHasLocaleFunction());
        }
        return m_hasLocale;
    }

    /**
     * Returns a lazy initialized Map that provides a Map that provides Booleans that 
     * indicate if a value (xpath) is available in the XML content in the selected locale.<p>
     * 
     * The first provided Map key is assumed to be a String that represents the Locale,
     * the second provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:if test="${content.hasLocaleValue['de']['Title']}" &gt;
     *         The content has a "Title" value in the "de" Locale! 
     *     &lt;/c:if&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * Please note that you can also test if a locale value exists like this:<pre>
     * &lt;c:if test="${content.value['de']['Title'].exists}" &gt; ... &lt;/c:if&gt;</pre>
     *  
     * @return a lazy initialized Map that provides a Map that provides Booleans that 
     *      indicate if a value (xpath) is available in the XML content in the selected locale
     * 
     * @see #getHasValue()
     */
    public Map<String, Map<String, Boolean>> getHasLocaleValue() {

        if (m_hasLocaleValue == null) {
            m_hasLocaleValue = CmsCollectionUtil.makeComputingMap(new CmsHasLocaleValueFunction());
        }
        return m_hasLocaleValue;
    }

    /**
     * Returns a lazy initialized Map that provides Booleans that 
     * indicate if a value (xpath) is available in the XML content in the current locale.<p>
     * 
     * The provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:if test="${content.hasValue['Title']}" &gt;
     *         The content has a "Title" value in the current locale! 
     *     &lt;/c:if&gt;
     * &lt;/cms:contentload&gt;</pre>
     * 
     * Please note that you can also test if a value exists like this:<pre>
     * &lt;c:if test="${content.value['Title'].exists}" &gt; ... &lt;/c:if&gt;</pre>
     *  
     * @return a lazy initialized Map that provides Booleans that 
     *      indicate if a value (xpath) is available in the XML content in the current locale
     * 
     * @see #getHasLocaleValue()
     */
    public Map<String, Boolean> getHasValue() {

        return getHasLocaleValue().get(m_locale);
    }

    /**
     * Returns the Locale this bean was initialized with.<p>
     *
     * @return the locale  this bean was initialized with
     */
    public Locale getLocale() {

        return m_locale;
    }

    /**
     * Returns a lazy initialized Map that provides a List with all available elements paths (Strings) 
     * used in this document in the selected locale.<p>
     * 
     * The provided Map key is assumed to be a String that represents the Locale.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach items="${content.localeNames['de']}" var="elem"&gt;
     *         &lt;c:out value="${elem}" /&gt;
     *     &lt;/c:forEach&gt;  
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides a Map that provides 
     *      values from the XML content in the selected locale
     * 
     * @see #getNames()
     */
    public Map<String, List<String>> getLocaleNames() {

        if (m_localeNames == null) {
            m_localeNames = CmsCollectionUtil.makeComputingMap(new CmsLocaleNamesFunction());
        }
        return m_localeNames;
    }

    /**
     * Returns a lazy initialized Map that provides a Map that provides Lists of direct sub values 
     * from the XML content in the selected locale.<p>
     * 
     * The first provided Map key is assumed to be a String that represents the Locale,
     * the second provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach var="item" items="${content.localeSubValueList['de']['Items']}"&gt;
     *         ${item}
     *     &lt;/c:forEach&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides a Map that provides Lists of direct sub values 
     *      from the XML content in the selected locale
     * 
     * @see #getLocaleValue()
     */
    public Map<String, Map<String, List<CmsJspContentAccessValueWrapper>>> getLocaleSubValueList() {

        if (m_localeSubValueList == null) {
            m_localeSubValueList = CmsCollectionUtil.makeComputingMap(new CmsLocaleSubValueListFunction());
        }
        return m_localeSubValueList;
    }

    /**
     * Returns a lazy initialized Map that provides a Map that provides 
     * values from the XML content in the selected locale.<p>
     * 
     * The first provided Map key is assumed to be a String that represents the Locale,
     * the second provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     The Title in Locale "de": ${content.localeValue['de']['Title']}
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides a Map that provides 
     *      values from the XML content in the selected locale
     * 
     * @see #getValue()
     */
    public Map<String, Map<String, CmsJspContentAccessValueWrapper>> getLocaleValue() {

        if (m_localeValue == null) {
            m_localeValue = CmsCollectionUtil.makeComputingMap(new CmsLocaleValueFunction());
        }
        return m_localeValue;
    }

    /**
     * Returns a lazy initialized Map that provides a Map that provides Lists of values 
     * from the XML content in the selected locale.<p>
     * 
     * The first provided Map key is assumed to be a String that represents the Locale,
     * the second provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach var="teaser" items="${content.localeValueList['de']['Teaser']}"&gt;
     *         ${teaser}
     *     &lt;/c:forEach&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides a Map that provides Lists of values 
     *      from the XML content in the selected locale
     * 
     * @see #getLocaleValue()
     */
    public Map<String, Map<String, List<CmsJspContentAccessValueWrapper>>> getLocaleValueList() {

        if (m_localeValueList == null) {
            m_localeValueList = CmsCollectionUtil.makeComputingMap(new CmsLocaleValueListFunction());
        }
        return m_localeValueList;
    }

    /**
     * Returns a list with all available elements paths (Strings) used in this document
     * in the current locale.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach items="${content.names}" var="elem"&gt;
     *         &lt;c:out value="${elem}" /&gt;
     *     &lt;/c:forEach&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a list with all available elements paths (Strings) used in this document in the current locale
     * 
     * @see #getLocaleNames()
     */
    public List<String> getNames() {

        return getLocaleNames().get(m_locale);
    }

    /**
     * Returns the raw XML content object that is accessed by this bean.<p>
     * 
     * @return the raw XML content object that is accessed by this bean
     */
    public I_CmsXmlDocument getRawContent() {

        if (m_content == null) {
            // content has not been provided, must unmarshal XML first
            CmsFile file;
            try {
                file = m_cms.readFile(m_resource);
                if (CmsResourceTypeXmlPage.isXmlPage(file)) {
                    // this is an XML page
                    m_content = CmsXmlPageFactory.unmarshal(m_cms, file);
                } else {
                    // this is an XML content
                    m_content = CmsXmlContentFactory.unmarshal(m_cms, file);
                }
            } catch (CmsException e) {
                // this usually should not happen, as the resource already has been read by the current user 
                // and we just upgrade it to a File
                throw new CmsRuntimeException(Messages.get().container(
                    Messages.ERR_XML_CONTENT_UNMARSHAL_1,
                    m_resource.getRootPath()), e);
            }
        }
        return m_content;
    }

    /**
     * Returns a lazy initialized Map that provides Lists of direct sub values 
     * of the given value from the XML content in the current locale.<p>
     * 
     * The provided Map key is assumed to be a String that represents the xpath to the value.
     * Use this method in case you want to iterate over a List of sub values from the XML content.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach var="teaser" items="${content.subValueList['Items']}"&gt;
     *         ${item}
     *     &lt;/c:forEach&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides Lists of values from the XML content in the current locale
     * 
     * @see #getLocaleValueList()
     */
    public Map<String, List<CmsJspContentAccessValueWrapper>> getSubValueList() {

        return getLocaleSubValueList().get(m_locale);
    }

    /**
     * Returns a lazy initialized Map that provides values from the XML content in the current locale.<p>
     * 
     * The provided Map key is assumed to be a String that represents the xpath to the value.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     The Title: ${content.value['Title']}
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides values from the XML content in the current locale
     * 
     * @see #getLocaleValue()
     */
    public Map<String, CmsJspContentAccessValueWrapper> getValue() {

        return getLocaleValue().get(m_locale);
    }

    /**
     * Returns a lazy initialized Map that provides Lists of values from the XML content in the current locale.<p>
     * 
     * The provided Map key is assumed to be a String that represents the xpath to the value.
     * Use this method in case you want to iterate over a List of values form the XML content.<p>
     * 
     * Usage example on a JSP with the JSTL:<pre>
     * &lt;cms:contentload ... &gt;
     *     &lt;cms:contentaccess var="content" /&gt;
     *     &lt;c:forEach var="teaser" items="${content.valueList['Teaser']}"&gt;
     *         ${teaser}
     *     &lt;/c:forEach&gt;
     * &lt;/cms:contentload&gt;</pre>
     *  
     * @return a lazy initialized Map that provides Lists of values from the XML content in the current locale
     * 
     * @see #getLocaleValueList()
     */
    public Map<String, List<CmsJspContentAccessValueWrapper>> getValueList() {

        return getLocaleValueList().get(m_locale);
    }

    /**
     * Returns an instance of a VFS access bean,
     * initialized with the OpenCms user context this bean was created with.<p>
     * 
     * @return an instance of a VFS access bean, 
     *      initialized with the OpenCms user context this bean was created with
     */
    public CmsJspVfsAccessBean getVfs() {

        return CmsJspVfsAccessBean.create(m_cms);
    }

    /**
     * Initialize this instance.<p>
     * 
     * @param cms the OpenCms context of the current user
     * @param locale the Locale to use when accessing the content
     * @param content the XML content to access
     * @param resource the resource to create the content from
     */
    public void init(CmsObject cms, Locale locale, I_CmsXmlDocument content, CmsResource resource) {

        m_cms = cms;
        m_locale = locale;
        m_content = content;
        m_resource = resource;
    }
}