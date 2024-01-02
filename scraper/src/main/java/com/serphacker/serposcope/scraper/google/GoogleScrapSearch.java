/* 
 * Serposcope - SEO rank checker https://serposcope.serphacker.com/
 * 
 * Copyright (c) 2016 SERP Hacker
 * @author Pierre Nogues <support@serphacker.com>
 * @license https://opensource.org/licenses/MIT MIT License
 */
package com.serphacker.serposcope.scraper.google;

import java.util.Objects;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleScrapSearch {
    
    private final static Random random = new Random();
    private static final Logger LOG = LoggerFactory.getLogger(GoogleScrapSearch.class);

    public GoogleScrapSearch() {
    }
    
    int resultPerPage = 10;
    int pages = 5;
    long minPauseBetweenPageMS = 0l;
    long maxPauseBetweenPageMS = 0l;
    String keyword;
    GoogleCountryCode country;
    String datacenter;
    GoogleDevice device = GoogleDevice.DESKTOP;
    String local;
    String customParameters;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public GoogleCountryCode getCountry() {
        return country;
    }

    public void setCountry(GoogleCountryCode countryCode) {
        this.country = countryCode;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public GoogleDevice getDevice() {
        return device;
    }

    public void setDevice(GoogleDevice device) {
        this.device = device;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
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

    public void setPagePauseMS(long minMs, long maxMs){
        if(minMs > maxMs){
            throw new IllegalArgumentException("minMs > maxMs");
        }
        minPauseBetweenPageMS = minMs;
        maxPauseBetweenPageMS = maxMs;
    }
    
    public long getRandomPagePauseMS(){
        if(minPauseBetweenPageMS == maxPauseBetweenPageMS){
            return maxPauseBetweenPageMS;
        }
        return minPauseBetweenPageMS + Math.abs(random.nextLong()%(maxPauseBetweenPageMS-minPauseBetweenPageMS));
    }

	public void doRandomPagePause (boolean doLog) throws InterruptedException
	{
        long pause = getRandomPagePauseMS();
		if (pause > 0)
		{
			try
			{
				if (doLog == true) { LOG.info("sleeping {} milliseconds", pause); }
				Thread.sleep(pause);
			}
			catch(InterruptedException ex)
			{
				throw ex;
			}
		}
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.resultPerPage;
        hash = 79 * hash + this.pages;
        hash = 79 * hash + (int) (this.minPauseBetweenPageMS ^ (this.minPauseBetweenPageMS >>> 32));
        hash = 79 * hash + (int) (this.maxPauseBetweenPageMS ^ (this.maxPauseBetweenPageMS >>> 32));
        hash = 79 * hash + Objects.hashCode(this.keyword);
        hash = 79 * hash + (this.country == null ? 0 : (this.country.ordinal()+1) )*100;
        hash = 79 * hash + Objects.hashCode(this.datacenter);
        hash = 79 * hash + (this.device == null ? 0 : (this.device.ordinal()+1) );
        hash = 79 * hash + Objects.hashCode(this.local);
        hash = 79 * hash + Objects.hashCode(this.customParameters);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GoogleScrapSearch other = (GoogleScrapSearch) obj;
        if (this.resultPerPage != other.resultPerPage) {
            return false;
        }
        if (this.pages != other.pages) {
            return false;
        }
        if (this.minPauseBetweenPageMS != other.minPauseBetweenPageMS) {
            return false;
        }
        if (this.maxPauseBetweenPageMS != other.maxPauseBetweenPageMS) {
            return false;
        }
        if (!Objects.equals(this.keyword, other.keyword)) {
            return false;
        }
        if (this.country != other.country) {
            return false;
        }
        if (!Objects.equals(this.datacenter, other.datacenter)) {
            return false;
        }
        if (!Objects.equals(this.local, other.local)) {
            return false;
        }
        if (!Objects.equals(this.customParameters, other.customParameters)) {
            return false;
        }
        if (this.device != other.device) {
            return false;
        }
        return true;
    }
    
}