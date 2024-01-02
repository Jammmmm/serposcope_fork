/* 
 * Serposcope - SEO rank checker https://serposcope.serphacker.com/
 * 
 * Copyright (c) 2016 SERP Hacker
 * @author Pierre Nogues <support@serphacker.com>
 * @license https://opensource.org/licenses/MIT MIT License
 */
package com.serphacker.serposcope.db.google;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.serphacker.serposcope.db.base.ConfigDB;
import com.serphacker.serposcope.models.google.GoogleSettings;

@Singleton
public class GoogleOptionsDB {
    
    private final static String PAGES = "google.pages";
    private final static String RESULT_PER_PAGE = "google.result_per_page";
    private final static String MIN_PAUSE_BETWEEN_PAGE_SEC = "google.min_pause_between_page_sec";
    private final static String MAX_PAUSE_BETWEEN_PAGE_SEC = "google.max_pause_between_page_sec";    
    private final static String MAX_THREADS = "google.maxThreads";
    private final static String FETCH_RETRY = "google.fetchRetry";
    private final static String UA_DESKTOP = "google.uaDesktop";
    private final static String UA_DESKTOP_DEFAULT = "google.uaDesktopDefault";
    private final static String UA_MOBILE = "google.uaMobile";
    private final static String UA_MOBILE_DEFAULT = "google.uaMobileDefault";

    private final static String ELEMENTS_REMOVE_DESKTOP = "google.elementsRemoveDesktop";
    private final static String ELEMENTS_REMOVE_DESKTOP_DEFAULT = "google.elementsRemoveDesktopDefault";
    private final static String ELEMENTS_REMOVE_MOBILE = "google.elementsRemoveMobile";
    private final static String ELEMENTS_REMOVE_MOBILE_DEFAULT = "google.elementsRemoveMobileDefault";

    private final static String LINKS_PARSE_DESKTOP = "google.linksParseDesktop";
    private final static String LINKS_PARSE_DESKTOP_DEFAULT = "google.linksParseDesktopDefault";
    private final static String LINKS_PARSE_MOBILE = "google.linksParseMobile";
    private final static String LINKS_PARSE_MOBILE_DEFAULT = "google.linksParseMobileDefault";

    private final static String LINK_HAS_PARENT_DESKTOP = "google.linkHasParentDesktop";
    private final static String LINK_HAS_PARENT_DESKTOP_DEFAULT = "google.linkHasParentDesktopDefault";
    private final static String LINK_HAS_PARENT_MOBILE = "google.linkHasParentMobile";
    private final static String LINK_HAS_PARENT_MOBILE_DEFAULT = "google.linkHasParentMobileDefault";

    private final static String LINK_HAS_CHILDREN_DESKTOP = "google.linkHasChildrenDesktop";
    private final static String LINK_HAS_CHILDREN_DESKTOP_DEFAULT = "google.linkHasChildrenDesktopDefault";
    private final static String LINK_HAS_CHILDREN_MOBILE = "google.linkHasChildrenMobile";
    private final static String LINK_HAS_CHILDREN_MOBILE_DEFAULT = "google.linkHasChildrenMobileDefault";

	private final static String DEBUG_PATH = "google.debugPath";

    private final static String DEFAULT_DATACENTER = "google.default_datacenter";
    private final static String DEFAULT_DEVICE = "google.default.device";
    private final static String DEFAULT_LOCAL = "google.default.local";
    private final static String DEFAULT_COUNTRY = "google.default.country";
    private final static String DEFAULT_CUSTOM_PARAMETERS = "google.default.custom";

    @Inject
    ConfigDB configDB;

    public GoogleSettings get(){
        GoogleSettings options = new GoogleSettings();
        
        options.setPages(configDB.getInt(PAGES, options.getPages()));
        options.setResultPerPage(configDB.getInt(RESULT_PER_PAGE, options.getResultPerPage()));
        options.setMinPauseBetweenPageSec(configDB.getInt(MIN_PAUSE_BETWEEN_PAGE_SEC, options.getMinPauseBetweenPageSec()));
        options.setMaxPauseBetweenPageSec(configDB.getInt(MAX_PAUSE_BETWEEN_PAGE_SEC, options.getMaxPauseBetweenPageSec()));        
        options.setMaxThreads(configDB.getInt(MAX_THREADS, options.getMaxThreads()));
        options.setFetchRetry(configDB.getInt(FETCH_RETRY, options.getFetchRetry()));
        options.setUADesktop(configDB.get(UA_DESKTOP, options.getUADesktop().trim ()));
        options.setUADesktopDefault(configDB.get(UA_DESKTOP_DEFAULT, options.getUADesktopDefault().trim ()));
        options.setUAMobile(configDB.get(UA_MOBILE, options.getUAMobile().trim ()));
        options.setUAMobileDefault(configDB.get(UA_MOBILE_DEFAULT, options.getUAMobileDefault().trim ()));

        options.setElementsRemoveDesktop(configDB.get(ELEMENTS_REMOVE_DESKTOP, options.getElementsRemoveDesktop().trim ()));
        options.setElementsRemoveDesktopDefault(configDB.get(ELEMENTS_REMOVE_DESKTOP_DEFAULT, options.getElementsRemoveDesktopDefault().trim ()));
        options.setElementsRemoveMobile(configDB.get(ELEMENTS_REMOVE_MOBILE, options.getElementsRemoveMobile().trim ()));
        options.setElementsRemoveMobileDefault(configDB.get(ELEMENTS_REMOVE_MOBILE_DEFAULT, options.getElementsRemoveMobileDefault().trim ()));

        options.setLinksParseDesktop(configDB.get(LINKS_PARSE_DESKTOP, options.getLinksParseDesktop().trim ()));
        options.setLinksParseDesktopDefault(configDB.get(LINKS_PARSE_DESKTOP_DEFAULT, options.getLinksParseDesktopDefault().trim ()));
        options.setLinksParseMobile(configDB.get(LINKS_PARSE_MOBILE, options.getLinksParseMobile().trim ()));
        options.setLinksParseMobileDefault(configDB.get(LINKS_PARSE_MOBILE_DEFAULT, options.getLinksParseMobileDefault().trim ()));

        options.setLinkHasParentDesktop(configDB.get(LINK_HAS_PARENT_DESKTOP, options.getLinkHasParentDesktop().trim ()));
        options.setLinkHasParentDesktopDefault(configDB.get(LINK_HAS_PARENT_DESKTOP_DEFAULT, options.getLinkHasParentDesktopDefault().trim ()));
        options.setLinkHasParentMobile(configDB.get(LINK_HAS_PARENT_MOBILE, options.getLinkHasParentMobile().trim ()));
        options.setLinkHasParentMobileDefault(configDB.get(LINK_HAS_PARENT_MOBILE_DEFAULT, options.getLinkHasParentMobileDefault().trim ()));

        options.setLinkHasChildrenDesktop(configDB.get(LINK_HAS_CHILDREN_DESKTOP, options.getLinkHasChildrenDesktop().trim ()));
        options.setLinkHasChildrenDesktopDefault(configDB.get(LINK_HAS_CHILDREN_DESKTOP_DEFAULT, options.getLinkHasChildrenDesktopDefault().trim ()));
        options.setLinkHasChildrenMobile(configDB.get(LINK_HAS_CHILDREN_MOBILE, options.getLinkHasChildrenMobile().trim ()));
        options.setLinkHasChildrenMobileDefault(configDB.get(LINK_HAS_CHILDREN_MOBILE_DEFAULT, options.getLinkHasChildrenMobileDefault().trim ()));

        options.setDebugPath(configDB.get(DEBUG_PATH, options.getDebugPath()));

        options.setDefaultDatacenter(configDB.get(DEFAULT_DATACENTER, options.getDefaultDatacenter()));
        options.setDefaultDevice(configDB.get(DEFAULT_DEVICE, null));
        options.setDefaultLocal(configDB.get(DEFAULT_LOCAL, options.getDefaultLocal()));
        options.setDefaultCountry(configDB.get(DEFAULT_COUNTRY, null));
        options.setDefaultCustomParameters(configDB.get(DEFAULT_CUSTOM_PARAMETERS, options.getDefaultCustomParameters()));
        
        return options;
    }
    
    public void update(GoogleSettings opts){
        
        GoogleSettings def = new GoogleSettings();

        // scraping
        configDB.updateInt(PAGES, nullIfDefault(opts.getPages(), def.getPages()));
        configDB.updateInt(RESULT_PER_PAGE, nullIfDefault(opts.getResultPerPage(), def.getResultPerPage()));
        configDB.updateInt(MIN_PAUSE_BETWEEN_PAGE_SEC, nullIfDefault(opts.getMinPauseBetweenPageSec(), def.getMinPauseBetweenPageSec()));
        configDB.updateInt(MAX_PAUSE_BETWEEN_PAGE_SEC, nullIfDefault(opts.getMaxPauseBetweenPageSec(), def.getMaxPauseBetweenPageSec()));
        configDB.updateInt(MAX_THREADS, nullIfDefault(opts.getMaxThreads(), def.getMaxThreads()));
        configDB.updateInt(FETCH_RETRY, nullIfDefault(opts.getFetchRetry(), def.getFetchRetry()));
        configDB.update(UA_DESKTOP, nullIfDefault(opts.getUADesktop().trim (), def.getUADesktop().trim ()));
        configDB.update(UA_DESKTOP_DEFAULT, nullIfDefault(opts.getUADesktopDefault().trim (), def.getUADesktopDefault().trim ()));
        configDB.update(UA_MOBILE, nullIfDefault(opts.getUAMobile().trim (), def.getUAMobile().trim ()));
        configDB.update(UA_MOBILE_DEFAULT, nullIfDefault(opts.getUAMobileDefault().trim (), def.getUAMobileDefault().trim ()));

        configDB.update(ELEMENTS_REMOVE_DESKTOP, nullIfDefault(opts.getElementsRemoveDesktop().trim (), def.getElementsRemoveDesktop().trim ()));
        configDB.update(ELEMENTS_REMOVE_DESKTOP_DEFAULT, nullIfDefault(opts.getElementsRemoveDesktopDefault().trim (), def.getElementsRemoveDesktopDefault().trim ()));
        configDB.update(ELEMENTS_REMOVE_MOBILE, nullIfDefault(opts.getElementsRemoveMobile().trim (), def.getElementsRemoveMobile().trim ()));
        configDB.update(ELEMENTS_REMOVE_MOBILE_DEFAULT, nullIfDefault(opts.getElementsRemoveMobileDefault().trim (), def.getElementsRemoveMobileDefault().trim ()));

        configDB.update(LINKS_PARSE_DESKTOP, nullIfDefault(opts.getLinksParseDesktop().trim (), def.getLinksParseDesktop().trim ()));
        configDB.update(LINKS_PARSE_DESKTOP_DEFAULT, nullIfDefault(opts.getLinksParseDesktopDefault().trim (), def.getLinksParseDesktopDefault().trim ()));
        configDB.update(LINKS_PARSE_MOBILE, nullIfDefault(opts.getLinksParseMobile().trim (), def.getLinksParseMobile().trim ()));
        configDB.update(LINKS_PARSE_MOBILE_DEFAULT, nullIfDefault(opts.getLinksParseMobileDefault().trim (), def.getLinksParseMobileDefault().trim ()));

        configDB.update(LINK_HAS_PARENT_DESKTOP, nullIfDefault(opts.getLinkHasParentDesktop().trim (), def.getLinkHasParentDesktop().trim ()));
        configDB.update(LINK_HAS_PARENT_DESKTOP_DEFAULT, nullIfDefault(opts.getLinkHasParentDesktopDefault().trim (), def.getLinkHasParentDesktopDefault().trim ()));
        configDB.update(LINK_HAS_PARENT_MOBILE, nullIfDefault(opts.getLinkHasParentMobile().trim (), def.getLinkHasParentMobile().trim ()));
        configDB.update(LINK_HAS_PARENT_MOBILE_DEFAULT, nullIfDefault(opts.getLinkHasParentMobileDefault().trim (), def.getLinkHasParentMobileDefault().trim ()));

        configDB.update(LINK_HAS_CHILDREN_DESKTOP, nullIfDefault(opts.getLinkHasChildrenDesktop().trim (), def.getLinkHasChildrenDesktop().trim ()));
        configDB.update(LINK_HAS_CHILDREN_DESKTOP_DEFAULT, nullIfDefault(opts.getLinkHasChildrenDesktopDefault().trim (), def.getLinkHasChildrenDesktopDefault().trim ()));
        configDB.update(LINK_HAS_CHILDREN_MOBILE, nullIfDefault(opts.getLinkHasChildrenMobile().trim (), def.getLinkHasChildrenMobile().trim ()));
        configDB.update(LINK_HAS_CHILDREN_MOBILE_DEFAULT, nullIfDefault(opts.getLinkHasChildrenMobileDefault().trim (), def.getLinkHasChildrenMobileDefault().trim ()));

		configDB.update(DEBUG_PATH, nullIfDefault(opts.getDebugPath(), def.getDebugPath()));

        // search
        configDB.update(DEFAULT_DATACENTER, nullIfDefault(opts.getDefaultDatacenter(), def.getDefaultDatacenter()));
        configDB.updateInt(DEFAULT_DEVICE, nullIfDefault(opts.getDefaultDevice().ordinal(), def.getDefaultDevice().ordinal()));
        configDB.update(DEFAULT_LOCAL, nullIfDefault(opts.getDefaultLocal(), def.getDefaultLocal()));
        configDB.update(DEFAULT_COUNTRY, nullIfDefault(opts.getDefaultCountry().name(), def.getDefaultCountry().name()));
        configDB.update(DEFAULT_CUSTOM_PARAMETERS, nullIfDefault(opts.getDefaultCustomParameters(),def.getDefaultCustomParameters()));
        
    }
    
    protected Integer nullIfDefault(Integer value, Integer def){
        return (Integer)nullIfDefaultObject(value, def);
    }
    
    protected String nullIfDefault(String value, String def){
        return (String)nullIfDefaultObject(value, def);
    }
    
    protected Object nullIfDefaultObject(Object value, Object def){
        if(def == null && value != null){
            return value;
        }
        
        if(value == null){
            return null;
        }
        
        if(def.equals(value)){
            return null;
        }
        return value;
    }
    
}
