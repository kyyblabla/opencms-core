CREATE TABLE CMS_CONTENTS (
	PUBLISH_TAG_FROM INTEGER NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	FILE_CONTENT BYTE, 
	ONLINE_FLAG INTEGER, 
	PUBLISH_TAG_TO INTEGER, 
	PRIMARY KEY (PUBLISH_TAG_FROM, RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_COUNTERS (
	NAME VARCHAR(255) NOT NULL, 
	COUNTER INTEGER, 
	PRIMARY KEY (NAME)
) LOCK MODE ROW;

CREATE TABLE CMS_GROUPS (
	GROUP_ID VARCHAR(36) NOT NULL, 
	GROUP_DESCRIPTION VARCHAR(255) NOT NULL, 
	GROUP_FLAGS INTEGER, 
	GROUP_NAME VARCHAR(128) NOT NULL, 
	GROUP_OU VARCHAR(128) NOT NULL, 
	PARENT_GROUP_ID VARCHAR(36) NOT NULL, 
	PRIMARY KEY (GROUP_ID), 
	UNIQUE (GROUP_NAME, GROUP_OU) CONSTRAINT UK_GROUPS
) LOCK MODE ROW;

CREATE TABLE CMS_GROUPUSERS (
	GROUP_ID VARCHAR(36) NOT NULL, 
	USER_ID VARCHAR(36) NOT NULL, 
	GROUPUSER_FLAGS INTEGER, 
	PRIMARY KEY (GROUP_ID, USER_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_PRINCIPALS (
	PRINCIPAL_ID VARCHAR(36) NOT NULL, 
	PRINCIPAL_DATEDELETED NUMERIC(32,0), 
	PRINCIPAL_DESCRIPTION VARCHAR(255) NOT NULL, 
	PRINCIPAL_EMAIL VARCHAR(128) NOT NULL, 
	PRINCIPAL_NAME VARCHAR(128) NOT NULL, 
	PRINCIPAL_OU VARCHAR(128), 
	PRINCIPAL_TYPE VARCHAR(5) NOT NULL, 
	PRINCIPAL_USERDELETED VARCHAR(36) NOT NULL, 
	PRIMARY KEY (PRINCIPAL_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_PROJECTRESOURCES (
	PROJECT_ID VARCHAR(36) NOT NULL, 
	PUBLISH_TAG INTEGER NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024) NOT NULL, 
	PRIMARY KEY (PROJECT_ID, PUBLISH_TAG, RESOURCE_PATH)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_PROJECTS (
	PUBLISH_TAG INTEGER NOT NULL, 
	DATE_CREATED NUMERIC(32,0), 
	GROUP_ID VARCHAR(36) NOT NULL, 
	MANAGERGROUP_ID VARCHAR(36) NOT NULL, 
	PROJECT_DESCRIPTION VARCHAR(255) NOT NULL, 
	PROJECT_ID VARCHAR(36) NOT NULL, 
	PROJECT_NAME VARCHAR(255) NOT NULL, 
	PROJECT_OU VARCHAR(128) NOT NULL, 
	PROJECT_PUBLISHDATE NUMERIC(32,0), 
	PROJECT_PUBLISHED_BY VARCHAR(36) NOT NULL, 
	PROJECT_TYPE INTEGER, 
	USER_ID VARCHAR(36) NOT NULL, 
	PRIMARY KEY (PUBLISH_TAG)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_PROPERTIES (
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_TYPE INTEGER NOT NULL, 
	PUBLISH_TAG INTEGER NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL, 
	PROPERTY_VALUE LVARCHAR(2048) NOT NULL, 
	PRIMARY KEY (PROPERTYDEF_ID, PROPERTY_MAPPING_TYPE, PUBLISH_TAG, STRUCTURE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) NOT NULL, 
	PROPERTYDEF_TYPE INTEGER, 
	PRIMARY KEY (PROPERTYDEF_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_RESOURCES (
	PUBLISH_TAG INTEGER NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	DATE_CONTENT NUMERIC(32,0), 
	DATE_CREATED NUMERIC(32,0), 
	DATE_LASTMODIFIED NUMERIC(32,0), 
	PROJECT_LASTMODIFIED VARCHAR(36) NOT NULL, 
	RESOURCE_FLAGS INTEGER, 
	RESOURCE_SIZE INTEGER, 
	RESOURCE_STATE INTEGER, 
	RESOURCE_TYPE INTEGER, 
	RESOURCE_VERSION INTEGER, 
	SIBLING_COUNT INTEGER, 
	USER_CREATED VARCHAR(36) NOT NULL, 
	USER_LASTMODIFIED VARCHAR(36) NOT NULL, 
	PRIMARY KEY (PUBLISH_TAG, RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_HISTORY_STRUCTURE (
	PUBLISH_TAG INTEGER NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	VERSION INTEGER NOT NULL, 
	DATE_EXPIRED NUMERIC(32,0), 
	DATE_RELEASED NUMERIC(32,0), 
	PARENT_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024), 
	STRUCTURE_STATE INTEGER, 
	STRUCTURE_VERSION INTEGER, 
	PRIMARY KEY (PUBLISH_TAG, STRUCTURE_ID, VERSION)
) LOCK MODE ROW;

CREATE TABLE CMS_LOG (
	LOG_DATE NUMERIC(32,0) NOT NULL, 
	LOG_TYPE INTEGER NOT NULL, 
	USER_ID VARCHAR(36) NOT NULL, 
	LOG_DATA LVARCHAR(1024), 
	STRUCTURE_ID VARCHAR(36), 
	PRIMARY KEY (LOG_DATE, LOG_TYPE, USER_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_ACCESSCONTROL (
	PRINCIPAL_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	ACCESS_ALLOWED INTEGER, 
	ACCESS_DENIED INTEGER, 
	ACCESS_FLAGS INTEGER, 
	PRIMARY KEY (PRINCIPAL_ID, RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_CONTENTS (
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	FILE_CONTENT BYTE, 
	PRIMARY KEY (RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_PROPERTIES (
	PROPERTY_ID VARCHAR(36) NOT NULL, 
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_TYPE INTEGER, 
	PROPERTY_VALUE LVARCHAR(2048) NOT NULL, 
	PRIMARY KEY (PROPERTY_ID), 
	UNIQUE (PROPERTYDEF_ID, PROPERTY_MAPPING_ID) CONSTRAINT UK_OFFLINE_PROPERTIES
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) NOT NULL, 
	PROPERTYDEF_TYPE INTEGER, 
	PRIMARY KEY (PROPERTYDEF_ID), 
	UNIQUE (PROPERTYDEF_NAME) CONSTRAINT UK_OFFLINE_PROPERTYDEF
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_RESOURCES (
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	DATE_CONTENT NUMERIC(32,0), 
	DATE_CREATED NUMERIC(32,0), 
	DATE_LASTMODIFIED NUMERIC(32,0), 
	PROJECT_LASTMODIFIED VARCHAR(36) NOT NULL, 
	RESOURCE_FLAGS INTEGER, 
	RESOURCE_SIZE INTEGER, 
	RESOURCE_STATE INTEGER, 
	RESOURCE_TYPE INTEGER, 
	RESOURCE_VERSION INTEGER, 
	SIBLING_COUNT INTEGER, 
	USER_CREATED VARCHAR(36) NOT NULL, 
	USER_LASTMODIFIED VARCHAR(36) NOT NULL, 
	PRIMARY KEY (RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_RESOURCE_RELATIONS (
	RELATION_SOURCE_ID VARCHAR(36) NOT NULL, 
	RELATION_SOURCE_PATH LVARCHAR(1024) NOT NULL, 
	RELATION_TARGET_ID VARCHAR(36) NOT NULL, 
	RELATION_TARGET_PATH LVARCHAR(1024) NOT NULL, 
	RELATION_TYPE INTEGER NOT NULL 
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	DATE_EXPIRED NUMERIC(32,0), 
	DATE_RELEASED NUMERIC(32,0), 
	PARENT_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024), 
	STRUCTURE_STATE INTEGER, 
	STRUCTURE_VERSION INTEGER, 
	PRIMARY KEY (STRUCTURE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_OFFLINE_URLNAME_MAPPINGS (
	DATE_CHANGED NUMERIC(32,0) NOT NULL, 
	NAME VARCHAR(255) NOT NULL, 
	STATE INTEGER NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	LOCALE VARCHAR(10)
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_ACCESSCONTROL (
	PRINCIPAL_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	ACCESS_ALLOWED INTEGER, 
	ACCESS_DENIED INTEGER, 
	ACCESS_FLAGS INTEGER, 
	PRIMARY KEY (PRINCIPAL_ID, RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_PROPERTIES (
	PROPERTY_ID VARCHAR(36) NOT NULL, 
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_ID VARCHAR(36) NOT NULL, 
	PROPERTY_MAPPING_TYPE INTEGER, 
	PROPERTY_VALUE LVARCHAR(2048) NOT NULL, 
	PRIMARY KEY (PROPERTY_ID), 
	UNIQUE (PROPERTYDEF_ID, PROPERTY_MAPPING_ID) CONSTRAINT UK_ONLINE_PROPERTIES
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_PROPERTYDEF (
	PROPERTYDEF_ID VARCHAR(36) NOT NULL, 
	PROPERTYDEF_NAME VARCHAR(128) NOT NULL, 
	PROPERTYDEF_TYPE INTEGER, 
	PRIMARY KEY (PROPERTYDEF_ID), 
	UNIQUE (PROPERTYDEF_NAME) CONSTRAINT UK_ONLINE_PROPERTYDEF
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_RESOURCES (
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	DATE_CONTENT NUMERIC(32,0), 
	DATE_CREATED NUMERIC(32,0), 
	DATE_LASTMODIFIED NUMERIC(32,0), 
	PROJECT_LASTMODIFIED VARCHAR(36) NOT NULL, 
	RESOURCE_FLAGS INTEGER, 
	RESOURCE_SIZE INTEGER, 
	RESOURCE_STATE INTEGER, 
	RESOURCE_TYPE INTEGER, 
	RESOURCE_VERSION INTEGER, 
	SIBLING_COUNT INTEGER, 
	USER_CREATED VARCHAR(36) NOT NULL, 
	USER_LASTMODIFIED VARCHAR(36) NOT NULL, 
	PRIMARY KEY (RESOURCE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_RESOURCE_RELATIONS (
	RELATION_SOURCE_ID VARCHAR(36) NOT NULL, 
	RELATION_SOURCE_PATH LVARCHAR(1024) NOT NULL, 
	RELATION_TARGET_ID VARCHAR(36) NOT NULL, 
	RELATION_TARGET_PATH LVARCHAR(1024) NOT NULL, 
	RELATION_TYPE INTEGER NOT NULL
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_STRUCTURE (
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	DATE_EXPIRED NUMERIC(32,0), 
	DATE_RELEASED NUMERIC(32,0), 
	PARENT_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024), 
	STRUCTURE_STATE INTEGER, 
	STRUCTURE_VERSION INTEGER, 
	PRIMARY KEY (STRUCTURE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_ONLINE_URLNAME_MAPPINGS (
	DATE_CHANGED NUMERIC(32,0) NOT NULL, 
	NAME VARCHAR(255) NOT NULL, 
	STATE INTEGER NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	LOCALE VARCHAR(10)
) LOCK MODE ROW;

CREATE TABLE CMS_PROJECTRESOURCES (
	PROJECT_ID VARCHAR(36) NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024) NOT NULL, 
	PRIMARY KEY (PROJECT_ID, RESOURCE_PATH)
) LOCK MODE ROW;

CREATE TABLE CMS_PROJECTS (
	PROJECT_ID VARCHAR(36) NOT NULL, 
	DATE_CREATED NUMERIC(32,0), 
	GROUP_ID VARCHAR(36) NOT NULL, 
	MANAGERGROUP_ID VARCHAR(36) NOT NULL, 
	PROJECT_DESCRIPTION VARCHAR(255) NOT NULL, 
	PROJECT_FLAGS INTEGER, 
	PROJECT_NAME VARCHAR(200) NOT NULL, 
	PROJECT_OU VARCHAR(128) NOT NULL, 
	PROJECT_TYPE INTEGER, 
	USER_ID VARCHAR(36) NOT NULL, 
	PRIMARY KEY (PROJECT_ID), 
	UNIQUE (PROJECT_OU, PROJECT_NAME, DATE_CREATED) CONSTRAINT UK_PROJECTS
) LOCK MODE ROW;

CREATE TABLE CMS_PUBLISH_HISTORY (
	HISTORY_ID VARCHAR(36) NOT NULL, 
	PUBLISH_TAG INTEGER NOT NULL, 
	RESOURCE_PATH LVARCHAR(1024) NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	RESOURCE_ID VARCHAR(36) NOT NULL, 
	RESOURCE_STATE INTEGER, 
	RESOURCE_TYPE INTEGER, 
	SIBLING_COUNT INTEGER, 
	PRIMARY KEY (HISTORY_ID, PUBLISH_TAG, RESOURCE_PATH, STRUCTURE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_PUBLISH_JOBS (
	HISTORY_ID VARCHAR(36) NOT NULL, 
	ENQUEUE_TIME NUMERIC(32,0), 
	FINISH_TIME NUMERIC(32,0), 
	PROJECT_ID VARCHAR(36) NOT NULL, 
	PROJECT_NAME VARCHAR(255) NOT NULL, 
	PUBLISH_FLAGS INTEGER, 
	PUBLISH_LIST BYTE, 
	PUBLISH_LOCALE VARCHAR(16) NOT NULL, 
	PUBLISH_REPORT BYTE, 
	RESOURCE_COUNT INTEGER, 
	START_TIME NUMERIC(32,0), 
	USER_ID VARCHAR(36) NOT NULL, 
	PRIMARY KEY (HISTORY_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_RESOURCE_LOCKS (
	RESOURCE_PATH LVARCHAR(1024) NOT NULL, 
	LOCK_TYPE INTEGER, 
	PROJECT_ID VARCHAR(36) NOT NULL, 
	USER_ID VARCHAR(36) NOT NULL
) LOCK MODE ROW;

CREATE TABLE CMS_STATICEXPORT_LINKS (
	LINK_ID VARCHAR(36) NOT NULL, 
	LINK_PARAMETER LVARCHAR(1024), 
	LINK_RFS_PATH LVARCHAR(1024), 
	LINK_TIMESTAMP NUMERIC(32,0), 
	LINK_TYPE INTEGER, 
	PRIMARY KEY (LINK_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_SUBSCRIPTION (
	PRINCIPAL_ID VARCHAR(36) NOT NULL, 
	STRUCTURE_ID VARCHAR(36) NOT NULL, 
	DATE_DELETED NUMERIC(32,0), 
	PRIMARY KEY (PRINCIPAL_ID, STRUCTURE_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_SUBSCRIPTION_VISIT (
	USER_ID VARCHAR(36) NOT NULL, 
	VISIT_DATE NUMERIC(32,0) NOT NULL, 
	STRUCTURE_ID VARCHAR(36), 
	PRIMARY KEY (USER_ID, VISIT_DATE)
) LOCK MODE ROW;

CREATE TABLE CMS_USERDATA (
	DATA_KEY VARCHAR(255) NOT NULL, 
	USER_ID VARCHAR(36) NOT NULL, 
	DATA_TYPE VARCHAR(128) NOT NULL, 
	DATA_VALUE BYTE, 
	PRIMARY KEY (DATA_KEY, USER_ID)
) LOCK MODE ROW;

CREATE TABLE CMS_USERS (
	USER_ID VARCHAR(36) NOT NULL, 
	USER_DATECREATED NUMERIC(32,0), 
	USER_EMAIL VARCHAR(128) NOT NULL, 
	USER_FIRSTNAME VARCHAR(128) NOT NULL, 
	USER_FLAGS INTEGER, 
	USER_LASTLOGIN NUMERIC(32,0), 
	USER_LASTNAME VARCHAR(128) NOT NULL, 
	USER_NAME VARCHAR(128) NOT NULL, 
	USER_OU VARCHAR(128) NOT NULL, 
	USER_PASSWORD VARCHAR(64) NOT NULL, 
	PRIMARY KEY (USER_ID), 
	UNIQUE (USER_NAME, USER_OU) CONSTRAINT UK_USERS
) LOCK MODE ROW;


CREATE TABLE CMS_ALIASES (path VARCHAR(256) NOT NULL, site_root VARCHAR(64) NOT NULL, alias_mode INTEGER NOT NULL, structure_id VARCHAR(36) NOT NULL, PRIMARY KEY (path, site_root)) LOCK MODE ROW;
CREATE INDEX CMS_ALIASES_IDX_1 ON CMS_ALIASES (structure_id);


CREATE TABLE CMS_USER_PUBLISH_LIST (STRUCTURE_ID VARCHAR(36) NOT NULL, USER_ID VARCHAR(36) NOT NULL, DATE_CHANGED NUMERIC(32,0) NOT NULL, PRIMARY KEY (STRUCTURE_ID, USER_ID)) LOCK MODE ROW;
CREATE INDEX CMS_USERPUBLIST_IDX_01 ON CMS_USER_PUBLISH_LIST (USER_ID);
CREATE INDEX CMS_USERPUBLIST_IDX_02 ON CMS_USER_PUBLISH_LIST (STRUCTURE_ID);