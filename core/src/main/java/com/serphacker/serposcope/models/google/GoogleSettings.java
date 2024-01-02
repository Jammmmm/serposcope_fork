/* 
 * Serposcope - SEO rank checker https://serposcope.serphacker.com/
 * 
 * Copyright (c) 2016 SERP Hacker
 * @author Pierre Nogues <support@serphacker.com>
 * @license https://opensource.org/licenses/MIT MIT License
 */
package com.serphacker.serposcope.models.google;

import com.serphacker.serposcope.scraper.google.GoogleCountryCode;
import com.serphacker.serposcope.scraper.google.GoogleDevice;
import java.util.Arrays;
import java.util.List;


public class GoogleSettings {
    
    int resultPerPage = 100;
    int pages = 1;
    int minPauseBetweenPageSec = 5;
    int maxPauseBetweenPageSec = 5;
    int maxThreads = 1;
    int fetchRetry = 3;

    String uaDesktop = "", uaDesktopDefault = "";
    String uaMobile = "", uaMobileDefault = "";

    String elementsRemoveDesktop = "", elementsRemoveDesktopDefault = "";
	String elementsRemoveMobile = "", elementsRemoveMobileDefault = "";

    String linksParseDesktop = "", linksParseDesktopDefault = "";
    String linksParseMobile = "", linksParseMobileDefault = "";

    String linkHasParentDesktop = "", linkHasParentDesktopDefault = "";
    String linkHasParentMobile = "", linkHasParentMobileDefault = "";

    String linkHasChildrenDesktop = "", linkHasChildrenDesktopDefault = "";
    String linkHasChildrenMobile = "", linkHasChildrenMobileDefault = "";

	String debugPath = "";

    GoogleCountryCode defaultCountry = GoogleCountryCode.__;
    String defaultDatacenter = null;
    GoogleDevice defaultDevice = GoogleDevice.DESKTOP;
    String defaultLocal = null;
    String defaultCustomParameters = null;

    public int getResultPerPage() {
        return resultPerPage;
    }

    public void setResultPerPage(int resultPerPage) {
        this.resultPerPage = resultPerPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getMinPauseBetweenPageSec() {
        return minPauseBetweenPageSec;
    }

    public void setMinPauseBetweenPageSec(int minPauseBetweenPageSec) {
        this.minPauseBetweenPageSec = minPauseBetweenPageSec;
    }

    public int getMaxPauseBetweenPageSec() {
        return maxPauseBetweenPageSec;
    }

    public void setMaxPauseBetweenPageSec(int maxPauseBetweenPageSec) {
        this.maxPauseBetweenPageSec = maxPauseBetweenPageSec;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getFetchRetry() {
        return fetchRetry;
    }

    public void setFetchRetry(int fetchRetry) {
        this.fetchRetry = fetchRetry;
    }

    public String getUADesktop() {
        return uaDesktop;
    }

    public void setUADesktop(String uaDesktop) {
        this.uaDesktop = uaDesktop;
    }

    public String getUADesktopDefault() {
        return uaDesktopDefault;
    }

    public void setUADesktopDefault(String uaDesktopDefault) {
        this.uaDesktopDefault = uaDesktopDefault;
    }

    public String getUAMobile() {
        return uaMobile;
    }

    public void setUAMobile(String uaMobile) {
        this.uaMobile = uaMobile;
    }

    public String getUAMobileDefault() {
        return uaMobileDefault;
    }

    public void setUAMobileDefault(String uaMobileDefault) {
        this.uaMobileDefault = uaMobileDefault;
    }

    public String getElementsRemoveDesktop() {
        return elementsRemoveDesktop;
    }

    public void setElementsRemoveDesktop(String elementsRemoveDesktop) {
        this.elementsRemoveDesktop = elementsRemoveDesktop;
    }

    public String getElementsRemoveMobile() {
        return elementsRemoveMobile;
    }

    public void setElementsRemoveMobile(String elementsRemoveMobile) {
        this.elementsRemoveMobile = elementsRemoveMobile;
    }

    public String getElementsRemoveDesktopDefault() {
        return elementsRemoveDesktopDefault;
    }

    public void setElementsRemoveDesktopDefault(String elementsRemoveDesktopDefault) {
        this.elementsRemoveDesktopDefault = elementsRemoveDesktopDefault;
    }

    public String getElementsRemoveMobileDefault() {
        return elementsRemoveMobileDefault;
    }

    public void setElementsRemoveMobileDefault(String elementsRemoveMobileDefault) {
        this.elementsRemoveMobileDefault = elementsRemoveMobileDefault;
    }

    public String getLinksParseDesktop() {
        return linksParseDesktop;
    }

    public void setLinksParseDesktop(String linksParseDesktop) {
        this.linksParseDesktop = linksParseDesktop;
    }

    public String getLinksParseMobile() {
        return linksParseMobile;
    }

    public void setLinksParseMobile(String linksParseMobile) {
        this.linksParseMobile = linksParseMobile;
    }

    public String getLinksParseDesktopDefault() {
        return linksParseDesktopDefault;
    }

    public void setLinksParseDesktopDefault(String linksParseDesktopDefault) {
        this.linksParseDesktopDefault = linksParseDesktopDefault;
    }

    public String getLinksParseMobileDefault() {
        return linksParseMobileDefault;
    }

    public void setLinksParseMobileDefault(String linksParseMobileDefault) {
        this.linksParseMobileDefault = linksParseMobileDefault;
    }

    public String getLinkHasParentDesktop() {
        return linkHasParentDesktop;
    }

    public void setLinkHasParentDesktop(String linkHasParentDesktop) {
        this.linkHasParentDesktop = linkHasParentDesktop;
    }

    public String getLinkHasParentMobile() {
        return linkHasParentMobile;
    }

    public void setLinkHasParentMobile(String linkHasParentMobile) {
        this.linkHasParentMobile = linkHasParentMobile;
    }

    public String getLinkHasParentDesktopDefault() {
        return linkHasParentDesktopDefault;
    }

    public void setLinkHasParentDesktopDefault(String linkHasParentDesktopDefault) {
        this.linkHasParentDesktopDefault = linkHasParentDesktopDefault;
    }

    public String getLinkHasParentMobileDefault() {
        return linkHasParentMobileDefault;
    }

    public void setLinkHasParentMobileDefault(String linkHasParentMobileDefault) {
        this.linkHasParentMobileDefault = linkHasParentMobileDefault;
    }

    public String getLinkHasChildrenDesktop() {
        return linkHasChildrenDesktop;
    }

    public void setLinkHasChildrenDesktop(String linkHasChildrenDesktop) {
        this.linkHasChildrenDesktop = linkHasChildrenDesktop;
    }

    public String getLinkHasChildrenMobile() {
        return linkHasChildrenMobile;
    }

    public void setLinkHasChildrenMobile(String linkHasChildrenMobile) {
        this.linkHasChildrenMobile = linkHasChildrenMobile;
    }

    public String getLinkHasChildrenDesktopDefault() {
        return linkHasChildrenDesktopDefault;
    }

    public void setLinkHasChildrenDesktopDefault(String linkHasChildrenDesktopDefault) {
        this.linkHasChildrenDesktopDefault = linkHasChildrenDesktopDefault;
    }

    public String getLinkHasChildrenMobileDefault() {
        return linkHasChildrenMobileDefault;
    }

    public void setLinkHasChildrenMobileDefault(String linkHasChildrenMobileDefault) {
        this.linkHasChildrenMobileDefault = linkHasChildrenMobileDefault;
    }

    public String getDebugPath() {
        return debugPath;
    }

    public void setDebugPath(String debugPath) {
        this.debugPath = debugPath;
    }

    // search

    public GoogleCountryCode getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(GoogleCountryCode defaultCountry) {
        if(defaultCountry == null){
            defaultCountry = GoogleCountryCode.__;
        }
        this.defaultCountry = defaultCountry;
    }
    
    public void setDefaultCountry(String country){
        this.defaultCountry = GoogleCountryCode.__;
        
        if(country == null){
            return;
        }
        
        try {
            this.defaultCountry = GoogleCountryCode.valueOf(country.toUpperCase());
        } catch(Exception ex){
        }
    }    

    public String getDefaultDatacenter() {
        return defaultDatacenter;
    }

    public void setDefaultDatacenter(String defaultDatacenter) {
        this.defaultDatacenter = defaultDatacenter;
    }

    public GoogleDevice getDefaultDevice() {
        return defaultDevice;
    }

    public void setDefaultDevice(GoogleDevice defaultDevice) {
        this.defaultDevice = defaultDevice;
    }
    
    public void setDefaultDevice(String deviceId){
        this.defaultDevice = GoogleDevice.DESKTOP;
        
        if(deviceId == null){
            return;
        }
        
        try {
            this.defaultDevice = GoogleDevice.values()[Integer.parseInt(deviceId)];
        } catch(Exception ex){
        }
    }
    
    public String getDefaultLocal() {
        return defaultLocal;
    }

    public void setDefaultLocal(String defaultLocal) {
        this.defaultLocal = defaultLocal;
    }

    public String getDefaultCustomParameters() {
        return defaultCustomParameters;
    }

    public void setDefaultCustomParameters(String defaultCustomParameters) {
        this.defaultCustomParameters = defaultCustomParameters;
    }
    
}
